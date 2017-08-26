package org.vanquishmc.vanquishcombat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class NPCHandler {

    private VanquishCombat plugin;
    private Map<NPC, BukkitTask> npcTasks;

    public NPCHandler(VanquishCombat plugin) {
        this.plugin = plugin;
        this.npcTasks = new HashMap<>();
    }

    private BukkitTask newTask(NPC npc) {
        return Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                NPCHandler.this.removeNPC(npc);
            }
        }, (plugin.getConfig().getInt("NPC_DESPAWN_DELAY") * 20));
    }

    public void resetTask(NPC npc) {
        if (npcTasks.containsKey(npc)) {
            npcTasks.get(npc).cancel();
        }
        npcTasks.put(npc, newTask(npc));
    }

    public NPC fromZombie(Zombie zombie) {
        for (NPC npc : npcTasks.keySet()) {
            if (npc.isNPC(zombie)) {
                return npc;
            }
        }
        return null;
    }

    public NPC fromPlayer(Player player) {
        for (NPC npc : npcTasks.keySet()) {
            if (npc.getLogger().equals(player.getUniqueId())) {
                return npc;
            }
        }
        return null;
    }

    public void handleDeath(NPC npc) {
        for (ItemStack contents : npc.getInventoryContents()) {
            if (contents != null && contents.getType() != Material.AIR) {
                npc.getEntity().getWorld().dropItem(npc.getEntity().getLocation(), contents);
            }
        }
        plugin.getTagHandler().addLogger(npc.getLogger());
        removeNPC(npc);
    }

    private void spawnNPC(Player player) {
        NPC npc = new NPC(player);
        npcTasks.put(npc, newTask(npc));
    }

    private void removeNPC(NPC npc) {
        if (npc.getEntity() != null && !npc.getEntity().isDead()) {
            npc.getEntity().remove();
        }
        if (npcTasks.containsKey(npc)) {
            npcTasks.remove(npc);
        }
    }
}
