package com.github.Syaaddd.milestoneMP.gui;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.data.PlayerData;
import com.github.Syaaddd.milestoneMP.milestone.Milestone;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class MilestoneGUI {

    private final MilestoneMP plugin;

    public MilestoneGUI(MilestoneMP plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, 
            plugin.getConfigManager().getGuiTitle());

        PlayerData data = plugin.getRepository().getPlayerData(player.getUniqueId());
        List<Milestone> milestones = plugin.getConfigManager().getMilestonesInOrder();
        MilestoneManager manager = plugin.getMilestoneManager();

        int slot = 10;
        
        for (Milestone milestone : milestones) {
            boolean claimed = data != null && data.hasClaimed(milestone.getId());
            boolean available = data != null && manager.hasReached(data, milestone);

            ItemStack item = createMilestoneItem(milestone, claimed, available, data);
            inv.setItem(slot, item);

            slot += 2;
            if (slot % 9 == 8) {
                slot += 2;
            }
        }

        player.openInventory(inv);
    }

    private ItemStack createMilestoneItem(Milestone milestone, boolean claimed, boolean available, PlayerData data) {
        ItemStack item;
        
        if (claimed) {
            item = new ItemStack(Material.GOLD_BLOCK);
        } else if (available) {
            item = new ItemStack(Material.LIME_STAINED_GLASS);
        } else {
            item = new ItemStack(Material.GRAY_STAINED_GLASS);
        }

        ItemMeta meta = item.getItemMeta();
        String color = claimed ? plugin.getConfigManager().getClaimedColor() :
                       (available ? plugin.getConfigManager().getAvailableColor() :
                        plugin.getConfigManager().getLockedColor());

        String typeStr = milestone.getType().name().replace("_", " ");
        String amountStr = formatAmount(milestone.getAmount(), milestone.getType());

        meta.setDisplayName(color + milestone.getId());
        
        List<String> lore = new ArrayList<>();
        lore.add("&7Tipe: " + typeStr);
        lore.add("&7Target: " + amountStr);
        
        if (claimed) {
            lore.add("&a&l✓ Sudah diklaim");
            String choiceId = data.getClaimedChoice(milestone.getId());
            if (choiceId != null) {
                final String finalChoiceId = choiceId;
                var choice = milestone.getChoices().stream()
                    .filter(c -> c.getId().equals(finalChoiceId))
                    .findFirst()
                    .orElse(null);
                if (choice != null) {
                    lore.add("&eReward: " + choice.getName());
                }
            }
        } else if (available) {
            lore.add("&a" + plugin.getConfigManager().getClaimButton());
            if (milestone.hasChoices()) {
                lore.add("&e" + plugin.getConfigManager().getChooseButton());
            }
        } else {
            lore.add("&cTerkunci");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private String formatAmount(int amount, com.github.Syaaddd.milestoneMP.milestone.MilestoneType type) {
        return switch (type) {
            case PLAYTIME -> formatTime(amount);
            default -> String.valueOf(amount);
        };
    }

    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        if (hours > 0) {
            return hours + " jam" + (minutes > 0 ? " " + minutes + " menit" : "");
        }
        return minutes + " menit";
    }
}
