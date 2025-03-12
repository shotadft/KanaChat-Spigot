package net.ironingot.kanachat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class KanaChatCommand implements CommandExecutor {
    private final KanaChat plugin;
    private final String pluginName;
    private final String pluginVersion;

    public KanaChatCommand(KanaChat plugin){
        this.plugin = plugin;
        this.pluginName = plugin.getDescription().getName();
        this.pluginVersion = plugin.getDescription().getVersion();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String commandLabel, String[] args) {
        String command;
        String option;

        command = (args.length >= 1) ? args[0].toLowerCase() : "get";
        option  = (args.length >= 2) ? args[1].toLowerCase() : null;

        return executeCommand(sender, command, option);
    }

    private boolean executeCommand(CommandSender sender, String command, String option) {
        switch (command) {
            case "version" -> {
                sender.sendMessage(ChatColor.GOLD + this.pluginName + "-" + this.pluginVersion);
                return true;
            }
            case "kanji" -> {
                if ("on".equals(option) || "true".equals(option)) {
                    plugin.setUserKanjiConversion(sender.getName(), true);
                } else if ("off".equals(option) || "false".equals(option)) {
                    plugin.setUserKanjiConversion(sender.getName(), false);
                }

                if (plugin.getUserKanjiConversion(sender.getName())) {
                    sender.sendMessage(ChatColor.GOLD + pluginName + " Kanji conversion is enabled.");
                } else {
                    sender.sendMessage(ChatColor.GOLD + pluginName + " Kanji conversion is disabled.");
                }
                return true;
            }
            case "on", "true" -> plugin.setUserMode(sender.getName(), true);
            case "off", "false" -> plugin.setUserMode(sender.getName(), false);
            default -> { return false; }
        }

        if (plugin.getUserMode(sender.getName())) {
            sender.sendMessage(ChatColor.GOLD + pluginName + " is enabled.");
        } else {
            sender.sendMessage(ChatColor.GOLD + pluginName + " is disabled.");
        }
        return true;
    }
}