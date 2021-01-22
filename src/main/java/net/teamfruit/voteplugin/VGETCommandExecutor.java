package net.teamfruit.voteplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VGETCommandExecutor implements CommandExecutor {
    private static final VoteAPI api = VoteAPI.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "権限がありません。");
            return true;
        }

        if (api.isVoting()) {
            sender.sendMessage(ChatColor.RED + "投票中は投票先を表示することはできません。");
            return true;
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "========= 投票先一覧 =========");
        VoteResult result = api.getResult();
        if (result != null) {
            result.getDestinations().forEach((key, value) -> Bukkit.broadcastMessage(ChatColor.DARK_GREEN + key + " >> " + value));
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "========= 投票先一覧 =========");
        return true;
    }
}
