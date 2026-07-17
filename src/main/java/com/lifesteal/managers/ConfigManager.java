package com.lifesteal.managers;

import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    
    private Plugin plugin;
    private FileConfiguration config;
    
    // Configuration values
    private double mobKillChance;
    private String banMessage;
    private String unbanMessage;
    private String playerKillMessage;
    private int unbanHearts;
    
    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        // Create default config if it doesn't exist
        plugin.saveDefaultConfig();
        
        // Load the configuration
        config = plugin.getConfig();
        
        // Load values from config with defaults
        mobKillChance = config.getDouble("settings.mob-kill-chance", 0.15);
        banMessage = config.getString("messages.ban-message", "§c§lYou have been BANNED! Your hearts reached 0.");
        unbanMessage = config.getString("messages.unban-message", "§a§lYou have been UNBANNED! You now have 4 hearts.");
        playerKillMessage = config.getString("messages.player-kill-message", "§6You killed §b{PLAYER}§6 and gained 1 heart!");
        unbanHearts = config.getInt("settings.unban-hearts", 4);
        
        plugin.getLogger().info("✓ Configuration loaded");
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }
    
    public double getMobKillChance() {
        return mobKillChance;
    }
    
    public String getBanMessage() {
        return banMessage;
    }
    
    public String getUnbanMessage() {
        return unbanMessage;
    }
    
    public String getPlayerKillMessage() {
        return playerKillMessage;
    }
    
    public int getUnbanHearts() {
        return unbanHearts;
    }
}
