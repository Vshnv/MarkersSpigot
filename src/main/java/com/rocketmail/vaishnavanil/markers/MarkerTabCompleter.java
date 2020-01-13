package com.rocketmail.vaishnavanil.markers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkerTabCompleter implements TabCompleter
{
    private static List<String> arg1 = Arrays.asList("track","view","set","delete","help");
    private static List<String> arg1ADMIN;
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("")){
                if(sender.hasPermission("Markers.Admin")){
                    if(arg1ADMIN==null){
                        arg1ADMIN = Arrays.asList("track","view","set","delete","help","admin");
                    }
                    return arg1ADMIN;
                }
                return arg1;
            }
            List<String> result = new ArrayList<>();
            for(String s :arg1){
                if(s.startsWith(args[0].toLowerCase())){
                    result.add(s);
                }
            }
            return result;
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("admin")){
                if(!sender.hasPermission("Markers.Admin")){
                    return null;
                }
            }else{
                if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("view")){
                    return null;
                }
                if(sender instanceof Player){
                    return Arrays.asList(DataManager.getInstance().getMarkersString(((Player)sender).getUniqueId()).split(", "));
                }
            }
        }
        return null;
    }
}
