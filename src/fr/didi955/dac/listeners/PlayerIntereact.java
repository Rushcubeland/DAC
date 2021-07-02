package fr.didi955.dac.listeners;

import fr.rushcubeland.rcbapi.network.Network;
import fr.rushcubeland.rcbapi.network.ServerUnit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerIntereact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getItem() == null) {
            return;
        }

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && event.getItem().getType().equals(Material.RED_BED)) {
            player.closeInventory();
            Network.sendPlayerToServer(player, ServerUnit.Lobby_1);
        }
    }
}
