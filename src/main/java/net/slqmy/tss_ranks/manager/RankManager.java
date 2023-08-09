package net.slqmy.tss_ranks.manager;

import net.slqmy.tss_core.data.type.Permission;
import net.slqmy.tss_core.data.type.Rank;
import net.slqmy.tss_core.util.FileUtil;
import net.slqmy.tss_ranks.TSSRanksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public final class RankManager {

	private final TSSRanksPlugin plugin;

	private final Rank[] ranks;
	private final Rank defaultRank;
	private final HashMap<UUID, PermissionAttachment> playerPermissionsMap = new HashMap<>();

	public RankManager(@NotNull TSSRanksPlugin plugin) {

		this.plugin = plugin;
		this.ranks = FileUtil.readJsonFile(plugin.getRanksFile(), Rank[].class);
		this.defaultRank = getRank(plugin.getConfig().getString("default-rank-name"));
	}

	public Rank[] getRanks() {
		return ranks;
	}

	public Rank getDefaultRank() {
		return defaultRank;
	}

	public HashMap<UUID, PermissionAttachment> getPlayerPermissionsMap() {
		return playerPermissionsMap;
	}

	public void setRank(UUID uuid, Rank targetRank, boolean isFirstJoin) {
		if (targetRank == null) {
			return;
		}

		String targetRankName = targetRank.getName();
		plugin.getCore().getPlayerManager().getProfile(uuid).setRankName(targetRankName);

		if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
			final Player player = Bukkit.getPlayer(uuid);
			assert player != null;

			final NameTagManager nameTagManager = plugin.getNameTagManager();

			nameTagManager.removeNameTag(player);
			nameTagManager.addNewNameTag(player);

			if (!isFirstJoin) {
				setPermissions(player);
			}
		}
	}

	public void setRank(UUID uuid, Rank targetRank) {
		setRank(uuid, targetRank, false);
	}

	public void setRank(UUID uuid, String rankName, boolean isFirstJoin) {
		setRank(uuid, getRank(rankName), isFirstJoin);
	}

	public void setRank(UUID uuid, String rankName) {
		setRank(uuid, getRank(rankName), false);
	}

	public Rank getPlayerRank(UUID uuid) {
		return getRank(plugin.getCore().getPlayerManager().getProfile(uuid).getRankName());
	}

	public Rank getPlayerRank(@NotNull Player player) {
		return getPlayerRank(player.getUniqueId());
	}

	public @Nullable Rank getRank(String rankName) {
		for (Rank rank : ranks) {
			String targetRankName = rank.getName();

			if (targetRankName.equals(rankName)) {
				return rank;
			}
		}

		return null;
	}

	public void setPermissions(@NotNull Player player) {
		UUID uuid = player.getUniqueId();
		PermissionAttachment attachment;

		if (playerPermissionsMap.containsKey(uuid)) {
			attachment = playerPermissionsMap.get(uuid);
		} else {
			attachment = player.addAttachment(plugin);
			playerPermissionsMap.put(uuid, attachment);
		}

		for (Permission permission : plugin.getRankManager().getPlayerRank(uuid).getPermissions()) {
			attachment.unsetPermission(permission.getPermissionNode());
		}

		for (Permission permission : getPlayerRank(player).getPermissions()) {
			attachment.setPermission(permission.getPermissionNode(), permission.isEnabled());
		}
	}
}
