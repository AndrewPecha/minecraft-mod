package dev.andrewpecha.spigottest.tracklayer;

import java.util.ArrayList;

public final class TrackLayerTracker {
    public static ArrayList<String> getPlayersEnabled() {
        return playersEnabled;
    }

    public static void addPlayer(String playerName) {
        playersEnabled.add(playerName);
    }

    public static void removePlayer(String playerName) {
        playersEnabled.remove(playerName);
    }

    public static ArrayList<String> playersEnabled = new ArrayList<String>();

    private TrackLayerTracker() {

    }


}
