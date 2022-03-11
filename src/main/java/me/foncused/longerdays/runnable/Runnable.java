package me.foncused.longerdays.runnable;

import me.foncused.longerdays.LongerDays;
import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Runnable {

	private final LongerDays plugin;
	private final ConfigManager cm;
	private long count;

	public Runnable(final LongerDays plugin) {
		this.plugin = plugin;
		this.cm = this.plugin.getConfigManager();
	}

	public void runCycles(final World world) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final long time = world.getTime();
				if(isDay(world)) {
					setTime(world, convertMinsToTicks(cm.getDay()));
				} else if(isNight(world)) {
					setTime(world, convertMinsToTicks(cm.getNight()));
				} else {
					LongerDaysUtil.consoleWarning(world.getName() + " world time " + time + " is impossible");
				}
			}
		}.runTaskTimer(this.plugin, 0, 1);
	}

	private boolean isDay(final World world) {
		final long time = world.getTime();
		return time >= 0 && time < 12000;
	}

	private boolean isNight(final World world) {
		return (!(this.isDay(world)));
	}

	private void setTime(final World world, final long val) {
		final double ratio = (1.0 / (val / 12000.0));
		long time = world.getTime();
		// Speedup
		if(ratio > 1.0) {
			time += Math.round(ratio);
			world.setTime(time);
			this.count = 0;
		// Slowdown
		} else if(ratio < 1.0) {
			if(this.count <= 0) {
				// Slow
				time += 1;
				world.setTime(time);
				this.count = Math.round(1.0 / ratio) - 1;
			} else {
				// Wait
				this.count--;
			}
		}
	}

	private long convertMinsToTicks(final long min) {
		return min * 60 * 20;
	}

}
