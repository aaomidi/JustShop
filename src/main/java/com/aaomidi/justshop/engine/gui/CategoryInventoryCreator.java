package com.aaomidi.justshop.engine.gui;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.configuration.ConfigurationReader;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSCategory;
import com.aaomidi.justshop.utils.Calculations;
import com.aaomidi.justshop.utils.StringManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

/**
 * @author aaomidi
 */
public class CategoryInventoryCreator implements InventoryHolder {
    private final JustShop instance;
    private Inventory inventory;

    public CategoryInventoryCreator(JustShop instance) {
        this.instance = instance;
        this.purgeCache();
    }

    private Inventory _getInventory() {
        List<JSCategory> categories = GlobalCaching.getCategoryCache().getCategories();
        System.out.println(categories);
        System.out.println(categories.size());
        inventory = Bukkit.createInventory(this,
                Calculations.getInventorySize(categories.size()),
                StringManager.colorizeString(ConfigurationReader.getGUITitle()));
        for (JSCategory jsCategory : categories) {
            inventory.addItem(jsCategory.getItemStack());
        }
        return inventory;
    }

    public Inventory getInventory() {
        if (inventory != null) {
            return inventory;
        }
        return this._getInventory();
    }

    public void purgeCache() {
        inventory = null;
    }
}
