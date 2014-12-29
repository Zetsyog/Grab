package com.zetsyog.plugin.grab;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitTask;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Clem.
 */
public class GrabListener implements Listener {

    private GrabPlugin plugin;

    private HashMap<UUID, Integer> playersGrabbing;

    public GrabListener(GrabPlugin plugin)
    {
        this.plugin = plugin;
        playersGrabbing = new HashMap<UUID, Integer>();
    }

    @EventHandler
    public void onPlayerInterract(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            if(event.getMaterial() == Material.BOW)
            {
                if(playersGrabbing.containsKey(event.getPlayer().getUniqueId()))
                {
                    this.removePlayerAndGrabTask(event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        this.removePlayerAndGrabTask(event.getEntity());
    }

    public void removePlayerAndGrabTask(Player player)
    {
        if(playersGrabbing.containsKey(player.getUniqueId()))
        {
            Bukkit.getScheduler().cancelTask(playersGrabbing.get(player.getUniqueId()));
            player.setFallDistance(0);
            playersGrabbing.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        this.removePlayerAndGrabTask(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        this.removePlayerAndGrabTask(event.getPlayer());
    }

    @EventHandler
    public void onArrowBlockCollision(ProjectileHitEvent event)
    {
        if(event.getEntity() instanceof Arrow)
        {
            Arrow arrow = (Arrow) event.getEntity();
            if(arrow.getShooter() instanceof Player)
            {
                Player player = (Player) arrow.getShooter();
                this.removePlayerAndGrabTask(player);
                BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new PlayerGrabTask(this, player, arrow), 0, 1);

                this.playersGrabbing.put(player.getUniqueId(), task.getTaskId());
            }
        }
    }

}
