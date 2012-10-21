package to.joe.j2mc.survival.listeners;

import org.bukkit.event.Listener;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.maps.event.ArenaCommandEvent;
import to.joe.j2mc.survival.J2MC_Survival;

public class ArenaCommandListener implements Listener {
    
    J2MC_Survival plugin;

    public ArenaCommandListener(J2MC_Survival survival) {
        plugin = survival;
    }
    
    public void onArenaCommand(ArenaCommandEvent event) {
        if (!J2MC_Manager.getPermissions().hasFlag(event.getPlayer().getName(), 'P')) {
            event.setCancelled(true);
        }
    }

}
