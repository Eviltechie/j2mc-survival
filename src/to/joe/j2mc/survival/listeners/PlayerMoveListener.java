package to.joe.j2mc.survival.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import to.joe.j2mc.survival.Game.GameStatus;
import to.joe.j2mc.survival.J2MC_Survival;

public class PlayerMoveListener implements Listener {

    J2MC_Survival plugin;

    public PlayerMoveListener(J2MC_Survival survival) {
        plugin = survival;
    }

    //stolen from https://github.com/tomjw64/HungerBarGames/blob/master/HungerBarGames/src/me/tomjw64/HungerBarGames/Listeners/Countdown/CountdownMotionListener.java
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getGame().getStatus() == GameStatus.Countdown && plugin.getGame().getParticipants().contains(event.getPlayer().getName())) {
            Location from = event.getFrom();
            Location to = event.getTo();
            double x = Math.floor(from.getX());
            double z = Math.floor(from.getZ());
            if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
                if (plugin.tubeMines) {
                    event.getFrom().getWorld().createExplosion(to, 0, false);
                    event.getPlayer().damage(20);
                } else {
                    x += .5;
                    z += .5;
                    event.getPlayer().teleport(new Location(from.getWorld(), x, from.getY(), z, to.getYaw(), to.getPitch()));
                }
            }
        }
    }

}
