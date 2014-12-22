package com.aaomidi.justshop.engine.global;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.commands.*;
import com.aaomidi.justshop.engine.global.objects.JSCommand;
import com.aaomidi.justshop.utils.StringManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aaomidi
 */
public class CommandsManager implements CommandExecutor {
    private final JustShop instance;
    private Map<String, JSCommand> jsCommands;

    public CommandsManager(JustShop instance) {
        this.instance = instance;
        jsCommands = new HashMap<>();
        registerCommands();
    }

    private void registerCommands() {
        //TODO: Add commands that need registerin'
        registerCommand(new ShopCommand(instance, "shop", "justshop.shop", true, "&b/shop"));
        registerCommand(new BuyCommand(instance, "buy", "justshop.buy", true, "&b/buy &a[Item] [Amount]"));
        registerCommand(new SellCommand(instance, "sell", "justshop.sell", true, "&b/sell &a[hand/all/item]"));
        registerCommand(new ShopReloadCommand(instance, "shopreload", "justshop.reload", false, "&b/shopreload"));
        registerCommand(new WorthCommand(instance, "worth", "justshop.worth", false, "&b/worth &a[Item]"));
    }

    public void registerCommand(JSCommand jsCommand) {
        instance.getCommand(jsCommand.getName().toLowerCase()).setExecutor(this);
        jsCommands.put(jsCommand.getName().toLowerCase(), jsCommand);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String commandLabel, String[] args) {
        if (!jsCommands.containsKey(cmd.getName())) {
            throw new RuntimeException(String.format("Command %s was not recognized.", cmd.getName()));
        }
        JSCommand jsCommand = jsCommands.get(cmd.getName());
        if (!commandSender.hasPermission(jsCommand.getPermission())) {
            StringManager.sendMessage(commandSender, "&cYou do not have permission to execute that command.");
            return true;
        }
        if (jsCommand.isForcePlayer() && !(commandSender instanceof Player)) {
            StringManager.sendMessage(commandSender, "&cThis command is only executable in game.");
            return true;
        }
        if (!jsCommand.execute(cmd, commandSender, args)) {
            StringManager.sendMessage(commandSender, jsCommand.getUsage());
            return true;
        }
        return true;
    }
}
