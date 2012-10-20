package to.joe.j2mc.survival.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerInteractListener implements Listener {

    J2MC_Survival plugin;

    public PlayerInteractListener(J2MC_Survival survival) {
        plugin = survival;
    }

    //This is a cheap trick for getting spawn point coords
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.hasBlock() && event.getPlayer().isOp()) {
            Location l = event.getClickedBlock().getLocation();
            plugin.getServer().getLogger().info("- " + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ());
        }
    }

}
