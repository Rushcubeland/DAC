package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (DAC.getInstance().isState(GameState.STARTING) || DAC.getInstance().isState(GameState.WAITING) || DAC.getInstance().isState(GameState.FINISH)) {
            e.setCancelled(true);
        }
    }
}
