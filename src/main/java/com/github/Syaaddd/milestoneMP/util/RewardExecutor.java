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
        String command = choice.formatCommand(player.getName());
        
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        final String finalCommand = command;
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
        });

        if (plugin.getConfigManager().isCommunityRewardBroadcast()) {
            String msg = plugin.getConfigManager().getMsgMilestoneClaimed()
                .replace("%reward%", choice.getName());
            Bukkit.broadcastMessage(plugin.getConfigManager().getPrefix() + 
                "&7" + player.getName() + " " + msg);
        }
    }
}
