package com.github.Syaaddd.milestoneMP.gui;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.milestone.Milestone;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneChoice;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ChoiceGUI {

    private final MilestoneMP plugin;

    public ChoiceGUI(MilestoneMP plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, String milestoneId) {
        Milestone milestone = plugin.getConfigManager().getMilestone(milestoneId);
        if (milestone == null || !milestone.hasChoices()) {
            plugin.getMilestoneManager().claimMilestone(player, milestoneId, null);
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, 
            "&8Pilih Reward - " + milestone.getId());

        int slot = 10;
        for (MilestoneChoice choice : milestone.getChoices()) {
            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();
            
            meta.setDisplayName("&e" + choice.getName());
            
            List<String> lore = new ArrayList<>();
            lore.add("&7Klik untuk klaim reward ini");
            lore.add("&8Command: " + choice.getCommand());
            meta.setLore(lore);
            
            item.setItemMeta(meta);
            inv.setItem(slot, item);
            
            slot += 2;
            if (slot % 9 == 8) {
                slot += 2;
            }
        }

        player.openInventory(inv);
    }

    public void handleChoice(Player player, String milestoneId, int slot) {
        Milestone milestone = plugin.getConfigManager().getMilestone(milestoneId);
        if (milestone == null || !milestone.hasChoices()) return;

        List<MilestoneChoice> choices = milestone.getChoices();
        
        int[] slots = {10, 12, 14, 16, 19, 21, 23, 25};
        
        for (int i = 0; i < choices.size(); i++) {
            if (slot == slots[i]) {
                MilestoneChoice choice = choices.get(i);
                player.closeInventory();
                plugin.getMilestoneManager().claimMilestone(player, milestoneId, choice.getId());
                return;
            }
        }
    }
}
