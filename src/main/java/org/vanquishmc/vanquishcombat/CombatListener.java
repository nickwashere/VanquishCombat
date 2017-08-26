package org.vanquishmc.vanquishcombat;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CombatListener implements Listener {

    private VanquishCombat plugin;
    private NPCHandler npcHandler;
    private TagHandler tagHandler;

    public CombatListener(VanquishCombat plugin) {
        this.plugin = plugin;
        this.npcHandler = plugin.getNpcHandler();
        this.tagHandler = plugin.getTagHandler();
    }

    private String parse(String message, String playerName) {
        return plugin.getConfig().getString(message).replace("%player%", playerName).replace("%time%", String.valueOf(plugin.getConfig().getInt("TAG_EXPIRE_DELAY")));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (plugin.getConfig().getInt("TAG_EXPIRE_DELAY") == -1 || plugin.getConfig().getStringList("DISABLED_WORLDS").contains(event.getEntity().getWorld().getName())) {
                return;
            }
            tagHandler.tagPlayer((Player) event.getEntity(), tagHandler.isTagged((Player) event.getEntity()) ? null : parse("DAMAGED_TAG_MESSAGE", ((Player) event.getDamager()).getName()));
            tagHandler.tagPlayer((Player) event.getDamager(), tagHandler.isTagged((Player) event.getDamager()) ? null : parse("DAMAGER_TAG_MESSAGE", ((Player) event.getEntity()).getName()));
        }
        else if (event.getEntity() instanceof Zombie && event.getDamager() instanceof Player) {
            NPC npc = npcHandler.fromZombie((Zombie) event.getEntity());
            if (npc != null) {
                npcHandler.resetTask(npc);
            }
        }
    }
}