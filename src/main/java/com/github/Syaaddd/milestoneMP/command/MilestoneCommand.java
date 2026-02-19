package com.github.Syaaddd.milestoneMP.command;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.gui.ChoiceGUI;
import com.github.Syaaddd.milestoneMP.gui.MilestoneGUI;
import com.github.Syaaddd.milestoneMP.util.MessageUtil;
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
            sender.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cHanya pemain yang bisa menggunakan command ini."));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("open")) {
            if (!player.hasPermission("milestonemp.open")) {
                player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cTidak ada izin."));
                return true;
            }
            milestoneGUI.open(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help" -> sendHelp(player);
            
            case "claim" -> {
                if (!player.hasPermission("milestonemp.claim")) {
                    player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cTidak ada izin."));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cUsage: /milestone claim <milestone_id>"));
                    return true;
                }
                plugin.getMilestoneManager().claimMilestone(player, args[1], null);
            }
            
            case "reload" -> {
                if (!player.hasPermission("milestonemp.admin")) {
                    player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cTidak ada izin."));
                    return true;
                }
                plugin.reloadConfig();
                plugin.getConfigManager().load();
                player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + plugin.getConfigManager().getMsgConfigReloaded()));
            }
            
            case "check" -> {
                if (!player.hasPermission("milestonemp.check")) {
                    player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cTidak ada izin."));
                    return true;
                }
                var milestone = plugin.getMilestoneManager().getCurrentProgressMilestone(player);
                if (milestone != null) {
                    double percent = plugin.getMilestoneManager().getProgressPercentage(player, milestone);
                    player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + 
                        "&7Progress: &a" + String.format("%.1f", percent) + "% &7(" + milestone.getId() + ")"));
                } else {
                    player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + 
                        plugin.getConfigManager().getMsgNoMilestone()));
                }
            }
            
            default -> {
                player.sendMessage(MessageUtil.color(plugin.getConfigManager().getPrefix() + "&cUsage: /milestone [open|claim|check|help]"));
            }
        }

        return true;
    }

    private void sendHelp(Player player) {
        String prefix = plugin.getConfigManager().getPrefix();
        
        player.sendMessage(MessageUtil.color("&8&m----------------------------------------"));
        player.sendMessage(MessageUtil.color("&6&lMilestoneMP &7- Help"));
        player.sendMessage(MessageUtil.color("&8&m----------------------------------------"));
        player.sendMessage(MessageUtil.color(prefix + "&e/milestone &7- " + "&fBuka GUI milestone"));
        player.sendMessage(MessageUtil.color(prefix + "&e/milestone open &7- " + "&fBuka GUI milestone"));
        player.sendMessage(MessageUtil.color(prefix + "&e/milestone check &7- " + "&fCek progress milestone"));
        player.sendMessage(MessageUtil.color(prefix + "&e/milestone claim <id> &7- " + "&fKlaim milestone tertentu"));
        player.sendMessage(MessageUtil.color(prefix + "&e/milestone help &7- " + "&fTampilkan help ini"));
        
        if (player.hasPermission("milestonemp.admin")) {
            player.sendMessage(MessageUtil.color(prefix + "&e/milestone reload &7- " + "&fReload konfigurasi"));
        }
        
        player.sendMessage(MessageUtil.color("&8&m----------------------------------------"));
    }

    public ChoiceGUI getChoiceGUI() {
        return choiceGUI;
    }
}
