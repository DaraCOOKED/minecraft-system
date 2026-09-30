package com.questmaster.role;

/**
 * nika heng
 */
public enum Role {
    MERCHANT("Merchant"),
    BLACKSMITH("Blacksmith"),
    POTIONER("Potioner"),
    FARMER("Farmer"),
    ENCHANTER("Enchanter"),
    BUILDER("Builder"),
    EXPLORER("Explorer"),
    MINER("Miner"),
    FREELANCER("Freelancer");

    // showplayers
    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Finds a role from text typed by a player (ignores upper/lower case).
     * Returns null if no role matches.
     */
    public static Role fromName(String input) {
        for (Role role : values()) {
            if (role.name().equalsIgnoreCase(input) || role.displayName.equalsIgnoreCase(input)) {
                return role;
            }
        }
        return null;
    }
}