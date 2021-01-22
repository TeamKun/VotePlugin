package net.teamfruit.voteplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

@SuppressWarnings("ConstantConditions")
public class ScoreboardUtil {
    public static String[] cutUnranked(String[] content) {
        String[] elements = Arrays.copyOf(content, 16);
        if (elements[0] == null) {
            elements[0] = "Unnamed board";
        }

        if (elements[0].length() > 32) {
            elements[0] = elements[0].substring(0, 32);
        }

        for (int i = 1; i < elements.length; ++i) {
            if (elements[i] != null && elements[i].length() > 40) {
                elements[i] = elements[i].substring(0, 40);
            }
        }

        return elements;
    }

    public static String cutRankedTitle(String title) {
        if (title == null) {
            return "Unnamed board";
        } else {
            return title.length() > 32 ? title.substring(0, 32) : title;
        }
    }

    public static HashMap<String, Integer> cutRanked(HashMap<String, Integer> content) {
        HashMap<String, Integer> elements = new HashMap<>(content);

        String minimumKey;
        label43:
        for (; elements.size() > 15; elements.remove(minimumKey)) {
            minimumKey = (String) elements.keySet().toArray()[0];
            int minimum = elements.get(minimumKey);
            Iterator<String> var4 = elements.keySet().iterator();

            while (true) {
                String string;
                do {
                    if (!var4.hasNext()) {
                        continue label43;
                    }

                    string = var4.next();
                } while (elements.get(string) >= minimum && (elements.get(string) != minimum || string.compareTo(minimumKey) >= 0));

                minimumKey = string;
                minimum = elements.get(string);
            }
        }

        Iterator<String> var6 = (new ArrayList<>(elements.keySet())).iterator();

        while (var6.hasNext()) {
            String string = var6.next();
            if (string != null && string.length() > 40) {
                int value = elements.get(string);
                var6.remove();
                elements.put(string.substring(0, 40), value);
            }
        }

        return elements;
    }

    public static boolean unrankedSidebarDisplay(Player p, String... elements) {
        elements = cutUnranked(elements);

        try {
            if (p.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() || p.getScoreboard().getObjectives().size() != 1) {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }

            if (p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)) == null) {
                p.getScoreboard().registerNewObjective(p.getUniqueId().toString().substring(0, 16), "dummy");
                p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)).setDisplaySlot(DisplaySlot.SIDEBAR);
            }

            p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(elements[0]);

            for (int i = 1; i < elements.length; ++i) {
                if (elements[i] != null && p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).getScore() != 16 - i) {
                    p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).setScore(16 - i);

                    for (String string : p.getScoreboard().getEntries()) {
                        if (p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)).getScore(string).getScore() == 16 - i && !string.equals(elements[i])) {
                            p.getScoreboard().resetScores(string);
                        }
                    }
                }
            }

            for (String entry : p.getScoreboard().getEntries()) {
                boolean toErase = true;

                for (String element : elements) {
                    if (element != null && element.equals(entry) && p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)).getScore(entry).getScore() == 16 - Arrays.asList(elements).indexOf(element)) {
                        toErase = false;
                        break;
                    }
                }

                if (toErase) {
                    p.getScoreboard().resetScores(entry);
                }
            }

            return true;
        } catch (Exception var9) {
            var9.printStackTrace();
            return false;
        }
    }

    public static boolean unrankedSidebarDisplay(Collection<Player> players, String[] elements) {
        Iterator<Player> var2 = players.iterator();

        Player player;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            player = var2.next();
        } while (unrankedSidebarDisplay(player, elements));

        return false;
    }

    public static boolean unrankedSidebarDisplay(Collection<Player> players, Scoreboard board, String... elements) {
        try {
            String objName = "COLLAB-SB-WINTER";
            if (board == null) {
                board = Bukkit.getScoreboardManager().getNewScoreboard();
            }

            elements = cutUnranked(elements);

            for (Player player : players) {
                if (player.getScoreboard() != board) {
                    player.setScoreboard(board);
                }
            }

            if (board.getObjective(objName) == null) {
                board.registerNewObjective(objName, "dummy");
                board.getObjective(objName).setDisplaySlot(DisplaySlot.SIDEBAR);
            }

            board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(elements[0]);

            for (int i = 1; i < elements.length; ++i) {
                if (elements[i] != null && board.getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).getScore() != 16 - i) {
                    board.getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).setScore(16 - i);

                    for (String string : board.getEntries()) {
                        if (board.getObjective(objName).getScore(string).getScore() == 16 - i && !string.equals(elements[i])) {
                            board.resetScores(string);
                        }
                    }
                }
            }

            for (String entry : board.getEntries()) {
                boolean toErase = true;

                for (String element : elements) {
                    if (element != null && element.equals(entry) && board.getObjective(objName).getScore(entry).getScore() == 16 - Arrays.asList(elements).indexOf(element)) {
                        toErase = false;
                        break;
                    }
                }

                if (toErase) {
                    board.resetScores(entry);
                }
            }

            return true;
        } catch (Exception var11) {
            var11.printStackTrace();
            return false;
        }
    }

    public static boolean rankedSidebarDisplay(Player p, String title, HashMap<String, Integer> elements) {
        try {
            title = cutRankedTitle(title);
            elements = cutRanked(elements);
            if (p.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() || p.getScoreboard().getObjectives().size() != 1) {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }

            if (p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)) == null) {
                p.getScoreboard().registerNewObjective(p.getUniqueId().toString().substring(0, 16), "dummy");
                p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)).setDisplaySlot(DisplaySlot.SIDEBAR);
            }

            p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(title);
            Iterator<String> var3 = elements.keySet().iterator();

            String string;
            while (var3.hasNext()) {
                string = var3.next();
                if (p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(string).getScore() != elements.get(string)) {
                    p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(string).setScore(elements.get(string));
                }
            }

            var3 = (new ArrayList<>(p.getScoreboard().getEntries())).iterator();

            while (var3.hasNext()) {
                string = var3.next();
                if (!elements.containsKey(string)) {
                    p.getScoreboard().resetScores(string);
                }
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean rankedSidebarDisplay(Collection<Player> players, String title, HashMap<String, Integer> elements) {
        Iterator<Player> var3 = players.iterator();

        Player player;
        do {
            if (!var3.hasNext()) {
                return true;
            }

            player = var3.next();
        } while (rankedSidebarDisplay(player, title, elements));

        return false;
    }

    public static boolean rankedSidebarDisplay(Collection<Player> players, String title, HashMap<String, Integer> elements, Scoreboard board) {
        try {
            title = cutRankedTitle(title);
            elements = cutRanked(elements);
            String objName = "COLLAB-SB-WINTER";
            if (board == null) {
                board = Bukkit.getScoreboardManager().getNewScoreboard();
            }

            for (Player player : players) {
                if (player.getScoreboard() != board) {
                    player.setScoreboard(board);
                }
            }

            if (board.getObjective(objName) == null) {
                board.registerNewObjective(objName, "dummy");
                board.getObjective(objName).setDisplaySlot(DisplaySlot.SIDEBAR);
            }

            board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(title);

            for (String string : elements.keySet()) {
                if (board.getObjective(DisplaySlot.SIDEBAR).getScore(string).getScore() != elements.get(string)) {
                    board.getObjective(DisplaySlot.SIDEBAR).getScore(string).setScore(elements.get(string));
                }
            }

            for (String string : board.getEntries()) {
                if (!elements.containsKey(string)) {
                    board.resetScores(string);
                }
            }

            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }
}
