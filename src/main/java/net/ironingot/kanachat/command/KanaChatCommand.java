package net.ironingot.kanachat.command;

import net.ironingot.kanachat.KanaChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public record KanaChatCommand(KanaChat plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String commandLabel, @Nonnull String[] args) {
        if (sender instanceof Player && ((Player) sender).isOnline()) {
            String command;
            String option;

            command = (args.length >= 1) ? args[0].toLowerCase() : "get";
            option = (args.length >= 2) ? args[1].toLowerCase() : null;

            return executeCommand(sender, command, option);
        } else return false;
    }

    private boolean executeCommand(CommandSender sender, String command, String option) {
        String pluginName = plugin.getDescription().getName();
        String pluginVersion = plugin.getDescription().getVersion();

        switch (command) {
            case "version" -> {
                sender.sendMessage(ChatColor.GOLD + pluginName + "-" + pluginVersion);
                return true;
            }
            case "kanji" -> {
                switch (option) {
                    case "on", "true" -> plugin.getConfigHandler().setUserKanjiConversion(sender.getName(), Boolean.TRUE);
                    case "off", "false" -> plugin.getConfigHandler().setUserKanjiConversion(sender.getName(), Boolean.FALSE);
                    case null, default -> { return false; }
                }

                if (plugin.getConfigHandler().getUserKanjiConversion(sender.getName())) {
                    sender.sendMessage(ChatColor.GOLD + pluginName + " Kanji conversion is enabled.");
                } else {
                    sender.sendMessage(ChatColor.GOLD + pluginName + " Kanji conversion is disabled.");
                }
                return true;
            }
            case "on", "true" -> plugin.getConfigHandler().setUserMode(sender.getName(), Boolean.TRUE);
            case "off", "false" -> plugin.getConfigHandler().setUserMode(sender.getName(), Boolean.FALSE);
            case null, default -> { return false; }
        }

        if (plugin.getConfigHandler().getUserMode(sender.getName())) {
            sender.sendMessage(ChatColor.GOLD + pluginName + " is enabled.");
        } else {
            sender.sendMessage(ChatColor.GOLD + pluginName + " is disabled.");
        }
        return true;
    }
}