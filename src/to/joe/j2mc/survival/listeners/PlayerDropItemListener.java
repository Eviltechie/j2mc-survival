package to.joe.j2mc.survival.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import to.joe.j2mc.survival.Game.GameStatus;
import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerDropItemListener implements Listener {

    J2MC_Survival plugin;

    public PlayerDropItemListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if ((plugin.getGame().getStatus() == GameStatus.Countdown || plugin.getGame().getStatus() == GameStatus.InGame) && plugin.getGame().getParticipants().contains(event.getPlayer().getName()) && event.getItemDrop().getItemStack().getType().equals(Material.COMPASS))
            event.setCancelled(true);
    }

}
