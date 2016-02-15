package com.aaomidi.justshop.utils;

import com.aaomidi.justshop.JustShop;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author aaomidi
 */
public class StaticManager {
    public static JustShop getInstance() {
        return JavaPlugin.getPlugin(JustShop.class);
    }
}
