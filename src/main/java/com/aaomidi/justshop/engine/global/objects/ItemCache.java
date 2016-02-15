package com.aaomidi.justshop.engine.global.objects;

import com.aaomidi.justshop.utils.StaticManager;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;


public class ItemCache implements Iterable<JSItem> {
    private List<JSItem> items = new ArrayList<>();
    private HashMap<JSIdentifier, JSItem> materialJSItems = new HashMap<>();
    private HashMap<Integer, List<JSItem>> guiItems = new HashMap<>();

    /**
     * Get a JSItem using the index i.
     *
     * @param i Index.
     * @return JSItem.
     */
    public JSItem getItem(int i) {
        return items.get(i);
    }

    /**
     * Get a JSItem using the material type of it.
     *
     * @param material   Material of the item.
     * @param durability Type of the material.
     * @return JSItem.
     */
    public JSItem getItemByMaterialAndDurability(Material material, Integer durability) {
        return materialJSItems.get(new JSIdentifier(material, durability));
    }

    /**
     * Get a JSItem list using a category ID.
     *
     * @param i category ID.
     * @return List of JSItems.
     */
    public List<JSItem> getCategoryItems(Integer i) {
        return this.guiItems.get(i);
    }

    /**
     * Adds a JSItem to the cache.
     *
     * @param jsItem JSItem object.
     */
    public void add(JSItem jsItem) {
        JSIdentifier jsIdentifier = new JSIdentifier(jsItem.getIcon(), (int) jsItem.getDurability());
        if (items.contains(jsItem) || materialJSItems.containsKey(jsIdentifier)) {
            return;
        }
        if (jsItem.getIcon() == null || jsItem.getBuyPrice() == -1 || jsItem.getSellPrice() == -1 || jsItem.getCategory() == -1) {
            StaticManager.getInstance().getLogger().log(Level.INFO, String.format("Icon: %s BuyPrice: %s SellPrice: %s Category: %s", jsItem.getName(), jsItem.getBuyPrice(), jsItem.getSellPrice(), jsItem.getCategory()));
            throw new Error("Unrecoverable error occurred when saving the stock with the Name: " + jsItem.getName());
        }
        items.add(jsItem);
        materialJSItems.put(jsIdentifier, jsItem);
        if (!guiItems.containsKey(jsItem.getCategory())) {
            guiItems.put(jsItem.getCategory(), new ArrayList<>());
        }
        guiItems.get(jsItem.getCategory()).add(jsItem);
    }

    @Override
    public Iterator<JSItem> iterator() {
        return items.iterator();
    }
}
