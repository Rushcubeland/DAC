package fr.didi955.dac.game;

import fr.didi955.dac.DAC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class PlayerTurn {

    private Player playerTurn;
    private int position;

    public PlayerTurn() {
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Player playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void chooseFirstPlayer(){
        Random rand = new Random();
        position = rand.nextInt(DAC.getInstance().getPlayersGameList().size());
        playerTurn = DAC.getInstance().getPlayersGameList().get(position);
        playerTurn.teleport(Locations.DIVING_PLATFORM.getLocation());
    }

    public void makeAnnouncement(){
        playerTurn.sendMessage("§6C'est votre tour !");
        playerTurn.sendTitle("§6C'est votre tour !", "§6Bonne chance", 10, 70, 20);
        for(Player pls : DAC.getInstance().getPlayersGameList()){
            pls.sendMessage("§6C'est au tour de §c" + this.playerTurn.getDisplayName());
            pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }

    }

    public void chooseNextPlayer(){
        if(position == DAC.getInstance().getPlayersGameList().size()-1){
            position = 0;
        }
        else
        {
            position +=1;
        }
        setPlayerTurn(DAC.getInstance().getPlayersGameList().get(position));
        makeAnnouncement();
        teleportPlayer();
    }

    public void teleportPlayer(){
        playerTurn.teleport(Locations.DIVING_PLATFORM.getLocation());
    }

    public String getPlayerName(){
        if(this.playerTurn == null){
            return "Choix en cours";
        }
        return this.playerTurn.getDisplayName();
    }
}
