package com.rocketmail.vaishnavanil.markers;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MarkersSpigot extends JavaPlugin {
    private static MarkersSpigot plugin;
    public static MarkersSpigot getInstance(){
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new CacheManager(),this);
        PluginCommand cmd = this.getServer().getPluginCommand("marker");
                cmd.setExecutor(MarkerCommand.getInstance());
                cmd.setTabCompleter(new MarkerTabCompleter());
        saveConfig();
    }

    @Override
    public void onDisable() {
        for(Player p: Bukkit.getOnlinePlayers()){
            DataManager.getInstance().unloadData(p.getUniqueId());
        }
    }
}
