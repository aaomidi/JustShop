package com.aaomidi.justshop.engine.signs.events;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.engine.signs.objects.JSSign;
import com.aaomidi.justshop.utils.StringManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class SignInteractListener implements Listener {
    private final JustShop instance;

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!(block.getState() instanceof Sign)) {
            System.out.println("Debug 1 - edit");
            return;
        }
        JSSign jsSign = instance.getSignManager().getSignCache().getSignAtLocation(block.getLocation());
        if (jsSign == null) {
            System.out.println("Debug 2 - interact");
            return;
        }
        JSItem newJSItem = jsSign.getItem().clone();

        switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK: {
                if (newJSItem.getSellPrice() <= 0) {
                    StringManager.sendMessage(player, "&cThis item is not saleable.");
                    return;
                }
                StringManager.sendMessage(player, "&aPlease enter the amount you want to sell.");
                newJSItem.setBuy(false);
                GlobalCaching.getItemBuyCache().put(player, newJSItem);
                break;
            }
            case LEFT_CLICK_BLOCK: {
                if (!player.hasPermission(newJSItem.getPermission())) {
                    StringManager.sendMessage(player, "&cYou do not have permission to buy this item.");
                    return;
                }
                StringManager.sendMessage(player, "&aPlease enter the amount you want to buy.");
                newJSItem.setBuy(true);
                GlobalCaching.getItemBuyCache().put(player, newJSItem);
                break;
            }
        }
    }
}
