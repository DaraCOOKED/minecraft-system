package com.questmaster.role;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class TabTitle {

    private TabTitle() {}

    /** Shows "[Role] Name" in the Tab list and above the head. Pass null to reset. */
    public static void apply(Player player, Role role) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        // Remove the player from any old QuestMaster role team
        for (Team t : board.getTeams()) {
            if (t.getName().startsWith("qm_")) {
                t.removeEntry(player.getName());
            }
        }

        if (role == null) {
            player.setPlayerListName(null);
            return;
        }

        // Tab list
        player.setPlayerListName(
            ChatColor.GRAY + "[" + ChatColor.GOLD + role.getDisplayName() + ChatColor.GRAY + "] "
            + ChatColor.WHITE + player.getName());

        // Nametag above the head (one team per role)
        String teamName = "qm_" + role.name().toLowerCase();
        Team team = board.getTeam(teamName);
        if (team == null) {
            team = board.registerNewTeam(teamName);
        }
        team.prefix(Component.text("[" + role.getDisplayName() + "] ", NamedTextColor.GOLD));
        team.addEntry(player.getName());
    }
}
