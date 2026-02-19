package com.github.Syaaddd.milestoneMP.milestone;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.data.PlayerData;
import com.github.Syaaddd.milestoneMP.data.MilestoneRepository;
import com.github.Syaaddd.milestoneMP.util.MessageUtil;
import com.github.Syaaddd.milestoneMP.util.RewardExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MilestoneManager {

    private final MilestoneMP plugin;
    private final MilestoneRepository repository;
    private final RewardExecutor rewardExecutor;

    public MilestoneManager(MilestoneMP plugin) {
        this.plugin = plugin;
        this.repository = plugin.getRepository();
        this.rewardExecutor = new RewardExecutor(plugin);
    }

    public void checkMilestones(Player player) {
        PlayerData data = repository.getPlayerData(player.getUniqueId());
        if (data == null) return;

        List<Milestone> milestones = plugin.getConfigManager().getMilestonesInOrder();
        
        for (Milestone milestone : milestones) {
            if (data.hasClaimed(milestone.getId())) continue;

            if (hasReached(data, milestone)) {
                String msg = plugin.getConfigManager().getMsgMilestoneAvailable();
                player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + msg));
                break;
            }
        }
    }

    public boolean hasReached(PlayerData data, Milestone milestone) {
        MilestoneType type = milestone.getType();
        int required = milestone.getAmount();

        return switch (type) {
            case PLAYTIME -> data.getPlaytimeSeconds() >= required;
            case BLOCK_BREAK -> data.getBlocksBroken() >= required;
            case BLOCK_PLACE -> data.getBlocksPlaced() >= required;
            case MOB_KILL -> data.getMobsKilled() >= required;
            case PLAYER_KILL -> data.getPlayersKilled() >= required;
            case JOIN -> data.getJoinDays() >= required;
            case COMMUNITY_PLAYTIME -> repository.getTotalCommunityPlaytime() >= required;
        };
    }

    public Milestone getNextMilestone(Player player) {
        PlayerData data = repository.getPlayerData(player.getUniqueId());
        if (data == null) return null;

        List<Milestone> milestones = plugin.getConfigManager().getMilestonesInOrder();
        
        for (Milestone milestone : milestones) {
            if (!data.hasClaimed(milestone.getId()) && hasReached(data, milestone)) {
                return milestone;
            }
        }
        return null;
    }

    public Milestone getCurrentProgressMilestone(Player player) {
        PlayerData data = repository.getPlayerData(player.getUniqueId());
        if (data == null) return null;

        List<Milestone> milestones = plugin.getConfigManager().getMilestonesInOrder();
        
        for (Milestone milestone : milestones) {
            if (!data.hasClaimed(milestone.getId())) {
                return milestone;
            }
        }
        return null;
    }

    public double getProgressPercentage(Player player, Milestone milestone) {
        PlayerData data = repository.getPlayerData(player.getUniqueId());
        if (data == null || milestone == null) return 0;

        int current = getCurrentValue(data, milestone.getType());
        int required = milestone.getAmount();
        
        return Math.min(100.0, (double) current / required * 100);
    }

    private int getCurrentValue(PlayerData data, MilestoneType type) {
        return switch (type) {
            case PLAYTIME -> data.getPlaytimeSeconds();
            case BLOCK_BREAK -> data.getBlocksBroken();
            case BLOCK_PLACE -> data.getBlocksPlaced();
            case MOB_KILL -> data.getMobsKilled();
            case PLAYER_KILL -> data.getPlayersKilled();
            case JOIN -> data.getJoinDays();
            case COMMUNITY_PLAYTIME -> repository.getTotalCommunityPlaytime();
        };
    }

    public void claimMilestone(Player player, String milestoneId, String choiceId) {
        UUID uuid = player.getUniqueId();
        Milestone milestone = plugin.getConfigManager().getMilestone(milestoneId);
        
        if (milestone == null) {
            player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMsgNoMilestone()));
            return;
        }

        PlayerData data = repository.getPlayerData(uuid);
        if (data == null) return;

        if (data.hasClaimed(milestoneId)) {
            player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cMilestone sudah diklaim."));
            return;
        }

        if (!hasReached(data, milestone)) {
            player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMsgMilestoneLocked()));
            return;
        }

        if (milestone.hasChoices() && choiceId == null) {
            return;
        }

        final String selectedChoiceId;
        if (choiceId != null) {
            selectedChoiceId = choiceId;
        } else if (milestone.hasChoices()) {
            selectedChoiceId = milestone.getChoices().get(0).getId();
        } else {
            selectedChoiceId = null;
        }

        if (selectedChoiceId != null) {
            final String finalChoiceId = selectedChoiceId;
            var choice = milestone.getChoices().stream()
                .filter(c -> c.getId().equals(finalChoiceId))
                .findFirst()
                .orElse(null);

            if (choice != null) {
                rewardExecutor.executeReward(player, choice);
                repository.claimMilestone(uuid, milestoneId, selectedChoiceId);
                
                String msg = plugin.getConfigManager().getMsgMilestoneClaimed()
                    .replace("%reward%", choice.getName());
                player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + msg));
            }
        } else {
            repository.claimMilestone(uuid, milestoneId, "");
            player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + 
                plugin.getConfigManager().getMsgMilestoneClaimed()));
        }
    }

    public boolean canClaimAny(UUID uuid) {
        PlayerData data = repository.getPlayerData(uuid);
        if (data == null) return false;

        List<Milestone> milestones = plugin.getConfigManager().getMilestonesInOrder();
        for (Milestone milestone : milestones) {
            if (!data.hasClaimed(milestone.getId()) && hasReached(data, milestone)) {
                return true;
            }
        }
        return false;
    }
}
