package dev.andrewpecha.spigottest;

import dev.andrewpecha.spigottest.tracklayer.TrackLayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        // Register our command "kit" (set an instance of your command class as executor)
        this.getCommand("kit").setExecutor(new CommandKit());
        this.getCommand("move").setExecutor(new CommandMove());
        this.getCommand("choochoo").setExecutor(new CommandChooChoo());
        this.getCommand("stairs").setExecutor(new CommandStairBlocks());
        this.getCommand("tnt").setExecutor(new CommandTnt());
        getServer().getPluginManager().registerEvents(new TrackLayerListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());

    }


}