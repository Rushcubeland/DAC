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

public class EmprisonnementSpell extends Spell {

    public EmprisonnementSpell(Player player) {
        super(player);
    }

    @Override
    public void use() {
        super.use();
        DAC.getInstance().getPlayerTurn().setNextPositionRequired(2);
        stop();
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
