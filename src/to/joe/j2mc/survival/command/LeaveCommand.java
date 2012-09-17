package to.joe.j2mc.survival.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.survival.J2MC_Survival;
import to.joe.j2mc.survival.J2MC_Survival.LossMethod;

public class LeaveCommand extends MasterCommand {

    J2MC_Survival plugin;

    public LeaveCommand(J2MC_Survival survival) {
        super(survival);
        this.plugin = survival;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (!isPlayer) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return;
        }
        switch (plugin.status) {
            case InGame:
            case Countdown:
                this.plugin.handleLoss(player, LossMethod.Left);
            case PreRound:
                this.plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " has abandoned the survival games!");
                this.plugin.spm.removePlayer(player.getName());
                this.plugin.participants.remove(player.getName());
                this.plugin.readyPlayers.remove(player.getName());
                return;
            case PostRound:
                sender.sendMessage(ChatColor.RED + "You cannot leave the survival games after they end");
        }
    }

}
