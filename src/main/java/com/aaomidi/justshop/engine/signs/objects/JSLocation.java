package com.aaomidi.justshop.engine.signs.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.logging.Level;

/**
 * @author aaomidi
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class JSLocation {
    @Getter
    @Setter
    int x;
    @Getter
    @Setter
    int y;
    @Getter
    @Setter
    int z;
    @Getter
    @Setter
    String worldName;

    public Location toLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().log(Level.WARNING, "World was null, did not return location.");
            return null;
        }
        return new Location(world, x, y, z);
    }

    public JSLocation fromLocation(Location location) {
        JSLocation jsLocation = new JSLocation();
        jsLocation.setX(location.getBlockX());
        jsLocation.setY(location.getBlockY());
        jsLocation.setZ(location.getBlockZ());
        jsLocation.setWorldName(location.getWorld().getName());
        return jsLocation;
    }


    public Block getBlock() {
        Location location = toLocation();
        if (location != null) {
            return location.getBlock();
        }
        return null;
    }
}
