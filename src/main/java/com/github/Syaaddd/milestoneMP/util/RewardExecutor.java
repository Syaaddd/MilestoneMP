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

        plugin.getLogger().info("Executing reward command: " + command + " for player: " + player.getName());
        
        Bukkit.getScheduler().runTask(plugin, () -> {
            boolean success = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            if (success) {
                plugin.getLogger().info("Reward command executed successfully: " + command);
            } else {
                plugin.getLogger().warning("Failed to execute reward command: " + command);
            }
        });

        String msg = plugin.getConfigManager().getMsgMilestoneClaimed()
            .replace("%reward%", choice.getName());
        player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + msg));

        if (plugin.getConfigManager().isCommunityRewardBroadcast()) {
            Bukkit.broadcastMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + 
                "&7" + player.getName() + " " + msg));
        }
    }
}
