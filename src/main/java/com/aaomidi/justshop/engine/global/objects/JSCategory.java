package com.aaomidi.justshop.engine.global.objects;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.gui.GUICaching;
import com.aaomidi.justshop.utils.StringManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class JSCategory {
    @Getter
    private final JustShop instance;
    @Setter
    @Getter
    private Integer id;
    @Setter
    @Getter
    private Material icon;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> lore;
    @Getter
    @Setter
    private boolean glowing;
    private ItemStack _item;

    public ItemStack getItemStack() {
        if (_item != null) {
            return _item;
        }
        ItemStack itemStack = new ItemStack(icon, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringManager.colorizeString(name));
        itemMeta.setLore(StringManager.parseLore(lore, null));
        if (glowing) {
            Glow.appply(itemStack);
        }
        itemStack.setItemMeta(itemMeta);
        _item = itemStack;
        return itemStack;
    }

    public void onClick(Player player) {
        Inventory inventory = instance.getGuiManager().getStockInventoryCreator().createInventory(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(inventory);
            }
        }.runTaskLater(instance, 1L);
        GUICaching.getOpenMainInventory().remove(player);
        GUICaching.getOpenCategoryInventory().put(player, id);
    }
}
