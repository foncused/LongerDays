package me.foncused.longerdays.event;

import me.foncused.longerdays.LongerDays;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldLoad implements Listener {

	private final LongerDays plugin;

	public WorldLoad(final LongerDays plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onWorldLoad(final WorldLoadEvent event) {
		final World world = event.getWorld();
		if(this.plugin.getConfigManager().getWorlds().contains(world.getName())) {
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		}
	}

}
