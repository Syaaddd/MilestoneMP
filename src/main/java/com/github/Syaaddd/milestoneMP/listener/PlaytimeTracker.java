package com.github.Syaaddd.milestoneMP.listener;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.data.PlayerData;
import com.github.Syaaddd.milestoneMP.data.MilestoneRepository;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

public class PlaytimeTracker extends BukkitRunnable {

    private final MilestoneMP plugin;

    public PlaytimeTracker(MilestoneMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        int interval = plugin.getConfigManager().getCheckInterval();
        
        new BukkitRunnable() {
            @Override
            public void run() {
                Collection<Player> players = (Collection<Player>) plugin.getServer().getOnlinePlayers();
                MilestoneRepository repository = plugin.getRepository();
                
                for (Player player : players) {
                    UUID uuid = player.getUniqueId();
                    PlayerData data = repository.getPlayerData(uuid);

                    if (data != null) {
                        data.addPlaytime(interval);

                        long now = System.currentTimeMillis();
                        long lastJoin = data.getLastJoinTime();
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(lastJoin);
                        int lastDay = cal.get(Calendar.DAY_OF_YEAR);
                        
                        cal.setTimeInMillis(now);
                        int currentDay = cal.get(Calendar.DAY_OF_YEAR);
                        
                        if (currentDay > lastDay) {
                            data.setJoinDays(data.getJoinDays() + 1);
                        }
                        data.setLastJoinTime(now);
                    }
                }
            }
        }.runTask(plugin);
    }

    public void start() {
        int interval = plugin.getConfigManager().getCheckInterval() * 20;
        this.runTaskTimerAsynchronously(plugin, interval, interval);
    }
}
