package fr.didi955.dac.spells;

public class DistorsionSpell extends Spell {

    private final int timer = 5;


    @Override
    public void use(){
        super.use();
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, timer*20, 250));
        Bukkit.broadcastMessage(ChatColor.WHITE + getPlayer().getDisplayName() + " " + ChatColor.GOLD + "a utilisé son sort de " + ChatColor.RED + getName()
                + ChatColor.GOLD + " pour " + ChatColor.YELLOW + getPrice() + ChatColor.GOLD + " points");
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1F, 1F);
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
