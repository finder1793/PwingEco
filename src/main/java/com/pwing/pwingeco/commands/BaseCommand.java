package com.pwing.pwingeco.commands;

import com.pwing.pwingeco.PwingEco;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class BaseCommand implements CommandExecutor, TabCompleter {
    protected final PwingEco plugin;

    public BaseCommand(PwingEco plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
