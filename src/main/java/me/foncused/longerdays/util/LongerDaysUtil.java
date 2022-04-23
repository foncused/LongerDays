package me.foncused.longerdays.util;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class LongerDaysUtil {

	private static final String PREFIX = "[LongerDays] ";

	public static void console(final String message) {
		Bukkit.getLogger().info(PREFIX + message);
	}

	public static void consoleWarning(final String message) {
		Bukkit.getLogger().warning(PREFIX + message);
	}

	public static boolean isDay(final World world) {
		final long time = world.getTime();
		return time >= 0 && time < 12000;
	}

	public static boolean isNight(final World world) {
		return (!(isDay(world)));
	}

}
