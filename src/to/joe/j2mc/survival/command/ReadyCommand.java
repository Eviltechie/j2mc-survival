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
        switch(this.plugin.status) {
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
        if (!this.plugin.participants.contains(player.getName())) {
            sender.sendMessage(ChatColor.RED + "You must be a participant to indicate ready");
            return;
        }
        if (this.plugin.readyPlayers.contains(player.getName())) {
            this.plugin.readyPlayers.remove(player.getName());
            this.plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " is no longer ready to start");
        } else {
            this.plugin.readyPlayers.add(player.getName());
            if ((this.plugin.participants.size() / this.plugin.readyPlayers.size() > this.plugin.minReadyPercent) && this.plugin.participants.size() >= this.plugin.minPlayers) {
                this.plugin.startCountdown();
            } else {
                this.plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " is ready to start");
            }
        }
    }

}
