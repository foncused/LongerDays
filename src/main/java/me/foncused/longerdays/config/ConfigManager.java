package me.foncused.longerdays.config;

import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigManager {

	private final FileConfiguration config;
	private int day;
	private int night;
	private boolean nightSkipping;
	private int playersSleepingPercentage;
	private Set<String> worlds;

	public ConfigManager(final FileConfiguration config) {
		this.config = config;
	}

	public void validate() {

		// day
		final int day = this.config.getInt("day", 30);
		if(day <= 0) {
			this.day = 30;
			LongerDaysUtil.consoleWarning("Set day cycle to " + day + " minutes is not safe, reverting to default...");
		} else {
			this.day = day;
		}
		LongerDaysUtil.console("Set day cycle to " + this.day + " minutes");

		// night
		final int night = this.config.getInt("night", 5);
		if(night <= 0) {
			this.night = 5;
			LongerDaysUtil.consoleWarning("Set night cycle to " + night + " minutes is not safe, reverting to default...");
		} else {
			this.night = night;
		}
		LongerDaysUtil.console("Set night cycle to " + this.night + " minutes");

		// night-skipping
		this.nightSkipping = this.config.getBoolean("night-skipping", true);
		LongerDaysUtil.console(this.nightSkipping ? "Night skipping is enabled" : "Night skipping is disabled");

		// players-sleeping-percentage
		final int playersSleepingPercentage = this.config.getInt("players-sleeping-percentage", 50);
		if(playersSleepingPercentage < 0 || playersSleepingPercentage > 100) {
			this.playersSleepingPercentage = 50;
			LongerDaysUtil.consoleWarning("Set players sleeping percentage to " + playersSleepingPercentage + "% is not safe, reverting to default...");
		} else {
			this.playersSleepingPercentage = playersSleepingPercentage;
		}
		LongerDaysUtil.console("Set players sleeping percentage to " + playersSleepingPercentage + "%");

		// worlds
		final List<String> worlds = this.config.getStringList("worlds");
		this.worlds = new HashSet<>();
		this.worlds.addAll(worlds);
		this.worlds = Collections.unmodifiableSet(this.worlds);

	}

	public int getDay() {
		return this.day;
	}

	public int getNight() {
		return this.night;
	}

	public Set<String> getWorlds() {
		return Collections.unmodifiableSet(this.worlds);
	}

	public boolean isNightSkipping() {
		return this.nightSkipping;
	}

	public int getPlayersSleepingPercentage() {
		return this.playersSleepingPercentage;
	}

}
