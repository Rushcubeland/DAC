package fr.rushcubeland.dac.tasks;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.dac.game.Locations;
import org.bukkit.ChatColor;
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
            player.sendMessage(ChatColor.RED + "Il te reste " + ChatColor.YELLOW + timer + " secondes " + ChatColor.RED + "pour sauter !");
        }
        if(timer == 5){
            player.sendMessage(ChatColor.RED + "" + timer + " secondes pour sauter !");
        }
        if(timer == 0 && DAC.getInstance().getPlayerTurn().getPlayer().equals(player)){
            cancel();
            player.sendTitle(ChatColor.RED + "Dommage, tu as pris trop de temps", ChatColor.WHITE + "Tu feras mieux la prochaine fois", 10, 70, 20);
            player.setGameMode(GameMode.SPECTATOR);
            player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 0L, 0L);
            DAC.getInstance().getPlayersGameList().remove(player);
            for (Player pls : DAC.getInstance().getPlayersGameList()){
                pls.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.RED + " a été disqualifié pour avoir été innactif");
            }
            player.teleport(Locations.POOL.getLocation());
            if(DAC.getInstance().getPlayersGameList().size() == 1) {
                DAC.getInstance().setGameState(GameState.FINISH);
                return;
            }
            DAC.getInstance().getPlayerTurn().initNextPlayer(DAC.getInstance().getPlayerTurn().getNextPositionRequired());
        }
        timer--;
    }
}
