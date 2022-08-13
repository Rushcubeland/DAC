package fr.rushcubeland.dac.game;

import fr.rushcubeland.dac.DAC;
import fr.rushcubeland.dac.spells.SpellUnit;
import fr.rushcubeland.dac.tasks.Afk;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.security.SecureRandom;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class PlayerTurn {

    private Player player;

    private boolean first;
    private int position;
    private Afk afk;
    private int nextPositionRequired = 1;
    private SpellUnit nextspell;

    public Player getPlayer() {
        return player;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.player= playerTurn;
    }

    public void setNextPositionRequired(int nextPositionRequired) {
        this.nextPositionRequired = nextPositionRequired;
    }

    public int getNextPositionRequired() {
        return nextPositionRequired;
    }

    public void chooseFirstPlayer(){
        first = true;
        position = new SecureRandom().nextInt(DAC.getInstance().getPlayersGameList().size());
        this.player= DAC.getInstance().getPlayersGameList().get(position);
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.teleport(Locations.DIVING_PLATFORM.getLocation());
        SpellUnit.giveItems(this.player);
    }

    public void makeAnnouncement(){
        this.player.sendMessage("§eC'est votre tour !");
        this.player.sendTitle("§eC'est votre tour !", "§6Bonne chance", 10, 70, 20);
        for(Player pls : DAC.getInstance().getPlayersGameList()){
            pls.sendMessage("§6C'est au tour de §c" + this.player.getDisplayName());
            pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }

    }

    public void initNextPlayer(int i){
        first = false;
        this.player.getInventory().clear();
        for (PotionEffect effect : this.player.getActivePotionEffects()) {
            this.player.removePotionEffect(effect.getType());
        }
        if(afk != null){
            Bukkit.getScheduler().cancelTask(this.afk.getTaskId());
        }
        if(i == 1){
            if(position == DAC.getInstance().getPlayersGameList().size()-1){
                position = 0;
            }
            else
            {
                position +=1;
            }
        }
        else
        {
            if(position == DAC.getInstance().getPlayersGameList().size()-1){
                position = 1;
            }
            else
            {
                position = 0;
            }
        }
        setPlayerTurn(DAC.getInstance().getPlayersGameList().get(position));
        makeAnnouncement();
        teleportPlayer();
        setNextPositionRequired(1);
        SpellUnit.giveItems(this.player);
        if(this.nextspell == SpellUnit.DISTORSION){
            getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 99999, 2));
            setNextspell(null);
        }
        Afk afk = new Afk(this.player);
        afk.runTaskTimer(DAC.getInstance(), 0L, 20L);
        this.afk = afk;
    }

    public void teleportPlayer(){
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.teleport(Locations.DIVING_PLATFORM.getLocation());
    }

    public String getPlayerName(){
        if(this.player == null){
            return "Choix en cours";
        }
        return this.player.getDisplayName();
    }

    public boolean isFirst() {
        return first;
    }

    public SpellUnit getNextspell() {
        return nextspell;
    }

    public void setNextspell(SpellUnit nextspell) {
        this.nextspell = nextspell;
    }
}
