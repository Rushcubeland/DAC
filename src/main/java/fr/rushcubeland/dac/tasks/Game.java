package fr.rushcubeland.dac.tasks;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.dac.game.Locations;
import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.tools.ItemBuilder;
import fr.rushcubeland.rcbcore.bukkit.tools.ScoreboardSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class Game extends BukkitRunnable {

    private static final String SEPARATOR = ChatColor.YELLOW + "-------------------------";

    private int timer = 2;

    @Override
    public void run() {

        for (Map.Entry<Player, ScoreboardSign> sign : RcbAPI.getInstance().boards.entrySet()) {
            Player player = sign.getKey();
            sign.getValue().setLine(4, ChatColor.GOLD + "Tour: " + ChatColor.YELLOW + DAC.getInstance().getPlayerTurn().getPlayerName());
            if(DAC.getInstance().getPlayersPoints().containsKey(player)){
                sign.getValue().setLine(6, ChatColor.RED + "Points: " + ChatColor.GOLD + DAC.getInstance().getPlayersPoints().get(player));
            }
            sign.getValue().setLine(8, ChatColor.GOLD + "Joueurs restant: " + ChatColor.RED + DAC.getInstance().getPlayersGameList().size());
        }

        if(timer == 1){
            DAC.getInstance().getPlayerTurn().makeAnnouncement();
        }

        if(timer == 0 && !DAC.getInstance().getPlayerTurn().isFirst()){
            DAC.getInstance().getPlayerTurn().teleportPlayer();
        }

        if(DAC.getInstance().isState(GameState.FINISH)){
            cancel();
            Player winner = DAC.getInstance().getPlayersGameList().get(0);
            if (winner != null) {
                RcbAPI.getInstance().getAccount(winner, result -> {
                    Account account = (Account) result;
                    account.setCoins(account.getCoins() + 100);
                    RcbAPI.getInstance().sendAccountToRedis(account);
                    Bukkit.broadcastMessage(account.getRank().getPrefix() + winner.getDisplayName() + ChatColor.GREEN + " a gagné la partie !");
                });
                RcbAPI.getInstance().getAccountStatsDAC(winner, result -> {
                    AStatsDAC aStatsDAC = (AStatsDAC) result;
                    aStatsDAC.setWins(aStatsDAC.getWins() + 1);
                    RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
                });
                winner.sendTitle(ChatColor.GOLD + "Félicitations !", ChatColor.WHITE + "Vous avez gagné", 10, 70, 20);
                winner.sendMessage(" ");
                winner.sendMessage(SEPARATOR);
                winner.sendMessage(ChatColor.GOLD + "Récompenses:");
                winner.sendMessage(" ");
                winner.sendMessage(ChatColor.YELLOW + "Points: " + ChatColor.GOLD + DAC.getInstance().getPlayersPoints().get(winner));
                winner.sendMessage(ChatColor.YELLOW + "Victoire: " + ChatColor.RED + "100 Coins");
                winner.sendMessage(ChatColor.YELLOW + "Participation: " + ChatColor.RED + "10 Coins");
                winner.sendMessage(SEPARATOR);
            }
            for(Player pls : DAC.getInstance().getPlayersServerList()){
                for (PotionEffect effect : pls.getActivePotionEffects()) {
                    pls.removePotionEffect(effect.getType());
                }
                RcbAPI.getInstance().getTablist().resetTabListPlayer(pls);
                RcbAPI.getInstance().getTablist().setTabListPlayer(pls);
                if(DAC.getInstance().getPlayersPoints().containsKey(pls) && !pls.equals(winner)){
                    pls.sendMessage(" ");
                    pls.sendMessage(SEPARATOR);
                    pls.sendMessage(ChatColor.GOLD + "Récompenses:");
                    pls.sendMessage(" ");
                    pls.sendMessage(ChatColor.YELLOW + "Points: " + ChatColor.GOLD + DAC.getInstance().getPlayersPoints().get(pls));
                    pls.sendMessage(ChatColor.YELLOW + "Participation: " + ChatColor.RED + "10 Coins");
                    pls.sendMessage(SEPARATOR);
                    if (winner != null) {
                        pls.showPlayer(DAC.getInstance(), winner);
                    }
                }
                pls.setGameMode(GameMode.ADVENTURE);
                pls.teleport(Locations.getPoolLocation());
                pls.getInventory().clear();
                giveItems(pls);
                pls.setAllowFlight(true);
                pls.setFlying(true);
            }
            FinishFireworks finishFireworks = new FinishFireworks();
            finishFireworks.runTaskTimer(DAC.getInstance(), 0L, 20L);
        }
        this.timer--;
    }

    public static void giveItems(Player player){
        ItemStack bed = new ItemBuilder(Material.RED_BED).setName(ChatColor.RED + "Retour au Hub").removeFlags().toItemStack();
        player.getInventory().setItem(8, bed);
        player.updateInventory();

        ItemStack star = new ItemBuilder(Material.NETHER_STAR).setName(ChatColor.GOLD + "Rejouer").removeFlags().toItemStack();
        player.getInventory().setItem(4, star);
        player.updateInventory();
    }
}
