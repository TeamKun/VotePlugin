package net.teamfruit.voteplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VSCommandExecutor implements CommandExecutor {
    private static final VoteAPI api = VoteAPI.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "権限がありません。");
        }

        if (api.isVoting()) {
            api.endVoting();
        } else {
            api.beginVoting();
        }
        return false;
    }
}
