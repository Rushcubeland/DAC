package fr.rushcubeland.dac.listeners;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.dac.game.Locations;
import fr.rushcubeland.dac.spells.*;
import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.map.MapUnit;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

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
        if(DAC.getInstance().isState(GameState.INPROGRESS) && DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
            Location location = player.getLocation();
            if((location.getBlock().isLiquid() && location.getBlock().getType().equals(Material.WATER)) ||
                    (location.getBlock().getRelative(BlockFace.DOWN).isLiquid() && location.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.WATER))){
                Location block = new Location(Bukkit.getWorld(MapUnit.DAC.getPath()), location.getBlock().getX(), location.getBlock().getY(), location.getBlock().getZ());
                Block b = block.getBlock();
                b.setType(DAC.getInstance().getPlayersBlock().get(player));
                DAC.getInstance().getBlocksLocation().put(b, b.getLocation());
                int currentPoints = DAC.getInstance().getPlayersPoints().get(player);
                int pointsToGive = (100 * getMultiplierPoints(block));
                DAC.getInstance().getPlayersPoints().put(player, currentPoints+pointsToGive);
                for (Player pls : DAC.getInstance().getPlayersGameList()){
                    pls.sendMessage("§f" + player.getDisplayName() + " §6a réussi son saut de l'ange pour §c" + pointsToGive + " §6points !");
                }
                player.setGameMode(GameMode.SPECTATOR);
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
                DAC.getInstance().getPlayerTurn().initNextPlayer(DAC.getInstance().getPlayerTurn().getNextPositionRequired());
                RcbAPI.getInstance().getAccountStatsDAC(player, result -> {
                    AStatsDAC aStatsDAC = (AStatsDAC) result;
                    aStatsDAC.setNbSuccessJumps(aStatsDAC.getNbSuccessJumps()+1);
                    aStatsDAC.setNbJumps(aStatsDAC.getNbJumps()+1);
                    RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
                });
            }
            else if(DAC.getInstance().getPlayersSpell().containsKey(player) && DAC.getInstance().getPlayersSpell().get(player) instanceof LevitationSpell && DAC.getInstance().getPlayersSpell().get(player).isActivated()){
                Block b = location.getBlock().getRelative(BlockFace.DOWN);
                if(!b.getType().equals(Material.WATER) && !b.isLiquid() && b.getY() < Locations.DIVING_PLATFORM.getLocation().getY()-5 && !b.getType().equals(Material.AIR)){
                    LevitationSpell spell = (LevitationSpell) DAC.getInstance().getPlayersSpell().get(player);
                    spell.stop();
                    DAC.getInstance().deathMethod(player);
                }
            }
            else if(DAC.getInstance().getPlayersSpell().containsKey(player) && DAC.getInstance().getPlayersSpell().get(player).isActivated() && (DAC.getInstance().getPlayersSpell().get(player) instanceof TrismegisteSpell || DAC.getInstance().getPlayersSpell().get(player) instanceof LevitationSpell)){
                Block b = location.getBlock().getRelative(BlockFace.DOWN);
                if(!b.getType().equals(Material.WATER) && !b.isLiquid() && b.getY() < Locations.DIVING_PLATFORM.getLocation().getY()-3 && !b.getType().equals(Material.AIR)){
                    DAC.getInstance().deathMethod(player);
                }
            }
        }
    }

    public boolean poolIsFull(){
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

    public int getMultiplierPoints(Location loc){
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
