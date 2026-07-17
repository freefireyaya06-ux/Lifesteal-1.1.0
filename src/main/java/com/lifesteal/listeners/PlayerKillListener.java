package com.lifesteal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import com.lifesteal.Lifesteal;

public class PlayerKillListener implements Listener {
    
    private Lifesteal plugin;
    
    public PlayerKillListener(Lifesteal plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        // Check if killed by another player
        if (killer == null) {
            return;
        }
        
        // Don't process if killer is banned
        if (plugin.getBanManager().isBanned(killer.getName())) {
            killer.sendMessage("§c§lYou are BANNED and cannot gain hearts!");
            return;
        }
        
        // Transfer hearts
        transferHearts(killer, victim);
    }
    
    private void transferHearts(Player killer, Player victim) {
        // Victim loses 1 heart (2 health points)
        double victimHealth = victim.getHealth();
        victimHealth = Math.max(0, victimHealth - 2);
        victim.setHealth(victimHealth);
        
        // Killer gains 1 heart (heal by 2 HP, max 20)
        double killerHealth = killer.getHealth();
        killerHealth = Math.min(20, killerHealth + 2);
        killer.setHealth(killerHealth);
        
        // Send messages
        String killMessage = plugin.getConfigManager().getPlayerKillMessage()
            .replace("{PLAYER}", victim.getName());
        killer.sendMessage(killMessage);
        
        String deathMessage = "§c§lYou lost 1 heart to §b" + killer.getName() + "§c!";
        victim.sendMessage(deathMessage);
        
        // Broadcast
        Bukkit.broadcastMessage("§6" + killer.getName() + " §c❤ §6" + victim.getName() + 
            " (§e" + formatHearts(killer.getHealth()) + "§6 → §e" + formatHearts(victim.getHealth()) + "§6)");
        
        // Check if victim reached 0 hearts
        if (victim.getHealth() <= 0) {
            banVictim(victim);
        }
    }
    
    private void banVictim(Player victim) {
        plugin.getBanManager().banPlayer(victim.getName(), "Reached 0 hearts in combat");
        victim.kickPlayer(plugin.getConfigManager().getBanMessage());
        
        // Announce ban
        Bukkit.broadcastMessage("§c§l╔════════════════════════════════════╗");
        Bukkit.broadcastMessage("§c§l║  " + victim.getName() + " HAS BEEN BANNED!  ║");
        Bukkit.broadcastMessage("§c§l║  They ran out of hearts            ║");
        Bukkit.broadcastMessage("§c§l╚════════════════════════════════════╝");
    }
    
    private String formatHearts(double health) {
        int hearts = (int) (health / 2);
        return hearts + "❤";
    }
}
