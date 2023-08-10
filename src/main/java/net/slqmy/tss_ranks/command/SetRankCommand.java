package net.slqmy.tss_ranks.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.OfflinePlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.TextComponent;
import net.slqmy.tss_core.data.Message;
import net.slqmy.tss_core.data.type.Rank;
import net.slqmy.tss_core.manager.MessageManager;
import net.slqmy.tss_core.util.DebugUtil;
import net.slqmy.tss_ranks.TSSRanksPlugin;
import net.slqmy.tss_ranks.manager.RankManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class SetRankCommand {

	private final RankManager rankManager;

	public SetRankCommand(@NotNull TSSRanksPlugin plugin) {
		this.rankManager = plugin.getRankManager();

		DebugUtil.log((Object) rankManager.getRanks());

		new CommandAPICommand("set-rank")
						.withPermission(CommandPermission.OP)
						.withArguments(new OfflinePlayerArgument("player"), new GreedyStringArgument("rank").replaceSuggestions(
										ArgumentSuggestions.strings(Arrays.stream(rankManager.getRanks()).map(Rank::getName).toArray(String[]::new))
						))
						.executesPlayer((executionInfo) -> {
							Player player = executionInfo.sender();
							CommandArguments args = executionInfo.args();

							OfflinePlayer target = (OfflinePlayer) args.get("player");
							assert target != null;

							MessageManager messageManager = plugin.getCore().getMessageManager();

							if (target.getName() == null) {
								player.sendMessage(messageManager.getPlayerMessage(Message.NONEXISTENT_PLAYER, player));
								return;
							}

							String rankName = (String) args.get("rank");
							Rank rank = rankManager.getRank(rankName);

							if (rank == null) {
								player.sendMessage(messageManager.getPlayerMessage(Message.NONEXISTENT_RANK, player));
								return;
							}

							UUID targetUuid = target.getUniqueId();
							Rank previousRank = rankManager.getPlayerRank(targetUuid);
							String previousRankName = previousRank.getName();

							String correctRankName = rank.getName();
							boolean areSameRank = previousRankName.equals(correctRankName);

							TextComponent rankDisplayName = rank.getDisplayName();

											if (player.getUniqueId() == targetUuid) {
												if (areSameRank) {
													messageManager.sendMessage(player, Message.RANK_ALREADY_SET, rankDisplayName);
												} else {
													rankManager.setRank(targetUuid, rankName);
													messageManager.sendMessage(player, Message.RANK_SUCCESSFULLY_SET, rankDisplayName);
												}
											} else {
												if (areSameRank) {
													messageManager.sendMessage(player, Message.RANK_ALREADY_SET_OTHER, target.getName(), rankDisplayName);
												} else {
													rankManager.setRank(targetUuid, rankName);

													messageManager.sendMessage(player, Message.RANK_SUCCESSFULLY_SET_OTHER, target.getName(), rankDisplayName);

													if (target.isOnline()) {
														messageManager.sendMessage(player, Message.RANK_SET_NOTIFICATION, rankDisplayName);
													}
												}
											}
										}
						)
						.register();
	}
}
