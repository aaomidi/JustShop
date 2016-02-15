package com.aaomidi.justshop.engine.signs.objects;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.engine.signs.SignConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author aaomiWell
 */
/*
    [JustShop]
    ItemType:Durability
 */
@RequiredArgsConstructor
public class JSSign {
    @Getter
    @Setter
    private UUID uuid;
    @Getter
    @Setter
    private JSLocation location = new JSLocation();
    @Getter
    @Setter
    private JSItem item;
    @Getter
    @Setter
    private Material signType;

    public static JSSign fromSign(Sign sign, String lines[]) {
        JSSign temp = null;
        String information = lines[1];
        if (information == null) {
            return temp;
        }
        // Split
        String info[] = information.split(":");
        Integer itemDurability;
        if (info.length == 2) {
            try {
                itemDurability = Integer.valueOf(info[1]);
            } catch (Exception ex) {
                return temp;
            }
        } else {
            itemDurability = 0;
        }
        Material mat;
        try {
            String itemType = info[0];
            mat = JustShop.getEssentials().getItemDb().get(itemType).getType();
        } catch (Exception ex) {
            return temp;
        }
        if (mat == null) {
            return temp;
        }
        JSItem jsItem = GlobalCaching.getItemCache().getItemByMaterialAndDurability(mat, itemDurability);
        if (jsItem == null) {
            return temp;
        }
        // Create the sign.
        temp = new JSSign();
        temp.setLocation(sign.getLocation());
        temp.setItem(jsItem);
        temp.setSignType(sign.getType());
        return temp;
    }


    public void setLocation(Location loc) {
        location = new JSLocation();
        location.setX(loc.getBlockX());
        location.setY(loc.getBlockY());
        location.setZ(loc.getBlockZ());
        location.setWorldName(loc.getWorld().getName());
    }

    public void unregister() {
        System.out.println(uuid.toString());
        JSConfig jsConfig = SignConfiguration.getJsConfig();
        List<Map<?, ?>> signs = jsConfig.getConfig().getMapList("Signs");
        Iterator<Map<?, ?>> iterator = signs.iterator();
        while (iterator.hasNext()) {
            Map<?, ?> sign = iterator.next();
            System.out.println(sign);
            if (!sign.containsKey("ID")) {
                continue;
            }
            if (!sign.get("ID").equals(uuid.toString())) {
                System.out.println(sign.get("ID"));
                continue;
            }
            System.out.println("Woot, deleted.");
            iterator.remove();
            break;
        }
        jsConfig.getConfig().set("Signs", signs);
        jsConfig.save();
        jsConfig.load();
    }


}
