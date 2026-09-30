package com.questmaster.role;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/** Restores the "[Role] Name" tab title whenever a player joins. */
public class TabTitleListener implements Listener {

    private final RoleManager roleManager;

    public TabTitleListener(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Role role = roleManager.getRole(event.getPlayer().getUniqueId());
        if (role != null) {
            TabTitle.apply(event.getPlayer(), role);
        }
    }
}
