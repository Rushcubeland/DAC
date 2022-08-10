package fr.rushcubeland.dac.game;

import fr.rushcubeland.rcbcore.bukkit.map.MapUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public enum Locations {

    LOBBY(new Location(Bukkit.getWorld(MapUnit.WAITING_LOBBY.getPath()), 82D, 55D, -74D)),
    POOL(new Location(Bukkit.getWorld(MapUnit.DAC.getPath()), -161D, 59D, -636.5D)),
    DIVING_PLATFORM(new Location(Bukkit.getWorld(MapUnit.DAC.getPath()), -162.4D, 55D, -638.5D));

    private final Location location;

    Locations(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
