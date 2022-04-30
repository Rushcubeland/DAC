package fr.didi955.dac.listeners;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.map.MapView;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class FrameIntegrity implements Listener {


    @EventHandler
    public void onDestroyFrame(HangingBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void UseFrame(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame || e.getRightClicked() instanceof MapView) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void FrameEntity(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof ItemFrame || e.getEntity() instanceof MapView) &&
                e.getDamager() instanceof Player) {
            e.setCancelled(true);
        }
    }


}
