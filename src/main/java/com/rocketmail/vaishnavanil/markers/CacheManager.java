package com.rocketmail.vaishnavanil.markers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CacheManager implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        DataManager.getInstance().loadData(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        DataManager.getInstance().unloadData(e.getPlayer().getUniqueId());
    }
}
