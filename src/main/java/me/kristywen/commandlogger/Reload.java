package me.kristywen.commandlogger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.kristywen.commandlogger.CommandLogger.getPlugin;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length == 0) {
                    getPlugin().reloadConfig();
                    p.sendMessage("§aThe config file has reloaded.");
                }
            } else {
                p.sendMessage("§cYou don't use the command!");
            }
            return true;
        }
    }
}
