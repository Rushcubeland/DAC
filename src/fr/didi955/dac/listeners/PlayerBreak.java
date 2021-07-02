package fr.didi955.dac.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBreak implements Listener {

    @EventHandler
    public void onBreak(BlockPlaceEvent event){
        event.setCancelled(true);
    }
}
