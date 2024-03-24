package me.foncused.longerdays;

import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.event.player.PlayerBed;
import me.foncused.longerdays.runnable.Runnable;
import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LongerDays extends JavaPlugin {

	public static final String PREFIX = "[LongerDays] ";

	private ConfigManager cm;

	@Override
	public void onEnable() {
		this.registerConfig();
		this.registerEvents();
		new BukkitRunnable() {
			@Override
			public void run() {
				setDaylightCycle(false);
				setPlayersSleepingPercentage(cm.getPlayersSleepingPercentage());
				registerRunnables();
			}
		}.runTask(this);
	}

	@Override
	public void onDisable() {
		this.setDaylightCycle(true);
	}

	private void registerConfig() {
		this.saveDefaultConfig();
		this.cm = new ConfigManager(this.getConfig());
		this.cm.validate();
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new PlayerBed(this), this);
	}

	private void registerRunnables() {
		final Runnable runnable = new Runnable(this);
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(runnable::runCycles);
	}

	private void setDaylightCycle(final boolean value) {
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(world -> {
					world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, value);
					LongerDaysUtil.console("Setting GameRule.DO_DAYLIGHT_CYCLE to " + value + " for world '" + world.getName() + "'");
				});
	}

	private void setPlayersSleepingPercentage(final int percentage) {
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(world -> {
					try {
						world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, percentage);
						LongerDaysUtil.console("Setting GameRule.PLAYERS_SLEEPING_PERCENTAGE to " + percentage + "% for world '" + world.getName() + "'");
					} catch(final Exception e) {
						LongerDaysUtil.consoleWarning("Failed to set GameRule.PLAYERS_SLEEPING_PERCENTAGE to " + percentage + "% for world '" + world.getName() + "'");
					}
				});
	}

	public ConfigManager getConfigManager() {
		return this.cm;
	}

}
