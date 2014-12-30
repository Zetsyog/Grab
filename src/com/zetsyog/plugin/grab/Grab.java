/*
 *   Created by Zetsyog
 */
package com.zetsyog.plugin.grab;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Grab {

    private static ItemStack getGrabItemStackWithoutLore()
    {
        ItemStack result = new ItemStack(Material.BOW, 1);

        ItemMeta meta = result.getItemMeta();
        meta.setDisplayName("Grab");
        meta.addEnchant(Enchantment.THORNS, 10, true);
        result.setItemMeta(meta);

        return result;
    }

    public static boolean isGrabItemStack(ItemStack stack)
    {
        if(stack.getType() == Material.BOW)
        {
            if(stack.hasItemMeta() && stack.getItemMeta().getDisplayName().equals("Grab"))
            {
                if(stack.getItemMeta().hasEnchant(Enchantment.THORNS))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemStack getGrabItemStack(FileConfiguration config)
    {
        ItemStack result = getGrabItemStackWithoutLore();
        ItemMeta meta = result.getItemMeta();
        meta.setLore(config.getStringList("grab-lore"));

        result.setItemMeta(meta);

        return result;
    }

}
