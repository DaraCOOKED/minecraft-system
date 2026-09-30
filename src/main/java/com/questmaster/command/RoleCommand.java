package com.questmaster.command;

import com.questmaster.role.Role;
import com.questmaster.role.RoleManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the /role command:
 *   /role                 -> shows your current role
 *   /role list            -> shows all roles
 *   /role choose <role>   -> picks a role
 */
public class RoleCommand implements TabExecutor {

    private final RoleManager roleManager;

    public RoleCommand(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Roles belong to players, so the console can't use this
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            showCurrentRole(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list" -> listRoles(player);
            case "choose" -> chooseRole(player, args);
            default -> player.sendMessage(ChatColor.RED + "Usage: /role [list | choose <role>]");
        }
        return true;
    }

    private void showCurrentRole(Player player) {
        Role role = roleManager.getRole(player.getUniqueId());
        if (role == null) {
            player.sendMessage(ChatColor.YELLOW + "You don't have a role yet. Use /role list, then /role choose <role>.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Your role: " + ChatColor.GOLD + role.getDisplayName());
        }
    }

    private void listRoles(Player player) {
        player.sendMessage(ChatColor.GREEN + "Available roles:");
        for (Role role : Role.values()) {
            player.sendMessage(ChatColor.GRAY + " - " + ChatColor.WHITE + role.getDisplayName());
        }
    }

    private void chooseRole(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /role choose <role>");
            return;
        }

        Role role = Role.fromName(args[1]);
        if (role == null) {
            player.sendMessage(ChatColor.RED + "Unknown role. Use /role list to see all roles.");
            return;
        }

        // Note: for now players can change role at any time.
        // Switch rules (cooldown, cost) will be added once the spec says so.
        roleManager.setRole(player.getUniqueId(), role);
        player.sendMessage(ChatColor.GREEN + "Your role is now " + ChatColor.GOLD + role.getDisplayName() + ChatColor.GREEN + "!");
    }

    /** Auto-complete when players press TAB. */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("list");
            options.add("choose");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("choose")) {
            for (Role role : Role.values()) {
                options.add(role.name().toLowerCase());
            }
        }

        // Keep only the options that start with what the player already typed
        String typed = args[args.length - 1].toLowerCase();
        options.removeIf(option -> !option.startsWith(typed));
        return options;
    }
}