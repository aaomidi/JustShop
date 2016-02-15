package com.aaomidi.justshop.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

/**
 * @author aaomidi
 */
public class PlayerInventoryManager {
    public static void addItems(Player player, ItemStack itemStack, int amount) {
        PlayerInventory inventory = player.getInventory();
        itemStack.setAmount(amount);
        HashMap<Integer, ItemStack> excessItems = inventory.addItem(itemStack);
        if (!excessItems.isEmpty()) {
            StringManager.sendMessage(player, "&aNot all the items you bought fit in your inventory so we are dropping the rest on the ground.");
            for (ItemStack excessItem : excessItems.values()) {
                player.getLocation().getWorld().dropItem(player.getLocation(), excessItem);
            }
        }
    }

    public static int countItems(Player player, ItemStack itemStack) {
        PlayerInventory inventory = player.getInventory();
        int count = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) continue;
            if (item.getType().equals(itemStack.getType())
                    && item.getDurability() == itemStack.getDurability()) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public static void removeItems(Player player, ItemStack itemStack, int amount) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize() && amount > 0; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType().equals(Material.AIR)) continue;
            if (item.getType().equals(itemStack.getType())
                    && item.getDurability() == itemStack.getDurability()) {
                amount -= item.getAmount();
                if (amount >= 0) {
                    inventory.setItem(i, null);
                } else {
                    item.setAmount(-1 * amount);
                    break;
                }
            }
        }
    }
}
