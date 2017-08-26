package org.vanquishmc.vanquishcombat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TagHandler {

    private VanquishCombat plugin;
    private YamlConfiguration tagConfig;
    private List<UUID> loggers;
    private Map<UUID, Long> taggedPlayers;

    public TagHandler(VanquishCombat plugin) {
        this.loggers = new ArrayList<>();
        this.taggedPlayers = new HashMap<>();
        this.plugin = plugin;
        this.tagConfig = getTagConfig();
    }


    public void loadLoggers() {
        for (String uuid : tagConfig.getStringList("loggers")) {
            loggers.add(UUID.fromString(uuid));
        }
    }

    public void saveLoggers() {
        List<String> loggers = new ArrayList<>();
        for (UUID uuid : this.loggers) {
            loggers.add(uuid.toString());
        }
        tagConfig.set("loggers", loggers);
        try {
            tagConfig.save(new File(plugin.getDataFolder(), "tags.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLogger(Player player) {
        return loggers.contains(player.getUniqueId());
    }

    public void addLogger(UUID uuid) {
        loggers.add(uuid);
    }

    public void handleLogger(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(0.0);
        loggers.remove(player.getUniqueId());
    }

    public boolean isTagged(Player player) {
        return taggedPlayers.containsKey(player.getUniqueId()) && !isTagExpired(player.getUniqueId());
    }

    private boolean isTagExpired(UUID player) {
        return System.currentTimeMillis() - taggedPlayers.get(player) >= plugin.getConfig().getInt("TAG_EXPIRE_DELAY") * 1000;
    }

    public int getRemainingTime(Player player) {
        return (int) (plugin.getConfig().getInt("TAG_EXPIRE_DELAY") - (System.currentTimeMillis() - taggedPlayers.get(player.getUniqueId())) / 1000L);
    }

    public void tagPlayer(Player player, String message) {
        if (player.isOp()) {
            return;
        }
        taggedPlayers.put(player.getUniqueId(), System.currentTimeMillis());
        if (message != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    private YamlConfiguration getTagConfig() {
        File file = new File(plugin.getDataFolder(), "tags.yml");
        if (!file.exists()) {
            plugin.saveResource("tags.yml", false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
