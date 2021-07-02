package fr.didi955.dac.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn implements Listener {

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event){
        event.setCancelled(true);
    }
}
