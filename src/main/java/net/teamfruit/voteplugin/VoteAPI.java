package net.teamfruit.voteplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VoteAPI {
    private static final VoteAPI instance = new VoteAPI();

    private boolean isVoting;
    private Consumer<VoteResult> listener;
    private Set<UUID> votedPlayers;
    private Map<UUID, Integer> countMap;
    private Map<String, String> destinations;
    private VoteResult result;

    private VoteAPI() {
    }

    public void init() {
        isVoting = false;
        listener = null;
        votedPlayers = new HashSet<>();
        countMap = new HashMap<>();
        destinations = new HashMap<>();
        result = null;
    }

    public static VoteAPI getInstance() {
        return instance;
    }

    public boolean isVoting() {
        return isVoting;
    }

    public VoteResult getResult() {
        return result;
    }

    public boolean isVoted(Player player) {
        return votedPlayers.contains(player.getUniqueId());
    }

    public void vote(Player player, Player dest) {
        if (!isVoting || isVoted(dest)) {
            throw new IllegalStateException();
        }
        votedPlayers.add(player.getUniqueId());

        UUID uuid = dest.getUniqueId();
        boolean isVoted = countMap.containsKey(uuid);
        countMap.put(uuid, isVoted ? countMap.get(uuid) + 1 : 1);
        destinations.put(player.getName(), dest.getName());
    }

    public void beginVoting() {
        Bukkit.broadcastMessage(ChatColor.GOLD + "投票を開始しました。");
        Bukkit.broadcastMessage(ChatColor.GREEN + "/v <プレイヤー名>で投票をしてください。");

        Bukkit.getOnlinePlayers().forEach(ScoreboardUtil::unrankedSidebarDisplay);

        votedPlayers.clear();
        destinations.clear();
        countMap.clear();
        result = null;

        isVoting = true;
    }

    public void beginVoting(Consumer<VoteResult> listener) {
        this.listener = listener;
        beginVoting();
    }

    public void endVoting() {
        isVoting = false;
        Bukkit.broadcastMessage(ChatColor.GOLD + "投票を終了しました。");
        List<Map.Entry<UUID, Integer>> list_entries = countMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        List<String> leaderboard = IntStream.range(0, 5)
                .filter(i -> i < list_entries.size())
                .mapToObj(i -> {
                    Map.Entry<UUID, Integer> entry = list_entries.get(i);
                    return "" + ChatColor.GREEN + ChatColor.AQUA + "" + (i + 1) + "位 " + ChatColor.GOLD + entry.getKey() + ChatColor.GRAY + " (" + ChatColor.GRAY + entry.getValue() + "票)";
                })
                .collect(Collectors.toList());

        leaderboard.forEach(Bukkit::broadcastMessage);

        String[] s = Stream.concat(
                Stream.of(ChatColor.GREEN + "   投票結果   "),
                leaderboard.stream()
        ).toArray(String[]::new);

        Bukkit.getOnlinePlayers().forEach(p -> ScoreboardUtil.unrankedSidebarDisplay(p, s));
        result = new VoteResult(countMap, destinations);
        if (listener != null) {
            listener.accept(result);
            listener = null;
        }
    }
}
