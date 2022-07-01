package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.spells.DestructionSpell;
import fr.didi955.dac.spells.LevitationSpell;
import fr.didi955.dac.spells.Spell;
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
                        else
                        {
                            spell.stop();
                        }
                        if(spell instanceof LevitationSpell){
                            return;
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
