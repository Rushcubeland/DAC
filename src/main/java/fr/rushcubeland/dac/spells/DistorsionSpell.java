package fr.rushcubeland.dac.spells;

import fr.rushcubeland.dac.DAC;
import org.bukkit.entity.Player;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public class DistorsionSpell extends Spell {

    public DistorsionSpell(Player player) {
        super(player);
    }

    @Override
    public void use(){
        super.use();
        DAC.getInstance().getPlayerTurn().setNextspell(SpellUnit.DISTORSION);
        broadcast();
        stop();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported for this spell");
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
