package fr.didi955.dac.spells;

import fr.didi955.dac.DAC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Spell {

    private final Player player;
    private BukkitTask bukkitTask;

    public Spell(Player player) {
        this.player = player;
    }

    public void use(){
        if(DAC.getInstance().getPlayersSpell().containsKey(player)){
            DAC.getInstance().getPlayersSpell().replace(player, this);
        }
        else
        {
            DAC.getInstance().getPlayersSpell().put(player, this);
        }
        run();
    }

    public void stop(){
        cancel();
        DAC.getInstance().getPlayersSpell().remove(player);
    }

    public void stop(int tid){
        Bukkit.getScheduler().cancelTask(tid);
        DAC.getInstance().getPlayersSpell().remove(player);
    }

    public abstract void run();

    private void cancel(){
        getBukkitTask().cancel();
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
