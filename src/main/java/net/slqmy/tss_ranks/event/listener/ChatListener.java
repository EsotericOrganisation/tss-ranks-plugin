package net.slqmy.tss_ranks.event.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.slqmy.tss_ranks.TSSRanksPlugin;
import net.slqmy.tss_ranks.manager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public final class ChatListener implements Listener {

	private final RankManager rankManager;

	public ChatListener(@NotNull TSSRanksPlugin plugin) {
		this.rankManager = plugin.getRankManager();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onChat(final @NotNull AsyncChatEvent event) {
		event.setCancelled(true);

		final Player player = event.getPlayer();
		final TextComponent rankDisplayName = rankManager.getPlayerRank(player).getDisplayName();

		Bukkit.broadcast(
						rankDisplayName.append(Component.space()).append(Component.text(player.getName())).append(Component.text(" » ")).append(event.message())
		);
	}
}
