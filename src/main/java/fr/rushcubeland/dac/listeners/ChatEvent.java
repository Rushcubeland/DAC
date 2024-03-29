package fr.rushcubeland.dac.listeners;

import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.commons.permissions.PermissionsUnit;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import fr.rushcubeland.rcbcore.bukkit.commands.ReportMsgCommand;
import fr.rushcubeland.rcbcore.bukkit.sanction.MuteData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
        if(MuteData.isInMuteData(player.getUniqueId().toString()) && !message.startsWith("/")){
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Vous avez été mute !");
            return;
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

        RcbAPI.getInstance().getAccount(player, result -> {
            Account account = (Account) result;
            TextComponent format = new TextComponent(account.getRank().getPrefix() + player.getDisplayName() + ": " + message);
            format.setClickEvent(null);
            format.setHoverEvent(null);
            for(Player pls : Bukkit.getOnlinePlayers()) {
                RcbAPI.getInstance().getAccountOptions(pls, result1 -> {
                    AOptions aOptions = (AOptions) result1;
                    TextComponent sign;
                    if (aOptions.getStateChat().equals(OptionUnit.OPEN)) {
                        if(pls.hasPermission(PermissionsUnit.ALL.getPermission()) || pls.hasPermission(PermissionsUnit.SANCTION_GUI.getPermission()) || pls.hasPermission(PermissionsUnit.SANCTION_GUI_MSG.getPermission())){
                            sign = new TextComponent("⚒");
                            sign.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Sanctionner").color(ChatColor.DARK_RED).create()));
                            sign.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/apmsgb " + player.getName()));
                        }
                        else
                        {
                            sign = new TextComponent("⚠");
                            sign.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Signaler le message").color(ChatColor.DARK_RED).create()));
                            sign.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportmsg " + player.getName() + " " + message));
                        }
                        pls.spigot().sendMessage(new ComponentBuilder(sign).color(ChatColor.DARK_RED).append(format).reset().create());
                    }
                });
            }
        });
        event.setCancelled(true);
    }
}
