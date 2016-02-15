package com.aaomidi.justshop.commands;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.global.objects.JSCommand;
import com.aaomidi.justshop.utils.StringManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author aaomidi
 */
public class ShopReloadCommand extends JSCommand {
    public ShopReloadCommand(JustShop instance, String name, String permission, boolean forcePlayer, String usage) {
        super(instance, name, permission, forcePlayer, usage);
    }

    @Override
    public boolean execute(Command command, CommandSender commandSender, String[] args) {
        getInstance().getConfigurationReader().reloadConfiguration();
        getInstance().getSignManager().reload();
        getInstance().getGuiManager().reload();
        getInstance().getSignManager().getSignsRunnable().reCreateSigns();
        StringManager.sendMessage(commandSender, "&bConfiguration reloaded.");
        return true;
    }
}
