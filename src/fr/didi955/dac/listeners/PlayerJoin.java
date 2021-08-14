package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.didi955.dac.game.Locations;
import fr.didi955.dac.tasks.AutoStart;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbapi.bukkit.RcbAPI;
import fr.rushcubeland.rcbapi.bukkit.tools.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

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

        Account account = RcbAPI.getInstance().getAccount(player);
        RankUnit rank = account.getRank();

        if (!DAC.getInstance().isState(GameState.WAITING) && !DAC.getInstance().isState(GameState.STARTING)) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(Locations.POOL.getLocation());
            player.sendMessage("§cLa partie à déja commencée !");
            event.setJoinMessage(null);
            DAC.getInstance().setScorboardIP(player);
            giveJoinItems(player);
            player.sendTitle("§cLa Partie à", "§fdéja commencé",10, 70, 20);
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
            event.setJoinMessage("§e[§bDé §6à §bCoudre§e] " + rank.getPrefix() + player.getDisplayName() + " §6a rejoin la partie ! " + "§7<" + DAC.getInstance().getPlayersGameList().size() + "§e/§6" + DAC.getInstance().getMaxPlayer() + "§7>");
            for(Player pls : DAC.getInstance().getPlayersGameList()){
                for (Player pls2 : DAC.getInstance().getPlayersGameList()){
                    pls.showPlayer(DAC.getInstance(), pls2);
                }
            }
            if(DAC.getInstance().isState(GameState.WAITING)){
                DAC.getInstance().setScorboardW(player);
                if(DAC.getInstance().getPlayersGameList().size() > 1){
                    DAC.getInstance().setGameState((GameState.STARTING));
                    AutoStart start = new AutoStart();
                    start.runTaskTimer(DAC.getInstance(), 0L, 20L);
                }
            }
            else
            {
                DAC.getInstance().setScorboardS(player);
            }
        }

        RcbAPI.getInstance().getTablist().sendTabList(player);
        DAC.getInstance().getPlayersServerList().add(player);

    }

    private void giveJoinItems(Player player){
        ItemStack bed = new ItemBuilder(Material.RED_BED).setName("§cRetour au Hub").removeFlags().toItemStack();
        player.getInventory().setItem(8, bed);
    }

    private void initRankPlayerPermissions(Player player, RankUnit rank){
        if(rank.getPermissions().isEmpty()){
            return;
        }
        for(String perm : rank.getPermissions()){
            player.addAttachment(RcbAPI.getInstance()).setPermission(perm, true);
        }
    }

    private void initFlyPlayer(Player player, RankUnit rank){
        if(rank.getPower() <= 40){
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }


}
