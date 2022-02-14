package dev.andrewpecha.spigottest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTnt implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location playerCurrentLocation = player.getLocation();
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    placeBlockAt(player.getWorld(),
                            (int) playerCurrentLocation.getX() + i,
                            (int) playerCurrentLocation.getY(),
                            (int) playerCurrentLocation.getZ() + j,
                            Material.TNT);
                }
            }
        }

        return true;
    }

    private void placeBlockAt(World world, int x, int y, int z, Material material) {
        world.getBlockAt(new Location(world, x, y, z)).setType(material);
    }
}