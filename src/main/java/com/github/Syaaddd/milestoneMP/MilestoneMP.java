package com.github.Syaaddd.milestoneMP;

import com.github.Syaaddd.milestoneMP.command.MilestoneCommand;
import com.github.Syaaddd.milestoneMP.config.ConfigManager;
import com.github.Syaaddd.milestoneMP.data.DatabaseManager;
import com.github.Syaaddd.milestoneMP.data.MilestoneRepository;
import com.github.Syaaddd.milestoneMP.gui.ChoiceGUI;
import com.github.Syaaddd.milestoneMP.listener.EventListeners;
import com.github.Syaaddd.milestoneMP.listener.PlaytimeTracker;
import com.github.Syaaddd.milestoneMP.milestone.MilestoneManager;
import com.github.Syaaddd.milestoneMP.placeholder.PlaceholderHook;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MilestoneMP extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private MilestoneRepository repository;
    private MilestoneManager milestoneManager;
    private MilestoneCommand milestoneCommand;
    private PlaytimeTracker playtimeTracker;
    private PlaceholderHook placeholderHook;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.load();

        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();

        repository = new MilestoneRepository(this);

        milestoneManager = new MilestoneManager(this);

        milestoneCommand = new MilestoneCommand(this);
        getCommand("milestone").setExecutor(milestoneCommand);

        getServer().getPluginManager().registerEvents(new EventListeners(this), this);

        getServer().getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getView().getTitle().contains("Pilih Reward")) {
                    event.setCancelled(true);
                    if (event.getWhoClicked() instanceof org.bukkit.entity.Player player) {
                        String title = event.getView().getTitle();
                        String milestoneId = title.replace("&8Pilih Reward - ", "").replace("§8Pilih Reward - ", "");
                        milestoneCommand.getChoiceGUI().handleChoice(player, milestoneId, event.getSlot());
                    }
                }
            }
        }, this);

        playtimeTracker = new PlaytimeTracker(this);
        playtimeTracker.start();

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderHook = new PlaceholderHook(this);
            placeholderHook.register();
            getLogger().info("PlaceholderAPI integration enabled.");
        }

        getLogger().info("MilestoneMP enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (repository != null) {
            repository.saveAll();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("MilestoneMP disabled.");
    }

    public ConfigManager getConfigManager() { return configManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public MilestoneRepository getRepository() { return repository; }
    public MilestoneManager getMilestoneManager() { return milestoneManager; }
}
