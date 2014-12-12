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

import java.util.*;


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
        System.out.println("fromSign - 1");
        System.out.println("Information: " + information);
        System.out.println("Information: " + Arrays.toString(sign.getLines()));
        if (information == null) {
            System.out.println("fromSign - 2");
            return temp;
        }
        // Split
        String info[] = information.split(":");
        Integer itemDurability;
        if (info.length == 2) {
            try {
                itemDurability = Integer.valueOf(info[1]);
                System.out.println("fromSign - 3");
            } catch (Exception ex) {
                System.out.println("fromSign - 4");
                return temp;
            }
        } else {
            System.out.println("fromSign - 5");
            itemDurability = 0;
        }
        Material mat;
        try {
            String itemType = info[0];
            mat = JustShop.getEssentials().getItemDb().get(itemType).getType();
            System.out.println("fromSign - 6");
        } catch (Exception ex) {
            System.out.println("fromSign - 7");
            return temp;
        }
        if (mat == null) {
            System.out.println("fromSign - 8");
            return temp;
        }
        JSItem jsItem = GlobalCaching.getItemCache().getItemByMaterialAndDurability(mat, itemDurability);
        if (jsItem == null) {
            System.out.println("fromSign - 9");
            return temp;
        }
        // Create the sign.
        temp = new JSSign();
        temp.setLocation(sign.getLocation());
        temp.setItem(jsItem);
        temp.setSignType(sign.getType());
        System.out.println("fromSign - 10");
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
        JSConfig jsConfig = SignConfiguration.getJsConfig();
        List<Map<?, ?>> signs = jsConfig.getConfig().getMapList("Signs");
        Iterator<Map<?, ?>> iterator = signs.iterator();
        while (iterator.hasNext()) {
            Map<?, ?> sign = iterator.next();
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
