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
		if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
            return;
        }
		this.plugin.handleLoss(player, LossMethod.Left);
	}

}
