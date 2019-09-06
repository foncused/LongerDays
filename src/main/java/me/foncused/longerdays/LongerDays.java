package me.foncused.longerdays;

import org.bukkit.plugin.java.JavaPlugin;

public class LongerDays extends JavaPlugin {

	@Override
	public void onEnable() {
		this.registerConfig();
	}

	private void registerConfig() {
		this.saveDefaultConfig();
	}

}
