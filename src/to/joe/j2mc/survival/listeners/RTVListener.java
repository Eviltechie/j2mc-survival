package to.joe.j2mc.survival.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import to.joe.j2mc.maps.event.RockTheVoteEvent;
import to.joe.j2mc.survival.J2MC_Survival;

public class RTVListener implements Listener {

    private J2MC_Survival plugin;

    public RTVListener(J2MC_Survival survival) {
        plugin = survival;
    }

    @EventHandler
    public void onRTV(RockTheVoteEvent event) {
        plugin.getServer().broadcastMessage(ChatColor.RED + "Please end this round quickly!");
    }

}
