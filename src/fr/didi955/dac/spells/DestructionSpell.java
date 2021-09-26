package fr.didi955.dac.spells;

import fr.didi955.dac.DAC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

public class DestructionSpell extends Spell {

    private int tid;
    private boolean activate = false;

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

    public void activate(){
        this.activate = true;
    }

    @Override
    public void run() {
        tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(DAC.getInstance(), () -> {

            if(getPlayer().getInventory().getItemInMainHand().getType().equals(SpellUnit.DESTRUCTION.getMaterial())){
                Location location = getPlayer().getEyeLocation();
                BlockIterator blocks = new BlockIterator(location, 0D, 50);
                while(blocks.hasNext()){
                    Block block = blocks.next();
                    if(block.getType().equals(Material.AIR)){
                        getPlayer().getWorld().spawnParticle(Particle.FLAME, block.getLocation(), 1);
                    }
                    // DOESN'T WORK
                    else if(block.getType().toString().endsWith("WOOL") && this.activate){
                        DAC.getInstance().getBlocksLocation().remove(block);
                        blocks.next().setType(Material.AIR);
                        DestructionSpell spell = (DestructionSpell) DAC.getInstance().getPlayersSpell().get(getPlayer());
                        spell.end();
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
