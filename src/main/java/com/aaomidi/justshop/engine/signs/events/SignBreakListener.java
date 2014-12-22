package com.aaomidi.justshop.engine.signs.events;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.signs.objects.JSSign;
import com.aaomidi.justshop.utils.StringManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class SignBreakListener implements Listener {
    private final JustShop instance;

    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
            return;
        }
        JSSign jsSign = instance.getSignManager().getSignCache().getSignAtLocation(block.getLocation());
        if (jsSign == null) {
            return;
        }
        if (!player.hasPermission("justshop.createsigns")) {
            StringManager.sendMessage(player, "&cYou do not have permissions to break JustShop signs.");
            event.setCancelled(true);
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            StringManager.sendMessage(player, "&cYou need to be in creative to unregister a sign.");
        }
        jsSign.unregister();
        StringManager.sendMessage(player, "&aSign was successfully removed.");
    }
}
