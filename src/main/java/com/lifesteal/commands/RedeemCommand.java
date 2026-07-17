package com.lifesteal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import com.lifesteal.Lifesteal;

public class RedeemCommand implements CommandExecutor {
    
    private Lifesteal plugin;
    
    public RedeemCommand(Lifesteal plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only players can use this command
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c§lOnly players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check if player is banned
        if (plugin.getBanManager().isBanned(player.getName())) {
            player.sendMessage("§c§lYou are BANNED and cannot redeem hearts!");
            return true;
        }
        
        // Check arguments
        if (args.length < 2) {
            player.sendMessage("§c§lUsage: /redeem heart <count>");
            player.sendMessage("§eExample: §c/redeem heart 3");
            return true;
        }
        
        String itemType = args[0].toLowerCase();
        
        if (!itemType.equals("heart")) {
            player.sendMessage("§c§lInvalid item type! Use: /redeem heart <count>");
            return true;
        }
        
        // Parse count
        int count;
        try {
            count = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("§c§lInvalid count! Must be a number.");
            return true;
        }
        
        if (count <= 0) {
            player.sendMessage("§c§lCount must be greater than 0!");
            return true;
        }
        
        if (count > 64) {
            player.sendMessage("§c§lYou can only redeem up to 64 hearts at once!");
            return true;
        }
        
        // Check if player has enough hearts
        int heartCount = countHearts(player);
        if (heartCount < count) {
            player.sendMessage("§c§lYou only have §e" + heartCount + "§c hearts! (Need: §e" + count + "§c)");
            return true;
        }
        
        // Check max health
        double currentHealth = player.getHealth();
        double healthGain = count * 2.0; // Each heart = 2 health points
        double maxHealth = 20.0;
        
        if (currentHealth + healthGain > maxHealth) {
            int maxRedeemable = (int) Math.floor((maxHealth - currentHealth) / 2.0);
            player.sendMessage("§c§lYou can only redeem §e" + maxRedeemable + "§c hearts (would exceed max health)");
            return true;
        }
        
        // Remove hearts from inventory
        int remaining = count;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != Material.HEART_OF_THE_SEA) {
                continue;
            }
            
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String displayName = item.getItemMeta().getDisplayName();
                if (displayName.contains("Heart")) {
                    int toRemove = Math.min(remaining, item.getAmount());
                    item.setAmount(item.getAmount() - toRemove);
                    remaining -= toRemove;
                    
                    if (remaining <= 0) break;
                }
            }
        }
        
        // Apply health
        double newHealth = Math.min(maxHealth, currentHealth + healthGain);
        player.setHealth(newHealth);
        
        // Send success messages
        player.sendMessage("§a§l╔════════════════════════════════════╗");
        player.sendMessage("§a§l║   HEARTS REDEEMED SUCCESSFULLY!   ║");
        player.sendMessage("§a§l║   " + count + " hearts consumed                 ║");
        player.sendMessage("§a§l║   +" + (count) + "❤ health gained              ║");
        player.sendMessage("§a§l╚════════════════════════════════════╝");
        
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendActionBar("§a§l+" + count + "❤ Redeemed!");
        
        return true;
    }
    
    private int countHearts(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != Material.HEART_OF_THE_SEA) {
                continue;
            }
            
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String displayName = item.getItemMeta().getDisplayName();
                if (displayName.contains("Heart")) {
                    count += item.getAmount();
                }
            }
        }
        return count;
    }
}
