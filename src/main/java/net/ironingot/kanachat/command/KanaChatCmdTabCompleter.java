package net.ironingot.kanachat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class KanaChatCmdTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return switch (strings.length) {
            case 1 -> List.of("on", "off", "kanji", "version");
            case 2 -> {
                if (strings[0].equalsIgnoreCase("kanji")) {
                    yield List.of("on", "off");
                } else if (strings[0].equalsIgnoreCase("on") || strings[0].equalsIgnoreCase("off")) {
                    yield List.of();
                } else {
                    yield List.of("true", "false");
                }
            }
            default -> Collections.emptyList();
        };
    }
}
