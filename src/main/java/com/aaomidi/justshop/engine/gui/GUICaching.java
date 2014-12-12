package com.aaomidi.justshop.engine.gui;

import com.aaomidi.justshop.engine.global.GlobalCaching;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author aaomidi
 */
public class GUICaching {
    @Getter
    private static Set<Player> openMainInventory;

    @Getter
    private static WeakHashMap<Player, Integer> openCategoryInventory;

    public GUICaching() {
        purgeCache();
    }

    public static void purgeCache() {
        openMainInventory = Collections.newSetFromMap(new WeakHashMap<Player, Boolean>());
        openCategoryInventory = new WeakHashMap<>();

    }

    public static void clearCache(Player player) {
        if (openMainInventory.contains(player)) {
            openMainInventory.remove(player);
        }
        if (openCategoryInventory.containsKey(player)) {
            openCategoryInventory.remove(player);
        }
        if (GlobalCaching.getItemBuyCache().containsKey(player)) {
            GlobalCaching.getItemBuyCache().remove(player);
        }
    }
}
