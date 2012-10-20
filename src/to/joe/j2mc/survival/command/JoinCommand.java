package to.joe.j2mc.survival.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import to.joe.j2mc.core.command.MasterCommand;
import to.joe.j2mc.survival.J2MC_Survival;

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
        if (plugin.getGame().getParticipants().contains(player.getName())) {
            sender.sendMessage(ChatColor.RED + "You are already in the survival game");
            return;
        }
        switch (plugin.getGame().getStatus()) {
            case InGame:
                sender.sendMessage(ChatColor.RED + "You may not join a game in progress");
            case PostRound:
                sender.sendMessage(ChatColor.RED + "Please wait for the next round before joining");
                return;
            case PreRound:
                if (plugin.getGame().getSpawnPointManager().addPlayer(player.getName())) {
                    plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " has entered the survival games!");
                    plugin.getGame().getParticipants().add(player.getName());
                    if (plugin.getGame().getParticipants().size() == plugin.getGame().getMaxPlayers()) {
                        plugin.getGame().startCountdown();
                        plugin.getServer().broadcastMessage(ChatColor.AQUA + "All slots have been filled");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "There are no free slots in the next round");
                }
                return;
            case Countdown:
                if (plugin.getGame().getSpawnPointManager().addPlayerLate(player)) {
                    plugin.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA + " has entered the survival games!");
                    plugin.getGame().getParticipants().add(player.getName());
                } else {
                    sender.sendMessage(ChatColor.RED + "There are no free slots in this round");
                }
                return;
        }
    }

}
