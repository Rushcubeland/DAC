package fr.didi955.dac.spells;

import fr.didi955.dac.DAC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DistorsionSpell extends Spell {

    public DistorsionSpell(Player player) {
        super(player);
    }

    @Override
    public void use(){
        super.use();
        DAC.getInstance().getPlayerTurn().setNextspell(SpellUnit.DISTORSION);
        Bukkit.broadcastMessage(ChatColor.WHITE + getPlayer().getDisplayName() + " " + ChatColor.GOLD + "a utilisé son sort de " + ChatColor.RED + getName()
                + ChatColor.GOLD + " pour " + ChatColor.YELLOW + getPrice() + ChatColor.GOLD + " points");
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1F, 1F);
        stop();
    }

    @Override
    public void run() {
    }

    @Override
    public String getName() {
        return "de Distorsion";
    }

    @Override
    public int getPrice() {
        return SpellUnit.DISTORSION.getPrice();
    }
}
