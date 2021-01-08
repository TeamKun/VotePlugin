package net.teamfruit.voteplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VSCommandExecutor implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (VotePlugin.instance.isVoting) {
                VotePlugin.instance.isVoting = false;
                Bukkit.broadcastMessage(ChatColor.GOLD + "投票を終了しました。");
                List<Entry<String, Integer>> list_entries = VotePlugin.instance.data.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toList());

                List<String> leaderboard = IntStream.range(0, 5)
                        .filter(i -> i < list_entries.size())
                        .mapToObj(i -> {
                            Entry<String, Integer> entry = list_entries.get(i);
                            return "" + ChatColor.GREEN + ChatColor.AQUA + "" + (i + 1) + "位 " + ChatColor.GOLD + entry.getKey() + ChatColor.GRAY + " (" + ChatColor.GRAY + entry.getValue() + "票)";
                        })
                        .collect(Collectors.toList());

                leaderboard.forEach(Bukkit::broadcastMessage);

                String[] s = Stream.concat(
                        Stream.of(ChatColor.GREEN + "   投票結果   "),
                        leaderboard.stream()
                ).toArray(String[]::new);

                Bukkit.getOnlinePlayers().forEach(p -> ScoreboardUtil.unrankedSidebarDisplay(p, s));
            } else {
                VotePlugin.instance.isVoting = true;
                Bukkit.broadcastMessage(ChatColor.GOLD + "投票を開始しました。");
                Bukkit.broadcastMessage(ChatColor.GREEN + "/v <プレイヤー名>で投票をしてください。");

                Bukkit.getOnlinePlayers().forEach(p -> ScoreboardUtil.unrankedSidebarDisplay(p));

                VotePlugin.instance.votedplayers = new ArrayList<>();
                VotePlugin.instance.votedata = new HashMap<>();
                VotePlugin.instance.data = new HashMap<>();
            }
        } else {
            sender.sendMessage(ChatColor.RED + "権限がありません。");
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}
