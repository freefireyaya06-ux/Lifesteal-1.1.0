package com.lifesteal;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import com.lifesteal.listeners.PlayerKillListener;
import com.lifesteal.listeners.PlayerJoinListener;
import com.lifesteal.listeners.ItemInteractListener;
import com.lifesteal.listeners.MobKillListener;
import com.lifesteal.listeners.HeartInteractListener;
import com.lifesteal.managers.BanManager;
import com.lifesteal.managers.ConfigManager;
import com.lifesteal.commands.UnbanCommand;
import com.lifesteal.commands.LifeStealCommand;
import com.lifesteal.commands.RedeemCommand;

public class Lifesteal extends JavaPlugin implements Listener {
    
    private BanManager banManager;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        getLogger().info("╔════════════════════════════════════╗");
        getLogger().info("║   LIFESTEAL PLUGIN - Loading...    ║");
        getLogger().info("╚════════════════════════════════════╝");
        
        // Initialize managers
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        banManager = new BanManager(this);
        banManager.loadBannedPlayers();
        
        // Register event listeners
        registerListeners();
        
        // Register commands
        registerCommands();
        
        // Register crafting recipes
        registerRecipes();
        
        getLogger().info("✓ Lifesteal plugin enabled successfully!");
        getLogger().info("✓ Loaded " + banManager.getBannedCount() + " banned players");
    }
    
    @Override
    public void onDisable() {
        if (banManager != null) {
            banManager.saveBannedPlayers();
        }
        getLogger().info("╔════════════════════════════════════╗");
        getLogger().info("║   LIFESTEAL PLUGIN - Disabled      ║");
        getLogger().info("╚════════════════════════════════════╝");
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(this), this);
        getServer().getPluginManager().registerEvents(new HeartInteractListener(this), this);
    }
    
    private void registerCommands() {
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("lifesteal").setExecutor(new LifeStealCommand(this));
        getCommand("redeem").setExecutor(new RedeemCommand(this));
    }
    
    private void registerRecipes() {
        try {
            // Create Heart item (crafted from diamond blocks)
            ItemStack heart = createHeartItem();
            ShapedRecipe heartRecipe = new ShapedRecipe(new NamespacedKey(this, "heart_item"), heart);
            heartRecipe.shape(
                "DDD",
                "DHD",
                "DDD"
            );
            heartRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
            heartRecipe.setIngredient('H', Material.REDSTONE_BLOCK);
            Bukkit.addRecipe(heartRecipe);
            getLogger().info("✓ Heart crafting recipe registered");
            
            // Create Unban Book
            ItemStack unbanBook = createUnbanBook();
            ShapedRecipe unbanRecipe = new ShapedRecipe(new NamespacedKey(this, "unban_book"), unbanBook);
            unbanRecipe.shape(
                "H T h",
                "CBBC",
                "H T h"
            );
            unbanRecipe.setIngredient('H', Material.HEART_OF_THE_SEA);  // Player hearts (crafted item)
            unbanRecipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
            unbanRecipe.setIngredient('C', Material.CRYING_OBSIDIAN);   // Dark block for corners
            unbanRecipe.setIngredient('B', Material.BOOK);
            Bukkit.addRecipe(unbanRecipe);
            getLogger().info("✓ Unban Book crafting recipe registered");
            
        } catch (Exception e) {
            getLogger().warning("Failed to register recipes: " + e.getMessage());
        }
    }
    
    public static ItemStack createHeartItem() {
        ItemStack heart = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = heart.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c❤ Heart");
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.setLore(java.util.Arrays.asList(
                "§7Right-click to consume and gain 1 heart",
                "§7Or use: §c/redeem heart 1"
            ));
            heart.setItemMeta(meta);
        }
        return heart;
    }
    
    private ItemStack createUnbanBook() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§d✦ Unban Book ✦");
            meta.setLore(java.util.Arrays.asList(
                "§7Right-click to unban a player",
                "§c§oWarning: Unbans cost 1 book!"
            ));
            book.setItemMeta(meta);
        }
        return book;
    }
    
    public BanManager getBanManager() {
        return banManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
