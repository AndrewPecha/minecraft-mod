package dev.andrewpecha.spigottest.tracklayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
    private static int counter = 0;
    private static Location location;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //if player moves diagonally
        if (TrackLayerTracker.PlayerHasChooChooEnabled(event.getPlayer())) {

//            Bukkit.broadcastMessage("Player has rails: " + playerHasRails(event.getPlayer()) + "\n Player has powered rails: " + playerHasPoweredRails(event.getPlayer()));
            if (!playerHasAnyMaterialsToBuildWith(event))
                return;

            var playerLocation = event.getPlayer().getLocation();
            if (location == null)
                location = playerLocation;

            if (playerHasMovedOneFullBlockOnXZAxis(playerLocation)) {
                //leave method if no rails next to player
                var lastRailLocation = getLastPlacedRailOrPoweredRailLocationFromPlayer(event.getPlayer());
                if(lastRailLocation == null){
                    Bukkit.broadcastMessage("no rail detected next to player");
                    return;
                }


                Bukkit.broadcastMessage("" + lastPoweredRailLocationIsAtLeastTwoBlocksUpOrDownFromPlayer(event.getPlayer()));
                if(lastPoweredRailLocationIsAtLeastTwoBlocksUpOrDownFromPlayer(event.getPlayer())) {
                    if (playerHasPoweredRails(event.getPlayer())) {
                        decrementPlayerPoweredRailStack(event);
                        placeBlockAtPlayer(event, Material.POWERED_RAIL);
                    }
                }

                //want to place redstone torches eventually too
//                if (counter == 7) {
//                    if (playerHasPoweredRails(event.getPlayer())) {
//                        decrementPlayerPoweredRailStack(event);
//                        placeBlockAtPlayer(event, Material.POWERED_RAIL);
//                    }
//                    counter = 0;
//                } else {
//                    if (playerHasRails(event.getPlayer())) {
//                        decrementPlayerRailStack(event);
//                        placeBlockAtPlayer(event, Material.RAIL);
//                    }
//                    counter++;
//                }
            }

        }

    }

    private boolean lastPoweredRailLocationIsAtLeastTwoBlocksUpOrDownFromPlayer(Player player) {
        var locationOfLastRailOrPoweredRail = getLastPlacedRailOrPoweredRailLocationFromPlayer(player);
        if(locationOfLastRailOrPoweredRail == null)
            return false;

        var locationOfLastRailOrPoweredRailTwoBlocksAway =
                getLastPlacedRailOrPoweredRailLocationFromLocation(locationOfLastRailOrPoweredRail);

        if(Math.abs((int)locationOfLastRailOrPoweredRailTwoBlocksAway.getY() - player.getLocation().getY()) >= 2)
            return true;

        return false;
    }

    private Location getLastPlacedRailOrPoweredRailLocationFromLocation(Location inputLocation) {
        var boxAroundPlayer = getBlockLocationsAroundLocation(inputLocation);
//        Bukkit.broadcastMessage("player at:\n");
        for(Location location : boxAroundPlayer){
            //find rail around player
            if(locationIsMaterial(inputLocation.getWorld(), location, Material.POWERED_RAIL) ||
                    locationIsMaterial(inputLocation.getWorld(), location, Material.RAIL))
                return location;
//            Bukkit.broadcastMessage("x: " + location.getX() + " y: " + location.getY() + " z: " + location.getZ());

        }

        return null;
    }

    private Location getLastPlacedRailOrPoweredRailLocationFromPlayer(Player player) {
        var boxAroundPlayer = getBlockLocationsAroundPlayer(player);
//        Bukkit.broadcastMessage("player at:\n");
        for(Location location : boxAroundPlayer){
            //find rail around player
            if(locationIsMaterial(player.getWorld(), location, Material.POWERED_RAIL) ||
                    locationIsMaterial(player.getWorld(), location, Material.RAIL))
                return location;
//            Bukkit.broadcastMessage("x: " + location.getX() + " y: " + location.getY() + " z: " + location.getZ());

        }

        return null;

    }

    private boolean locationIsMaterial(World world, Location location, Material material) {
        return world.getBlockAt(location).getType() == material;
    }

    private Location[] getBlockLocationsAroundLocation(Location inputLocation) {
        List<Location> blocksAroundLocation = new ArrayList<Location>();
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    //don't detect current location
                    if(y == 0 && x == 0 && z == 0)
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

    private Location[] getBlockLocationsAroundPlayer(Player player) {
        var playerLocation = player.getLocation();
        List<Location> blocksAroundLocation = new ArrayList<Location>();
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    //don't detect current location
                    if(y == 0 && x == 0 && z == 0)
                        continue;
                    blocksAroundLocation.add(new Location(player.getWorld(),
                            playerLocation.getX() + x,
                            playerLocation.getY() + y,
                            playerLocation.getZ() + z));
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
        if ((int) currentLocation.getX() == (int) location.getX() && (int) currentLocation.getZ() == (int) location.getZ()) {
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
