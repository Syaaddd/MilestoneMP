package com.github.Syaaddd.milestoneMP.config;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.milestone.Milestone;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneChoice;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneType;
import com.github.Syaaddd.milestoneMP.util.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.Arrays;
import java.util.Arrays;

public class ConfigManager {

    private final MilestoneMP plugin;
    private FileConfiguration config;

    private String databaseType;
    private String dbHost;
    private int dbPort;
    private String dbDatabase;
    private String dbUsername;
    private String dbPassword;

    private int checkInterval;
    private boolean communityRewardBroadcast;

    private String prefix;
    private String msgMilestoneAvailable;
    private String msgMilestoneLocked;
    private String msgMilestoneClaimed;
    private String msgNoMilestone;
    private String msgPlayerNotFound;
    private String msgConfigReloaded;
    private String msgNoPermission;
    private String msgAlreadyClaimed;
    private String msgMilestoneClaimedSelf;

    private String guiTitle;
    private String availableColor;
    private String lockedColor;
    private String claimedColor;
    private String claimButton;
    private String chooseButton;
    private int[] milestoneSlots;

    private Map<String, Milestone> milestones;

    public ConfigManager(MilestoneMP plugin) {
        this.plugin = plugin;
        this.milestones = new LinkedHashMap<>();
    }

    public void load() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        databaseType = config.getString("database.type", "sqlite");
        dbHost = config.getString("database.host", "localhost");
        dbPort = config.getInt("database.port", 3306);
        dbDatabase = config.getString("database.database", "milestoneMP");
        dbUsername = config.getString("database.username", "root");
        dbPassword = config.getString("database.password", "");

        checkInterval = config.getInt("settings.check-interval", 60);
        communityRewardBroadcast = config.getBoolean("settings.community-reward-broadcast", true);

        prefix = config.getString("messages.prefix", "&8[&6Milestone&8] ");
        msgMilestoneAvailable = config.getString("messages.milestone-available", "&aMilestone available! Click to claim.");
        msgMilestoneLocked = config.getString("messages.milestone-locked", "&cThis milestone is still locked.");
        msgMilestoneClaimed = config.getString("messages.milestone-claimed", "&eReward claimed: %reward%");
        msgNoMilestone = config.getString("messages.no-milestone", "&cNo milestone available.");
        msgPlayerNotFound = config.getString("messages.player-not-found", "&cPlayer not found.");
        msgConfigReloaded = config.getString("messages.config-reloaded", "&aConfiguration reloaded successfully.");
        msgNoPermission = config.getString("messages.no-permission", "&cYou don't have permission.");
        msgAlreadyClaimed = config.getString("messages.already-claimed", "&cThis milestone has already been claimed.");
        msgMilestoneClaimedSelf = config.getString("messages.milestone-claimed-self", "&eYou claimed: %reward%");

        guiTitle = config.getString("gui.title", "&8Progression Tree");
        availableColor = config.getString("gui.available-color", "&a");
        lockedColor = config.getString("gui.locked-color", "&7");
        claimedColor = config.getString("gui.claimed-color", "&e");
        claimButton = config.getString("gui.claim-button", "&aClick to Claim");
        chooseButton = config.getString("gui.choose-button", "&eChoose Reward");
        
        List<Integer> slotList = config.getIntegerList("gui.milestone-slots");
        if (slotList.isEmpty()) {
            slotList = Arrays.asList(10, 12, 14, 16, 19, 21, 23, 25, 28, 30, 32, 34);
        }
        milestoneSlots = slotList.stream().mapToInt(Integer::intValue).toArray();

        loadMilestones();
    }

    private void loadMilestones() {
        milestones.clear();
        ConfigurationSection milestonesSection = config.getConfigurationSection("milestones");
        if (milestonesSection == null) return;

        for (String key : milestonesSection.getKeys(false)) {
            ConfigurationSection ms = milestonesSection.getConfigurationSection(key);
            if (ms == null) continue;

            String typeStr = ms.getString("type", "PLAYTIME");
            MilestoneType type;
            try {
                type = MilestoneType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                type = MilestoneType.PLAYTIME;
            }

            int amount = ms.getInt("amount", 3600);
            String icon = ms.getString("icon", "");
            String displayColor = ms.getString("color", "&6");

            List<MilestoneChoice> choices = new ArrayList<>();
            ConfigurationSection choicesSection = ms.getConfigurationSection("choices");
            if (choicesSection != null) {
                for (String choiceKey : choicesSection.getKeys(false)) {
                    ConfigurationSection choice = choicesSection.getConfigurationSection(choiceKey);
                    if (choice == null) continue;

                    String id = choice.getString("id", choiceKey);
                    String name = choice.getString("name", "Reward");
                    String command = choice.getString("command", "");
                    choices.add(new MilestoneChoice(id, name, command));
                }
            }

            Milestone milestone = new Milestone(key, type, amount, choices, icon, displayColor);
            milestones.put(key, milestone);
        }
    }

    public List<Milestone> getMilestonesInOrder() {
        return new ArrayList<>(milestones.values());
    }

    public Milestone getMilestone(String id) {
        return milestones.get(id);
    }

    public String getDatabaseType() { return databaseType; }
    public String getDbHost() { return dbHost; }
    public int getDbPort() { return dbPort; }
    public String getDbDatabase() { return dbDatabase; }
    public String getDbUsername() { return dbUsername; }
    public String getDbPassword() { return dbPassword; }
    public int getCheckInterval() { return checkInterval; }
    public boolean isCommunityRewardBroadcast() { return communityRewardBroadcast; }
    public String getPrefix() { return MessageUtil.color(prefix); }
    public String getMsgMilestoneAvailable() { return MessageUtil.color(msgMilestoneAvailable); }
    public String getMsgMilestoneLocked() { return MessageUtil.color(msgMilestoneLocked); }
    public String getMsgMilestoneClaimed() { return MessageUtil.color(msgMilestoneClaimed); }
    public String getMsgNoMilestone() { return MessageUtil.color(msgNoMilestone); }
    public String getMsgPlayerNotFound() { return MessageUtil.color(msgPlayerNotFound); }
    public String getMsgConfigReloaded() { return MessageUtil.color(msgConfigReloaded); }
    public String getMsgNoPermission() { return MessageUtil.color(msgNoPermission); }
    public String getMsgAlreadyClaimed() { return MessageUtil.color(msgAlreadyClaimed); }
    public String getMsgMilestoneClaimedSelf() { return MessageUtil.color(msgMilestoneClaimedSelf); }
    public String getGuiTitle() { return MessageUtil.color(guiTitle); }
    public String getAvailableColor() { return MessageUtil.color(availableColor); }
    public String getLockedColor() { return MessageUtil.color(lockedColor); }
    public String getClaimedColor() { return MessageUtil.color(claimedColor); }
    public String getClaimButton() { return MessageUtil.color(claimButton); }
    public String getChooseButton() { return MessageUtil.color(chooseButton); }
    public int[] getMilestoneSlots() { return milestoneSlots; }
}
