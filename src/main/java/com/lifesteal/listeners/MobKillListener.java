package com.lifesteal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

import com.lifesteal.Lifesteal;
import java.util.Random;

public class MobKillListener implements Listener {
    
    private Lifesteal plugin;
    private Random random;
    
    public MobKillListener(Lifesteal plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        
        // Check if killed by a player
        if (killer == null) {
            return;
        }
        
        // Check if killer is banned
        if (plugin.getBanManager().isBanned(killer.getName())) {
            return;
        }
        
        // Only process monster kills
        if (!(entity instanceof Monster)) {
            return;
        }
        
        // Get the mob kill chance from config
        double chance = plugin.getConfigManager().getMobKillChance();
        
        // Random chance to give heart
        if (random.nextDouble() < chance) {
            giveLuckyHeart(killer);
        }
    }
    
    private void giveLuckyHeart(Player player) {
        // Give 1 heart (2 health points)
        double currentHealth = player.getHealth();
        double newHealth = Math.min(20, currentHealth + 2);
        player.setHealth(newHealth);
        
        // Send message
        String luckMessage = "§a§l✦ Lucky! §aYou gained a heart from a mob!";
        player.sendMessage(luckMessage);
        
        // Play sound effect
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }
}
