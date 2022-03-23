package me.foncused.longerdays;

import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.runnable.Runnable;
import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LongerDays extends JavaPlugin {

	private ConfigManager cm;

	@Override
	public void onEnable() {
		this.registerConfig();
		new BukkitRunnable() {
			@Override
			public void run() {
				registerGameRules();
				registerRunnables();
			}
		}.runTask(this);
	}

	@Override
	public void onDisable() {
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(world -> {
					world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
					LongerDaysUtil.console("Setting GameRule.DO_DAYLIGHT_CYCLE to true for world '" + world.getName() + "'");
				});
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

	private void registerGameRules() {
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(world -> {
					world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
					LongerDaysUtil.console("Setting GameRule.DO_DAYLIGHT_CYCLE to false for world '" + world.getName() + "'");
				});
	}

	private void registerRunnables() {
		final Runnable runnable = new Runnable(this);
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(runnable::runCycles);
	}

	public ConfigManager getConfigManager() {
		return this.cm;
	}

}
