package com.lifesteal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import com.lifesteal.Lifesteal;

public class UnbanCommand implements CommandExecutor {
    
    private Lifesteal plugin;
    
    public UnbanCommand(Lifesteal plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("lifesteal.unban") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage("§c§lYou don't have permission to use this command!");
            return true;
        }
        
        if (args.length < 1) {
            sender.sendMessage("§c§lUsage: /unban <player>");
            return true;
        }
        
        String playerName = args[0];
        
        // Check if player exists in ban list
        if (!plugin.getBanManager().isBanned(playerName)) {
            sender.sendMessage("§c§l" + playerName + " is not banned!");
            return true;
        }
        
        // Unban the player
        plugin.getBanManager().unbanPlayer(playerName);
        
        // Try to restore the player if online
        Player unbannedPlayer = Bukkit.getPlayer(playerName);
        if (unbannedPlayer != null) {
            int unbanHearts = plugin.getConfigManager().getUnbanHearts();
            unbannedPlayer.setHealth(Math.min(20, unbanHearts * 2.0));
            unbannedPlayer.sendMessage(plugin.getConfigManager().getUnbanMessage());
        }
        
        // Send confirmation
        sender.sendMessage("§a§l╔════════════════════════════════════╗");
        sender.sendMessage("§a§l║   " + playerName + " HAS BEEN UNBANNED!    ║");
        sender.sendMessage("§a§l╚════════════════════════════════════╝");
        
        Bukkit.broadcastMessage("§a§l" + playerName + " §ahas been §aUNBANNED §aby " + sender.getName());
        
        return true;
    }
}
