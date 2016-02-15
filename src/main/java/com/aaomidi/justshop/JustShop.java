package com.aaomidi.justshop;

import com.aaomidi.justshop.configuration.ConfigurationReader;
import com.aaomidi.justshop.engine.global.CommandsManager;
import com.aaomidi.justshop.engine.global.EventsManager;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.Glow;
import com.aaomidi.justshop.engine.gui.GUIManager;
import com.aaomidi.justshop.engine.signs.SignManager;
import com.earth2me.essentials.Essentials;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * @author aaomidi
 */
public class JustShop extends JavaPlugin {
    @Getter
    public static Economy economy = null;
    @Getter
    private static Essentials essentials;
    @Getter
    private GUIManager guiManager;
    @Getter
    private ConfigurationReader configurationReader;
    @Getter
    private EventsManager eventsManager;
    @Getter
    private CommandsManager commandsManager;
    @Getter
    private SignManager signManager;

    @Override
    public void onLoad() {
        File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            this.saveDefaultConfig();
        }
        this.getLogger().setLevel(Level.INFO);
    }

    @Override
    public void onEnable() {
        this.setupVault();
        this.setupEssentials();
        Glow.register();
        new GlobalCaching();
        configurationReader = new ConfigurationReader(this);
        guiManager = new GUIManager(this);
        eventsManager = new EventsManager(this);
        commandsManager = new CommandsManager(this);
        signManager = new SignManager(this);
    }

    @Override
    public void onDisable() {
    }

    private void setupVault() {
        this.setupEconomy();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    private void setupEssentials() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("Essentials");
        if (plugin == null) {
            this.setEnabled(false);
            this.getLogger().log(Level.SEVERE, "MilkyKore was shut down since Essentials was not found!");
            return;
        }
        essentials = (Essentials) plugin;
    }
}
