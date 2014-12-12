package com.aaomidi.justshop.engine.gui;

import com.aaomidi.justshop.JustShop;
import lombok.Getter;

/**
 * @author aaomidi
 */
public class GUIManager {
    private final JustShop instance;
    @Getter
    private CategoryInventoryCreator categoryInventoryCreator;
    @Getter
    private StockInventoryCreator stockInventoryCreator;

    public GUIManager(JustShop instance) {
        this.instance = instance;
        this.reloadPlugin();
    }

    public void reloadPlugin() {
        categoryInventoryCreator = null;
        categoryInventoryCreator = new CategoryInventoryCreator(instance);
        stockInventoryCreator = null;
        stockInventoryCreator = new StockInventoryCreator();
        new GUICaching();
    }
}
