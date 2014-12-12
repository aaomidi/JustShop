package com.aaomidi.justshop.commands;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.GlobalCaching;
import com.aaomidi.justshop.engine.global.objects.JSCommand;
import com.aaomidi.justshop.engine.global.objects.JSItem;
import com.aaomidi.justshop.utils.StringManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by aaomidi on 11/10/2014.
 */
public class BuyCommand extends JSCommand {
    public BuyCommand(JustShop instance, String name, String permission, boolean forcePlayer, String usage) {
        super(instance, name, permission, forcePlayer, usage);
    }

    @Override
    public boolean execute(Command command, CommandSender commandSender, String[] args) {
        if (args.length <= 0) {
            return false;
        }
        Player player = (Player) commandSender;
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
        if (!player.hasPermission(jsItem.getPermission())) {
            StringManager.sendMessage(commandSender, "&cYou do not have permission to buy that item.");
            return true;
        }
        itemName = JustShop.getEssentials().getItemDb().name(item);

        switch (args.length) {
            case 1:
                StringManager.sendMessage(commandSender, String.format("&aPlease enter the amount of &6%s &ayou want to buy.", itemName));
                GlobalCaching.getItemBuyCache().put(player, jsItem);
                break;
            case 2:
                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    StringManager.sendMessage(commandSender, "&cAmount must be a number.");
                    return true;
                }
                if (amount <= 0) {
                    StringManager.sendMessage(commandSender, "&cMoo!");
                }
                jsItem.onItemBuy(player, amount);
                break;
            default:
                return false;
        }
        return true;
    }
}
