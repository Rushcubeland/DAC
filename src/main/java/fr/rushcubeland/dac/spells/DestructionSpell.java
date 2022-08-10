package fr.rushcubeland.dac.spells;

import fr.rushcubeland.dac.DAC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class DestructionSpell extends Spell {

    private int tid;

    public DestructionSpell(Player player) {
        super(player);
    }

    @Override
    public void use() {
        super.use();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public String getName(){
        return "Destruction";
    }

    @Override
    public int getPrice() {
        return SpellUnit.DESTRUCTION.getPrice();
    }

    @Override
    public void run() {
        tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(DAC.getInstance(), () -> {

            if(getPlayer().getInventory().getItemInMainHand().getType().equals(SpellUnit.DESTRUCTION.getMaterial())){
                Location location = getPlayer().getEyeLocation();
                BlockIterator blocks = new BlockIterator(location, 0D, 80);
                while(blocks.hasNext()){
                    Block block = blocks.next();
                    if(block.getType().equals(Material.AIR)){
                        getPlayer().getWorld().spawnParticle(Particle.FLAME, block.getLocation(), 1);
                    }
                    else if(block.getType().toString().endsWith("WOOL") && this.isActivated()){
                        getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, block.getLocation(), 1);
                        DAC.getInstance().getBlocksLocation().remove(block);
                        block.setType(Material.WATER);
                        DestructionSpell spell = (DestructionSpell) DAC.getInstance().getPlayersSpell().get(getPlayer());
                        spell.end();
                        Bukkit.broadcastMessage(ChatColor.WHITE + getPlayer().getDisplayName() + " " + ChatColor.GOLD + "a utilisé son sort de " + ChatColor.RED + getName()
                                + ChatColor.GOLD + " pour " + ChatColor.YELLOW + getPrice() + ChatColor.GOLD + " points");
                        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                        break;
                    }
                }
            }
        }, 0L, 20L);
    }

    public void end(){
        stop(tid);
    }

}
