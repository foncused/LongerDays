package me.foncused.longerdays.event.player;

import me.foncused.longerdays.LongerDays;
import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class PlayerBed implements Listener {

	private final LongerDays plugin;
	private final ConfigManager cm;
	private int sleeping;

	public PlayerBed(final LongerDays plugin) {
		this.plugin = plugin;
		this.cm = this.plugin.getConfigManager();
	}

	@EventHandler
	public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
		if(this.cm.isNightSkipping()) {
			this.sleeping++;
			final World world = event.getPlayer().getWorld();
			int percentage;
			try {
				percentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
			} catch(final Exception e) {
				percentage = this.cm.getPlayersSleepingPercentage();
				LongerDaysUtil.consoleWarning("Failed to fetch GameRule.PLAYERS_SLEEPING_PERCENTAGE, please set 'players-sleeping-percentage' in your config.yml settings.");
			}
			if(LongerDaysUtil.isNight(world)
					&& event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK
					&& (this.sleeping / world.getPlayers().size()) * 100 >= this.cm.getPlayersSleepingPercentage()) {
				this.sleeping = 0;
				world.setTime(0);
				event.setCancelled(true);
				LongerDaysUtil.console("The night has been skipped by sleeping");
			}
		}
	}

	@EventHandler
	public void onPlayerBedLeave(final PlayerBedLeaveEvent event) {
		if(this.cm.isNightSkipping()) {
			if(this.sleeping > 0) {
				this.sleeping--;
			}
		}
	}

}
