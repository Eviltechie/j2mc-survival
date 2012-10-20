package to.joe.j2mc.survival.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import to.joe.j2mc.survival.J2MC_Survival;

public class WorldLoadListener implements Listener {

    private J2MC_Survival plugin;

    public WorldLoadListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (plugin.mapCycle.contains(event.getWorld().getName())) {
            plugin.startNewGame(event.getWorld().getName());
        }
    }

}
