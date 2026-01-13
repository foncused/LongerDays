package me.foncused.longerdays;

import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.event.player.PlayerBed;
import me.foncused.longerdays.runnable.Runnable;
import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class LongerDays extends JavaPlugin {

	public static final String PREFIX = "[LongerDays] ";

	private ConfigManager cm;

	@Override
	public void onEnable() {
		this.registerConfig();
		this.registerEvents();
		new BukkitRunnable() {
			@Override
			public void run() {
                setGameRules(Map.of(GameRule.ADVANCE_TIME, false));
				registerRunnables();
			}
		}.runTask(this);
	}

	@Override
	public void onDisable() {
        this.setGameRules(Map.of(GameRule.ADVANCE_TIME, true));
	}

	private void registerConfig() {
		this.saveDefaultConfig();
		this.cm = new ConfigManager(this.getConfig());
		this.cm.validate();
	}

	private void registerEvents() {
        if(this.cm.isNightSkipping()) {
            this.getServer().getPluginManager().registerEvents(new PlayerBed(this), this);
        }
	}

	private void registerRunnables() {
		final Runnable runnable = new Runnable(this);
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(runnable::runCycles);
	}

	private <V> void setGameRules(Map<GameRule, V> gameRules) {
		Bukkit.getWorlds()
				.stream()
				.filter(world -> this.cm.getWorlds().contains(world.getName()))
				.forEach(world -> {
                    gameRules.forEach((k, v) -> {
                        world.setGameRule(k, v);
                        LongerDaysUtil.console("Setting GameRule." + k.getName().toUpperCase() + " to '" + v + "' for world '" + world.getName() + "'");
                    });
				});
	}

	public ConfigManager getConfigManager() {
		return this.cm;
	}

}
