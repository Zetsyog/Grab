package com.zetsyog.plugin.grab;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by Clem.
 */
public class PlayerGrabTask implements Runnable {

    private GrabListener listener;
    private Player player;
    private Location dest;
    private int maxRange;

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
        Location arrowPos = dest.clone();
        Location playerPos = player.getLocation();

        if(playerPos.distance(arrowPos) > listener.getMaxRange())
        {
            listener.removeGrabTask(player);
            listener.getPlugin().debug("Cancel cause : maxRange " + playerPos.distance(arrowPos));
            return;
        }

        Location l = arrowPos.subtract(playerPos);

        Vector v = l.toVector();
        v.setX(v.getX() / 10);
        v.setY(v.getY() / 10);
        v.setZ(v.getZ() / 10);

        player.setVelocity(v);
    }
}
