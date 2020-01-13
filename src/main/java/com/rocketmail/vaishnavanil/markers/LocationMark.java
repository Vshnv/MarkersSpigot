package com.rocketmail.vaishnavanil.markers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationMark {
    private long x;
    private long y;
    private long z;
    private String world;
    public static LocationMark fromLocation(Location l){
        return new LocationMark(l.getX(),l.getY(),l.getZ(),l.getWorld().getName());
    }
    public static LocationMark fromString(String mark){
        String[] locWorld = mark.split(":::");
        String[] coords = locWorld[0].split(";");
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);
        return new LocationMark(x,y,z,locWorld[1]);
    }



    public String toString(){
        StringBuffer result = new StringBuffer();
        result.append(x);
        result.append(';');
        result.append(y);
        result.append(';');
        result.append(z);
        result.append(":::");
        result.append(world);
        return result.toString();
    }
    public LocationMark(double x, double y, double z, String world){
        this.x = Math.round(x);
        this.y = Math.round(y);
        this.z = Math.round(z);
        this.world = world;
    }



    Location pointing;
    public Location getLocation(){
        if(pointing == null){
            pointing = new Location(Bukkit.getWorld(world),x,y,z);
        }
        return pointing;
    }
}
