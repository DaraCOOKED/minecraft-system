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
 */
public class PlayerDataStorage {

    private final JavaPlugin plugin;
    private final File file;

    public PlayerDataStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "playerdata.yml");
    }

    /** Reads every saved player role from the file. */
    public Map<UUID, Role> loadRoles() {
        Map<UUID, Role> roles = new HashMap<>();

        // First run: no file yet, so nobody has a role
        if (!file.exists()) {
            return roles;
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection players = yaml.getConfigurationSection("players");
        if (players == null) {
            return roles;
        }

        for (String key : players.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                Role role = Role.fromName(players.getString(key + ".role", ""));
                if (role != null) {
                    roles.put(uuid, role);
                }
            } catch (IllegalArgumentException e) {
                // The key was not a valid UUID, so skip it
                plugin.getLogger().warning("Skipping invalid entry in playerdata.yml: " + key);
            }
        }
        return roles;
    }

    /** Writes every player role to the file. */
    public void saveRoles(Map<UUID, Role> roles) {
        YamlConfiguration yaml = new YamlConfiguration();

        for (Map.Entry<UUID, Role> entry : roles.entrySet()) {
            yaml.set("players." + entry.getKey() + ".role", entry.getValue().name());
        }

        try {
            plugin.getDataFolder().mkdirs(); // make sure the plugin folder exists
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save playerdata.yml: " + e.getMessage());
        }
    }
}