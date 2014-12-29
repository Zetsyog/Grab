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
