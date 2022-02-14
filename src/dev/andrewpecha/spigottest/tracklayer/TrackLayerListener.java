package dev.andrewpecha.spigottest.tracklayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TrackLayerListener implements Listener {
    private static Location previousLocation;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //if player moves diagonally
        if (TrackLayerTracker.PlayerHasChooChooEnabled(event.getPlayer())) {

            /*
             * Flat:
             * IF player moved full x or z block AND NOT a y block
             * IF last powered rail is null THEN return
             * IF last powered rail is < 7 blocks away THEN place normal rail
             * IF last powered rail is > 7 blocks away THEN place powered rails
             *
             * Escalating:
             * IF player moved full x or z block AND a y block
             */
            if (!playerHasAnyMaterialsToBuildWith(event))
                return;

            var playerLocation = event.getPlayer().getLocation();
            if (previousLocation == null)
                previousLocation = playerLocation;

            if (playerHasMovedOneFullBlockOnXZAxis(playerLocation)) {

                var railLocationNextToPlayer = getRailOrPoweredRailNextToLocation(event.getPlayer().getLocation());
                //leave method if no rails next to player
                if (railLocationNextToPlayer == null) {
//                    Bukkit.broadcastMessage("no rail detected next to player");
                    return;
                }

                //if player has not gone up or down from the rail next to them
                if ((int) railLocationNextToPlayer.getY() == (int) event.getPlayer().getLocation().getY()) {
                    var lastPoweredRailXZDistance = getXZTrackCountFromLastPoweredRailLocation(event.getPlayer().getLocation());
                    if (lastPoweredRailXZDistance < 7 && playerHasRails(event.getPlayer())) {
                        decrementPlayerRailStack(event);
                        placeBlockAtPlayer(event, Material.RAIL);
                    }
                }

            }
//                else {
//                    Bukkit.broadcastMessage("last rail location: " + railLocationNextToPlayer.getX() + ", " + railLocationNextToPlayer.getY() + ", " + railLocationNextToPlayer.getZ());
//                }

            if (lastPoweredRailLocationIsAtLeastTwoBlocksUpOrDownFromPlayer(event.getPlayer())) {
                if (playerHasPoweredRails(event.getPlayer())) {
                    decrementPlayerPoweredRailStack(event);
                    placeBlockAtPlayer(event, Material.POWERED_RAIL);
                } else if (playerHasRails(event.getPlayer())) {
                    decrementPlayerRailStack(event);
                    placeBlockAtPlayer(event, Material.RAIL);
                }
            }

        }

    }

    private int getXZTrackCountFromLastPoweredRailLocation(Location inputLocation) {
        var counter = 1;
        Location lastLastRailLocation;
        var lastAnyRailLocation = getRailOrPoweredRailNextToLocation(inputLocation);
        while (lastAnyRailLocation != null && !isLocationMadeOfMaterial(lastAnyRailLocation, Material.POWERED_RAIL)) {
            counter ++;
            lastLastRailLocation = lastAnyRailLocation;
            lastAnyRailLocation = getRailOrPoweredRailNextToLocationWithExclusion(lastAnyRailLocation, lastLastRailLocation);
            Bukkit.broadcastMessage("" + counter);
        }
        if(lastAnyRailLocation == null)
            return -1;

        return counter;
    }

    private boolean lastPoweredRailLocationIsAtLeastTwoBlocksUpOrDownFromPlayer(Player player) {
        var locationOfLastRailOrPoweredRail = getRailOrPoweredRailNextToLocation(player.getLocation());
        //if no rails next to player, false
        if (locationOfLastRailOrPoweredRail == null)
            return false;

        var locationOfLastRailOrPoweredRailTwoBlocksAway =
                getRailOrPoweredRailNextToLocation(locationOfLastRailOrPoweredRail);

        if (locationOfLastRailOrPoweredRailTwoBlocksAway == null)
            return false;

        return Math.abs((int) locationOfLastRailOrPoweredRailTwoBlocksAway.getY() - (int) player.getLocation().getY()) >= 2;
    }

    private Location getRailOrPoweredRailNextToLocation(Location inputLocation) {
        var boxAroundLocation = getBlockLocationsAroundLocation(inputLocation);
//        Bukkit.broadcastMessage("player at:\n");
        for (Location location : boxAroundLocation) {
            //find rail around player
            if (isLocationMadeOfMaterial(location, Material.POWERED_RAIL) ||
                    isLocationMadeOfMaterial(location, Material.RAIL))
                return location;
//            Bukkit.broadcastMessage("x: " + location.getX() + " y: " + location.getY() + " z: " + location.getZ());

        }

        return null;
    }

    private Location getRailOrPoweredRailNextToLocationWithExclusion(Location inputLocation, Location excludedLocation) {
        var boxAroundLocation = getBlockLocationsAroundLocation(inputLocation);
//        Bukkit.broadcastMessage("player at:\n");
        for (Location location : boxAroundLocation) {
            //find rail around player
            if (isLocationMadeOfMaterial(location, Material.POWERED_RAIL) ||
                    isLocationMadeOfMaterial(location, Material.RAIL) && !location.equals(excludedLocation))
                return location;
//            Bukkit.broadcastMessage("x: " + location.getX() + " y: " + location.getY() + " z: " + location.getZ());

        }

        return null;
    }

    private boolean isLocationMadeOfMaterial(Location location, Material material) {
        return location.getWorld().getBlockAt(location).getType() == material;
    }

    private Location[] getBlockLocationsAroundLocation(Location inputLocation) {
        List<Location> blocksAroundLocation = new ArrayList<Location>();
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    //don't detect current location
                    if (y == 0 && x == 0 && z == 0)
                        continue;
                    blocksAroundLocation.add(new Location(inputLocation.getWorld(),
                            inputLocation.getX() + x,
                            inputLocation.getY() + y,
                            inputLocation.getZ() + z));
                }
            }
        }

        Location[] result = new Location[blocksAroundLocation.size()];
        return blocksAroundLocation.toArray(result);
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
        return !getAllItemStacksInPlayersInventory(player, Material.POWERED_RAIL).findAny().isEmpty();
    }

    private boolean playerHasMovedOneFullBlockOnXZAxis(Location currentLocation) {
        if ((int) currentLocation.getX() == (int) previousLocation.getX() && (int) currentLocation.getZ() == (int) previousLocation.getZ()) {
            return false;
        }

        previousLocation = currentLocation;
        return true;
    }

    private void placeBlockAtPlayer(PlayerMoveEvent event, Material material) {
        event.getPlayer().getWorld().getBlockAt(event.getPlayer().getLocation()).setType(material);
    }

    private void placeBlockAtLocation(Location location, Material material) {
        location.getWorld().getBlockAt(location).setType(material);
    }

    private void announcePlayerPosition(PlayerMoveEvent event) {
        var consoleSender = Bukkit.getServer().getConsoleSender();
        var playerPosition = event.getPlayer().getLocation();
        Bukkit.broadcastMessage(event.getPlayer().getName() + " is at: " + playerPosition.getX() + ", " + playerPosition.getZ() + ", " + playerPosition.getY() + ", ");
    }
}
