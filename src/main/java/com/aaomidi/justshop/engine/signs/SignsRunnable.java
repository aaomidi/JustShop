package com.aaomidi.justshop.engine.signs;

import com.aaomidi.justshop.JustShop;
import com.aaomidi.justshop.engine.signs.objects.JSSign;
import com.aaomidi.justshop.utils.StringManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author aaomidi
 */
public class SignsRunnable {
    private final JustShop instance;
    @Getter
    private BukkitRunnable signRecreationRunnable;

    public SignsRunnable(JustShop instance) {
        this.instance = instance;
        this.scheduleReCreateSigns();
    }

    public void reCreateSigns() {
        for (JSSign jsSign : instance.getSignManager().getSignCache().getLocationJSSignMap().values()) {
            Block block = jsSign.getLocation().getBlock();
            Sign sign;
            if (block.getType().equals(Material.WALL_SIGN) ||
                    block.getType().equals(Material.SIGN_POST) ||
                    block.getType().equals(Material.SIGN)) {
                sign = (Sign) block.getState();
            } else {
                block.setType(jsSign.getSignType());
                if (!(block.getState() instanceof Sign)) {
                    return;
                }
                sign = (Sign) block.getState();
            }
            sign.setLine(0, StringManager.colorizeString("&l[JustShop]"));
            sign.setLine(1, StringManager.colorizeString("&b" + JustShop.getEssentials().getItemDb().name(jsSign.getItem().toItem())));
            sign.setLine(2, StringManager.colorizeString("&bBuy: &a$" + jsSign.getItem().getBuyPrice()));
            sign.setLine(3, StringManager.colorizeString("&bSell: &c$" + jsSign.getItem().getSellPrice()));
            sign.update();
        }
    }

    public void scheduleReCreateSigns() {
        signRecreationRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                reCreateSigns();
            }
        };
        signRecreationRunnable.runTaskTimer(instance, 100L, 6000L);
    }

}
