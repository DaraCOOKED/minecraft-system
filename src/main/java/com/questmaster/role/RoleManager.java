package com.questmaster.role;

import com.questmaster.data.PlayerDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of which role each player has.
 * Roles are kept in memory for speed, and saved through
 * PlayerDataStorage whenever something changes.
 */
public class RoleManager {

    private final PlayerDataStorage storage;
    private final Map<UUID, Role> roles;

    public RoleManager(PlayerDataStorage storage) {
        this.storage = storage;
        this.roles = new HashMap<>(storage.loadRoles()); // load saved roles at startup
    }

    /** Returns the player's role, or null if they have not chosen one. */
    public Role getRole(UUID playerId) {
        return roles.get(playerId);
    }

    /** Sets the player's role and saves it right away. */
    public void setRole(UUID playerId, Role role) {
        roles.put(playerId, role);
        storage.saveRoles(roles);
    }

    /** Saves everything (used when the server shuts down). */
    public void save() {
        storage.saveRoles(roles);
    }
}