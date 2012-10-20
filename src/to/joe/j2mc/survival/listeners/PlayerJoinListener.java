package to.joe.j2mc.survival.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import to.joe.j2mc.maps.J2MC_Maps;
import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerJoinListener implements Listener {

    J2MC_Survival plugin;

    public PlayerJoinListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getServer().broadcast(event.getJoinMessage(), "j2mc.chat.spectator");
        event.setJoinMessage(null);
        final Player player = event.getPlayer();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (!player.getWorld().equals(J2MC_Maps.getLobbyWorld()) || !player.getWorld().equals(J2MC_Maps.getGameWorld())) {
                    plugin.toLobby(player);
                    plugin.setSpectate(player, true);
                }
            }
        }, 1);
    }

}
