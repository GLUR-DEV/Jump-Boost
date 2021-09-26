package me.adamlawrence.jumpboost;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public final class JumpBoost extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Creating/Generating config file
        Bukkit.getServer().getLogger().info("Plugin is enabled");
        getConfig().options().copyDefaults();
        saveDefaultConfig();


        // Registering Listeners
        getServer().getPluginManager().registerEvents(this , this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getServer().getLogger().info("Plugin is disabled");
        this.saveConfig();

    }

    // Create Hash map for storing players cooldowns
    Map<String, Long> cooldown = new HashMap<>();

    // Defining Variables
    @EventHandler
    public void onKeyPress(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack i = e.getItem();

        //Cooldown added here:
        if (cooldown.containsKey(p.getName())) {
            // player inside hashmap
            if (cooldown.get(p.getName()) > System.currentTimeMillis()) {
                // This means they still have a cooldown
                long timeleft = (cooldown.get(p.getName()) - System.currentTimeMillis()) / 1000;
                p.sendMessage(ChatColor.RED + "You will be able to use JumpBoost in " + timeleft + " second(s)");
                return;
            }
        }

        // Giving potion effect
        if (i.getType().equals(Material.FEATHER)) {
            if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                int JumpMult = this.getConfig().getInt("JumpMult");
                int BoostLength = this.getConfig().getInt("BoostLength");
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, BoostLength, JumpMult));
                // Putting cooldown on player
                int CooldownLength = this.getConfig().getInt("CooldownLength");
                cooldown.put(p.getName(), System.currentTimeMillis() + (CooldownLength));
            }

        }

    }
}


