package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import fr.didi955.dac.game.PlayerTurn;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFall implements Listener {

    @EventHandler
    public void onFall(EntityDamageEvent event){
        if(DAC.getInstance().isState(GameState.INPROGRESS)){
            if(event.getEntity() instanceof Player){
                event.setCancelled(true);
                Player player = (Player) event.getEntity();
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL){
                    if(DAC.getInstance().getPlayerTurn().getPlayerTurn().equals(player)){
                        player.sendTitle("§cDommage, tu t'es loupé !", "§fTu feras mieux la prochaine fois", 10, 70, 20);
                        player.setGameMode(GameMode.SPECTATOR);
                        DAC.getInstance().getPlayersGameList().remove(player);
                        for (Player pls : DAC.getInstance().getPlayersGameList()){
                            pls.sendMessage("§e" + player.getDisplayName() + " §ca hurté un bloc !");
                        }
                        player.teleport(Locations.POOL.getLocation());
                        if(DAC.getInstance().getPlayersGameList().size() == 1) {
                            DAC.getInstance().setGameState(GameState.FINISH);
                            return;
                        }
                        DAC.getInstance().getPlayerTurn().chooseNextPlayer();
                    }

                }
            }
        }
        else
        {
            event.setCancelled(true);
        }
    }

}
