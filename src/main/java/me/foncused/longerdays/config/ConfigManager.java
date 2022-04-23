package me.foncused.longerdays.config;

import me.foncused.longerdays.util.LongerDaysUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigManager {

	private final int day;
	private final int night;
	private Set<String> worlds;

	public ConfigManager(
		final int day,
		final int night,
		final List<String> worlds
	) {
		if(day <= 0) {
			this.day = 30;
			LongerDaysUtil.consoleWarning("Set day cycle to " + day + " minutes is not safe, reverting to default...");
		} else {
			this.day = day;
		}
		LongerDaysUtil.console("Set day cycle to " + day + " minutes");
		if(night <= 0) {
			this.night = 5;
			LongerDaysUtil.consoleWarning("Set night cycle to " + night + " minutes is not safe, reverting to default...");
		} else {
			this.night = night;
		}
		LongerDaysUtil.console("Set night cycle to " + night + " minutes");
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

}
