package org.vanquishmc.vanquishcombat;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class NPC {

    private UUID logger;
    private Zombie entity;
    private List<ItemStack> inventoryContents;

    public NPC(Player player) {
        this.logger = player.getUniqueId();
    }

    public Zombie getEntity() {
        return entity;
    }

    private Zombie zombieNPC(Player player) {
        Zombie zombie = player.getWorld().spawn(player.getLocation(), Zombie.class);
        zombie.setBaby(false);
        zombie.setCustomName(player.getName());
        zombie.setCustomNameVisible(true);
        zombie.setRemoveWhenFarAway(true);
        zombie.getEquipment().setArmorContents(player.getInventory().getArmorContents());
        zombie.getEquipment().setHelmet(getPlayerSkull(player));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
        if (zombie.isInsideVehicle()) {
            zombie.getVehicle().eject();
        }
        return zombie;
    }

    private ItemStack getPlayerSkull(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(player.getName());
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
