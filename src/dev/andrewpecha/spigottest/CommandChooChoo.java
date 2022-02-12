package dev.andrewpecha.spigottest;

import dev.andrewpecha.spigottest.tracklayer.TrackLayerTracker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandChooChoo implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            TrackLayerTracker.addPlayer(player.getDisplayName());

            var consoleSender = Bukkit.getConsoleSender();

            consoleSender.sendMessage("Players with TrackR Enabled:");
            for (String playerName : TrackLayerTracker.getPlayersEnabled()) {
                consoleSender.sendMessage("\n" + playerName);
            }
        }

        return true;
    }
}