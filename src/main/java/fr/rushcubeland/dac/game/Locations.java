package fr.rushcubeland.dac.game;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.rcbcore.bukkit.map.MapUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public enum Locations {

    LOBBY(MapUnit.WAITING_LOBBY, LocationType.LOBBY, new Location(Bukkit.getWorld(MapUnit.WAITING_LOBBY.getPath()), 82D, 55D, -74D)),

    // DAC MAP
    POOL(MapUnit.DAC, LocationType.POOL, new Location(Bukkit.getWorld(MapUnit.DAC.getPath()), -161D, 59D, -636.5D)),
    DIVING_PLATFORM(MapUnit.DAC, LocationType.DIVING_PLATFORM, new Location(Bukkit.getWorld(MapUnit.DAC.getPath()), -162.4D, 55D, -638.5D));

    // DAC 2 MAP

    private final Location location;
    private final MapUnit mapUnit;
    private final LocationType locationType;

    Locations(MapUnit mapUnit, LocationType locationType, Location location) {
        this.mapUnit = mapUnit;
        this.locationType = locationType;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public MapUnit getMapUnit() {
        return mapUnit;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public static Location getPoolLocation() {
        return Arrays.stream(Locations.values()).filter(location -> location.getLocationType() == LocationType.POOL && location.getMapUnit().equals(DAC.getInstance().getMap())).findFirst().orElse(null).getLocation();
    }

    public static Location getDivingPlatformLocation() {
        return Arrays.stream(Locations.values()).filter(location -> location.getLocationType() == LocationType.DIVING_PLATFORM && location.getMapUnit().equals(DAC.getInstance().getMap())).findFirst().orElse(null).getLocation();
    }

}
