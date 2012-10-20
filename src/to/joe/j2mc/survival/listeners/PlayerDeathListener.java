package to.joe.j2mc.survival.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import to.joe.j2mc.survival.Game.GameStatus;
import to.joe.j2mc.survival.Game.LossMethod;
import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerDeathListener implements Listener {

    J2MC_Survival plugin;

    public PlayerDeathListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        GameStatus status = plugin.getGame().getStatus();
        if (status == GameStatus.InGame || status == GameStatus.Countdown)
            event.setDeathMessage(null);
        if (plugin.getGame().getParticipants().contains(event.getEntity().getName())) {
            switch (status) {
                case InGame:
                case Countdown:
                    plugin.getGame().handleLoss(event.getEntity(), LossMethod.Killed);
                    return;
            }
        }
    }

}
