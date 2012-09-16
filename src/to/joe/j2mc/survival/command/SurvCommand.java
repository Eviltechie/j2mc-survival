package to.joe.j2mc.survival.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.survival.J2MC_Survival;

public class SurvCommand extends MasterCommand {
    
    J2MC_Survival plugin;

    public SurvCommand(J2MC_Survival survival) {
        super(survival);
        this.plugin = survival;
    }

    @Override
    public void exec(CommandSender sender, String commandName, String[] args, Player player, boolean isPlayer) {
        if (args[0].equalsIgnoreCase("lobby")) {
            this.plugin.toLobby(player);
            return;
        }
        if (args[0].equalsIgnoreCase("worlds")) {
            sender.sendMessage(this.plugin.getServer().getWorlds().toString());
            return;
        }
        if (args[0].equalsIgnoreCase("nextmap")) {
            this.plugin.loadMap(false);
            return;
        }
        if (args[0].equalsIgnoreCase("mapcycle")) {
            sender.sendMessage(this.plugin.mapCycle.toString());
            return;
        }
        if (args[0].equalsIgnoreCase("countdown")) {
            this.plugin.startCountdown();
            return;
        }
        if (args[0].equalsIgnoreCase("start")) {
            this.plugin.startGame();
            return;
        }
        sender.sendMessage("lobby, worlds, nextmap, mapcycle, countdown, start");
    }

}
