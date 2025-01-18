package com.pwing.pwingeco.commands.admin;

import com.pwing.pwingeco.PwingEco;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final PwingEco plugin;

    public ReloadCommand(PwingEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfiguration();
            sender.sendMessage("PwingEco configuration reloaded.");
            return true;
        }
        return false;
    }
}
