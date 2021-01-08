package net.teamfruit.voteplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotePlugin extends JavaPlugin {
    public static VotePlugin instance;

    public boolean isVoting;
    public List<String> votedplayers;
    public Map<String, String> votedata;
    public Map<String, Integer> data;

    public VotePlugin() {
        instance = this;
    }

    public void onEnable() {
        this.isVoting = false;
        this.votedplayers = new ArrayList<>();
        this.votedata = new HashMap<>();
        this.data = new HashMap<>();
        this.getServer().getPluginCommand("vs").setExecutor(new VSCommandExecutor());
        this.getServer().getPluginCommand("v").setExecutor(new VCommandExecutor());
        this.getServer().getPluginCommand("vget").setExecutor(new VGETCommandExecutor());
    }
}
