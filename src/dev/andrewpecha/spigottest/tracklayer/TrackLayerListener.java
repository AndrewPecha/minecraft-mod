package dev.andrewpecha.spigottest.tracklayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.Stream;

public class TrackLayerListener implements Listener {
    private static int counter = 0;
    private static Location location;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (TrackLayerTracker.getPlayersEnabled().contains(event.getPlayer().getName())) {

            if (!playerHasAnyMaterialsToBuildWith(event))
                return;

            var playerLocation = event.getPlayer().getLocation();
            if (location == null)
                location = playerLocation;

            if (playerHasMovedOneFullBlock(playerLocation)) {
                if (counter == 7) {
                    decrementPlayerPoweredRailStack(event);
                    placeBlockAtPlayer(event, Material.POWERED_RAIL);
                    counter = 0;
                } else {
                    decrementPlayerRailStack(event);
                    placeBlockAtPlayer(event, Material.RAIL);
                    counter++;
                }
            }

        }

    }

    private boolean playerHasAnyMaterialsToBuildWith(PlayerMoveEvent event) {
        return playerHasRails(event.getPlayer()) ||
                playerHasPoweredRails(event.getPlayer());
        //redstone torches
    }

    private void decrementPlayerRailStack(PlayerMoveEvent event) {
        getSingleRailStack(event.getPlayer())
                .setAmount(getSingleRailStack(event.getPlayer()).getAmount() - 1);
    }

    private void decrementPlayerPoweredRailStack(PlayerMoveEvent event) {
        getSinglePoweredRailStack(event.getPlayer())
                .setAmount(getSinglePoweredRailStack(event.getPlayer()).getAmount() - 1);
    }

    private ItemStack getSingleStack(Stream<ItemStack> railStacks) {
        return railStacks.findAny().get();
    }

    private ItemStack getSingleRailStack(Player player) {
        return getSingleStack(getAllItemStacksInPlayersInventory(player, Material.RAIL));
    }

    private ItemStack getSinglePoweredRailStack(Player player) {
        return getSingleStack(getAllItemStacksInPlayersInventory(player, Material.POWERED_RAIL));
    }

    private Stream<ItemStack> getAllItemStacksInPlayersInventory(Player player, Material material) {
        //null check here since individual items can be null according to the docs
        //https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/Inventory.html#getContents()
        return Arrays.stream(player.getInventory().getContents()).filter(x -> x != null && x.getType() == material);
    }

    private boolean playerHasRails(Player player) {
        return !getAllItemStacksInPlayersInventory(player, Material.RAIL).findAny().isEmpty();
    }

    private boolean playerHasPoweredRails(Player player) {
        return !getAllItemStacksInPlayersInventory(player, Material.RAIL).findAny().isEmpty();
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

    private void announcePlayerPosition(PlayerMoveEvent event) {
        var consoleSender = Bukkit.getServer().getConsoleSender();
        var playerPosition = event.getPlayer().getLocation();
        Bukkit.broadcastMessage(event.getPlayer().getName() + " is at: " + playerPosition.getX() + ", " + playerPosition.getZ() + ", " + playerPosition.getY() + ", ");
    }
}
