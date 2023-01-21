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

	private final ConfigManager configManager;
	private int sleeping;

	public PlayerBed(ConfigManager configManager) {
		this.configManager = configManager;
	}

	@EventHandler
	public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
		this.sleeping++;

		//do nothing if night skipping is disabled
		if(!configManager.isNightSkippingEnabled()){
			return;
		}

		final World world = event.getPlayer().getWorld();

		int percentage;


		if(!configManager.isPercentageEnabled()){

			try{
				percentage = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
			} catch (Exception e) {
				LongerDaysUtil.consoleWarning("Could not fetch game-rule value 'playersSleepingPercentage!" +
						" Please go to the config.yml and enable players-sleeping-percentage");
				return;
			}
		}

		percentage = configManager.getPercentage();
		if(LongerDaysUtil.isNight(world)
				&& event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK
				&& (this.sleeping / world.getPlayers().size()) * 100 >= percentage) {
			this.sleeping = 0;
			world.setTime(0);
			event.setCancelled(true);
			LongerDaysUtil.console("The night has been skipped by sleeping");
		}
	}

	@EventHandler
	public void onPlayerBedLeave(final PlayerBedLeaveEvent event) {
		if(this.sleeping > 0) {
			this.sleeping--;
		}
	}

}
