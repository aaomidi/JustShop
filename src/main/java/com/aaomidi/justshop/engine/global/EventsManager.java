package com.aaomidi.justshop.engine.global;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.gui.events.GUIChatListener;
import com.aaomidi.justshop.engine.gui.events.GUIInventoryListener;
import org.bukkit.event.Listener;

/**
 * @author aaomidi
 */
public class EventsManager {
    private final JustShop instance;

    public EventsManager(JustShop instance) {
        this.instance = instance;
        this.registerEvents();
    }

    private void registerEvents() {
        registerEvent(new GUIChatListener(instance));
        registerEvent(new GUIInventoryListener(instance));

    }

    public void registerEvent(Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }
}
