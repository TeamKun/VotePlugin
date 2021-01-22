package net.teamfruit.voteplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VCommandExecutor implements CommandExecutor, TabCompleter {
    private static final VoteAPI api = VoteAPI.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!api.isVoting()) {
            sender.sendMessage(ChatColor.RED + "投票は始まっていません。");
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (api.isVoted(player)) {
                sender.sendMessage(ChatColor.RED + "すでに投票済みです。");
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "/v <プレイヤー名>で投票してください。");
                return true;
            }

            Player dest = Bukkit.getPlayer(args[0]);
            if (dest == null) {
                sender.sendMessage(ChatColor.RED + args[0] + "は投票リストに入っていません。");
                return true;
            }

            api.vote(player, dest);
            player.sendMessage(ChatColor.GREEN + dest.getName() + "に投票しました。");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0)
            return new ArrayList<>();

        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(s -> s.startsWith(args[0]))
                .collect(Collectors.toList());
    }
}
