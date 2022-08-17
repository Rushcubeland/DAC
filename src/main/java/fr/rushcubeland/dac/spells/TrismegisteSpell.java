package fr.rushcubeland.dac.spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TrismegisteSpell extends Spell {


    public TrismegisteSpell(Player player) {
        super(player);
    }

    @Override
    public void use() {
        super.use();
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 2));
        broadcast();
    }

    @Override
    public void run() {
    }

    @Override
    public String getName() {
        return "trismégiste";
    }

    @Override
    public int getPrice() {
        return SpellUnit.TRISMEGISTE.getPrice();
    }

}
