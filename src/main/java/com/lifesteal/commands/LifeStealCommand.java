package com.lifesteal.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;

import com.lifesteal.Lifesteal;
import java.util.Set;

public class LifeStealCommand implements CommandExecutor {
    
    private Lifesteal plugin;
    
    public LifeStealCommand(Lifesteal plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subcommand = args[0].toLowerCase();
        
        switch (subcommand) {
            case "reload":
                return handleReload(sender);
            case "stats":
            case "bans":
                return handleStats(sender);
            case "help":
                return handleHelp(sender);
            default:
                sendHelp(sender);
                return true;
        }
    }
    
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("lifesteal.admin")) {
            sender.sendMessage("§c§lYou don't have permission!");
            return true;
        }
        
        plugin.getConfigManager().reloadConfig();
        sender.sendMessage("§a§l✓ Configuration reloaded!");
        return true;
    }
    
    private boolean handleStats(CommandSender sender) {
        if (!sender.hasPermission("lifesteal.stats")) {
            sender.sendMessage("§c§lYou don't have permission!");
            return true;
        }
        
        Set<String> bannedPlayers = plugin.getBanManager().getBannedPlayersList();
        
        sender.sendMessage("§6§l╔════════════════════════════════════╗");
        sender.sendMessage("§6§l║      LIFESTEAL STATISTICS         ║");
        sender.sendMessage("§6§l╚════════════════════════════════════╝");
        sender.sendMessage("§aBanned Players: §e" + bannedPlayers.size());
        sender.sendMessage("");
        
        if (bannedPlayers.isEmpty()) {
            sender.sendMessage("§eNo players currently banned");
        } else {
            sender.sendMessage("§6Banned Players List:");
            int counter = 1;
            for (String player : bannedPlayers) {
                sender.sendMessage("  §e[" + counter + "] §b" + player);
                counter++;
            }
        }
        
        sender.sendMessage("");
        sender.sendMessage("§6Online Players: §e" + Bukkit.getOnlinePlayers().size() + "§6/§e" + Bukkit.getMaxPlayers());
        
        return true;
    }
    
    private boolean handleHelp(CommandSender sender) {
        sendHelp(sender);
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l╔════════════════════════════════════╗");
        sender.sendMessage("§6§l║    LIFESTEAL PLUGIN - HELP        ║");
        sender.sendMessage("§6§l╚════════════════════════════════════╝");
        sender.sendMessage("§e/lifesteal reload §6- Reload config");
        sender.sendMessage("§e/lifesteal stats §6- View ban statistics");
        sender.sendMessage("§e/lifesteal help §6- Show this help");
        sender.sendMessage("§e/unban <player> §6- Unban a player (Admin)");
        sender.sendMessage("§e/redeem heart <count> §6- Redeem hearts");
        sender.sendMessage("");
        sender.sendMessage("§d§lHeart Crafting Recipe:");
        sender.sendMessage("§7D = Diamond Block (8)");
        sender.sendMessage("§7R = Redstone Block (1)");
        sender.sendMessage("§cD D D");
        sender.sendMessage("§cD R D");
        sender.sendMessage("§cD D D");
        sender.sendMessage("");
        sender.sendMessage("§d§lUnban Book Crafting Recipe:");
        sender.sendMessage("§7H = Heart (crafted)");
        sender.sendMessage("§7T = Totem of Undying");
        sender.sendMessage("§7C = Crying Obsidian");
        sender.sendMessage("§7B = Book");
        sender.sendMessage("§cH T h");
        sender.sendMessage("§cCBBC");
        sender.sendMessage("§cH T h");
    }
}
