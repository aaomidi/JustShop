package com.aaomidi.justshop.engine.global;

import com.aaomidi.justshop.engine.global.objects.CategoryCache;
import com.aaomidi.justshop.engine.global.objects.ItemCache;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.WeakHashMap;

/**
 * @author aaomidi
 */
public class GlobalCaching {
    @Getter
    private static ItemCache itemCache;
    @Getter
    private static CategoryCache categoryCache;
    @Getter
    @Setter
    private static JSItem backButton;
    @Getter
    private static WeakHashMap<Player, JSItem> itemBuyCache;

    public GlobalCaching() {
        purgeCache();
    }

    public static void purgeCache() {
        itemCache = new ItemCache();
        categoryCache = new CategoryCache();
        itemBuyCache = new WeakHashMap<>();
    }

}
