package fr.didi955.dac.listeners;

import fr.didi955.dac.DAC;
import fr.didi955.dac.game.GameState;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.commons.rank.RankUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.commands.ReportMsgCommand;
import fr.rushcubeland.rcbcore.bukkit.sanction.MuteData;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String message = event.getMessage();
        if(MuteData.isInMuteData(player.getUniqueId().toString())){
            if(!message.startsWith("/")){
                event.setCancelled(true);
                player.sendMessage("§cVous avez été mute !");
                return;
            }
        }
        if(ReportMsgCommand.msgs.containsKey(player.getName())){
            ReportMsgCommand.msgs.get(player.getName()).add(message);
        }
        else
        {
            ArrayList<String> msgs = new ArrayList<>();
            msgs.add(event.getMessage());
            ReportMsgCommand.msgs.put(player.getName(), msgs);
        }

        RcbAPI.getInstance().getAccount(player, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {
                Account account = (Account) result;
                RankUnit rank = account.getRank();
                TextComponent sign = new TextComponent("⚠");
                TextComponent format;
                if (DAC.getInstance().isState(GameState.WAITING) || DAC.getInstance().isState(GameState.STARTING) || DAC.getInstance().isState(GameState.FINISH)) {
                    format = new TextComponent(rank.getPrefix() + player.getDisplayName() + ": " + message);
                }
                else if(rank.getPower() <= 45){
                    format = new TextComponent(ChatColor.WHITE + player.getDisplayName() + ": " + message);
                }
                else
                {
                    format = new TextComponent(ChatColor.GRAY + player.getDisplayName() + ": " + message);
                }
                sign.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Signaler le message").color(net.md_5.bungee.api.ChatColor.DARK_RED).create()));
                sign.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportmsg " + player.getName() + " " + message));
                format.setClickEvent(null);
                format.setHoverEvent(null);
                for(Player pls : Bukkit.getOnlinePlayers()){
                    RcbAPI.getInstance().getAccountOptions(pls, new AsyncCallBack() {
                        @Override
                        public void onQueryComplete(Object result) {
                            AOptions aOptions = (AOptions) result;
                            if(aOptions.getStateChat().equals(OptionUnit.OPEN)){
                                pls.spigot().sendMessage(new ComponentBuilder(sign).color(net.md_5.bungee.api.ChatColor.DARK_RED).append(format).reset().create());
                            }
                        }
                    });
                }
                event.setCancelled(true);
            }
        });
        event.setCancelled(true);
    }
}
