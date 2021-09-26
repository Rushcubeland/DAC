package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.spells.DestructionSpell;
import fr.didi955.dac.spells.Spell;
import fr.didi955.dac.spells.SpellUnit;
import fr.rushcubeland.rcbapi.bukkit.BukkitSend;
import fr.rushcubeland.rcbapi.bukkit.network.Network;
import fr.rushcubeland.rcbapi.bukkit.network.ServerGroup;
import fr.rushcubeland.rcbapi.bukkit.network.ServerUnit;
import fr.rushcubeland.rcbapi.bukkit.queue.QueueUnit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
            }
            if(event.getItem().getType().equals(Material.NETHER_STAR)){
                BukkitSend.requestJoinQueue(player, QueueUnit.DE_A_COUDRE);
            }
            if(DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
                if(!DAC.getInstance().getPlayersSpell().containsKey(player)){
                    if(event.getItem().getType().equals(SpellUnit.DESTRUCTION.getMaterial())){
                        Constructor<? extends Spell> constructor = SpellUnit.DESTRUCTION.getClazz().getConstructor(Player.class);
                        Spell spell = constructor.newInstance(player);
                        spell.use();
                    }
                    if(event.getItem().getType().equals(SpellUnit.LEVITATION.getMaterial())){
                        Constructor<? extends Spell> constructor = SpellUnit.LEVITATION.getClazz().getConstructor(Player.class);
                        Spell spell = constructor.newInstance(player);
                        spell.use();
                    }
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
