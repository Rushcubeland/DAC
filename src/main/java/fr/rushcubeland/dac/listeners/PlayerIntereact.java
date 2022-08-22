package fr.rushcubeland.dac.listeners;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.spells.DestructionSpell;
import fr.rushcubeland.dac.spells.Spell;
import fr.rushcubeland.dac.spells.SpellUnit;
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
            buySort(event, player);
        }
        if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK) &&
                player.getInventory().getItemInMainHand().getType().equals(SpellUnit.DESTRUCTION.getMaterial()) &&
                DAC.getInstance().getPlayerTurn().getPlayer().equals(player) && DAC.getInstance().getPlayersSpell().containsKey(player) &&
                DAC.getInstance().getPlayersSpell().get(player) instanceof DestructionSpell){
            DestructionSpell spell = (DestructionSpell) DAC.getInstance().getPlayersSpell().get(player);
            if(spell != null){
                spell.activate();
            }
        }
    }

    private void buySort(PlayerInteractEvent event, Player player) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException{
        if(DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
            event.setCancelled(true);
            if(DAC.getInstance().getPlayersSpell().containsKey(player)){
                player.sendMessage(ChatColor.RED + " Vous ne pouvez pas utiliser ce sort !");
                return;
            }
            if(DAC.getInstance().getPlayersPoints().containsKey(player)){
                if(event.getItem() == null) {
                    return;
                }
                if(event.getItem().getType().equals(SpellUnit.DESTRUCTION.getMaterial()) && DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.DESTRUCTION.getPrice()){
                    Constructor<? extends Spell> constructor = SpellUnit.DESTRUCTION.getClazz().getConstructor(Player.class);
                    Spell spell = constructor.newInstance(player);
                    spell.use();
                    DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.DESTRUCTION.getPrice());
                    return;
                }
                if(event.getItem().getType().equals(SpellUnit.LEVITATION.getMaterial()) && DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.LEVITATION.getPrice()){
                    Constructor<? extends Spell> constructor = SpellUnit.LEVITATION.getClazz().getConstructor(Player.class);
                    Spell spell = constructor.newInstance(player);
                    spell.use();
                    DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.LEVITATION.getPrice());
                    return;
                }
                if(event.getItem().getType().equals(SpellUnit.EMPRISONNEMENT.getMaterial()) && DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.EMPRISONNEMENT.getPrice()){
                    Constructor<? extends Spell> constructor = SpellUnit.EMPRISONNEMENT.getClazz().getConstructor(Player.class);
                    Spell spell = constructor.newInstance(player);
                    spell.use();
                    DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.EMPRISONNEMENT.getPrice());
                    return;
                }
                if(event.getItem().getType().equals(SpellUnit.DISTORSION.getMaterial()) && DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.DISTORSION.getPrice()){
                    Constructor<? extends Spell> constructor = SpellUnit.DISTORSION.getClazz().getConstructor(Player.class);
                    Spell spell = constructor.newInstance(player);
                    spell.use();
                    DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.DISTORSION.getPrice());
                    return;
                }
                if(event.getItem().getType().equals(SpellUnit.TRISMEGISTE.getMaterial()) && DAC.getInstance().getPlayersPoints().get(player) >= SpellUnit.TRISMEGISTE.getPrice()){
                    Constructor<? extends Spell> constructor = SpellUnit.TRISMEGISTE.getClazz().getConstructor(Player.class);
                    Spell spell = constructor.newInstance(player);
                    spell.use();
                    DAC.getInstance().getPlayersPoints().replace(player, DAC.getInstance().getPlayersPoints().get(player)-SpellUnit.TRISMEGISTE.getPrice());
                    return;
                }
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de points pour utiliser ce sort !");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.AMBIENT, 1F, 1F);
            }
        }
    }
}
