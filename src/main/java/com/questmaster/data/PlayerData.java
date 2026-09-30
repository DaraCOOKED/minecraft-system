package com.questmaster.data;

import com.questmaster.role.Role;

/**
 * Everything we remember about ONE player.
 * Milestone 3 will add the XP for each role here.
 */
public class PlayerData {

    private Role role;              // the player's current role
    private long lastSwitchMillis;  // when the role was last set (0 = never)

    public PlayerData(Role role, long lastSwitchMillis) {
        this.role = role;
        this.lastSwitchMillis = lastSwitchMillis;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getLastSwitchMillis() {
        return lastSwitchMillis;
    }

    public void setLastSwitchMillis(long lastSwitchMillis) {
        this.lastSwitchMillis = lastSwitchMillis;
    }
}