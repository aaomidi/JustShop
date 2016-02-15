package com.aaomidi.justshop.configuration;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSCategory;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import net.ess3.api.IItemDb;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author aaomidi
 */
public class ConfigurationReader {
    private static FileConfiguration config;
    private static ConfigurationSection generalSettings;
    private static ConfigurationSection defaultSettings;
    private static ConfigurationSection guiSettings;
    private final JustShop instance;

    public ConfigurationReader(JustShop plugin) {
        instance = plugin;
        this.reloadConfiguration();
    }

    public static String getPrefix() {
        return generalSettings.getString("Prefix");
    }

    public static List<String> getDefaultLore() {
        return defaultSettings.getStringList("Lore");
    }

    public static String getGUITitle() {
        return guiSettings.getString("Title");
    }

    public void setupCategories() {
        IItemDb itemDb = JustShop.getEssentials().getItemDb();
        for (Map<?, ?> categories : guiSettings.getMapList("Categories")) {
            JSCategory jsCategory = new JSCategory(instance);
            jsCategory.setId(-1);
            for (Map.Entry<?, ?> entry : categories.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    throw new Error("Unrecoverable error occurred at the category settings.\nKey was not a string.");
                }
                String key = (String) entry.getKey();
                instance.getLogger().log(Level.FINE, String.format("Reading category %s with the value %s", key, String.valueOf(entry.getValue())));
                switch (key.toLowerCase()) {
                    case "id":
                        if (entry.getValue() instanceof Integer) {
                            jsCategory.setId((Integer) entry.getValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the category settings.\nID must be an Integer");
                        }
                        break;
                    case "icon":
                        if (entry.getValue() instanceof String) {
                            try {
                                ItemStack item = itemDb.get((String) entry.getValue());
                                jsCategory.setIcon(item.getType());
                            } catch (Exception e) {
                                throw new Error("Unrecoverable error occurred at the category settings.\nCould not determine what item was meant from: " + entry.getValue());
                            }
                        } else {
                            throw new Error("Unrecoverable error occurred at the category settings.\nIcon must be a String.\nError occurred for the category: " + jsCategory.getId());
                        }
                        break;
                    case "name":
                        if (entry.getValue() instanceof String) {
                            jsCategory.setName((String) entry.getValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the category settings.\nName must be a String.\nError occurred for the category: " + jsCategory.getId());
                        }
                        break;
                    case "lore":
                        if (entry.getValue() instanceof List) {
                            jsCategory.setLore((List) entry.getValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the category settings.\nLore must be a StringList.\nError occurred for the category: " + jsCategory.getId());
                        }
                        break;
                    case "glowing":
                        if (entry.getValue() instanceof Boolean) {
                            jsCategory.setGlowing((Boolean) entry.getValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the category settings.\nGlowing must be a Boolean.\nError occurred for the category: " + jsCategory.getId());
                        }
                        break;
                    default:
                        throw new Error("Unrecoverable error occurred at the category settings.\nUnrecognized argument " + entry.getValue());
                }

            }

            GlobalCaching.getCategoryCache().add(jsCategory);
        }
    }

    public void setupItems() {
        IItemDb itemDb = JustShop.getEssentials().getItemDb();
        for (Map<?, ?> stock : config.getMapList("Stock")) {
            JSItem jsItem = new JSItem(instance);
            for (Map.Entry entry : stock.entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    throw new Error("Unrecoverable error occurred at the stock settings.\nKey was not a string.");
                }
                String key = (String) entry.getKey();
                instance.getLogger().log(Level.FINE, String.format("Reading stock %s with the value %s", key, String.valueOf(entry.getValue())));
                switch (key.toLowerCase()) {
                    case "icon":
                        if (entry.getValue() instanceof String) {
                            try {
                                ItemStack item = itemDb.get((String) entry.getValue());
                                jsItem.setIcon(item.getType());
                            } catch (Exception e) {
                                throw new Error("Unrecoverable error occurred at the stock settings.\nCould not determine what item was meant from: " + entry.getValue());
                            }
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nIcon must be a String.\nError occurred at the Item: " + jsItem.getIcon());
                        }
                        break;
                    case "durability":
                        if (entry.getValue() instanceof Number) {
                            jsItem.setDurability(((Number) entry.getValue()).shortValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nIcon must be a Integer.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    case "name":
                        if (entry.getValue() instanceof String) {
                            jsItem.setName((String) entry.getValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nName must be a String.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    case "lore":
                        if (entry.getValue() instanceof List) {
                            jsItem.setLore((List<String>) entry.getValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nLore must be a List.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    case "buy-price":
                        if (entry.getValue() instanceof Number) {
                            jsItem.setBuyPrice(((Number) entry.getValue()).doubleValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nBuy-Price must be a Double.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    case "sell-price":
                        if (entry.getValue() instanceof Number) {
                            jsItem.setSellPrice(((Number) entry.getValue()).doubleValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nSell-Price must be a Double.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    case "category":
                        if (entry.getValue() instanceof Number) {
                            jsItem.setCategory(((Number) entry.getValue()).intValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nCategory must be a Integer.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    case "glowing":
                        if (entry.getValue() instanceof Boolean) {
                            jsItem.setGlowing((Boolean) entry.getValue());
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nGlowing must be a Boolean.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    case "permission":
                        if (entry.getValue() instanceof String) {
                            jsItem.setPermission(new Permission((String) entry.getValue()));
                        } else {
                            throw new Error("Unrecoverable error occurred at the stock settings.\nPermission must be a String.\nError occurred at the Item: " + jsItem.getName());
                        }
                        break;
                    default:
                        throw new Error("Unrecoverable error occurred at the stock settings.\nUnrecognized argument " + entry.getValue());
                }
            }

            GlobalCaching.getItemCache().add(jsItem);
        }
    }

    public void setupBackButton() {
        JSItem jsItem = new JSItem(instance);
        String icon = guiSettings.getString("Back-Button.Icon");
        Material material;
        try {
            material = JustShop.getEssentials().getItemDb().get(icon).getType();
        } catch (Exception e) {
            throw new Error("Unrecoverable error occurred when creating the Back-Item.\n Material not found.");

        }
        String name = guiSettings.getString("Back-Button.Name");
        List<String> lore = guiSettings.getStringList("Back-Button.Lore");
        Boolean glowing = guiSettings.getBoolean("Back-Button.Glowing");
        if (icon == null || name == null || lore == null || glowing == null || material == null) {
            throw new Error("Unrecoverable error occurred when creating the Back-Item.");
        }
        jsItem.setIcon(material);
        jsItem.setName(name);
        jsItem.setLore(lore);
        jsItem.setGlowing(glowing);
        jsItem.setBackButton(true);
        GlobalCaching.setBackButton(jsItem);
    }

    public void reloadConfiguration() {
        instance.reloadConfig();
        config = instance.getConfig();
        generalSettings = config.getConfigurationSection("General-Settings");
        defaultSettings = config.getConfigurationSection("Default-Settings");
        guiSettings = config.getConfigurationSection("GUI-Settings");
        GlobalCaching.purgeCache();
        setupCategories();
        setupItems();
        setupBackButton();
    }
}
