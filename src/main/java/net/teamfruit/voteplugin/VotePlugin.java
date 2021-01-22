package net.teamfruit.voteplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class VotePlugin extends JavaPlugin {
    public void onEnable() {
        VoteAPI.getInstance().init();

        VCommandExecutor command = new VCommandExecutor();

        getServer().getPluginCommand("v").setExecutor(command);
        getServer().getPluginCommand("v").setTabCompleter(command);
        getServer().getPluginCommand("vs").setExecutor(new VSCommandExecutor());
        getServer().getPluginCommand("vget").setExecutor(new VGETCommandExecutor());
    }
}
