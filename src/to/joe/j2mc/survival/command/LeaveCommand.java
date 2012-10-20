package to.joe.j2mc.survival.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.survival.Game.LossMethod;
import to.joe.j2mc.survival.J2MC_Survival;

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
        switch (plugin.getGame().getStatus()) {
            case InGame:
            case Countdown:
                plugin.getGame().handleLoss(player, LossMethod.Left);
            case PreRound:
                plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " has abandoned the survival games!");
                plugin.getGame().getSpawnPointManager().removePlayer(player.getName());
                plugin.getGame().getParticipants().remove(player.getName());
                plugin.getGame().getReadyPlayers().remove(player.getName());
                return;
            case PostRound:
                sender.sendMessage(ChatColor.RED + "You cannot leave the survival games after they end");
        }
    }

}
