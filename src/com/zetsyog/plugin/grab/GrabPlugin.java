/*
 *   Grab - a Bukkit plugin.
 *   Copyright (C) 2014 - Zetsyog
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Grab.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.zetsyog.plugin.grab;

import com.zetsyog.plugin.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Clem.
 */
public class GrabPlugin extends JavaPlugin {

    private Logger log;
    private ConfigManager configManager;
    private FileConfiguration config;
    private boolean debug = false;

    @Override
    public void onEnable() {
        log = getLogger();


        configManager = new ConfigManager(this, "config.yml");
        this.configManager.saveDefaultConfig();
        config = this.configManager.getConfig();

        debug = config.getBoolean("debug");

        if(debug)
        {
            log.info("Debug mode is enabled, prepare for console flood :D");
        }

        Bukkit.getPluginManager().registerEvents(new GrabListener(this), this);
        this.registerRecipe();

        log.info("Done !");
    }

    public void debug(String str)
    {
        if(debug) {
            log.log(Level.INFO, "[DEBUG] " + str);
        }
    }

    @Override
    public FileConfiguration getConfig()
    {
        return configManager.getConfig();
    }


    private void registerRecipe()
    {
        ShapelessRecipe recipe = new ShapelessRecipe(Grab.getGrabItemStack(config));
        recipe.addIngredient(Material.BOW);
        recipe.addIngredient(Material.STRING);

        this.getServer().addRecipe(recipe);
    }



    @Override
    public void onDisable()
    {
        this.getServer().resetRecipes();
        log.info("Done !");
    }
}
