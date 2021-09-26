package fr.didi955.dac.spells;

import fr.didi955.dac.DAC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class LevitationSpell extends Spell {

    private int timer = 10;

    public LevitationSpell(Player player) {
        super(player);
    }

    @Override
    public void use() {
        super.use();
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 250));

    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void run() {

        setBukkitTask(Bukkit.getScheduler().runTaskTimer(DAC.getInstance(), () -> {

            Location location = getPlayer().getLocation();
            location.setY(location.getBlockY()-1);
            getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);

            for(int i = 0; i < 8; i++){
                // TEST
                location.setX(location.getBlockX()+1);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setX(location.getBlockX()-2);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setZ(location.getBlockZ()+1);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setZ(location.getBlockZ()-2);
                getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
                location.setZ(location.getBlockZ()-2);
            }

            getPlayer().getWorld().spawnParticle(Particle.CLOUD, location, 1);
            location.setX(location.getBlockY()-1);

            if(timer == 0){
                stop();
            }

            timer--;

        }, 0L, 20L));

    }
}
