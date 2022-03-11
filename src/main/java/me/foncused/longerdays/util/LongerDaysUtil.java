package me.foncused.longerdays.util;

import org.bukkit.Bukkit;

public class LongerDaysUtil {

	private static final String PREFIX = "[LongerDays] ";

	public static void console(final String message) {
		Bukkit.getLogger().info(PREFIX + message);
	}

	public static void consoleWarning(final String message) {
		Bukkit.getLogger().warning(PREFIX + message);
	}

}
