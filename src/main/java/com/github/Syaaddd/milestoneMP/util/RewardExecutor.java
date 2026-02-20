package com.github.Syaaddd.milestoneMP.util;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneChoice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RewardExecutor {

    private final MilestoneMP plugin;

    public RewardExecutor(MilestoneMP plugin) {
        this.plugin = plugin;
    }

    public void executeReward(Player player, MilestoneChoice choice) {
        String rawCommand = choice.formatCommand(player.getName());
        
        final String command;
        if (rawCommand.startsWith("/")) {
            command = rawCommand.substring(1);
        } else {
            command = rawCommand;
        }

        final String playerName = player.getName();
        final String rewardName = choice.getName();
        
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });

        String msg = plugin.getConfigManager().getMsgMilestoneClaimed()
            .replace("%reward%", rewardName);
        player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + msg));

        if (plugin.getConfigManager().isCommunityRewardBroadcast()) {
            Bukkit.broadcastMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + 
                "&7" + playerName + " " + msg));
        }
    }
}
