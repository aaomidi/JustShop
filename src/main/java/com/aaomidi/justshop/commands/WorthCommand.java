package com.aaomidi.justshop.commands;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSCommand;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.utils.StringManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

/**
 * @author aaomidi
 */
public class WorthCommand extends JSCommand {

    public WorthCommand(JustShop instance, String name, String permission, boolean forcePlayer, String usage) {
        super(instance, name, permission, forcePlayer, usage);
    }

    @Override
    public boolean execute(Command command, CommandSender commandSender, String[] args) {
        if (args.length != 1) {
            StringManager.sendMessage(commandSender, getUsage());
            return true;
        }
        ItemStack item;
        String itemInformation[] = args[0].split(":");
        String itemName = itemInformation[0];
        int durability = 0;
        if (itemInformation.length == 2) {
            try {
                durability = Integer.parseInt(itemInformation[1]);
            } catch (NumberFormatException ex) {
                StringManager.sendMessage(commandSender, "&cThe entered durability was not an Integer.");
            }
        }
        try {
            item = JustShop.getEssentials().getItemDb().get(itemName);
        } catch (Exception ex) {
            StringManager.sendMessage(commandSender, "&cThat item was not recognized.");
            return true;
        }
        JSItem jsItem = GlobalCaching.getItemCache().getItemByMaterialAndDurability(item.getType(), durability);
        if (jsItem == null) {
            StringManager.sendMessage(commandSender, "&cThat item does not exist in the shop.");
            return true;
        }
        StringManager.sendMessage(commandSender, String.format("&a%s &bis &a%.2f &bto buy and &a%.2f &bto sell.", JustShop.getEssentials().getItemDb().name(jsItem.toItem()), jsItem.getBuyPrice(), jsItem.getSellPrice()));
        return true;
    }
}
