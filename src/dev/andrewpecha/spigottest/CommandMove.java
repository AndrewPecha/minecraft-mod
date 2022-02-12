package dev.andrewpecha.spigottest;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CommandMove implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Bukkit.getServer().getConsoleSender().sendMessage("X: " + player.getLocation().getX() + " Y: " + player.getLocation().getY());
            player.teleport(player.getLocation().add(new Vector(100, 0, 0)));
        }

        return true;
    }
}