package com.aaomidi.justshop.commands;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.objects.JSCommand;
import com.aaomidi.justshop.engine.gui.GUICaching;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author aaomidi
 */
public class ShopCommand extends JSCommand {
    public ShopCommand(JustShop instance, String name, String permission, boolean forcePlayer, String usage) {
        super(instance, name, permission, forcePlayer, usage);
    }

    @Override
    public boolean execute(Command command, CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        GUICaching.clearCache(player);
        GUICaching.getOpenMainInventory().add(player);
        Inventory inventory = getInstance().getGuiManager().getCategoryInventoryCreator().getInventory();
        System.out.println(inventory.getTitle());
        System.out.println(player.getName());
        player.openInventory(inventory);
        return true;
    }
}
