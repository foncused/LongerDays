package me.foncused.longerdays.event.player;

import me.foncused.longerdays.LongerDays;
import me.foncused.longerdays.config.ConfigManager;
import me.foncused.longerdays.util.LongerDaysUtil;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerBed implements Listener {

    private LongerDays plugin;
    private ConfigManager cm;
    private int sleeping;

    public PlayerBed(final LongerDays plugin) {
        this.plugin = plugin;
        this.cm = this.plugin.getConfigManager();
        this.sleeping = 0;
    }

    @EventHandler
    public void onPlayerBedEnter(final PlayerBedEnterEvent event) {
        if(event.isAsynchronous()) {
            event.setCancelled(true);
            return;
        }
        final Player player = event.getPlayer();
        final World world = player.getWorld();
        if(this.cm.getWorlds().contains(world.getName())) {
            this.sleeping++;
            int p = 100;
            try {
                p = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
            } catch(final Exception e) {
                LongerDaysUtil.consoleWarning("Failed to fetch GameRule.PLAYERS_SLEEPING_PERCENTAGE");
            }
            final int percentage = p;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(LongerDaysUtil.isNight(world)
                            && event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK
                            && player.isOnline() && player.isSleeping()
                            && (sleeping / world.getPlayers().size()) * 100 >= percentage) {
                        world.setTime(0);
                        sleeping = 0;
                        LongerDaysUtil.consoleWarning("The night has been skipped due to sleeping");
                    }
                }
            }.runTaskLater(this.plugin, 5 * 20);
        }
    }

    @EventHandler
    public void onPlayerBedLeave(final PlayerBedLeaveEvent event) {
        if(this.cm.getWorlds().contains(event.getPlayer().getWorld().getName()) && this.sleeping > 0) {
            this.sleeping--;
        }
    }

}
