package fr.didi955.dac.tasks;

import fr.didi955.dac.DAC;
import fr.rushcubeland.rcbcore.bukkit.network.Network;
import fr.rushcubeland.rcbcore.bukkit.network.ServerGroup;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class FinishFireworks extends BukkitRunnable {

    private int timer = 7;

    public void run() {

        if(DAC.getInstance().getPlayersGameList().size() == 0){
            end();
            return;
        }
        Player winner = DAC.getInstance().getPlayersGameList().get(0);
        
        if (timer == 7 || timer == 6 || timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1) {

            Firework f = winner.getWorld().spawn(winner.getLocation(), Firework.class);
            FireworkMeta fm = f.getFireworkMeta();

            fm.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.AQUA).withColor(Color.YELLOW).withColor(Color.ORANGE).withColor(Color.ORANGE).withColor(Color.YELLOW).build());
            fm.setPower(0);
            f.setFireworkMeta(fm);
        }
        if (timer == 0) {
            end();
        }

        timer--;
    }

    private void end(){
        DAC.getInstance().getPlayersGameList().clear();
        cancel();

        new BukkitRunnable(){

            public void run()
            {
                for (Player pls : Bukkit.getOnlinePlayers()) {
                    Network.sendPlayerToServer(pls, Network.getBestServer(pls, ServerGroup.Lobby));
                }

                Bukkit.spigot().restart();
            }
        }.runTaskLater(DAC.getInstance(), 800L);
    }

}

