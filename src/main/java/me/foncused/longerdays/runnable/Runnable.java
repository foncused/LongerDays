package me.foncused.longerdays.runnable;

import me.foncused.longerdays.LongerDays;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Runnable {

	private final LongerDays plugin;
	private final long day;
	private final long night;
	private long count;
	private long freeze;

	public Runnable(final LongerDays plugin, final long day, final long night) {
		this.plugin = plugin;
		this.day = day;
		this.night = night;
	}

	public void runCycles() {
		new BukkitRunnable() {
			@Override
			public void run() {
				final World world = Bukkit.getWorlds().get(0);
				final long time = world.getTime();
				if(isDay(world)) {
					setTime(world, day);
				} else if(isNight(world)) {
					setTime(world, night);
				} else {
					plugin.consoleWarning("World time " + time + " is impossible");
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

	private void setTime(final World world, final long val) throws ArithmeticException {
		final double ratio = (1.0 / (val / 12000.0));
		final long time = world.getTime();
		// Speedup
		if(ratio > 1.0) {
			world.setTime(time + Math.round(ratio) - 1);
		// Slowdown
		} else if(ratio < 1.0) {
			if(this.count == 0) {
				this.count = Math.round(1.0 / ratio) - 1;
				this.freeze = time;
			} else {
				world.setTime(this.freeze);
				this.count--;
			}
		}
	}

}
