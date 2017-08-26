package org.vanquishmc.vanquishcombat;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class VanquishCombat extends JavaPlugin {

    private NPCHandler npcHandler;
    private TagHandler tagHandler;
    private WorldGuardPlugin worldGuardPlugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        getTagHandler().loadLoggers();
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
    }

    @Override
    public void onDisable() {
        getTagHandler().saveLoggers();
    }

    public NPCHandler getNpcHandler() {
        return npcHandler;
    }

    public TagHandler getTagHandler() {
        return tagHandler;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        if (worldGuardPlugin == null) {
            worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        }
        return this.worldGuardPlugin;
    }
}
