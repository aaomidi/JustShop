package com.aaomidi.justshop.engine.gui;

import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSCategory;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.utils.Calculations;
import com.aaomidi.justshop.utils.StringManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;

/**
 * @author aaomidi
 */
public class StockInventoryCreator {
    private static HashMap<Integer, Inventory> cachedInventories;

    public StockInventoryCreator() {
        purgeCache();
    }

    public Inventory createInventory(JSCategory jsCategory) {
        if (cachedInventories.containsKey(jsCategory.getId())) {
            return cachedInventories.get(jsCategory.getId());
        }

        List<JSItem> items = GlobalCaching.getItemCache().getCategoryItems(jsCategory.getId());
        Inventory inventory = Bukkit.createInventory(null,
                Calculations.getInventorySize(items.size() + 2),
                StringManager.colorizeString(jsCategory.getName()));

        for (JSItem jsItem : items) {
            inventory.addItem(jsItem.toShopItem());
        }
        inventory.setItem(inventory.getSize() - 1, GlobalCaching.getBackButton().toShopItem());
        cachedInventories.put(jsCategory.getId(), inventory);
        return inventory;
    }

    public void purgeCache() {
        cachedInventories = new HashMap<>();
    }
}
