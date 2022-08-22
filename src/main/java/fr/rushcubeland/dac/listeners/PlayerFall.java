package fr.rushcubeland.dac.listeners;

import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.dac.spells.*;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class PlayerFall implements Listener {

    @EventHandler
    public void onFall(EntityDamageEvent event){
        if(DAC.getInstance().isState(GameState.INPROGRESS)){
            if(event.getEntity() instanceof Player){
                event.setCancelled(true);
                Player player = (Player) event.getEntity();
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL && DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
                    if(DAC.getInstance().getPlayersSpell().containsKey(player)){
                        Spell spell = DAC.getInstance().getPlayersSpell().get(player);
                        if(spell instanceof DestructionSpell){
                            ((DestructionSpell) spell).end();
                        }
                        else if(spell instanceof TrismegisteSpell && spell.isActivated()){
                            for (Player pls : DAC.getInstance().getPlayersGameList()){
                                pls.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.GOLD + " a réussi son saut de l'ange grâce au " + ChatColor.RED +  SpellUnit.TRISMEGISTE.getName());
                            }
                            player.setGameMode(GameMode.SPECTATOR);
                            spell.stop();
                            RcbAPI.getInstance().getAccountStatsDAC(player, result -> {
                                AStatsDAC aStatsDAC = (AStatsDAC) result;
                                aStatsDAC.setNbSuccessJumps(aStatsDAC.getNbSuccessJumps()+1);
                                aStatsDAC.setNbJumps(aStatsDAC.getNbJumps()+1);
                                RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
                            });
                            DAC.getInstance().getPlayerTurn().initNextPlayer(DAC.getInstance().getPlayerTurn().getNextPositionRequired());
                            return;
                        }
                        else
                        {
                            spell.stop();
                        }
                    }
                    DAC.getInstance().deathMethod(player);
                }
            }
        }
        else
        {
            event.setCancelled(true);
        }
    }

}
