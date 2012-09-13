package to.joe.j2mc.survival;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.survival.command.JoinCommand;
import to.joe.j2mc.survival.command.LeaveCommand;

public class J2MC_Survival extends JavaPlugin implements Listener {
	
	public enum GameStatus {
		PreRound,
		Countdown,
		InGame,
		PostRound,
	}
	
	public enum LossMethod {
		Disconnect,
		Killed,
		Left,
	}
	
	GameStatus status = GameStatus.PreRound;
	public ArrayList<String> participants = new ArrayList<String>();
	public ArrayList<String> deadPlayers = new ArrayList<String>();
	World gameWorld;
	
	private void loadMap() {
		//TODO Move all players to other world
		//TODO Unload the map
		//TODO Replace the map
		//TODO Move all players back
		status = GameStatus.PreRound;
		//TODO gameWorld = something
	}
	
	@Override
	public void onEnable() {
		//TODO Load map configs
		
		this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("join").setExecutor(new JoinCommand(this));
        this.getCommand("leave").setExecutor(new LeaveCommand(this));
        
        //Run announceDead() once per day
        J2MC_Manager.getCore().getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				if (gameWorld.getTime() >= 13000 && gameWorld.getTime() < 14000)
					announceDead();
			}
		}, 1000, 1000);
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
		switch(m) {
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
