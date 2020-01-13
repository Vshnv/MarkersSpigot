package com.rocketmail.vaishnavanil.markers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.UUID;

public class MarkerCommand implements CommandExecutor {
    private static MarkerCommand ins = new MarkerCommand();
    public static MarkerCommand getInstance(){
        return ins;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0){
            sender.sendMessage(ChatColor.GREEN + "Not enough arguments! use /marker help");
            return true;
        }
        String subcommand = args[0];
        switch (subcommand.toLowerCase()){
            case "track":
                if(!(sender instanceof Player)){
                    sender.sendMessage("This sub-command can only be used by Players!");
                    return true;
                }
                if(args.length != 2){
                    sender.sendMessage(ChatColor.RED + "Invalid arguments! use /marker track <MARKER_NAME>");
                    return true;
                }
                String mName = args[1];
                Player tr_p = (Player) sender;
                if(DataManager.getInstance().hasLocationName(tr_p.getUniqueId(),mName)){
                    tr_p.setCompassTarget(DataManager.getInstance().getLocation(tr_p.getUniqueId(),mName).getLocation());
                    sender.sendMessage(ChatColor.GREEN + "Your compass now tracks your marker : " + mName);
                }else{
                    tr_p.sendMessage(ChatColor.RED+"No MarkerLocation with name +" + mName + " found!");
                }
                break;
            case "set":
                if(!(sender instanceof Player)){
                    sender.sendMessage("This sub-command can only be used by Players!");
                    return true;
                }
                if(args.length != 2){
                    sender.sendMessage(ChatColor.RED + "Invalid arguments! use /marker set <MARKER_NAME>");
                    return true;
                }
                String mSetName = args[1];
                Player set_p = (Player) sender;
                int max = getSetCount(set_p);
                if(max <= DataManager.getInstance().getMarkerCount(set_p.getUniqueId())){
                    sender.sendMessage(ChatColor.RED + "You are allowed to have only " + max + " number of markers!");
                    return true;
                }
                DataManager.getInstance().setMarker(set_p.getUniqueId(),mSetName,set_p.getLocation());
                sender.sendMessage(ChatColor.GREEN + "You have set a new marker here for yourself : " + mSetName);
                break;
            case "delete":
                if(!(sender instanceof Player)){
                    sender.sendMessage("This sub-command can only be used by Players!");
                    return true;
                }
                if(args.length != 2){
                    sender.sendMessage(ChatColor.RED + "Invalid arguments! use /marker delete <MARKER_NAME>");
                    return true;
                }
                String mDelName = args[1];
                Player del_p = (Player) sender;
                if(DataManager.getInstance().removeMarker(del_p.getUniqueId(),mDelName)){
                    sender.sendMessage(ChatColor.GREEN + "Successfully deleted your marker : " + mDelName);
                }else{
                    sender.sendMessage(ChatColor.RED + "Could not find your marker with name : " + mDelName);
                }
                break;
            case "view":
                if(!(sender instanceof Player)){
                    sender.sendMessage("This sub-command can only be used by Players!");
                    return true;
                }
                Player view_p = (Player) sender;
                String markers = DataManager.getInstance().getMarkersString(view_p.getUniqueId());
                view_p.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"Showing your markers :");
                view_p.sendMessage(ChatColor.GREEN+markers);
                break;
            case "admin":
                if(!(sender instanceof Player)){
                    sender.sendMessage("This sub-command can only be used by Players!");
                    return true;
                }
                Player admin = (Player)sender;
                if(!admin.hasPermission("Markers.Admin")){
                    sender.sendMessage(ChatColor.RED +"You do not have permission to use this command!");
                    return true;
                }
                String sub = 1 >=args.length ? "":args[1];
                String target = 2 >= args.length ? "":args[2];
                String value = 3 >=args.length ? "":args[3];
                admin(admin,sub,target,value);
                break;
            case "help":
                sender.sendMessage(ChatColor.DARK_GRAY +"=Help=");
                sender.sendMessage(ChatColor.GRAY +"/marker track <MarkerName>");
                sender.sendMessage(ChatColor.GRAY +"/marker set <MarkerName>");
                sender.sendMessage(ChatColor.GRAY +"/marker delete <MarkerName>");
                sender.sendMessage(ChatColor.GRAY +"/marker view <MarkerName>");
                if(sender.hasPermission("Markers.Admin")){
                    sender.sendMessage(ChatColor.RED +"/marker admin help");
                }
                break;
        }

        return true;
    }


    public void admin(Player admin,String sub,String target,String value){
        switch (sub){
            case "view":
                Player t = null;
                for(Player on: Bukkit.getOnlinePlayers()){
                    if(on.getName().equals(target)){
                        t = on;
                        break;
                    }
                }
                if(t== null){
                    admin.sendMessage(ChatColor.GRAY + "Could not find online target with name : " + target);
                    break;
                }
                String markers = DataManager.getInstance().getMarkersString(t.getUniqueId());
                admin.sendMessage(ChatColor.GRAY+""+ChatColor.BOLD+"Showing "+target+"'s markers :");
                admin.sendMessage(ChatColor.GRAY+markers);
                break;
            case "track":
                Player tr = null;
                for(Player on: Bukkit.getOnlinePlayers()){
                    if(on.getName().equals(target)){
                        tr = on;
                        break;
                    }
                }
                if(tr== null){
                    admin.sendMessage(ChatColor.GRAY + "Could not find online target with name : " + target);
                    break;
                }
                if(DataManager.getInstance().hasLocationName(tr.getUniqueId(),value)){
                    admin.setCompassTarget(DataManager.getInstance().getLocation(tr.getUniqueId(),value).getLocation());
                    admin.sendMessage(ChatColor.GRAY + "Your compass now tracks your marker : " + value);
                }else{
                    admin.sendMessage(ChatColor.GRAY+"No MarkerLocation with name " + value + " found!");
                }
                break;
            case "?":
            case "help":
                admin.sendMessage(ChatColor.DARK_GRAY + "try /marker admin view/track");
                break;
                default:
                    admin.sendMessage(ChatColor.DARK_GRAY + "Could not find mentioned admin command. try /marker admin help");
                    break;

        }


    }
    HashMap<UUID,Integer> setCount = new HashMap<>();
    public int getSetCount(Player p){
        if(setCount.containsKey(p.getUniqueId())){
            return setCount.get(p.getUniqueId());
        }
        if(p.hasPermission("Markers.Admin")){
            setCount.put(p.getUniqueId(),10);
            return 10;
        }

            for(int i = 10; i>0;i--){
                if(p.hasPermission("Markers.Use." + i)){
                    setCount.put(p.getUniqueId(),i);
                    return i;
                }
            }
        if(p.hasPermission("Markers.Use")){
            setCount.put(p.getUniqueId(),1);
            return 1;
        }
        return 0;
    }
}
