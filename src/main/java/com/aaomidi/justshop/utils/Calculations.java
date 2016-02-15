package com.aaomidi.justshop.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author aaomidi
 */
public class Calculations {
    public static int getInventorySize(int size) {
        return size + (9 - (size % 9));
    }

    public static Map<String, String> serializeLocation(Location location) {
        Map<String, String> serializedLocation = new HashMap<>();
        serializedLocation.put("x", String.valueOf(location.getBlockX()));
        serializedLocation.put("y", String.valueOf(location.getBlockY()));
        serializedLocation.put("z", String.valueOf(location.getBlockZ()));
        serializedLocation.put("world", location.getWorld().getUID().toString());
        return serializedLocation;
    }

    public static Location deserializeLocation(Map<String, String> map) {
        World world = Bukkit.getWorld(UUID.fromString(map.get("World")));
        return new Location(world, Integer.valueOf(map.get("x")), Integer.valueOf(map.get("y")), Integer.valueOf(map.get("z")));
    }

}
