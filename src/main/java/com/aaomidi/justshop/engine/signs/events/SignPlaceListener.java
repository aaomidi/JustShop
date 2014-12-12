package com.aaomidi.justshop.engine.signs.events;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.signs.objects.JSSign;
import com.aaomidi.justshop.utils.StringManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class SignPlaceListener implements Listener {
    private final JustShop instance;

    @EventHandler
    public void onSignEdit(SignChangeEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
            System.out.println("Debug 1 - edit");
            return;
        }

        Sign sign = (Sign) block.getState();
        if (!event.getLine(0).equalsIgnoreCase("[JustShop]")) {
            System.out.println("Debug 2 - edit");
            return;
        }

        if (!player.hasPermission("justshop.placesign")) {
            StringManager.sendMessage(player, "&cYou do not have permission to create a sign.");
            block.setType(Material.AIR);
            return;
        }
        JSSign jsSign = instance.getSignManager().getSignConfiguration().registerSign(sign, event.getLines());
        if (jsSign == null) {
            StringManager.sendMessage(player, "&cThere was a problem when registering that sign, sign is not registered.");
            block.setType(Material.AIR);
            return;
        } else {
            StringManager.sendMessage(player, "&bSign created successfully.");
            event.setLine(0, StringManager.colorizeString("&l[JustShop]"));
            event.setLine(1, StringManager.colorizeString("&b" + JustShop.getEssentials().getItemDb().name(jsSign.getItem().toItem())));
            event.setLine(2, StringManager.colorizeString("&bbuy: &a$" + jsSign.getItem().getBuyPrice()));
            event.setLine(3, StringManager.colorizeString("&bSell: &c$" + jsSign.getItem().getSellPrice()));
            return;
        }

    }
}

