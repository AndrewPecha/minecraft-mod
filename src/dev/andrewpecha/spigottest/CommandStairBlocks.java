package dev.andrewpecha.spigottest;

import dev.andrewpecha.spigottest.tracklayer.TrackLayerTracker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class CommandStairBlocks implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location playerCurrentLocation = player.getLocation();
            for (int i = 0; i < 100; i++) {
                placeBlockAt(player.getWorld(),
                        (int) playerCurrentLocation.getX() + i,
                        (int) playerCurrentLocation.getY() + i,
                        (int) playerCurrentLocation.getZ(),
                        Material.COBBLESTONE);
            }
        }

        return true;
    }

    private void placeBlockAt(World world, int x, int y, int z, Material material) {
        world.getBlockAt(new Location(world, x, y, z)).setType(material);
    }
}