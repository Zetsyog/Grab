package com.zetsyog.plugin.grab;

import com.zetsyog.plugin.util.Util;
import org.bukkit.Bukkit;
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
    private Arrow arrow;

    public PlayerGrabTask(GrabListener listener, Player p, Arrow arrow)
    {
        this.listener = listener;
        player = p;
        this.arrow = arrow;
    }

    @Override
    public void run()
    {
        Location arrowPos = arrow.getLocation();
        Location playerPos = player.getEyeLocation();

        if(playerPos.distance(arrowPos) > 30)
        {
            listener.removePlayerAndGrabTask(player);
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
