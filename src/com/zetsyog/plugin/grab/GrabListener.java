package com.zetsyog.plugin.grab;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Clem.
 */
public class GrabListener implements Listener {

    private GrabPlugin plugin;
    private int maxRange;

    private HashMap<UUID, Integer> playersGrabbing;
    private HashMap<UUID, Location> lastLocations;

    public GrabListener(GrabPlugin plugin)
    {
        this.plugin = plugin;
        playersGrabbing = new HashMap<UUID, Integer>();
        lastLocations = new HashMap<UUID, Location>();
        maxRange = plugin.getConfig().getInt("max-range");
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
                ItemStack itemInHand = player.getInventory().getItemInHand();
                if(Grab.isGrabItemStack(itemInHand)) {
                    if(itemInHand.getItemMeta().getLore() != Grab.getGrabItemStack(plugin.getConfig()).getItemMeta().getLore())
                    {
                        itemInHand.getItemMeta().setLore(Grab.getGrabItemStack(plugin.getConfig()).getItemMeta().getLore());
                    }
                    this.addLastLocation(player, arrow.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInterract(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            if(Grab.isGrabItemStack(event.getItem()))
            {
                if(playersGrabbing.containsKey(event.getPlayer().getUniqueId()))
                {
                    /*PlayerGrabTask grabTask = null;
                    List<BukkitTask> tasks = Bukkit.getScheduler().getPendingTasks();
                    for(BukkitTask task : tasks)
                    {
                        if(task.getTaskId() == playersGrabbing.get(event.getPlayer().getUniqueId()))
                        {
                            grabTask = (PlayerGrabTask) task;
                            break;
                        }
                    }

                    if(grabTask != null)
                    {
                        if(grabTask.getDest() != lastLocations.get(event.getPlayer().getUniqueId()))
                        {
                            this.addGrabTask(event.getPlayer());
                        }
                        else
                        {
                            removeGrabTask(event.getPlayer());
                        }
                    }
                    else
                    {
                        removeGrabTask(event.getPlayer());
                    }*/
                    removeGrabTask(event.getPlayer());
                    event.setCancelled(true);
                }
                else
                {
                    this.addGrabTask(event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }

    public void addLastLocation(Player player, Location location)
    {
        removeLastLocation(player);
        if(player.getLocation().distance(location) <= maxRange) {
            lastLocations.put(player.getUniqueId(), location);
            plugin.debug("Adding new grab location to player " + player.getName() + " at x=" + location.getX() + ",y=" + location.getY() + ",z=" + location.getZ());
        }
    }

    public void removeLastLocation(Player player)
    {
        if(lastLocations.containsKey(player.getUniqueId()))
        {
            lastLocations.remove(player.getUniqueId());
            plugin.debug("Removing " + player.getName() + "'s grab location");
        }
    }

    public void removeGrabTask(Player player)
    {
        if(playersGrabbing.containsKey(player.getUniqueId()))
        {
            Bukkit.getScheduler().cancelTask(playersGrabbing.get(player.getUniqueId()));
            player.setFallDistance(0);
            playersGrabbing.remove(player.getUniqueId());
            plugin.debug("Removing " + player.getName() + "'s grab task");
        }
    }

    public void addGrabTask(Player player)
    {
        this.removeGrabTask(player);
        if(lastLocations.containsKey(player.getUniqueId())) {
            Location dest = lastLocations.get(player.getUniqueId());
            if(dest.distance(player.getLocation()) > maxRange) {
                reset(player);
                plugin.debug("Reset cause : distance (" + dest.distance(player.getLocation()) + " > to max range");
            }
            else {
                BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, new PlayerGrabTask(this, player, dest), 0, 5);
                playersGrabbing.put(player.getUniqueId(), task.getTaskId());
                plugin.debug("Registering player " + player.getName() + " grabbing to " + lastLocations.get(player.getUniqueId()).toString());
                plugin.debug("from " + player.getLocation().toString());
                plugin.debug("distance : " + player.getLocation().distance(dest));
                plugin.debug("vector : " + dest.subtract(player.getLocation()).toVector().toString());
            }
        }
    }

    public void reset(Player player)
    {
        if(lastLocations.containsKey(player.getUniqueId()))
            lastLocations.remove(player.getUniqueId());

        if(playersGrabbing.containsKey(player.getUniqueId()))
            playersGrabbing.remove(player.getUniqueId());

        plugin.debug("Reseting player " + player.getName());
    }

    public int getMaxRange()
    {
        return maxRange;
    }

    public GrabPlugin getPlugin()
    {
        return plugin;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event)
    {
        this.reset(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        this.reset(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event)
    {
        this.reset(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        this.reset(event.getEntity());
    }

}
