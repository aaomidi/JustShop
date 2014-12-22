package com.aaomidi.justshop.engine.gui.events;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.engine.gui.GUICaching;
import com.aaomidi.justshop.utils.StringManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class GUIChatListener implements Listener {
    private final JustShop instance;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!GlobalCaching.getItemBuyCache().containsKey(player)) return;
        event.setCancelled(true);
        event.getRecipients().clear();
        JSItem jsItem = GlobalCaching.getItemBuyCache().get(player);
        if (jsItem.getSellPrice() <= 0 && !jsItem.isBuy()) {
            StringManager.sendMessage(player, "&cThat item is not saleable.");
            return;
        }
        String message = event.getMessage();
        Integer amount = 0;
        try {
            amount = Integer.valueOf(message);
        } catch (NumberFormatException ex) {
            StringManager.sendMessage(player, "&cYour entry was not a number.");
            GUICaching.clearCache(player);
        }
        if (amount <= 0) {
            StringManager.sendMessage(player, "&cYour entry must be larger than 0.");
            GUICaching.clearCache(player);
            return;
        }
        if (jsItem.isBuy()) {
            jsItem.onItemBuy(player, amount);
        } else {
            jsItem.onItemSell(player, amount);
        }

    }
}