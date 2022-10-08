package fr.rushcubeland.dac.spells;

import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public abstract class Spell {

    private final Player player;
    private boolean activate = false;

    protected Spell(Player player) {
        this.player = player;
    }

    public void activate(){
        this.activate = true;
    }

    public void use(){
        // Tweak of the destructionSpell
        if(!(this instanceof DestructionSpell)){
            activate();
        }
        DAC.getInstance().getPlayersSpell().put(player, this);
        RcbAPI.getInstance().getAccountStatsDAC(player, result -> {
            AStatsDAC aStatsDAC = (AStatsDAC) result;
            aStatsDAC.setNbSortsUsed(aStatsDAC.getNbSortsUsed()+1);
            RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
        });
    }

    public boolean isActivated(){
        return this.activate;
    }

    public void stop(){
        this.activate = false;
        DAC.getInstance().getPlayersSpell().remove(player);
    }

    public abstract String getName();

    public abstract int getPrice();

    public void broadcast(){
        Bukkit.broadcastMessage(ChatColor.WHITE + getPlayer().getDisplayName() + " " + ChatColor.GOLD + "a utilisé son sort de " + ChatColor.RED + getName()
                + ChatColor.GOLD + " pour " + ChatColor.YELLOW + getPrice() + ChatColor.GOLD + " points");
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1F, 1F);
    }

    public Player getPlayer() {
        return player;
    }
}
