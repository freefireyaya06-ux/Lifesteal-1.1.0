package com.lifesteal.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.entity.Player;

import com.lifesteal.Lifesteal;

public class PlayerJoinListener implements Listener {
    
    private Lifesteal plugin;
    
    public PlayerJoinListener(Lifesteal plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        
        // Check if player is banned
        if (plugin.getBanManager().isBanned(playerName)) {
            event.disallow(
                PlayerLoginEvent.Result.KICK_BANNED,
                "§c§l═══════════════════════════════════\n" +
                "§c§l        YOU ARE BANNED!\n" +
                "§c§l═══════════════════════════════════\n" +
                "§c§lYour hearts reached 0 in combat.\n" +
                "§c§lYou can be unbanned by:\n" +
                "§e§l1. §c§lAn admin using /unban command\n" +
                "§e§l2. §c§lSomeone using an Unban Book\n" +
                "§c§l═══════════════════════════════════"
            );
            plugin.getLogger().info("§c[BAN] Blocked login attempt from banned player: " + playerName);
        }
    }
}
