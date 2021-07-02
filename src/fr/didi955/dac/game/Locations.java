package fr.didi955.dac.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum Locations {

    LOBBY(new Location(Bukkit.getWorld("Lobby"), 82D, 55D, -74D)),
    POOL(new Location(Bukkit.getWorld("DAC"), -161D, 59D, -636.5D)),
    DIVING_PLATFORM(new Location(Bukkit.getWorld("DAC"), -162.4D, 55D, -638.5D));

    private final Location location;

    Locations(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
