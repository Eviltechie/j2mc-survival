package to.joe.j2mc.survival.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerRespawnListener implements Listener {

    J2MC_Survival plugin;

    public PlayerRespawnListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        final Player p = event.getPlayer();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.setAllowFlight(true);
            }
        }, 1);
    }

}
