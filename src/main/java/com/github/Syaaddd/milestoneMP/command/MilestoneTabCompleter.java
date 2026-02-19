package com.github.Syaaddd.milestoneMP.command;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MilestoneTabCompleter implements TabCompleter {

    private final MilestoneMP plugin;

    public MilestoneTabCompleter(MilestoneMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull org.bukkit.command.CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();
            subCommands.add("open");
            subCommands.add("claim");
            subCommands.add("check");
            subCommands.add("help");
            
            if (sender.hasPermission("milestonemp.admin")) {
                subCommands.add("reload");
            }

            String partial = args[0].toLowerCase();
            for (String subCmd : subCommands) {
                if (subCmd.startsWith(partial)) {
                    completions.add(subCmd);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("claim")) {
            var milestones = plugin.getConfigManager().getMilestonesInOrder();
            String partial = args[1].toLowerCase();
            for (var milestone : milestones) {
                if (milestone.getId().toLowerCase().startsWith(partial)) {
                    completions.add(milestone.getId());
                }
            }
        }

        return completions;
    }
}
