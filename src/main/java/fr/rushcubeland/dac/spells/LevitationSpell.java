package fr.rushcubeland.dac.spells;

import fr.rushcubeland.dac.DAC;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class LevitationSpell extends Spell implements SpellRunnable {

    private int timer = 4;
    private BukkitTask task;

    public LevitationSpell(Player player) {
        super(player);
    }

    @Override
    public void use() {
        super.use();
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, timer*20, 250));
        broadcast();
    }

    @Override
    public String getName() {
        return "Lévitation";
    }

    @Override
    public int getPrice() {
        return SpellUnit.LEVITATION.getPrice();
    }

    @Override
    public void run() {

        this.task = Bukkit.getScheduler().runTaskTimer(DAC.getInstance(), () -> {

            Location location = getPlayer().getLocation();
            location.setY(location.getBlockY()-1D);
            getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);

            for(int i = 0; i < 8; i++){
                location.setX(location.getBlockX()+1D);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setX(location.getBlockX()-2D);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setZ(location.getBlockZ()+1D);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setZ(location.getBlockZ()-2D);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setZ(location.getBlockZ()-2D);
            }

            getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
            location.setX(location.getBlockY()-1D);

            if(timer == 0){
                stop();
            }

            timer--;

        }, 0L, 20L);
    }

    @Override
    public void stop(int tid) {
        Bukkit.getScheduler().cancelTask(tid);
    }

    @Override
    public void stop(BukkitTask task){
        task.cancel();
    }

    public void stop(){
        super.stop();
        stop(this.task);
    }
}
