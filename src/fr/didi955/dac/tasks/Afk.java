package fr.didi955.dac.tasks;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class Afk extends BukkitRunnable {

    private int timer = 40;
    private final Player player;

    public Afk(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if(!DAC.getInstance().getPlayerTurn().getPlayer().equals(player) || DAC.getInstance().isState(GameState.FINISH)){
            return;
        }
        if(timer == 15){
            player.sendMessage("§cIl te reste §e" + timer + " secondes §cpour sauter !");
        }
        if(timer == 5){
            player.sendMessage("§c" + timer + " secondes pour sauter !");
        }
        if(timer == 0){
            if(DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
                cancel();
                player.sendTitle("§cDommage, tu as pris trop de temps", "§fTu feras mieux la prochaine fois", 10, 70, 20);
                player.setGameMode(GameMode.SPECTATOR);
                player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 0L, 0L);
                DAC.getInstance().getPlayersGameList().remove(player);
                for (Player pls : DAC.getInstance().getPlayersGameList()){
                    pls.sendMessage("§e" + player.getDisplayName() + " §ca été disqualifié pour avoir été innactif");
                }
                player.teleport(Locations.POOL.getLocation());
                if(DAC.getInstance().getPlayersGameList().size() == 1) {
                    DAC.getInstance().setGameState(GameState.FINISH);
                    return;
                }
                DAC.getInstance().getPlayerTurn().initNextPlayer(DAC.getInstance().getPlayerTurn().getNextPositionRequired());
            }
        }
        timer--;
    }
}
