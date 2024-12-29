package me.kristywen.commandlogger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static me.kristywen.commandlogger.Utils.readFile;

public final class CommandLogger extends JavaPlugin implements Listener {
    private static CommandLogger mainPlugin;

    @Override
    public void onEnable() {
        File webhookFile = new File(getDataFolder(), "webhook.json");
        if (!webhookFile.exists()) {
            saveResource("webhook.json", false);
        }
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        mainPlugin = this;
        this.getCommand("reload").setExecutor(new Reload());
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {

    }

    public static CommandLogger getPlugin() {
        return mainPlugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        ConfigurationSection configSect = getConfig().getConfigurationSection("commands");
        for (String blockedCommand : configSect.getKeys(false)) {
            ConfigurationSection commandSect = configSect.getConfigurationSection(blockedCommand);
            if (message.contains("/"+blockedCommand)) {
                if(commandSect.getBoolean("ignore")) {
                    String ignoreMessage = getConfig().getString("messages.ignore");
                    if(ignoreMessage != "") player.sendMessage(ChatColor.translateAlternateColorCodes('&',ignoreMessage));
                    event.setCancelled(true);
                }
                String logMessage = getConfig().getString("messages.logged");
                if(logMessage != "") player.sendMessage(ChatColor.translateAlternateColorCodes('&',logMessage));
                Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                    new DiscordWebhook(commandSect.getString("webhook")).sendMessage(readFile(new File(getDataFolder() + "/webhook.json")).replaceAll("%player%",player.getName()).replaceAll("%command%",blockedCommand).replaceAll("%message%",message));
                });
                break;
            }
        }
    }

}
