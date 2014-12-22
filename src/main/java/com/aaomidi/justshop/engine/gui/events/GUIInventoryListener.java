package com.aaomidi.justshop.engine.gui.events;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSCategory;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.engine.gui.GUICaching;
import com.aaomidi.justshop.utils.StringManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class GUIInventoryListener implements Listener {
    private final JustShop instance;

    @EventHandler
    public void onMainInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack itemStack = event.getCurrentItem();
        int slot = event.getRawSlot();
        if (!GUICaching.getOpenMainInventory().contains(player)) {
            return;
        }
        event.setResult(Event.Result.DENY);
        event.setCancelled(true);
        if (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR || slot < 0 || slot > inventory.getSize() - 1) {
            player.closeInventory();
            StringManager.sendMessage(player, "&cDo not click an empty area.");
            GUICaching.clearCache(player);
            return;
        }
        JSCategory jsCategory = GlobalCaching.getCategoryCache().getCategory(slot);
        if (jsCategory == null) {
            StringManager.sendMessage(player, "&cThere was an error processing your request.");
            GUICaching.clearCache(player);
            instance.getLogger().log(Level.INFO, "The clicked slot returned an empty jsCategory.");
            player.closeInventory();
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                jsCategory.onClick(player);
            }
        }.runTaskLater(instance, 1L);

    }

    @EventHandler
    public void onSecondaryInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack itemStack = event.getCurrentItem();
        int slot = event.getRawSlot();
        if (!GUICaching.getOpenCategoryInventory().containsKey(player)) {
            return;
        }
        event.setResult(Event.Result.DENY);
        event.setCancelled(true);
        int category = GUICaching.getOpenCategoryInventory().get(player);
        if (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR || slot < 0 || slot > inventory.getSize() - 1) {
            player.closeInventory();
            StringManager.sendMessage(player, "&cDo not click an empty area.");
            GUICaching.clearCache(player);
            return;
        }

        if (slot == inventory.getSize() - 1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.closeInventory();
                    player.openInventory(instance.getGuiManager().getCategoryInventoryCreator().getInventory());
                    GUICaching.clearCache(player);
                    GUICaching.getOpenMainInventory().add(player);
                }
            }.runTaskLater(instance, 1L);
            return;
        }
        JSItem jsItem = GlobalCaching.getItemCache().getCategoryItems(category).get(slot);
        if (jsItem == null) {
            StringManager.sendMessage(player, "&cThere was an error processing your request.");
            GUICaching.clearCache(player);
            instance.getLogger().log(Level.INFO, "The clicked slot returned an empty jsItem.");
            player.closeInventory();
            return;
        }
        JSItem newJSItem = jsItem.clone();
        switch (event.getClick()) {
            case RIGHT:
                StringManager.sendMessage(player, "&aPlease enter the amount you want to sell.");
                newJSItem.setBuy(false);
                GlobalCaching.getItemBuyCache().put(player, newJSItem);
                break;
            case LEFT:
                if (!player.hasPermission(newJSItem.getPermission())) {
                    StringManager.sendMessage(player, "&cYou do not have permission to buy this item.");
                    return;
                }
                StringManager.sendMessage(player, "&aPlease enter the amount you want to buy.");
                newJSItem.setBuy(true);
                GlobalCaching.getItemBuyCache().put(player, newJSItem);
                break;
            default:
                StringManager.sendMessage(player, "&cOnly use left and right clicks.");
                newJSItem = null;
                GUICaching.getOpenCategoryInventory().remove(player);
                break;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
            }
        }.runTaskLater(instance, 1L);
    }

    public void onInventoryClose(InventoryCloseEvent event) {

    }
}
