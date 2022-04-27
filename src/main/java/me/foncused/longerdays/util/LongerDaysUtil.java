package me.foncused.longerdays.util;

import me.foncused.longerdays.LongerDays;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class LongerDaysUtil {

	public static void console(final String message) {
		Bukkit.getLogger().info(LongerDays.PREFIX + message);
	}

	public static void consoleWarning(final String message) {
		Bukkit.getLogger().warning(LongerDays.PREFIX + message);
	}

	public static boolean isDay(final World world) {
		final long time = world.getTime();
		return time >= 0 && time < 12000;
	}

	public static boolean isNight(final World world) {
		return (!(isDay(world)));
	}

}
