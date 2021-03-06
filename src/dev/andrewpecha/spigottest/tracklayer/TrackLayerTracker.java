package dev.andrewpecha.spigottest.tracklayer;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public final class TrackLayerTracker {
    public static ArrayList<String> getPlayersEnabled() {
        return playersEnabled;
    }

    public static void togglePlayer(String playerName) {
        if (!playersEnabled.contains(playerName))
            playersEnabled.add(playerName);
        else
            playersEnabled.remove(playerName);
    }

    public static ArrayList<String> playersEnabled = new ArrayList<String>();

    private TrackLayerTracker() {

    }

     protected static boolean PlayerHasChooChooEnabled(Player player) {
        return TrackLayerTracker.getPlayersEnabled().contains(player.getName());
    }

}
