package net.slqmy.tss_ranks.manager;

import net.kyori.adventure.text.Component;
import net.slqmy.tss_core.data.type.Rank;
import net.slqmy.tss_ranks.TSSRanksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class NameTagManager {

	private final TSSRanksPlugin plugin;

	public NameTagManager(TSSRanksPlugin plugin) {
		this.plugin = plugin;
	}

	public void setNameTags(@NotNull Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		player.setScoreboard(scoreboard);

		RankManager rankManager = plugin.getRankManager();
		Rank[] ranks = rankManager.getRanks();

		for (Rank rank : ranks) {
			Team team = scoreboard.registerNewTeam(rank.getName());
			team.prefix(rank.getDisplayName().append(Component.space()));
		}

		UUID playerUUID = player.getUniqueId();

		for (Player target : Bukkit.getOnlinePlayers()) {
			UUID targetUUID = target.getUniqueId();

			if (!targetUUID.equals(playerUUID)) {
				Rank targetRank = rankManager.getPlayerRank(targetUUID);

				Team targetRankTeam = scoreboard.getTeam(targetRank.getName());

				if (targetRankTeam != null) {
					targetRankTeam.addEntry(target.getName());
				}
			}
		}
	}

	public void addNewNameTag(@NotNull Player player) {
		Rank playerRank = plugin.getRankManager().getPlayerRank(player.getUniqueId());

		String rankName = playerRank.getName();
		String playerName = player.getName();

		for (Player target : Bukkit.getOnlinePlayers()) {
			Team targetRankTeam = target.getScoreboard().getTeam(rankName);

			if (targetRankTeam != null) {
				targetRankTeam.addEntry(playerName);
			}
		}
	}

	public void removeNameTag(@NotNull Player player) {
		String playerName = player.getName();

		for (Player target : Bukkit.getOnlinePlayers()) {
			Team playerTeam = target.getScoreboard().getEntryTeam(playerName);

			if (playerTeam != null) {
				playerTeam.removeEntry(playerName);
			}
		}
	}
}
