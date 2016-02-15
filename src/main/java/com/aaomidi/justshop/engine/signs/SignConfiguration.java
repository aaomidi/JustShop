package com.aaomidi.justshop.engine.signs;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.engine.signs.objects.JSConfig;
import com.aaomidi.justshop.engine.signs.objects.JSSign;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

public class SignConfiguration {
    @Getter
    private static JSConfig jsConfig;
    private final JustShop instance;
    private final SignManager signManager;
    private File signs;


    public SignConfiguration(JustShop instance, SignManager signManager) {
        this.instance = instance;
        this.signManager = signManager;
        signs = new File(instance.getDataFolder(), "signs.yml");
        this.createFile();
        this.loadSigns();
    }

    public void createFile() {
        try {
            if (!signs.exists()) {
                InputStream in = instance.getResource("signs.yml");
                Files.copy(in, signs.toPath());
            }
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(signs);
            jsConfig = new JSConfig(signs, fileConfiguration);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSSign registerSign(Sign sign, String[] lines) {
        JSSign jsSign = JSSign.fromSign(sign, lines);
        if (jsSign == null) {
            System.out.println("Debug 1 - register");
            return jsSign;
        }
        UUID uuid = UUID.randomUUID();
        Map<String, Object> main = new HashMap<>();
        Map<String, Object> location = new HashMap<>();

        location.put("X", jsSign.getLocation().getX());
        location.put("Y", jsSign.getLocation().getY());
        location.put("Z", jsSign.getLocation().getZ());
        location.put("World", jsSign.getLocation().getWorldName());

        main.put("ID", uuid.toString());
        main.put("Location", location);
        main.put("Item-Type", jsSign.getItem().getIcon().toString());
        main.put("Item-Durability", jsSign.getItem().getDurability());
        main.put("Type", jsSign.getSignType().toString());

        List<Map<?, ?>> list = jsConfig.getConfig().getMapList("Signs");
        list.add(main);

        jsConfig.getConfig().set("Signs", list);
        jsConfig.save();
        jsConfig.load();
        jsSign.setUuid(uuid);
        signManager.getSignCache().add(jsSign);
        return jsSign;
    }

    public void loadSigns() {
        List<Map<?, ?>> signs = jsConfig.getConfig().getMapList("Signs");
        Iterator<Map<?, ?>> iterator = signs.iterator();
        while (iterator.hasNext()) {
            JSSign jsSign = new JSSign();
            Map<?, ?> sign = iterator.next();
            Material mat = null;
            Integer dur = null;

            for (Map.Entry<?, ?> entry : sign.entrySet()) {

                if (!(entry.getKey() instanceof String)) {
                    throw new RuntimeException("Problem when reading the signs.yml file. Key was not a String.");
                }
                String key = (String) entry.getKey();

                switch (key.toLowerCase()) {
                    case "id":
                        if (!(entry.getValue() instanceof String)) {
                            throw new RuntimeException("Problem with reading the signs file. The input ID was not a String.");
                        }
                        UUID uuid = UUID.fromString((String) entry.getValue());
                        jsSign.setUuid(uuid);
                        Bukkit.getLogger().log(Level.INFO, "Now reading sign with the following UUID: " + uuid.toString());
                        break;

                    case "location":
                        System.out.println(entry.getValue().getClass().getSimpleName());
                        if (!(entry.getValue() instanceof LinkedHashMap<?, ?>)) {
                            throw new RuntimeException("Problem with reading the signs file. The input location was not a Map<?, ?>.");
                        }
                        LinkedHashMap<?, ?> locationMap = (LinkedHashMap<?, ?>) entry.getValue();
                        for (Map.Entry<?, ?> locationEntry : locationMap.entrySet()) {
                            System.out.println("Key: " + locationEntry.getKey() + "\nValue: " + locationEntry.getValue());
                            String locationKey = (String) locationEntry.getKey();
                            switch (locationKey.toLowerCase()) {
                                case "x": {
                                    if (!(locationEntry.getValue() instanceof Number)) {
                                        throw new RuntimeException("Problem reading the signs file. The input X was not a number.");
                                    }
                                    jsSign.getLocation().setX((((Number) locationEntry.getValue()).intValue()));
                                    break;
                                }
                                case "y":
                                    if (!(locationEntry.getValue() instanceof Number)) {
                                        throw new RuntimeException("Problem reading the signs file. The input Y was not a number.");
                                    }
                                    jsSign.getLocation().setY((((Number) locationEntry.getValue()).intValue()));
                                    break;
                                case "z":
                                    if (!(locationEntry.getValue() instanceof Number)) {
                                        throw new RuntimeException("Problem reading the signs file. The input Z was not a number.");
                                    }
                                    jsSign.getLocation().setZ((((Number) locationEntry.getValue()).intValue()));
                                    break;
                                case "world":
                                    if (!(locationEntry.getValue() instanceof String)) {
                                        throw new RuntimeException("Problem with reading the signs file. The input World was not of type String");
                                    }
                                    jsSign.getLocation().setWorldName((String) locationEntry.getValue());
                                    break;
                            }
                        }
                        break;

                    case "item-type":
                        if (!(entry.getValue() instanceof String)) {
                            throw new RuntimeException("Problem when reading the signs.yml file. Item-Type was not a String.");
                        }
                        try {
                            mat = Material.valueOf((String) entry.getValue());
                        } catch (Exception e) {
                            throw new RuntimeException("Problem when reading the signs.yml file. Item type was not recognized.");
                        }

                        break;

                    case "item-durability":
                        if (!(entry.getValue() instanceof Number)) {
                            throw new RuntimeException("Problem when reading the signs.yml file. Item-Durability was not a number.");
                        }
                        dur = ((Number) entry.getValue()).intValue();
                        break;
                    case "type":
                        if (!(entry.getValue() instanceof String)) {
                            throw new RuntimeException("Problem when reading the signs.yml file. Type was not a string.");
                        }
                        jsSign.setSignType(Material.valueOf(String.valueOf(entry.getValue())));
                }
            }

            if (mat == null || dur == null) {
                throw new RuntimeException("Material or Durability were not initialized");
            }

            JSItem jsItem = GlobalCaching.getItemCache().getItemByMaterialAndDurability(mat, dur);
            if (jsItem == null) {
                Bukkit.getLogger().log(Level.WARNING, "JSItem  does not exist. Removing sign from configuration.");
                // Remove this part from the main list.
                iterator.remove();
                continue;
            }
            jsSign.setItem(jsItem);
            signManager.getSignCache().add(jsSign);
        }

        jsConfig.getConfig().set("Signs", signs);
        jsConfig.save();
        jsConfig.load();
    }

    public void reload() {
        jsConfig.load();
    }

}
