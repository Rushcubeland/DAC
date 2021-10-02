package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.rushcubeland.commons.Account;;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbapi.bukkit.RcbAPI;
import fr.rushcubeland.rcbapi.bukkit.tools.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        Account account = RcbAPI.getInstance().getAccount(player);
        RankUnit rank = account.getRank();

        RcbAPI.getInstance().deleteScoreboard(player);

        if (DAC.getInstance().isState(GameState.STARTING) || DAC.getInstance().isState(GameState.WAITING)) {
            event.setQuitMessage("§e[§bDé §6à §bCoudre§e] " + rank.getPrefix() + player.getDisplayName() + " §ca quitté la partie ! " + "§7<" + DAC.getInstance().getPlayersGameList().size() + "§e/§6" + DAC.getInstance().getMaxPlayer() + "§7>");
        }
        if (DAC.getInstance().getPlayersGameList().contains(player)) {
            DAC.getInstance().getPlayersGameList().remove(player);
            DAC.getInstance().getPlayersServerList().remove(player);

            if (DAC.getInstance().isState(GameState.INPROGRESS)) {
                event.setQuitMessage("§e" + player.getName() + " §cest mort en se déconnectant !");
                for(Player pls : Bukkit.getOnlinePlayers()) {
                    TitleManager.sendActionBar(pls, "§6Il ne reste plus que " + DAC.getInstance().getPlayersGameList().size() + " §6joueurs");
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
