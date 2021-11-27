package fr.didi955.dac.tasks;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbapi.bukkit.RcbAPI;
import fr.rushcubeland.rcbapi.bukkit.tools.ScoreboardSign;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class AutoStart extends BukkitRunnable {

    private int timer = 15;

    public void run() {
        for (Map.Entry<Player, ScoreboardSign> sign : RcbAPI.getInstance().boards.entrySet()) {
            sign.getValue().setLine(6, "§6Lancement dans: §e" + this.timer);
            sign.getValue().setLine(8, "§7<" + DAC.getInstance().getPlayersGameList().size() + "§e/§6" + DAC.getInstance().getMaxPlayer() + "§7>");
        }

        for (Player pls : DAC.getInstance().getPlayersGameList()) {
            pls.setLevel(this.timer);
        }
        if (this.timer == 10 || this.timer == 15) {
            Bukkit.broadcastMessage("§6La partie commence dans " + this.timer + "§cs");
            for (Player pls : Bukkit.getOnlinePlayers()) {
                pls.playSound(pls.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            }
        }
        if (this.timer == 5 || this.timer == 4 || this.timer == 3 || this.timer == 2 || this.timer == 1) {
            for (Player pls : Bukkit.getOnlinePlayers()) {
                pls.sendTitle("§cLancement de la partie", "§6dans §6" + this.timer + "§cs", 20, 70, 20);
                pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }
        }

        if (this.timer > 0 && DAC.getInstance().getPlayersGameList().size() < 2) {
            cancel();
            DAC.getInstance().setGameState(GameState.WAITING);
            int configminpls = 2;
            int plsc = DAC.getInstance().getPlayersGameList().size();
            int minpls = configminpls - plsc;
            Bukkit.broadcastMessage("§cIl manque §6" + minpls + " §cjoueurs pour commencer la partie");
        }

        if (this.timer == 0) {
            cancel();
            Bukkit.broadcastMessage("§6La partie commence !");
            MinecraftServer s = (((CraftServer)Bukkit.getServer()).getHandle().getServer());
            s.setMotd("INPROGRESS");
            DAC.getInstance().setGameState(GameState.INPROGRESS);
            for (Player pls : DAC.getInstance().getPlayersServerList()) {
                pls.teleport(Locations.POOL.getLocation());
                pls.setGameMode(GameMode.ADVENTURE);
                pls.getInventory().clear();
                DAC.getInstance().setScorboardIP(pls);
            }
            for(Player pls : DAC.getInstance().getPlayersGameList()){
                pls.setFlying(false);
                pls.setAllowFlight(false);
                Account account = RcbAPI.getInstance().getAccount(pls);
                account.setCoins(account.getCoins()+10);
                RcbAPI.getInstance().sendAccountToRedis(account);
                AStatsDAC aStatsDAC = RcbAPI.getInstance().getAccountStatsDAC(pls);
                aStatsDAC.setNbParties(aStatsDAC.getNbParties()+1);
                RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
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

        this.timer--;
    }
}
