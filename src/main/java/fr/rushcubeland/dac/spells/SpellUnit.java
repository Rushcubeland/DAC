package fr.rushcubeland.dac.spells;

import fr.rushcubeland.rcbcore.bukkit.tools.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * This class file is a part of DAC project claimed by Rushcubeland project.
 * You cannot redistribute, modify or use it for personnal or commercial purposes
 * please contact admin@rushcubeland.fr for any requests or information about that.
 *
 * @author LANNUZEL Dylan
 */

public enum SpellUnit {

    LEVITATION("Sort de lévitation", 700, LevitationSpell.class, Material.SUGAR),
    EMPRISONNEMENT("Sort d'emprisonnement", 800, EmprisonnementSpell.class , Material.BARRIER),
    DISTORSION("Sort de distorsion", 1000, DistorsionSpell.class, Material.ENDER_PEARL),
    DESTRUCTION("Sort de destruction", 500, DestructionSpell.class, Material.TNT),
    TRISMEGISTE("Sort trismégiste", 1300, TrismegisteSpell.class, Material.FIREWORK_ROCKET);

    private final String name;
    private final int price;
    private final Class<? extends Spell> clazz;
    private final Material material;

    SpellUnit(String name, int price, Class<? extends Spell> clazz, Material material) {
        this.name = name;
        this.price = price;
        this.clazz = clazz;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Class<? extends Spell> getClazz() {
        return clazz;
    }

    public Material getMaterial() {
        return material;
    }

    public static void giveItems(Player player){
        for(SpellUnit spell : SpellUnit.values()){
            player.getInventory().addItem(new ItemBuilder(spell.getMaterial()).removeFlags().setName(ChatColor.WHITE + spell.getName() +  ": " + ChatColor.RED + spell.getPrice() + ChatColor.GOLD + " points").toItemStack());
        }
    }
}
