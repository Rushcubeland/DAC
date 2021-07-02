package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import fr.didi955.dac.game.PlayerTurn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Random;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location l = player.getLocation();

        if ((DAC.getInstance().isState(GameState.WAITING) || DAC.getInstance().isState(GameState.STARTING)) &&
                l.getBlockY() < 15) {
            player.sendTitle("§cPourquoi veux", "§7tu t'enfuirs ?", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
            player.teleport(Locations.LOBBY.getLocation());
        }

        if(DAC.getInstance().isState(GameState.INPROGRESS)){
            if(DAC.getInstance().getPlayerTurn().getPlayerTurn().equals(player)){
                Location location = player.getLocation();
                if(location.getBlock().isLiquid() && location.getBlock().getType().equals(Material.WATER)){
                    Location bloc = new Location(Bukkit.getWorld("DAC"), location.getBlock().getX(), location.getBlock().getY(), location.getBlock().getZ());
                    Block b = bloc.getBlock();
                    b.setType(DAC.getInstance().getPlayersBlock().get(player));
                    DAC.getInstance().getBlocksLocation().put(b, b.getLocation());
                    for (Player pls : DAC.getInstance().getPlayersGameList()){
                        pls.sendMessage("§f" + player.getDisplayName() + " §6a réussi son saut de l'ange !");
                    }
                    if(DAC.getInstance().getPlayersPoints().containsKey(player)){
                        int currentpoints = DAC.getInstance().getPlayersPoints().get(player);
                        DAC.getInstance().getPlayersPoints().put(player, currentpoints+100);
                    }
                    player.teleport(Locations.POOL.getLocation());
                    if(poolIsFull()){
                        DAC.getInstance().setGameState(GameState.FINISH);
                        return;
                    }
                    DAC.getInstance().getPlayerTurn().chooseNextPlayer();
                }
            }
        }
    }

    public Boolean poolIsFull(){
        for(int x=-163; x<=-155; x++){
            for(int z=-643; z<=-635; z++){
                Location loc = new Location(Bukkit.getWorld("DAC"), x, 1, z);
                Block b = loc.getBlock();
                if(b.getType().equals(Material.WATER)){
                    return false;
                }
            }
        }
        return true;
    }
}
