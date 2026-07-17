package com.lifesteal.managers;

import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;

public class BanManager {
    
    private Plugin plugin;
    private File bannedPlayersFile;
    private FileConfiguration bannedPlayersConfig;
    private Set<String> bannedPlayers;
    
    public BanManager(Plugin plugin) {
        this.plugin = plugin;
        this.bannedPlayers = new HashSet<>();
        
        // Create data folder if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        this.bannedPlayersFile = new File(plugin.getDataFolder(), "banned_players.yml");
    }
    
    public void loadBannedPlayers() {
        if (!bannedPlayersFile.exists()) {
            try {
                bannedPlayersFile.createNewFile();
                saveDefaultBannedFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create banned_players.yml: " + e.getMessage());
            }
        }
        
        bannedPlayersConfig = YamlConfiguration.loadConfiguration(bannedPlayersFile);
        
        if (bannedPlayersConfig.contains("banned_players")) {
            Set<String> keys = bannedPlayersConfig.getConfigurationSection("banned_players").getKeys(false);
            bannedPlayers.clear();
            bannedPlayers.addAll(keys);
            plugin.getLogger().info("Loaded " + bannedPlayers.size() + " banned players");
        }
    }
    
    public void saveBannedPlayers() {
        try {
            bannedPlayersConfig.save(bannedPlayersFile);
            plugin.getLogger().info("Banned players data saved");
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save banned_players.yml: " + e.getMessage());
        }
    }
    
    public void banPlayer(String playerName, String reason) {
        bannedPlayers.add(playerName);
        
        if (!bannedPlayersConfig.contains("banned_players")) {
            bannedPlayersConfig.createSection("banned_players");
        }
        
        String timestamp = LocalDateTime.now().toString();
        bannedPlayersConfig.set("banned_players." + playerName + ".banned_at", timestamp);
        bannedPlayersConfig.set("banned_players." + playerName + ".reason", reason);
        bannedPlayersConfig.set("banned_players." + playerName + ".hearts", 0);
        
        saveBannedPlayers();
        plugin.getLogger().info("§c[LIFESTEAL] " + playerName + " has been BANNED - Reason: " + reason);
    }
    
    public void unbanPlayer(String playerName) {
        if (bannedPlayersConfig.contains("banned_players." + playerName)) {
            bannedPlayersConfig.set("banned_players." + playerName, null);
            bannedPlayers.remove(playerName);
            saveBannedPlayers();
            plugin.getLogger().info("§a[LIFESTEAL] " + playerName + " has been UNBANNED");
        }
    }
    
    public boolean isBanned(String playerName) {
        return bannedPlayers.contains(playerName);
    }
    
    public Set<String> getBannedPlayersList() {
        return new HashSet<>(bannedPlayers);
    }
    
    public int getBannedCount() {
        return bannedPlayers.size();
    }
    
    private void saveDefaultBannedFile() {
        bannedPlayersConfig = new YamlConfiguration();
        bannedPlayersConfig.createSection("banned_players");
        try {
            bannedPlayersConfig.save(bannedPlayersFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not create default banned_players.yml");
        }
    }
}
