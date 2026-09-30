package com.questmaster.role;

import com.questmaster.data.PlayerData;
import com.questmaster.data.PlayerDataStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of each player's role and the role switch cooldown.
 * Data is kept in memory for speed and saved through PlayerDataStorage.
 */
public class RoleManager {

    private final PlayerDataStorage storage;
    private final FileConfiguration config;
    private final Map<UUID, PlayerData> players;

    public RoleManager(PlayerDataStorage storage, FileConfiguration config) {
        this.storage = storage;
        this.config = config;
        this.players = new HashMap<>(storage.loadAll()); // load saved data at startup
    }

    /** Returns the player's role, or null if they have not chosen one. */
    public Role getRole(UUID playerId) {
        PlayerData data = players.get(playerId);
        return data == null ? null : data.getRole();
    }

    /** How many milliseconds until the player may switch role again (0 = can switch now). */
    public long getRemainingCooldownMillis(UUID playerId) {
        PlayerData data = players.get(playerId);
        if (data == null) {
            return 0; // never chose a role, so no cooldown
        }
        long cooldownMillis = config.getLong("role-switch-cooldown-minutes", 15) * 60_000L;
        long endsAt = data.getLastSwitchMillis() + cooldownMillis;
        return Math.max(0, endsAt - System.currentTimeMillis());
    }

    /** Sets the player's role, starts the cooldown timer, and saves right away. */
    public void setRole(UUID playerId, Role role) {
        long now = System.currentTimeMillis();
        PlayerData data = players.get(playerId);

        if (data == null) {
            players.put(playerId, new PlayerData(role, now)); // first choice
        } else {
            data.setRole(role);
            data.setLastSwitchMillis(now);
        }
        storage.saveAll(players);
    }

    /** Saves everything (used when the server shuts down). */
    public void save() {
        storage.saveAll(players);
    }
}