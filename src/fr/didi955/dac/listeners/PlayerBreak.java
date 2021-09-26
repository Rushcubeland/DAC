package fr.didi955.dac.listeners;

import fr.didi955.dac.spells.Spell;
import fr.didi955.dac.spells.SpellUnit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PlayerBreak implements Listener {

    @EventHandler
    public void onBreak(BlockPlaceEvent event){
        event.setCancelled(true);
    }
}
