package com.questmaster.data;

import com.questmaster.role.Role;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Saves and loads player data from a YAML file.
 * This is the ONLY class that touches files. If we switch to SQLite
 * later, only this class needs to change.
 *
 * File: plugins/QuestMaster/playerdata.yml
 * Format:
 *   players:
 *     <player-uuid>:
 *       role: MINER
 *       last-switch: 1767225600000   (time in milliseconds)
 */
public class PlayerDataStorage {

    private final JavaPlugin plugin;
    private final File file;

    public PlayerDataStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "playerdata.yml");
    }

    /** Reads every saved player from the file. */
    public Map<UUID, PlayerData> loadAll() {
        Map<UUID, PlayerData> result = new HashMap<>();

        // First run: no file yet, so nobody has data
        if (!file.exists()) {
            return result;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection players = yaml.getConfigurationSection("players");
        if (players == null) {
            return result;
        }

        for (String key : players.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                Role role = Role.fromName(players.getString(key + ".role", ""));
                long lastSwitch = players.getLong(key + ".last-switch", 0L);
                if (role != null) {
                    result.put(uuid, new PlayerData(role, lastSwitch));
                }
            } catch (IllegalArgumentException e) {
                // The key was not a valid UUID, so skip it
                plugin.getLogger().warning("Skipping invalid entry in playerdata.yml: " + key);
            }
        }
        return result;
    }

    /** Writes every player to the file. */
    public void saveAll(Map<UUID, PlayerData> allData) {
        YamlConfiguration yaml = new YamlConfiguration();

        for (Map.Entry<UUID, PlayerData> entry : allData.entrySet()) {
            String path = "players." + entry.getKey();
            yaml.set(path + ".role", entry.getValue().getRole().name());
            yaml.set(path + ".last-switch", entry.getValue().getLastSwitchMillis());
        }

        try {
            plugin.getDataFolder().mkdirs(); // make sure the plugin folder exists
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save playerdata.yml: " + e.getMessage());
        }
    }
}