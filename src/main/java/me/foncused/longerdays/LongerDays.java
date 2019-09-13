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
		long day = config.getLong("day", 30);
		if(day <= 0) {
			this.consoleWarning("Set day cycle to " + day + " minutes is not safe, reverting to default...");
			day = 30;
		}
		this.console("Set day cycle to " + day + " minutes");
		long night = config.getLong("night", 5);
		if(night <= 0) {
			this.consoleWarning("Set night cycle to " + night + " minutes is not safe, reverting to default...");
			night = 5;
		}
		this.console("Set night cycle to " + night + " minutes");
		new Runnable(this, day, night).runCycles();
	}

	public void console(final String message) {
		Bukkit.getLogger().info(this.PREFIX + message);
	}

	public void consoleWarning(final String message) {
		Bukkit.getLogger().warning(this.PREFIX + message);
	}

}
