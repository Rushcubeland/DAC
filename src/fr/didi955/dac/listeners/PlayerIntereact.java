package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.spells.DestructionSpell;
import fr.didi955.dac.spells.Spell;
import fr.didi955.dac.spells.SpellUnit;
import fr.rushcubeland.rcbcore.bukkit.BukkitSend;
import fr.rushcubeland.rcbcore.bukkit.network.Network;
import fr.rushcubeland.rcbcore.bukkit.network.ServerGroup;
import fr.rushcubeland.rcbcore.bukkit.queue.QueueUnit;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class PlayerIntereact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Player player = event.getPlayer();

        if(event.getItem() == null) {
            return;
        }

        if((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
            if(event.getItem().getType().equals(Material.RED_BED)){
                player.closeInventory();
                Network.sendPlayerToServer(player, Network.getBestServer(player, ServerGroup.Lobby));
                return;
            }
            if(event.getItem().getType().equals(Material.NETHER_STAR)){
                BukkitSend.requestJoinQueue(player, QueueUnit.DE_A_COUDRE);
                return;
            }
            if(DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
                event.setCancelled(true);
                if(DAC.getInstance().getPlayersSpell().containsKey(player)){
                    player.sendMessage(ChatColor.RED + " Vous ne pouvez pas utiliser ce sort !");
                    return;
                }
                if(DAC.getInstance().getPlayersPoints().containsKey(player)){
                    if(event.getItem().getType().equals(SpellUnit.DESTRUCTION.getMaterial())){
                        if(DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.DESTRUCTION.getPrice()){
                            Constructor<? extends Spell> constructor = SpellUnit.DESTRUCTION.getClazz().getConstructor(Player.class);
                            Spell spell = constructor.newInstance(player);
                            spell.use();
                            DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.DESTRUCTION.getPrice());
                            return;
                        }
                    }
                    if(event.getItem().getType().equals(SpellUnit.LEVITATION.getMaterial())){
                        if(DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.LEVITATION.getPrice()){
                            Constructor<? extends Spell> constructor = SpellUnit.LEVITATION.getClazz().getConstructor(Player.class);
                            Spell spell = constructor.newInstance(player);
                            spell.use();
                            DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.LEVITATION.getPrice());
                            return;
                        }
                    }
                    if(event.getItem().getType().equals(SpellUnit.EMPRISONNEMENT.getMaterial())){
                        if(DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.EMPRISONNEMENT.getPrice()){
                            Constructor<? extends Spell> constructor = SpellUnit.EMPRISONNEMENT.getClazz().getConstructor(Player.class);
                            Spell spell = constructor.newInstance(player);
                            spell.use();
                            DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.EMPRISONNEMENT.getPrice());
                            return;
                        }
                    }
                    if(event.getItem().getType().equals(SpellUnit.DISTORSION.getMaterial())){
                        if(DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.DISTORSION.getPrice()){
                            Constructor<? extends Spell> constructor = SpellUnit.DISTORSION.getClazz().getConstructor(Player.class);
                            Spell spell = constructor.newInstance(player);
                            spell.use();
                            DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.DISTORSION.getPrice());
                            return;
                        }
                    }
                    player.sendMessage("§cVous n'avez pas assez de points pour utiliser ce sort !");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.AMBIENT, 1F, 1F);
                    return;
                }
            }
        }
        if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            if(player.getInventory().getItemInMainHand().getType().equals(SpellUnit.DESTRUCTION.getMaterial())){
                if(DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
                    if(DAC.getInstance().getPlayersSpell().containsKey(player)){
                        if(DAC.getInstance().getPlayersSpell().get(player) instanceof DestructionSpell){
                            DestructionSpell spell = (DestructionSpell) DAC.getInstance().getPlayersSpell().get(player);
                            spell.activate();
                        }
                    }
                }
            }
        }
    }
}
