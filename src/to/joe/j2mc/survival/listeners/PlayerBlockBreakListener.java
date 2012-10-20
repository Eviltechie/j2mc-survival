package to.joe.j2mc.survival.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import to.joe.j2mc.survival.Game.GameStatus;
import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerBlockBreakListener implements Listener {

    J2MC_Survival plugin;

    public PlayerBlockBreakListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        GameStatus status = plugin.getGame().getStatus();
        if ((status == GameStatus.InGame || status == GameStatus.Countdown) && plugin.getGame().getParticipants().contains(event.getPlayer().getName()) && !plugin.getGame().getBreakableBlocks().contains(event.getBlock().getTypeId()))
            event.setCancelled(true);
    }

}
