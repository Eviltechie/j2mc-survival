package to.joe.j2mc.survival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.survival.command.JoinCommand;
import to.joe.j2mc.survival.command.LeaveCommand;
import to.joe.j2mc.survival.command.SurvCommand;

public class J2MC_Survival extends JavaPlugin implements Listener {

    public void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    public boolean deleteFolder(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFolder(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //TODO /join while gamestatus is preround adds you to participants and announces your join
    //TODO /join while gamestatus is in countdown adds you to participants, announces your join, and instantly runs the setup (clear inventory, spawn, etc)
    //TODO /join while gamestatus is ingame fails
    //TODO /join while gamestatus is postgame fails

    //TODO /leave while gamestatus is preround removes you from participants and announces you leaving
    //TODO /leave while gamestatus is in countdown removes you from partipants and moves you back to the lobby
    //TODO /leave while gamestatus is ingame removes you from partipants and moves you back to the lobby, and clears your inventory
    //TODO /leave while gamestatus is postround fails

    //TODO disconnecting at any time removes you from participants and related

    //TODO /ready while gamestatus is in preround tallies your vote to start
    //TODO When greater than x percentage of people say ready countdown starts
    //TODO When all slots are full, countdown starts
    //TODO After a certain amount of time has passed, countdown starts
    //TODO /ready at any other time than preround fails

    //TODO When countdown starts, all players are teleported to a spawn, their inventory is cleared, they are set to survival, no fly, healed, fed

    //TODO Config needs spawns
    //TODO Config needs minplayers, maxplayers
    //TODO Config needs blocks that can be broken (or not)
    //TODO Config needs lobby

    //TODO Thing that prevents blocks from being broken

    //TODO Voting for maps

    //TODO Spectator mode, vanish players, filter chat

    public enum GameStatus {
        PreRound, Countdown, InGame, PostRound,
    }

    public enum LossMethod {
        Disconnect, Killed, Left,
    }

    public GameStatus status = GameStatus.PreRound;
    public ArrayList<String> participants = new ArrayList<String>();
    public ArrayList<String> deadPlayers = new ArrayList<String>();
    public ArrayList<String> mapCycle;
    int minPlayers = 2;
    public int maxPlayers = 2;
    int countdown;
    int maxWait;
    double minReadyPercent;
    World lobbyWorld;
    World gameWorld;

    public void loadMainConfig() {
        countdown = this.getConfig().getInt("countdown");
        maxWait = this.getConfig().getInt("maxWait");
        minReadyPercent = this.getConfig().getDouble("percentReady");
        mapCycle = new ArrayList<String>(this.getConfig().getStringList("mapcycle"));
    }

    public void loadMap(boolean firstLoad) {
        for (Player p : this.getServer().getOnlinePlayers()) {
            toLobby(p);
        }
        String newMap = mapCycle.get(0);
        mapCycle.add(mapCycle.remove(0));
        if (!firstLoad) {
            this.getServer().unloadWorld(gameWorld, false);
            deleteFolder(new File(gameWorld.getName()));
        }
        try {
            copyFolder(new File(newMap + "_bak"), new File(newMap));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        gameWorld = this.getServer().createWorld(new WorldCreator(newMap));
        this.getServer().getLogger().info("Sucessfully loaded lobby");

        for (Player p : this.getServer().getOnlinePlayers()) {
            p.teleport(gameWorld.getSpawnLocation());
        }
        status = GameStatus.PreRound;
    }

    public void toLobby(Player p) {
        p.teleport(lobbyWorld.getSpawnLocation());
    }

    @Override
    public void onEnable() {
        //TODO Load map configs
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        loadMainConfig();

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("join").setExecutor(new JoinCommand(this));
        this.getCommand("leave").setExecutor(new LeaveCommand(this));
        this.getCommand("surv").setExecutor(new SurvCommand(this));

        //Run announceDead() once per day
        J2MC_Manager.getCore().getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (gameWorld.getTime() >= 13000 && gameWorld.getTime() < 14000)
                    announceDead();
            }
        }, 1000, 1000);

        //We are assuming that the first world loaded (which should be defined in server.properties) will be the lobby.
        //Also good if the plugin breaks.
        lobbyWorld = this.getServer().getWorlds().get(0);
        loadMap(true);
    }

    @Override
    public void onDisable() {
        this.getServer().unloadWorld(gameWorld, false);
        deleteFolder(new File(gameWorld.getName()));
    }

    private void announceDead() {
        if (deadPlayers.size() == 0) {
            this.getServer().broadcastMessage(ChatColor.AQUA + "In the previous day no players were eliminated");
            return;
        }
        if (deadPlayers.size() > 1)
            this.getServer().broadcastMessage(ChatColor.AQUA + "In the previous day, the following players were eliminated");
        else
            this.getServer().broadcastMessage(ChatColor.AQUA + "In the previous day, the following player was eliminated");
        int offset = 0;
        for (final String s : deadPlayers) {
            offset += 30;
            J2MC_Manager.getCore().getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    getServer().broadcastMessage(ChatColor.RED + s);
                }
            }, offset);
        }
    }

    public void handleLoss(Player p, LossMethod m) {
        String n = p.getName();
        switch (m) {
            case Disconnect:
                J2MC_Manager.getCore().adminAndLog(ChatColor.RED + n + " has disconnected");
                break;
            case Killed:
                J2MC_Manager.getCore().adminAndLog(ChatColor.RED + n + " has been killed");
                break;
            case Left:
                J2MC_Manager.getCore().adminAndLog(ChatColor.RED + n + " has left the game");
                break;
        }
        deadPlayers.add(n);
        //TODO Cannon
        final boolean weather = p.getWorld().isThundering();
        p.getWorld().strikeLightningEffect(p.getLocation());
        p.damage(9001);
        p.sendMessage(ChatColor.RED + "You have been eliminated");
        p.getWorld().setStorm(weather);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if (status == GameStatus.InGame && participants.contains(event.getPlayer().getName()))
            handleLoss(event.getPlayer(), LossMethod.Disconnect);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (status == GameStatus.InGame && participants.contains(event.getEntity().getName())) {
            handleLoss(event.getEntity(), LossMethod.Killed);
            event.setDeathMessage("");
        }
    }
}
