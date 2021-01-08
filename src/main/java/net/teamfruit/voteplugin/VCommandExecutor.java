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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!VotePlugin.instance.isVoting) {
            sender.sendMessage(ChatColor.RED + "投票は始まっていません。");
            return true;
        }

        if (VotePlugin.instance.votedplayers.contains(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "すでに投票済みです。");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "/v <プレイヤー名>で投票してください。");
            return true;
        }

        if (Bukkit.getOnlinePlayers().stream().map(Player::getName).noneMatch(args[0]::equals)) {
            sender.sendMessage(ChatColor.RED + args[0] + "は投票リストに入っていません。");
            return true;
        }

        VotePlugin.instance.votedplayers.add(sender.getName());
        if (VotePlugin.instance.data.containsKey(args[0])) {
            VotePlugin.instance.data.replace(args[0], VotePlugin.instance.data.get(args[0]) + 1);
        } else {
            VotePlugin.instance.data.put(args[0], 1);
        }

        VotePlugin.instance.votedata.put(sender.getName(), args[0]);
        sender.sendMessage(ChatColor.GREEN + args[0] + "に投票しました。");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0)
            return new ArrayList<>();

        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(s -> s.startsWith(args[0]))
                .collect(Collectors.toList());
    }
}
