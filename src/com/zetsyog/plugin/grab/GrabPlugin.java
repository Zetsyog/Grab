package com.zetsyog.plugin.grab;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Clem.
 */
public class GrabPlugin extends JavaPlugin {

    private Logger log;

    @Override
    public void onEnable() {
        log = getLogger();

        Bukkit.getPluginManager().registerEvents(new GrabListener(this), this);

        log.info("Done !");
    }

    @Override
    public void onDisable() {

        log.info("Done !");
    }
}
