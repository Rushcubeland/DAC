package fr.rushcubeland.dac.tasks;

import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.dac.game.Locations;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.tools.ScoreboardSign;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class AutoStart extends BukkitRunnable {

    private int timer = 15;

    public void run() {
        for (Map.Entry<Player, ScoreboardSign> sign : RcbAPI.getInstance().boards.entrySet()) {
            sign.getValue().setLine(6, ChatColor.GOLD + "Lancement dans: " + ChatColor.YELLOW + this.timer);
            sign.getValue().setLine(8, ChatColor.GRAY + "<" + DAC.getInstance().getPlayersGameList().size() + ChatColor.YELLOW + "/" + ChatColor.GOLD + DAC.getInstance().getMaxPlayer() + ChatColor.GRAY + ">");
        }

        for (Player pls : DAC.getInstance().getPlayersGameList()) {
            pls.setLevel(this.timer);
        }
        if (this.timer == 10 || this.timer == 15) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "La partie commence dans " + ChatColor.RED + this.timer + "s");
            for (Player pls : Bukkit.getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            }
        }
        if (this.timer == 5 || this.timer == 4 || this.timer == 3 || this.timer == 2 || this.timer == 1) {
            for (Player pls : Bukkit.getOnlinePlayers()) {
                pls.sendTitle(ChatColor.RED + "Lancement de la partie", ChatColor.WHITE + "dans " + ChatColor.RED + this.timer + "s", 20, 70, 20);
                pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }
        }

        if (this.timer > 0 && DAC.getInstance().getPlayersGameList().size() < 2) {
            cancel();
            DAC.getInstance().setGameState(GameState.WAITING);
            int configminpls = 2;
            int plsc = DAC.getInstance().getPlayersGameList().size();
            int minpls = configminpls - plsc;
            Bukkit.broadcastMessage(ChatColor.RED + "Il manque " + ChatColor.GOLD + minpls + ChatColor.RED + " joueurs pour commencer la partie");
        }

        if (this.timer == 0) {
            cancel();
            timerFinished();
        }
        this.timer--;
    }

    private void timerFinished(){
        cancel();
        Bukkit.broadcastMessage(ChatColor.GOLD + "La partie commence !");
        MinecraftServer s = ((CraftServer)Bukkit.getServer()).getHandle().getServer();
        s.setMotd("INPROGRESS");
        DAC.getInstance().setGameState(GameState.INPROGRESS);
        for (Player pls : DAC.getInstance().getPlayersServerList()) {
            pls.teleport(Locations.POOL.getLocation());
            pls.setGameMode(GameMode.ADVENTURE);
            pls.getInventory().clear();
            DAC.getInstance().setScoreboard(pls, GameState.INPROGRESS);
        }
        for(Player pls : DAC.getInstance().getPlayersGameList()){
            pls.setFlying(false);
            pls.setAllowFlight(false);
            pls.setGameMode(GameMode.SPECTATOR);
            RcbAPI.getInstance().getAccount(pls, result -> {
                Account account = (Account) result;
                account.setCoins(account.getCoins()+10);
                RcbAPI.getInstance().sendAccountToRedis(account);
            });
            RcbAPI.getInstance().getAccountStatsDAC(pls, result -> {
                AStatsDAC aStatsDAC = (AStatsDAC) result;
                aStatsDAC.setNbParties(aStatsDAC.getNbParties()+1);
                RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
            });
            DAC.getInstance().getPlayersPoints().put(pls, 0);
            RcbAPI.getInstance().getTablist().resetTabListPlayer(pls);
            if(DAC.getInstance().getPlayersBlock().containsKey(pls)){
                continue;
            }
            for (Material m : Material.values()){
                if(m.toString().endsWith("WOOL")){
                    if(DAC.getInstance().getPlayersBlock().containsValue(m)){
                        continue;
                    }
                    DAC.getInstance().getPlayersBlock().put(pls, m);
                }
            }
        }
        DAC.getInstance().getPlayerTurn().chooseFirstPlayer();
        Game game = new Game();
        game.runTaskTimer(DAC.getInstance(), 0L, 20L);
    }
}
