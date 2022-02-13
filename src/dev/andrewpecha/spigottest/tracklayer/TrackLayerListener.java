package dev.andrewpecha.spigottest.tracklayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;

public class TrackLayerListener implements Listener {
    private static int counter = 0;
    private static Location location;

    @EventHandler
    public void onBlockPlace(PlayerMoveEvent event) {
        var consoleSender = Bukkit.getConsoleSender();

//        announceBlockPosition(event);

//        For use with BlockPlaceEvent
//        if(event.getBlockPlaced().getType() == Material.RAIL){
//            var placedRailLocation = event.getBlockPlaced().getLocation();
//            for (int i = 0; i < 5; i++){
//                placedRailLocation.setX(placedRailLocation.getX() + 1);
//                event.getPlayer().getWorld().getBlockAt(placedRailLocation).setType(Material.RAIL);
//            }
//        }

        if (TrackLayerTracker.getPlayersEnabled().contains(event.getPlayer().getName())) {
//          announcePlayerPosition(event);
//            consoleSender.sendMessage(Arrays.stream(event.getPlayer().getInventory().removeItem());
            var playerLocation = event.getPlayer().getLocation();
            if (location == null)
                location = playerLocation;

            if (playerHasMovedOneFullBlock(playerLocation)){

                if (counter == 7) {
                    placeBlockAtPlayer(event, Material.POWERED_RAIL);
                    counter = 0;
                } else{
                    placeBlockAtPlayer(event, Material.RAIL);
                    counter++;
                }
            }

        }

    }

    private boolean playerHasMovedOneFullBlock(Location currentLocation) {
        if ((int) currentLocation.getX() == (int) location.getX() &&
                (int) currentLocation.getY() == (int) location.getY() &&
                (int) currentLocation.getZ() == (int) location.getZ()) {
            return false;
        }

        location = currentLocation;
        return true;
    }

    private void placeBlockAtPlayer(PlayerMoveEvent event, Material material) {
        event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).setType(material);
    }

    private void announceBlockPosition(BlockPlaceEvent event) {
        var consoleSender = Bukkit.getServer().getConsoleSender();
        var blockPosition = event.getBlock().getLocation();
        Bukkit.broadcastMessage(event.getPlayer().getName() + " placed block at: " + blockPosition.getX() + ", " + blockPosition.getZ() + ", " + blockPosition.getY() + ", ");
    }

    private void announcePlayerPosition(PlayerMoveEvent event) {
        var consoleSender = Bukkit.getServer().getConsoleSender();
        var playerPosition = event.getPlayer().getLocation();
        Bukkit.broadcastMessage(event.getPlayer().getName() + " is at: " + playerPosition.getX() + ", " + playerPosition.getZ() + ", " + playerPosition.getY() + ", ");
    }
}
