package fr.didi955.dac.listeners;

import fr.rushcubeland.rcbapi.bukkit.BukkitSend;
import fr.rushcubeland.rcbapi.bukkit.network.Network;
import fr.rushcubeland.rcbapi.bukkit.network.ServerGroup;
import fr.rushcubeland.rcbapi.bukkit.network.ServerUnit;
import fr.rushcubeland.rcbapi.bukkit.queue.QueueUnit;
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

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
            if(event.getItem().getType().equals(Material.RED_BED)){
                player.closeInventory();
                Network.sendPlayerToServer(player, Network.getBestServer(player, ServerGroup.Lobby));
            }
            if(event.getItem().getType().equals(Material.NETHER_STAR)){
                BukkitSend.requestJoinQueue(player, QueueUnit.DE_A_COUDRE);
            }
        }
    }
}
