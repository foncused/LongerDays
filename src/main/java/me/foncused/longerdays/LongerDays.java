package me.foncused.longerdays;

import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.event.WorldLoad;
import me.foncused.longerdays.runnable.Runnable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LongerDays extends JavaPlugin {

	private ConfigManager cm;

	@Override
	public void onEnable() {
		this.registerConfig();
		this.registerEvents();
		this.registerRunnables();
	}

	private void registerConfig() {
		this.saveDefaultConfig();
		final FileConfiguration config = this.getConfig();
		this.cm = new ConfigManager(
				config.getInt("day", 30),
				config.getInt("night", 5),
				config.getStringList("worlds")
		);
	}

	private void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new WorldLoad(this), this);
	}

	private void registerRunnables() {
		final Runnable runnable = new Runnable(this);
		this.cm.getWorlds().forEach(world -> runnable.runCycles(Bukkit.getWorld(world)));
	}

	public ConfigManager getConfigManager() {
		return this.cm;
	}

}
