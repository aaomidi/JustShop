package com.aaomidi.justshop.engine.global.objects;

import com.aaomidi.justshop.JustShop;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public abstract class JSCommand {
    @Getter
    private final JustShop instance;
    @Getter
    private final String name;
    @Getter
    private final String permission;
    @Getter
    private final boolean forcePlayer;
    @Getter
    private final String usage;

    public abstract boolean execute(Command command, CommandSender commandSender, String[] args);

}
