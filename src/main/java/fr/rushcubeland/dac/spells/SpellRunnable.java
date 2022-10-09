package fr.rushcubeland.dac.spells;

import org.bukkit.scheduler.BukkitTask;

public interface SpellRunnable {
    

    void run();
    void stop(int tid);

    void stop(BukkitTask task);
}
