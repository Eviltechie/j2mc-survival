package to.joe.j2mc.survival.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.survival.J2MC_Survival;

public class ReadyCommand extends MasterCommand {

    J2MC_Survival plugin;

    public ReadyCommand(J2MC_Survival survival) {
        super(survival);
        this.plugin = survival;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (!isPlayer) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return;
        }
        switch (plugin.getGame().getStatus()) {
            case Countdown:
                sender.sendMessage(ChatColor.RED + "You cannot indicate ready, the countdown has already started");
                return;
            case InGame:
                sender.sendMessage(ChatColor.RED + "You cannot indicate ready, the game has already begun");
                return;
            case PostRound:
                sender.sendMessage(ChatColor.RED + "You cannot indicate ready, the next map is not loaded");
                return;
        }
        if (!plugin.getGame().getParticipants().contains(player.getName())) {
            sender.sendMessage(ChatColor.RED + "You must be a participant to indicate ready");
            return;
        }
        if (plugin.getGame().getReadyPlayers().contains(player.getName())) {
            plugin.getGame().getReadyPlayers().remove(player.getName());
            plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " is no longer ready to start");
        } else {
            plugin.getGame().getReadyPlayers().add(player.getName());
            if (((double) plugin.getGame().getReadyPlayers().size() / plugin.getGame().getParticipants().size() > this.plugin.minReadyPercent) && plugin.getGame().getParticipants().size() >= plugin.getGame().getMinPlayers()) {
                plugin.getGame().startCountdown();
            } else {
                this.plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " is ready to start");
            }
        }
    }

}
