package fr.didi955.dac.spells;

import fr.didi955.dac.DAC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EmprisonnementSpell extends Spell {

    public EmprisonnementSpell(Player player) {
        super(player);
    }

    @Override
    public void use() {
        super.use();
        DAC.getInstance().getPlayerTurn().setNextPositionRequired(2);
        Bukkit.broadcastMessage(ChatColor.WHITE + getPlayer().getDisplayName() + " " + ChatColor.GOLD + "a utilisé son sort " + ChatColor.RED + getName()
                + ChatColor.GOLD + " pour " + ChatColor.YELLOW + getPrice() + ChatColor.GOLD + " points");
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1F, 1F);
        stop();
    }

    @Override
    public void run() {
    }

    @Override
    public String getName() {
        return "d'Emprisonnement";
    }

    @Override
    public int getPrice() {
        return SpellUnit.EMPRISONNEMENT.getPrice();
    }
}
