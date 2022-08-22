package fr.rushcubeland.dac.listeners;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.tools.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        Account account = RcbAPI.getInstance().getAccount(player);
        RankUnit rank = account.getRank();

        RcbAPI.getInstance().deleteScoreboard(player);

        if (DAC.getInstance().isState(GameState.STARTING) || DAC.getInstance().isState(GameState.WAITING)) {
            event.setQuitMessage(DAC.DAC_PREFIX + " " + rank.getPrefix() + player.getDisplayName() + ChatColor.RED + " a quitté la partie ! " + ChatColor.GRAY + "<" + DAC.getInstance().getPlayersGameList().size() + ChatColor.YELLOW + "/" + ChatColor.GOLD + DAC.getInstance().getMaxPlayer() + ChatColor.GRAY + ">");
        }
        if (DAC.getInstance().getPlayersGameList().contains(player)) {
            DAC.getInstance().getPlayersGameList().remove(player);
            DAC.getInstance().getPlayersServerList().remove(player);

            if (DAC.getInstance().isState(GameState.INPROGRESS)) {
                event.setQuitMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " est mort en se déconnectant !");
                for(Player pls : Bukkit.getOnlinePlayers()) {
                    TitleManager.sendActionBar(pls, ChatColor.GRAY + "Il ne reste plus que " + ChatColor.RED + DAC.getInstance().getPlayersGameList().size() + ChatColor.GOLD + " joueurs");
                }
            }
            if(DAC.getInstance().getPlayersGameList().size() == 1) {
                DAC.getInstance().setGameState(GameState.FINISH);
                return;
            }
        }
        if (DAC.getInstance().isState(GameState.FINISH)) {
            event.setQuitMessage(null);
        }
    }

}
