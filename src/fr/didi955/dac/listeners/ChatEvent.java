package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbapi.bukkit.RcbAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        Account account = RcbAPI.getInstance().getAccount(player);
        RankUnit rank = account.getRank();

        if (DAC.getInstance().isState(GameState.WAITING) || DAC.getInstance().isState(GameState.STARTING) || DAC.getInstance().isState(GameState.FINISH)) {
                event.setFormat(rank.getPrefix() + player.getDisplayName() + ": " + event.getMessage());
        }
        else if(rank.getPower() <= 45){
            event.setFormat(ChatColor.WHITE + player.getDisplayName() + ": " + event.getMessage());

        }
        else
        {
            event.setFormat(ChatColor.GRAY + player.getDisplayName() + ": " + event.getMessage());

        }
    }
}
