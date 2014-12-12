package com.aaomidi.justshop.engine.signs;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.signs.events.SignBreakListener;
import com.aaomidi.justshop.engine.signs.events.SignInteractListener;
import com.aaomidi.justshop.engine.signs.events.SignPlaceListener;
import com.aaomidi.justshop.engine.signs.objects.SignCache;
import lombok.Getter;

/**
 * Created by aaomidi on 12/8/2014.
 */
public class SignManager {
    private JustShop instance;
    @Getter
    private SignPlaceListener signPlaceListener;
    @Getter
    private SignInteractListener signInteractListener;
    @Getter
    private SignConfiguration signConfiguration;
    @Getter
    private SignCache signCache;
    @Getter
    private SignsRunnable signsRunnable;
    @Getter
    private SignBreakListener signBreakListener;

    public SignManager(JustShop instance) {
        this.instance = instance;
        this.reloadPlugin();
    }

    public void reloadPlugin() {
        signCache = new SignCache();
        signConfiguration = new SignConfiguration(instance, this);
        signPlaceListener = new SignPlaceListener(instance);
        signBreakListener = new SignBreakListener(instance);
        signInteractListener = new SignInteractListener(instance);
        signsRunnable = new SignsRunnable(instance);
        instance.getServer().getPluginManager().registerEvents(signPlaceListener, instance);
        instance.getServer().getPluginManager().registerEvents(signInteractListener, instance);
        instance.getServer().getPluginManager().registerEvents(signBreakListener, instance);
    }
}
