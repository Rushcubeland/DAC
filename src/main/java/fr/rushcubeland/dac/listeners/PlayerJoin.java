package fr.rushcubeland.dac.listeners;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.game.GameState;
import fr.rushcubeland.dac.game.Locations;
import fr.rushcubeland.dac.tasks.AutoStart;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.tools.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        initPlayerOnMap(player);

        Account account = RcbAPI.getInstance().getAccount(player);
        RankUnit rank = account.getRank();

        if (!DAC.getInstance().isState(GameState.WAITING) && !DAC.getInstance().isState(GameState.STARTING)) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(Locations.POOL.getLocation());
            player.sendMessage(ChatColor.RED + "La partie à déja commencée !");
            event.setJoinMessage(null);
            DAC.getInstance().setScoreboard(player, GameState.INPROGRESS);
            giveJoinItems(player);
            player.sendTitle(ChatColor.RED + "La Partie à", ChatColor.WHITE + "déja commencé",10, 70, 20);
            for(Player pls : DAC.getInstance().getPlayersGameList()){
                pls.hidePlayer(DAC.getInstance(), player);
            }
            return;
        }

        if (!DAC.getInstance().getPlayersGameList().contains(player) && DAC.getInstance().getPlayersGameList().size() < DAC.getInstance().getMaxPlayer()) {

            player.teleport(Locations.LOBBY.getLocation());
            giveJoinItems(player);
            RcbAPI.getInstance().getTablist().setTabListPlayer(player);
            DAC.getInstance().getPlayersGameList().add(player);
            initFlyPlayer(player, rank);
            event.setJoinMessage(ChatColor.YELLOW + DAC.DAC_PREFIX + " " + rank.getPrefix() + player.getDisplayName() + ChatColor.GOLD + " a rejoin la partie ! " + ChatColor.GRAY + "<" + DAC.getInstance().getPlayersGameList().size() + ChatColor.YELLOW + "/" + ChatColor.GOLD + DAC.getInstance().getMaxPlayer() + ChatColor.GRAY + ">");
            for(Player pls : DAC.getInstance().getPlayersGameList()){
                for (Player pls2 : DAC.getInstance().getPlayersGameList()){
                    pls.showPlayer(DAC.getInstance(), pls2);
                }
            }
            if(DAC.getInstance().isState(GameState.WAITING)){
                DAC.getInstance().setScoreboard(player, GameState.WAITING);
                if(DAC.getInstance().getPlayersGameList().size() > 1){
                    DAC.getInstance().setGameState((GameState.STARTING));
                    AutoStart start = new AutoStart();
                    start.runTaskTimer(DAC.getInstance(), 0L, 20L);
                }
            }
            else
            {
                DAC.getInstance().setScoreboard(player, GameState.STARTING);
            }
        }

        RcbAPI.getInstance().getTablist().sendTabList(player);
        DAC.getInstance().getPlayersServerList().add(player);

    }

    private void giveJoinItems(Player player){
        ItemStack bed = new ItemBuilder(Material.RED_BED).setName(ChatColor.RED + "Retour au Hub").removeFlags().toItemStack();
        player.getInventory().setItem(8, bed);
    }

    private void initFlyPlayer(Player player, RankUnit rank){
        if(rank.getPower() <= 40){
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    private void initPlayerOnMap(Player player){
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setLevel(0);
        player.setExp(0.0F);
        player.setHealth(20.0D);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20D);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getEnderChest().clear();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

}
