package com.pwing.pwingeco.commands.admin;

import com.pwing.pwingeco.PwingEco;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveCommand implements CommandExecutor {
    private final PwingEco plugin;

    public SaveCommand(PwingEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.saveConfiguration();
        sender.sendMessage("PwingEco configuration saved.");
        return true;
    }
}
