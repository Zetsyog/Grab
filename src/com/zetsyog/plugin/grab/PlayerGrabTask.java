package com.zetsyog.plugin.grab;

import com.zetsyog.plugin.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Clem.
 */
public class PlayerGrabTask extends Thread {

    private GrabListener listener;
    private Player player;
    private Location dest;

    public PlayerGrabTask(GrabListener listener, Player p, Location dest)
    {
        this.listener = listener;
        this.player = p;
        this.dest = dest;
    }

    public Location getDest()
    {
        return dest;
    }

    @Override
    public void run()
    {
        Location destPos = dest.clone();
        Location playerPos = player.getLocation();

        if(playerPos.distance(destPos) > listener.getMaxRange())
        {
            listener.removeGrabTask(player);
            listener.getPlugin().debug("Cancel cause : maxRange " + playerPos.distance(destPos));
            listener.getPlugin().debug("player : " + playerPos.toString());
            listener.getPlugin().debug("dest : " + destPos.toString());
            return;
        }
        else if(playerPos.distance(destPos) <= 1)
        {
            if(LocationUtil.sameSqarredLocations(playerPos, destPos))
            {
                return;
            }
            else {
                player.getLocation().setX(destPos.getX());
                player.getLocation().setY(destPos.getY());
                player.getLocation().setZ(destPos.getZ());
                listener.getPlugin().debug("Cancel cause : grab done");
                return;
            }
        }

        Location l = destPos.subtract(playerPos);

        Vector v = l.toVector();
        v.setX(v.getX() / 10);
        v.setY(v.getY() / 10);
        v.setZ(v.getZ() / 10);

        player.setVelocity(v);
    }
}
