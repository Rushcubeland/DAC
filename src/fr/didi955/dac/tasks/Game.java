package fr.didi955.dac.tasks;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbapi.bukkit.RcbAPI;
import fr.rushcubeland.rcbapi.bukkit.tools.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class Game extends BukkitRunnable {

    private int timer = 2;

    @Override
    public void run() {

        for (Map.Entry<Player, ScoreboardSign> sign : RcbAPI.getInstance().boards.entrySet()) {
            Player player = sign.getKey();
            sign.getValue().setLine(4, "§6Tour: §e" + DAC.getInstance().getPlayerTurn().getPlayerName());
            if(DAC.getInstance().getPlayersPoints().containsKey(player)){
                sign.getValue().setLine(6, "§cPoints: §6" + DAC.getInstance().getPlayersPoints().get(player));
            }
            sign.getValue().setLine(8, "§6Joueurs restant: §c" + DAC.getInstance().getPlayersGameList().size());
        }

        if(timer == 1){
            DAC.getInstance().getPlayerTurn().makeAnnouncement();
        }

        if(timer == 0){
            DAC.getInstance().getPlayerTurn().teleportPlayer();
        }

        if(DAC.getInstance().isState(GameState.FINISH)){
            cancel();
            Player winner = DAC.getInstance().getPlayersGameList().get(0);
            Account account = RcbAPI.getInstance().getAccount(winner);
            account.setCoins(account.getCoins()+100);
            RcbAPI.getInstance().sendAccountToRedis(account);
            Bukkit.broadcastMessage(account.getRank().getPrefix() + winner.getDisplayName() + " §aa gagné la partie !");
            winner.sendTitle("§6Félicitations !", "§fVous avez gagné", 10, 70, 20);
            winner.sendMessage(" ");
            winner.sendMessage("§e-------------------------");
            winner.sendMessage("§6Récompenses:");
            winner.sendMessage("§c ");
            winner.sendMessage("§ePoints: §6" + DAC.getInstance().getPlayersPoints().get(winner));
            winner.sendMessage("§eVictoire: §c100 Coins");
            winner.sendMessage("§eParticipation: §c10 Coins");
            winner.sendMessage("§e-------------------------");
            for(Player pls : DAC.getInstance().getPlayersServerList()){
                RcbAPI.getInstance().getTablist().resetTabListPlayer(pls);
                RcbAPI.getInstance().getTablist().setTabListPlayer(pls);
                if(DAC.getInstance().getPlayersPoints().containsKey(pls)){
                    if(!pls.equals(winner)){
                        pls.sendMessage(" ");
                        pls.sendMessage("§e-------------------------");
                        pls.sendMessage("§6Récompenses:");
                        pls.sendMessage("§c ");
                        pls.sendMessage("§ePoints: §6" + DAC.getInstance().getPlayersPoints().get(pls));
                        pls.sendMessage("§eParticipation: §c10 Coins");
                        pls.sendMessage("§e-------------------------");
                    }
                }
            }
            for(Player pls : DAC.getInstance().getPlayersServerList()){
                pls.teleport(Locations.POOL.getLocation());
            }
            FinishFireworks finishFireworks = new FinishFireworks();
            finishFireworks.runTaskTimer(DAC.getInstance(), 0L, 20L);
        }

        this.timer--;

    }

    public void resetTimer(){
        timer = 2;
    }
}
