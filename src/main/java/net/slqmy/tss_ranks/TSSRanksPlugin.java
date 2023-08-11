package net.slqmy.tss_ranks;

import net.slqmy.tss_core.TSSCorePlugin;
import net.slqmy.tss_core.manager.FileManager;
import net.slqmy.tss_ranks.command.SetRankCommand;
import net.slqmy.tss_ranks.event.listener.ChatListener;
import net.slqmy.tss_ranks.event.listener.ConnectionListener;
import net.slqmy.tss_ranks.manager.NameTagManager;
import net.slqmy.tss_ranks.manager.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TSSRanksPlugin extends JavaPlugin {

  private final TSSCorePlugin core = (TSSCorePlugin) Bukkit.getPluginManager().getPlugin("TSS-Core");

  private FileManager fileManager;
  private RankManager rankManager;
  private NameTagManager nameTagManager;

  private File ranksFile;

  public TSSCorePlugin getCore() {
	return core;
  }

  public FileManager getFileManager() {
	return fileManager;
  }

  public RankManager getRankManager() {
	return rankManager;
  }

  public NameTagManager getNameTagManager() {
	return nameTagManager;
  }

  public File getRanksFile() {
	return ranksFile;
  }

  @Override
  public void onEnable() {
	getDataFolder().mkdir();

	getConfig().options().copyDefaults();
	saveDefaultConfig();

	fileManager = new FileManager(this);
	ranksFile = fileManager.initiateJsonFile("ranks");

	rankManager = new RankManager(this);
	nameTagManager = new NameTagManager(this);

	new SetRankCommand(this);

	PluginManager pluginManager = Bukkit.getPluginManager();

	pluginManager.registerEvents(new ChatListener(this), this);
	pluginManager.registerEvents(new ConnectionListener(this), this);

	for (final Player player : Bukkit.getOnlinePlayers()) {
	  nameTagManager.setNameTags(player);
	  nameTagManager.addNewNameTag(player);
	}
  }
}
