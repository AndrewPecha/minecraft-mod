package dev.andrewpecha.spigottest.tracklayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TrackLayerListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
//        announceBlockPosition(event);

        if(event.getBlockPlaced().getType() == Material.RAIL){
            var placedRailLocation = event.getBlockPlaced().getLocation();
            for (int i = 0; i < 5; i++){
                placedRailLocation.setX(placedRailLocation.getX() + 1);
                event.getPlayer().getWorld().getBlockAt(placedRailLocation).setType(Material.RAIL);
            }
        }
    }

    private void announceBlockPosition(BlockPlaceEvent event) {
        var consoleSender = Bukkit.getServer().getConsoleSender();
        var blockPosition = event.getBlock().getLocation();
        Bukkit.broadcastMessage(event.getPlayer().getName() + " placed block at: " + blockPosition.getX() + ", " + blockPosition.getZ() + ", " + blockPosition.getY() + ", ");
    }
}
