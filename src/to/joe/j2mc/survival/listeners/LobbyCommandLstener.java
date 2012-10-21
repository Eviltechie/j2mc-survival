package to.joe.j2mc.survival.listeners;

import org.bukkit.event.Listener;

import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.maps.event.LobbyCommandEvent;
import to.joe.j2mc.survival.J2MC_Survival;

public class LobbyCommandLstener implements Listener {
    
    J2MC_Survival plugin;

    public LobbyCommandLstener(J2MC_Survival survival) {
        plugin = survival;
    }
    
    public void onLobbyCommand(LobbyCommandEvent event) {
        if (!J2MC_Manager.getPermissions().hasFlag(event.getPlayer().getName(), 'P')) {
            event.setCancelled(true);
        }
    }

}
