package com.rocketmail.vaishnavanil.markers;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataManager {
    private static DataManager data = new DataManager();
    private DataManager(){}

    public static DataManager getInstance(){
        return data;
    }


    private HashMap<String,HashMap<String,LocationMark>> markMap = new HashMap<>();

    public boolean hasLocationName(UUID id,String mName){
        return markMap.get(id.toString()).containsKey(mName);
    }
    public LocationMark getLocation(UUID id,String mName){
        return markMap.get(id.toString()).get(mName);
    }
    public void setMarker(UUID id,String name,Location l){
        String uid = id.toString();
        LocationMark lm = LocationMark.fromLocation(l);
        markMap.get(uid).put(name,lm);
    }
    public boolean removeMarker(UUID id,String name){
        String uid = id.toString();
        if(!markMap.get(uid).containsKey(name))return false;
        markMap.get(uid).remove(name);
        return true;
    }
    public void loadData(UUID id){
        MarkersSpigot.getInstance().reloadConfig();
        FileConfiguration config = MarkersSpigot.getInstance().getConfig();
        String uid = id.toString();
        HashMap<String,LocationMark> locs = new HashMap<>();
        if(config.isSet("markers."+uid)){
            //PLAYER DATA EXISTS
            List<String> marks = config.getStringList("markers."+uid);
            for(String m:marks){
                String[] arg = m.split(" @@ ");
                String name = arg[0];
                LocationMark mark = LocationMark.fromString(arg[1]);
                locs.put(name,mark);
            }
        }
        markMap.put(uid,locs);

    }

    public String getMarkersString(UUID id){
        String uid = id.toString();
        if(markMap.containsKey(uid)){
            HashMap<String,LocationMark> marksRAW = markMap.get(uid);
            StringBuffer sb = new StringBuffer();
            boolean a = true;
            for(String name:marksRAW.keySet()){
                if (a){
                    a = false;
                    sb.append(name);
                    continue;
                }
                sb.append(", "+name);
            }
            return sb.toString();
        }
        return "None!";
    }
    public int getMarkerCount(UUID id){
        String uid = id.toString();
        if(markMap.containsKey(uid)){
            HashMap<String,LocationMark> marksRAW = markMap.get(uid);
            return marksRAW.size();
        }
        return 0;
    }
    public void saveData(UUID id){
        String uid = id.toString();
        if(markMap.containsKey(uid)){
            HashMap<String,LocationMark> marksRAW = markMap.get(uid);
            List<String> marks = new ArrayList<>();
            for(String name:marksRAW.keySet()){
                marks.add(name +" @@ " + marksRAW.get(name).toString());
            }
            FileConfiguration config = MarkersSpigot.getInstance().getConfig();
            config.set("markers." + uid,marks);
            MarkersSpigot.getInstance().saveConfig();
        }

        return;
    }
    public void unloadData(UUID id){
        saveData(id);
        markMap.remove(id);
    }

}
