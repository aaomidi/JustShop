package com.aaomidi.justshop.utils;

import com.aaomidi.justshop.configuration.ConfigurationReader;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author aaomidi
 */
public class StringManager {
    /**
     * Parse lore arguments.
     *
     * @param lore
     * @param jsItem
     * @return
     */
    public static List<String> parseLore(List<String> lore, JSItem jsItem) {
        // Stupidity check.
        if (jsItem == null) {
            jsItem = new JSItem(StaticManager.getInstance());
            jsItem.setBuyPrice(-1);
            jsItem.setSellPrice(-1);
        }
        for (int i = 0; i < lore.size(); i++) {
            String l = colorizeString(lore.get(i)
                    .replace("{buy-price}", Double.toString(jsItem.getBuyPrice()))
                    .replace("{sell-price}", Double.toString(jsItem.getSellPrice())));
            lore.set(i, l);
        }
        return lore;
    }

    /**
     * Colorize a string using Minecraft color codes.
     *
     * @param s
     * @return
     */
    public static String colorizeString(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(CommandSender player, String message) {
        message = colorizeString(ConfigurationReader.getPrefix() + message);
        player.sendMessage(message);

    }
}
