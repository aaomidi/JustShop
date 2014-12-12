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
 * @author aaomidi
 */
public class SellCommand extends JSCommand {
    public SellCommand(JustShop instance, String name, String permission, boolean forcePlayer, String usage) {
        super(instance, name, permission, forcePlayer, usage);
    }

    @Override
    public boolean execute(Command command, CommandSender commandSender, String[] args) {
        if (args.length <= 0) {
            return false;
        }
        Player player = (Player) commandSender;
        ItemStack item;
        String itemName = args[0];
        JSItem jsItem;
        switch (args[0].toLowerCase()) {
            case "hand":
                item = player.getItemInHand();
                if (item == null) {
                    StringManager.sendMessage(player, "&cYou can't sell nothing.");
                    return true;
                }
                jsItem = GlobalCaching.getItemCache().getItemByMaterialAndDurability(item.getType(), (int) item.getDurability());
                if (jsItem == null || jsItem.getSellPrice() <= 0) {
                    StringManager.sendMessage(commandSender, "&cThat item does not exist in the shop or you may not sell it.");
                    return true;
                }
                jsItem.onItemSell(player, item.getAmount());
                return true;
            case "all":
                StringBuilder sb = new StringBuilder("&aWe couldn't sell the following items.\n");
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null) {
                        continue;
                    }
                    itemName = JustShop.getEssentials().getItemDb().name(itemStack);
                    jsItem = GlobalCaching.getItemCache().getItemByMaterialAndDurability(itemStack.getType(), (int) itemStack.getDurability());
                    if (jsItem == null || jsItem.getSellPrice() <= 0) {
                        sb.append(String.format("&b%s\n", itemName));
                        continue;
                    }
                    jsItem.onItemSell(player, itemStack.getAmount());
                }
                if (sb.length() > 1) {
                    StringManager.sendMessage(player, sb.toString());
                }
                return true;
            default:
                try {
                    item = JustShop.getEssentials().getItemDb().get(itemName);
                } catch (Exception ex) {
                    StringManager.sendMessage(commandSender, "&cThat item was not recognized.");
                    return true;
                }
                jsItem = GlobalCaching.getItemCache().getItemByMaterialAndDurability(item.getType(), (int) item.getDurability());
                if (jsItem == null || jsItem.getSellPrice() <= 0) {
                    StringManager.sendMessage(commandSender, "&cThat item does not exist in the shop or you may not sell it.");
                    return true;
                }
                itemName = JustShop.getEssentials().getItemDb().name(item);
                if (args.length == 1) {
                    StringManager.sendMessage(commandSender, String.format("&aPlease enter the amount of &6%s &ayou want to buy.", itemName));
                    GlobalCaching.getItemBuyCache().put(player, jsItem);
                    return true;
                }
                if (args.length == 2) {
                    int amount;
                    try {
                        amount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        StringManager.sendMessage(commandSender, "&cAmount must be a number.");
                        return true;
                    }
                    if (amount <= 0) {
                        StringManager.sendMessage(commandSender, "&cMoo!");
                        return true;
                    }
                    jsItem.onItemSell(player, amount);
                    return true;
                }
                return false;
        }
    }
}
