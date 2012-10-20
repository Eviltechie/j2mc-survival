package to.joe.j2mc.survival.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import to.joe.j2mc.survival.Game.LossMethod;
import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerQuitListener implements Listener {

    J2MC_Survival plugin;

    public PlayerQuitListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        plugin.getServer().broadcast(event.getQuitMessage(), "j2mc.chat.spectator");
        event.setQuitMessage(null);
        if (plugin.getGame().getParticipants().contains(event.getPlayer().getName())) {
            switch (plugin.getGame().getStatus()) {
                case InGame:
                case Countdown:
                    plugin.getGame().handleLoss(event.getPlayer(), LossMethod.Disconnect);
                    return;
                case PreRound:
                    plugin.getServer().broadcastMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.AQUA + " has left the survival games!"); //TODO this might not work right, it should proabbly be checked against other stuff
                    plugin.getGame().getSpawnPointManager().removePlayer(event.getPlayer().getName());
                    plugin.getGame().getParticipants().remove(event.getPlayer().getName());
                    plugin.getGame().getReadyPlayers().remove(event.getPlayer().getName());
                    return;
            }
        }
    }

}
