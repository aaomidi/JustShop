package com.aaomidi.justshop.engine.global.objects;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;

/**
 * @author Tim [calebbfmv]
 */
public class Glow extends Enchantment {

    public static final int ID = 112;
    public static final Enchantment instance = new Glow();
    static boolean registered = false;

    public Glow() {
        super(ID);
    }

    public static boolean isRegistered() {
        return registered;
    }

    public static boolean appply(final ItemStack i) {
        if (i == null || registered == false) {
            System.out.println("Cannot do");
            return false;
        }
        ItemMeta meta = i.getItemMeta();
        meta.addEnchant(instance, 1, true);
        i.setItemMeta(meta);
        i.addEnchantment(instance, 1);
        return true;
    }

    public static void register() {
        try {
            final Enchantment e = Enchantment.getByName("GLOW");
            if (e != null && e.getClass() == Glow.class) {
                Glow.registered = true;
                return;
            }
            final Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(Glow.instance);
            Glow.registered = true;
            f.set(null, false);
        } catch (final Exception ex) {
            Glow.registered = true;
        }
    }

    @Override
    public boolean canEnchantItem(ItemStack arg0) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment arg0) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public String getName() {
        return "GLOW";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }
}