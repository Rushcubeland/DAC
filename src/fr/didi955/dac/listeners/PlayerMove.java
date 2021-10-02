package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import fr.didi955.dac.game.PlayerTurn;
import fr.didi955.dac.spells.DestructionSpell;
import fr.didi955.dac.spells.LevitationSpell;
import fr.didi955.dac.spells.Spell;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
            if(DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
                Location location = player.getLocation();
                if(location.getBlock().isLiquid() && location.getBlock().getType().equals(Material.WATER)){
                    Location block = new Location(Bukkit.getWorld("DAC"), location.getBlock().getX(), location.getBlock().getY(), location.getBlock().getZ());
                    Block b = block.getBlock();
                    b.setType(DAC.getInstance().getPlayersBlock().get(player));
                    DAC.getInstance().getBlocksLocation().put(b, b.getLocation());
                    int currentpoints = DAC.getInstance().getPlayersPoints().get(player);
                    int pointsToGive = (100 * getMultiplierPoints(block));
                    DAC.getInstance().getPlayersPoints().put(player, currentpoints+pointsToGive);
                    for (Player pls : DAC.getInstance().getPlayersGameList()){
                        pls.sendMessage("§f" + player.getDisplayName() + " §6a réussi son saut de l'ange pour §c" + pointsToGive + " §6points !");
                    }
                    player.teleport(Locations.POOL.getLocation());
                    if(poolIsFull()){
                        DAC.getInstance().setGameState(GameState.FINISH);
                        DAC.getInstance().getPlayersGameList().removeIf(pls -> pls != DAC.getInstance().getPlayerTurn().getPlayer());
                        Bukkit.broadcastMessage("§6La piscine est remplie !");
                        return;
                    }
                    if(DAC.getInstance().getPlayersSpell().containsKey(player)){
                        Spell spell = DAC.getInstance().getPlayersSpell().get(player);
                        if(spell instanceof DestructionSpell){
                            ((DestructionSpell) spell).end();
                        }
                        else
                        {
                            spell.stop();
                        }
                    }
                    DAC.getInstance().getPlayerTurn().chooseNextPlayer();
                }
                else if(DAC.getInstance().getPlayersSpell().containsKey(player)){
                    if(DAC.getInstance().getPlayersSpell().get(player) instanceof LevitationSpell && DAC.getInstance().getPlayersSpell().get(player).isActivated()){
                        Block b = location.getBlock().getRelative(BlockFace.DOWN);
                        if(!b.getType().equals(Material.WATER) && !b.isLiquid() && b.getY() < 50 && !b.getType().equals(Material.AIR)){
                            LevitationSpell spell = (LevitationSpell) DAC.getInstance().getPlayersSpell().get(player);
                            spell.stop();
                            DAC.getInstance().deathMethod(player);
                        }
                    }
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

    public Integer getMultiplierPoints(Location loc){
        int nb = 1;
        int x = loc.getBlock().getX();
        int z = loc.getBlock().getZ();
        loc.setZ(z+1);
        for(Material m : DAC.getInstance().getPlayersBlock().values()){
            if(loc.getBlock().getType().equals(m)){
                nb += 1;
            }
        }
        loc.setZ(z-2);
        for(Material m : DAC.getInstance().getPlayersBlock().values()){
            if(loc.getBlock().getType().equals(m)){
                nb += 1;
            }
        }
        loc.setX(x+1);
        loc.setZ(z);
        for(Material m : DAC.getInstance().getPlayersBlock().values()){
            if(loc.getBlock().getType().equals(m)){
                nb +=1;
            }
        }
        loc.setX(x-2);
        for(Material m : DAC.getInstance().getPlayersBlock().values()){
            if(loc.getBlock().getType().equals(m)){
                nb +=1;
            }
        }
        return nb;
    }
}
