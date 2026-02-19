package com.github.Syaaddd.milestoneMP.config;

import com.github.Syaaddd.milestoneMP.MilestoneMP;
import com.github.Syaaddd.milestoneMP.milestone.Milestone;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneChoice;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
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
        msgMilestoneAvailable = config.getString("messages.milestone-available", "&aMilestone tersedia! Klik untuk klaim.");
        msgMilestoneLocked = config.getString("messages.milestone-locked", "&cMilestone ini masih terkunci.");
        msgMilestoneClaimed = config.getString("messages.milestone-claimed", "&eReward berhasil diklaim: %reward%");
        msgNoMilestone = config.getString("messages.no-milestone", "&cTidak ada milestone yang tersedia.");
        msgPlayerNotFound = config.getString("messages.player-not-found", "&cPemain tidak ditemukan.");
        msgConfigReloaded = config.getString("messages.config-reloaded", "&aKonfigurasi berhasil dimuat ulang.");

        guiTitle = config.getString("gui.title", "&8Progression Tree");
        availableColor = config.getString("gui.available-color", "&a");
        lockedColor = config.getString("gui.locked-color", "&7");
        claimedColor = config.getString("gui.claimed-color", "&e");
        claimButton = config.getString("gui.claim-button", "&aKlik untuk Klaim");
        chooseButton = config.getString("gui.choose-button", "&ePilih Reward");
        
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

            Milestone milestone = new Milestone(key, type, amount, choices);
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
    public String getPrefix() { return prefix; }
    public String getMsgMilestoneAvailable() { return msgMilestoneAvailable; }
    public String getMsgMilestoneLocked() { return msgMilestoneLocked; }
    public String getMsgMilestoneClaimed() { return msgMilestoneClaimed; }
    public String getMsgNoMilestone() { return msgNoMilestone; }
    public String getMsgPlayerNotFound() { return msgPlayerNotFound; }
    public String getMsgConfigReloaded() { return msgConfigReloaded; }
    public String getGuiTitle() { return guiTitle; }
    public String getAvailableColor() { return availableColor; }
    public String getLockedColor() { return lockedColor; }
    public String getClaimedColor() { return claimedColor; }
    public String getClaimButton() { return claimButton; }
    public String getChooseButton() { return chooseButton; }
    public int[] getMilestoneSlots() { return milestoneSlots; }
}
