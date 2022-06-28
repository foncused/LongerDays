package me.foncused.longerdays.runnable;

import me.foncused.longerdays.LongerDays;
import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Runnable {

	private final LongerDays plugin;
	private final ConfigManager cm;
	private final Map<String, Long> counts;

	public Runnable(final LongerDays plugin) {
		this.plugin = plugin;
		this.cm = this.plugin.getConfigManager();
		this.counts = new HashMap<>();
	}

	public void runCycles(final World world) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final long time = world.getTime();
				if(LongerDaysUtil.isDay(world)) {
					setTime(world, convertMinsToTicks(cm.getDay()));
				} else if(LongerDaysUtil.isNight(world)) {
					setTime(world, convertMinsToTicks(cm.getNight()));
				} else {
					LongerDaysUtil.consoleWarning(world.getName() + " world time " + time + " is impossible");
				}
			}
		}.runTaskTimer(this.plugin, 0, 1);
		LongerDaysUtil.console("Running day and night cycles for world '" + world.getName() + "'");
	}

	private void setTime(final World world, final long val) {
		final String w = world.getName();
		this.counts.putIfAbsent(w, 0L);
		final double ratio = (1.0 / (val / 12000.0));
		long time = world.getTime();
		// Speedup
		if(ratio > 1.0) {
			time += Math.round(ratio);
			world.setTime(time);
			this.counts.put(w, 0L);
		// Slowdown
		} else if(ratio < 1.0) {
			final long count = this.counts.get(w);
			if(count <= 0) {
				// Slow
				time += 1;
				world.setTime(time);
				this.counts.put(w, Math.round(1.0 / ratio) - 1);
			} else {
				// Wait
				this.counts.put(w, count - 1);
			}
		// Normal
		} else {
			world.setTime(++time);
		}
	}

	private long convertMinsToTicks(final long min) {
		return min * 60 * 20;
	}

}
