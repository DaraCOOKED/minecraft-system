package com.questmaster;

import com.questmaster.command.RoleCommand;
import com.questmaster.data.PlayerDataStorage;
import com.questmaster.role.RoleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of QuestMaster.
 * It only sets things up: it creates the managers and registers commands.
 * Game logic lives in the other classes.
 */
public class QuestMasterPlugin extends JavaPlugin {

    private RoleManager roleManager;

    @Override
    public void onEnable() {
        // Storage first, because the RoleManager needs it
        PlayerDataStorage storage = new PlayerDataStorage(this);
        roleManager = new RoleManager(storage);

        // Connect /role to its handler (declared in plugin.yml)
        getCommand("role").setExecutor(new RoleCommand(roleManager));

        getLogger().info("QuestMaster enabled!");
    }

    @Override
    public void onDisable() {
        // Save roles when the server stops
        if (roleManager != null) {
            roleManager.save();
        }
        getLogger().info("QuestMaster disabled!");
    }
}