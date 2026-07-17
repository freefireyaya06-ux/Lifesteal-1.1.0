package com.lifesteal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import com.lifesteal.Lifesteal;

public class HeartInteractListener implements Listener {
    
    private Lifesteal plugin;
    
    public HeartInteractListener(Lifesteal plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().toString().contains("RIGHT_CLICK")) {
            return;
        }
        
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.HEART_OF_THE_SEA) {
            return;
        }
        
        // Check if it's a heart item with our custom display name
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String displayName = item.getItemMeta().getDisplayName();
            if (displayName.contains("Heart")) {
                event.setCancelled(true);
                consumeHeart(event.getPlayer(), item);
            }
        }
    }
    
    private void consumeHeart(Player player, ItemStack heart) {
        // Check if player is banned
        if (plugin.getBanManager().isBanned(player.getName())) {
            player.sendMessage("§c§lYou are BANNED and cannot use hearts!");
            return;
        }
        
        // Check if player is at max health
        if (player.getHealth() >= 20) {
            player.sendMessage("§c§lYou are already at maximum health!");
            return;
        }
        
        // Give player 1 heart (2 health points)
        double currentHealth = player.getHealth();
        double newHealth = Math.min(20, currentHealth + 2);
        player.setHealth(newHealth);
        
        // Remove heart from inventory
        ItemStack playerHand = player.getInventory().getItemInMainHand();
        if (playerHand.getType() == Material.HEART_OF_THE_SEA) {
            playerHand.setAmount(playerHand.getAmount() - 1);
        }
        
        // Send message
        player.sendMessage("§a§l❤ Heart Consumed! §a+" + formatHearts(2) + " health");
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        
        // Show action bar
        player.sendActionBar("§a§l+" + formatHearts(2) + " ❤");
    }
    
    private String formatHearts(int health) {
        int hearts = health / 2;
        return hearts + "❤";
    }
}
