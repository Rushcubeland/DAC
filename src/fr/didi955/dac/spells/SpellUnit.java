package fr.didi955.dac.spells;

import fr.rushcubeland.rcbapi.bukkit.tools.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum SpellUnit {

    LEVITATION("Sort de lévitation", 500, LevitationSpell.class, Material.SUGAR),
    DESTRUCTION("Sort de destruction", 500, DestructionSpell.class, Material.TNT);

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
            player.getInventory().addItem(new ItemBuilder(spell.getMaterial()).removeFlags().setName(spell.getName()).toItemStack());
        }
    }
}
