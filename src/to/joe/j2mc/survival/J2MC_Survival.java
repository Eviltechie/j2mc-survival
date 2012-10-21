package to.joe.j2mc.survival;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishPerms;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.maps.J2MC_Maps;
import to.joe.j2mc.survival.command.JoinCommand;
import to.joe.j2mc.survival.command.ReadyCommand;
import to.joe.j2mc.survival.listeners.ArenaCommandListener;
import to.joe.j2mc.survival.listeners.LobbyCommandLstener;
import to.joe.j2mc.survival.listeners.PlayerBlockBreakListener;
import to.joe.j2mc.survival.listeners.PlayerDeathListener;
import to.joe.j2mc.survival.listeners.PlayerDropItemListener;
import to.joe.j2mc.survival.listeners.PlayerInteractListener;
import to.joe.j2mc.survival.listeners.PlayerJoinListener;
import to.joe.j2mc.survival.listeners.PlayerMoveListener;
import to.joe.j2mc.survival.listeners.PlayerQuitListener;
import to.joe.j2mc.survival.listeners.PlayerRespawnListener;
import to.joe.j2mc.survival.listeners.RTVListener;
import to.joe.j2mc.survival.listeners.WorldLoadListener;
import to.joe.j2mc.survival.runnable.CompassUpdater;

public class J2MC_Survival extends JavaPlugin {

    private Game game;
    ArrayList<Listener> listeners = new ArrayList<Listener>();
    public boolean tubeMines;
    public ArrayList<String> mapCycle;
    int countdown;
    int maxWait;
    public double minReadyPercent;
    public World gameWorld;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        countdown = this.getConfig().getInt("countdown");
        maxWait = this.getConfig().getInt("maxWait");
        minReadyPercent = this.getConfig().getDouble("percentReady");
        mapCycle = new ArrayList<String>(this.getConfig().getStringList("mapcycle"));
        tubeMines = this.getConfig().getBoolean("tubeMines");

        getCommand("join").setExecutor(new JoinCommand(this));
        //getCommand("leave").setExecutor(new LeaveCommand(this));
        getCommand("ready").setExecutor(new ReadyCommand(this));

        getServer().getPluginManager().registerEvents(new WorldLoadListener(this), this);

        listeners.add(new PlayerBlockBreakListener(this));
        listeners.add(new PlayerJoinListener(this));
        listeners.add(new PlayerMoveListener(this));
        listeners.add(new PlayerDeathListener(this));
        listeners.add(new PlayerRespawnListener(this));
        listeners.add(new PlayerQuitListener(this));
        listeners.add(new PlayerInteractListener(this));
        listeners.add(new PlayerDropItemListener(this));
        listeners.add(new RTVListener(this));
        listeners.add(new ArenaCommandListener(this));
        listeners.add(new LobbyCommandLstener(this));

        J2MC_Manager.getPermissions().addFlagPermissionRelation("j2mc.chat.spectator", 'P', true);
        J2MC_Manager.getPermissions().addFlagPermissionRelation("worldedit.navigation.thru", 'P', true);
    }

    private void registerListeners() {
        for (Listener l : listeners) {
            getServer().getPluginManager().registerEvents(l, this);
        }
    }

    private void unregisterListeners() {
        for (Listener l : listeners) {
            HandlerList.unregisterAll(l);
        }
        getServer().getScheduler().cancelTasks(this);
    }

    public void startNewGame(String mapName) {
        game = new Game(mapName, this);
        registerListeners();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new CompassUpdater(this), 0, 100);
    }

    public void stopGame() {
        for (Player p : getServer().getOnlinePlayers()) {
            setSpectate(p, false);
        }
        unregisterListeners();
        J2MC_Maps.finished();
    }

    public Game getGame() {
        return game;
    }

    public void toLobby(Player p) {
        J2MC_Maps.sendToLobby(p);
        p.setAllowFlight(true);
    }

    public void setSpectate(Player player, boolean spec) {
        try {
            if (spec && !VanishNoPacket.isVanished(player.getName())) {
                player.setGameMode(GameMode.ADVENTURE);
                J2MC_Manager.getPermissions().addFlag(player, 'P');
                VanishPerms.toggleSeeAll(player);
                VanishPerms.toggleNoPickup(player);
                VanishPerms.toggleNoFollow(player);
                //Trample?
                VanishPerms.toggleNoInteract(player);
                VanishPerms.toggleSilentChestReads(player);
                VanishPerms.toggleDamageIn(player);
                VanishPerms.toggleDamageOut(player);
                VanishNoPacket.toggleVanishSilent(player);
                player.setAllowFlight(true);
            } else if (!spec && VanishNoPacket.isVanished(player.getName())) {
                player.setGameMode(GameMode.SURVIVAL);
                J2MC_Manager.getPermissions().delFlag(player, 'P');
                VanishPerms.toggleSeeAll(player);
                VanishPerms.toggleNoPickup(player);
                VanishPerms.toggleNoFollow(player);
                //Trample?
                VanishPerms.toggleNoInteract(player);
                VanishPerms.toggleSilentChestReads(player);
                VanishPerms.toggleDamageIn(player);
                VanishPerms.toggleDamageOut(player);
                VanishNoPacket.toggleVanishSilent(player);
                player.setAllowFlight(false);
            }
        } catch (VanishNotLoadedException e) {
            getServer().getLogger().severe("Vanish no packet missing!");
        }
    }
}
