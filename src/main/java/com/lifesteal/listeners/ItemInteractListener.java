package com.lifesteal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import com.lifesteal.Lifesteal;
import java.util.*;

public class ItemInteractListener implements Listener {
    
    private Lifesteal plugin;
    // Map to store unban selections temporarily
    private Map<String, List<String>> unbanSelections = new HashMap<>();
    
    public ItemInteractListener(Lifesteal plugin) {
        this.plugin = plugin;
        
        // Listen for chat messages
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onAsyncChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
                handleUnbanSelection(event.getPlayer(), event.getMessage());
                event.setCancelled(true);
            }
        }, plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().toString().contains("RIGHT_CLICK")) {
            return;
        }
        
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.BOOK) {
            return;
        }
        
        // Check if it's the Unban Book
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String displayName = item.getItemMeta().getDisplayName();
            if (displayName.contains("Unban Book")) {
                event.setCancelled(true);
                handleUnbanBook(event.getPlayer(), item);
            }
        }
    }
    
    private void handleUnbanBook(Player player, ItemStack book) {
        Set<String> bannedPlayers = plugin.getBanManager().getBannedPlayersList();
        
        if (bannedPlayers.isEmpty()) {
            player.sendMessage("§6§l[Unban Book] §cNo players are currently banned!");
            return;
        }
        
        // Send header
        player.sendMessage("§6§l╔════════════════════════════════════╗");
        player.sendMessage("§6§l║      UNBAN BOOK USED               ║");
        player.sendMessage("§6§l╚════════════════════════════════════╝");
        player.sendMessage("§6§lSelect which player to unban:");
        player.sendMessage("");
        
        // Display banned players with numbers
        List<String> bannedList = new ArrayList<>(bannedPlayers);
        Collections.sort(bannedList);
        
        int counter = 1;
        for (String bannedPlayer : bannedList) {
            player.sendMessage("§e[" + counter + "] §b" + bannedPlayer);
            counter++;
        }
        
        player.sendMessage("");
        player.sendMessage("§6Type the number in chat to unban (e.g., §e1§6)");
        
        // Store the banned list for this player
        unbanSelections.put(player.getName(), bannedList);
        
        // Remove the book
        ItemStack playerHand = player.getInventory().getItemInMainHand();
        if (playerHand.getType() == Material.BOOK) {
            playerHand.setAmount(playerHand.getAmount() - 1);
        }
    }
    
    private void handleUnbanSelection(Player player, String message) {
        if (!unbanSelections.containsKey(player.getName())) {
            return;
        }
        
        try {
            int selection = Integer.parseInt(message.trim());
            List<String> bannedList = unbanSelections.get(player.getName());
            
            if (selection < 1 || selection > bannedList.size()) {
                player.sendMessage("§c§lInvalid selection! Number out of range.");
                return;
            }
            
            String targetPlayer = bannedList.get(selection - 1);
            
            // Unban the player
            plugin.getBanManager().unbanPlayer(targetPlayer);
            
            // Restore player with hearts
            Player unbannedPlayer = Bukkit.getPlayer(targetPlayer);
            if (unbannedPlayer != null) {
                int unbanHearts = plugin.getConfigManager().getUnbanHearts();
                unbannedPlayer.setHealth(Math.min(20, unbanHearts * 2.0)); // Convert hearts to health
                unbannedPlayer.sendMessage(plugin.getConfigManager().getUnbanMessage());
            }
            
            // Send messages
            player.sendMessage("§a§l╔════════════════════════════════════╗");
            player.sendMessage("§a§l║   PLAYER UNBANNED SUCCESSFULLY!   ║");
            player.sendMessage("§a§l║   " + targetPlayer + " is now unbanned!     ║");
            player.sendMessage("§a§l╚════════════════════════════════════╝");
            
            Bukkit.broadcastMessage("§a§l" + targetPlayer + " §ahas been §aUNBANNED §aby " + player.getName());
            
            // Clean up
            unbanSelections.remove(player.getName());
            
        } catch (NumberFormatException e) {
            player.sendMessage("§c§lPlease type a valid number!");
        }
    }
}
