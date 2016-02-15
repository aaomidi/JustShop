package com.aaomidi.justshop.engine.global.objects;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.configuration.ConfigurationReader;
import com.aaomidi.justshop.engine.gui.GUICaching;
import com.aaomidi.justshop.utils.PlayerInventoryManager;
import com.aaomidi.justshop.utils.StringManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import java.util.List;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class JSItem {
    @Getter
    private final JustShop instance;
    @Getter
    @Setter
    private boolean isBackButton = false;
    @Getter
    @Setter
    /**
     * This is required.
     */
    private Material icon;
    @Getter
    @Setter
    private short durability = 0;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> lore;
    @Getter
    @Setter
    /**
     * This is required.
     */
    private double buyPrice = -1;
    @Getter
    @Setter
    /**
     * This is required.
     */
    private double sellPrice = -1;
    @Getter
    @Setter
    /**
     * This is required.
     */
    private int category = -1;
    @Getter
    @Setter
    private boolean glowing;
    @Getter
    @Setter
    private Permission permission = new Permission("justshop.base");
    private ItemStack _item;
    @Getter
    @Setter
    private boolean isBuy = false;

    public ItemStack toShopItem() {
        if (_item != null) {
            return _item;
        }
        ItemStack itemStack = new ItemStack(icon, 1, durability);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (glowing) {
            Glow.appply(itemStack);
        }
        if (name != null) {
            itemMeta.setDisplayName(StringManager.colorizeString(name));
        }
        if (lore != null) {
            itemMeta.setLore(StringManager.parseLore(lore, this));
        } else {
            itemMeta.setLore(StringManager.parseLore(ConfigurationReader.getDefaultLore(), this));
        }
        itemStack.setItemMeta(itemMeta);
        _item = itemStack;
        return itemStack;
    }

    public ItemStack toItem() {
        ItemStack temp = new ItemStack(icon, 1);
        temp.setDurability(durability);
        return temp;
    }

    // Item Buy
    public boolean onItemBuy(Player player, int amount) {
        if (amount <= 0) {
            return false;
        }

        EconomyResponse response = JustShop.getEconomy().withdrawPlayer(player, buyPrice * amount);
        if (response.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
            PlayerInventoryManager.addItems(player, toItem(), amount);
            StringManager.sendMessage(player, String.format("&aYou successfully bought &6%d %s&a.", amount, JustShop.getEssentials().getItemDb().name(toItem())));
        } else {
            StringManager.sendMessage(player, "&cYou do not have enough money to do that transaction.");
        }
        GUICaching.clearCache(player);
        return true;
    }

    // Item Sell
    public boolean onItemSell(Player player, int amount) {
        if (sellPrice <= 0) {
            return false;
        }
        int count = PlayerInventoryManager.countItems(player, toItem());
        if (count < amount) {
            StringManager.sendMessage(player, "&cYou do not have enough of that item.");
        } else {
            PlayerInventoryManager.removeItems(player, toItem(), amount);
            EconomyResponse response = JustShop.getEconomy().depositPlayer(player, amount * sellPrice);
            StringManager.sendMessage(player, String.format("&aYou successfully sold &6%d %s&a.", amount, JustShop.getEssentials().getItemDb().name(toItem())));
        }
        GUICaching.clearCache(player);
        return true;
    }

    @Override
    public JSItem clone() {
        JSItem newJSItem = new JSItem(instance);
        newJSItem.setBackButton(isBackButton);
        newJSItem.setIcon(icon);
        newJSItem.setDurability(durability);
        newJSItem.setName(name);
        newJSItem.setLore(lore);
        newJSItem.setBuyPrice(buyPrice);
        newJSItem.setSellPrice(sellPrice);
        newJSItem.setCategory(category);
        newJSItem.setGlowing(glowing);
        newJSItem.setBuy(isBuy);
        return newJSItem;
    }
}
