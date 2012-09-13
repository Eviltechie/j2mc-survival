package to.joe.j2mc.survival.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.survival.J2MC_Survival;
import to.joe.j2mc.survival.J2MC_Survival.GameStatus;

public class JoinCommand extends MasterCommand {

    J2MC_Survival plugin;

    public JoinCommand(J2MC_Survival survival) {
        super(survival);
        this.plugin = survival;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (!isPlayer) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return;
        }
        if (plugin.participants.contains(player.getName())) {
            sender.sendMessage(ChatColor.RED + "You are already in the survival game");
            return;
        }
        if (plugin.status == GameStatus.InGame) {
            sender.sendMessage(ChatColor.RED + "You may not join a game in progress");
            return;
        }
        if (plugin.status == GameStatus.PostRound) {
            sender.sendMessage(ChatColor.RED + "Please wait for the next round before joining");
            return;
        }
        if (plugin.participants.size() == plugin.maxPlayers) {
            sender.sendMessage(ChatColor.RED + "There are no more slots left in the next round");
            return;
        }
    }

}
