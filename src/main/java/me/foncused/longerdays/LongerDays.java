package me.foncused.longerdays;

import me.foncused.longerdays.runnable.Runnable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LongerDays extends JavaPlugin {

	private final String PREFIX = "[LongerDays] ";

	@Override
	public void onEnable() {
		this.registerConfig();
		this.registerRunnables();
	}

	private void registerConfig() {
		this.saveDefaultConfig();
	}

	private void registerRunnables() {
		final FileConfiguration config = this.getConfig();
		long day = config.getLong("day", 36000);
		if(day < 0) {
			this.consoleWarning("Set day cycle to " + day + " ticks is not safe, reverting to default...");
			day = 36000;
		}
		this.console("Set day cycle to " + day + " ticks");
		long night = config.getLong("night", 6000);
		if(night < 0) {
			this.consoleWarning("Set night cycle to " + night + " ticks is not safe, reverting to default...");
			night = 6000;
		}
		this.console("Set night cycle to " + night + " ticks");
		new Runnable(this, day, night).runCycles();
	}

	public void console(final String message) {
		Bukkit.getLogger().info(this.PREFIX + message);
	}

	public void consoleWarning(final String message) {
		Bukkit.getLogger().warning(this.PREFIX + message);
	}

}
