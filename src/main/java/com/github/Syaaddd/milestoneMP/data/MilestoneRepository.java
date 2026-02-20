package com.github.Syaaddd.milestoneMP.data;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MilestoneRepository {

    private final MilestoneMP plugin;
    private final Map<UUID, PlayerData> cache;

    public MilestoneRepository(MilestoneMP plugin) {
        this.plugin = plugin;
        this.cache = new ConcurrentHashMap<>();
    }

    public void loadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (cache.containsKey(uuid)) return;

        plugin.getDatabaseManager().loadPlayerData(uuid).thenAccept(data -> {
            if (data == null) {
                data = new PlayerData(uuid);
            }
            cache.put(uuid, data);
        }).join();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return cache.get(uuid);
    }

    public PlayerData getOrCreatePlayerData(UUID uuid) {
        return cache.computeIfAbsent(uuid, k -> new PlayerData(uuid));
    }

    public void savePlayer(UUID uuid) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            plugin.getDatabaseManager().savePlayerData(data);
        }
    }

    public void saveAll() {
        for (Map.Entry<UUID, PlayerData> entry : cache.entrySet()) {
            plugin.getDatabaseManager().savePlayerData(entry.getValue());
        }
    }

    public void claimMilestone(UUID uuid, String milestoneId, String choiceId) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            data.claimMilestone(milestoneId, choiceId);
            plugin.getDatabaseManager().saveClaimedMilestone(uuid, milestoneId, choiceId);
        }
    }

    public int getTotalCommunityPlaytime() {
        return plugin.getDatabaseManager().getTotalPlaytime();
    }
}
