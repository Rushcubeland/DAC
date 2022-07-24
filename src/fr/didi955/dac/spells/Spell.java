package fr.didi955.dac.spells;

import fr.didi955.dac.DAC;
import fr.rushcubeland.commons.AStatsDAC;
import fr.rushcubeland.commons.data.callbacks.AsyncCallBack;
import fr.rushcubeland.rcbcore.bukkit.RcbAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public abstract class Spell {

    private final Player player;
    private BukkitTask bukkitTask;
    private boolean activate = false;

    public Spell(Player player) {
        this.player = player;
    }

    public void activate(){
        this.activate = true;
    }

    public void use(){
        if(this instanceof LevitationSpell || this instanceof EmprisonnementSpell || this instanceof DistorsionSpell){
            activate();
        }
        DAC.getInstance().getPlayersSpell().put(player, this);
        run();
        RcbAPI.getInstance().getAccountStatsDAC(player, new AsyncCallBack() {
            @Override
            public void onQueryComplete(Object result) {
                AStatsDAC aStatsDAC = (AStatsDAC) result;
                aStatsDAC.setNbSortsUsed(aStatsDAC.getNbSortsUsed()+1);
                RcbAPI.getInstance().sendAStatsDACToRedis(aStatsDAC);
            }
        });
    }

    public boolean isActivated(){
        return this.activate;
    }

    public void stop(){
        DAC.getInstance().getPlayersSpell().remove(player);
        cancel();
    }

    public void stop(int tid){
        Bukkit.getScheduler().cancelTask(tid);
    }

    public abstract void run();

    public abstract String getName();

    public abstract int getPrice();

    private void cancel(){
        if(getBukkitTask() != null){
            getBukkitTask().cancel();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    public void setBukkitTask(BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }
}
