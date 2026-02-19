package com.github.Syaaddd.milestoneMP.command;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.gui.ChoiceGUI;
import com.github.Syaaddd.milestoneMP.gui.MilestoneGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MilestoneCommand implements CommandExecutor {

    private final MilestoneMP plugin;
    private final MilestoneGUI milestoneGUI;
    private final ChoiceGUI choiceGUI;

    public MilestoneCommand(MilestoneMP plugin) {
        this.plugin = plugin;
        this.milestoneGUI = new MilestoneGUI(plugin);
        this.choiceGUI = new ChoiceGUI(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getConfigManager().getPrefix() + "Hanya pemain yang bisa menggunakan command ini.");
            return true;
        }

        if (!player.hasPermission("milestonemp.open")) {
            player.sendMessage(plugin.getConfigManager().getPrefix() + "&cTidak ada izin.");
            return true;
        }

        if (args.length == 0) {
            milestoneGUI.open(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "open" -> milestoneGUI.open(player);
            
            case "claim" -> {
                if (args.length < 2) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /milestone claim <milestone_id>");
                    return true;
                }
                plugin.getMilestoneManager().claimMilestone(player, args[1], null);
            }
            
            case "reload" -> {
                if (!player.hasPermission("milestonemp.admin")) {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + "&cTidak ada izin.");
                    return true;
                }
                plugin.reloadConfig();
                plugin.getConfigManager().load();
                player.sendMessage(plugin.getConfigManager().getPrefix() + plugin.getConfigManager().getMsgConfigReloaded());
            }
            
            case "check" -> {
                var milestone = plugin.getMilestoneManager().getCurrentProgressMilestone(player);
                if (milestone != null) {
                    double percent = plugin.getMilestoneManager().getProgressPercentage(player, milestone);
                    player.sendMessage(plugin.getConfigManager().getPrefix() + 
                        "&7Progress: &a" + String.format("%.1f", percent) + "% &7(" + milestone.getId() + ")");
                } else {
                    player.sendMessage(plugin.getConfigManager().getPrefix() + 
                        plugin.getConfigManager().getMsgNoMilestone());
                }
            }
            
            default -> {
                player.sendMessage(plugin.getConfigManager().getPrefix() + "&cUsage: /milestone [open|claim|check|reload]");
            }
        }

        return true;
    }

    public ChoiceGUI getChoiceGUI() {
        return choiceGUI;
    }
}
