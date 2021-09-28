package fr.didi955.dac.spells;

import fr.didi955.dac.DAC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Spell {

    private final Player player;
    private BukkitTask bukkitTask;
    public boolean activate = false;

    public Spell(Player player) {
        this.player = player;
    }

    public void activate(){
        this.activate = true;
    }

    public void use(){
        if(this instanceof LevitationSpell){
            activate();
        }
        DAC.getInstance().getPlayersSpell().put(player, this);
        run();
    }

    public void stop(){
        cancel();
    }

    public void stop(int tid){
        Bukkit.getScheduler().cancelTask(tid);
    }

    public abstract void run();

    public abstract String getName();

    public abstract int getPrice();

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
