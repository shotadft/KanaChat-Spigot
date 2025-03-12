package net.ironingot.kanachat;

import net.ironingot.kanachat.command.KanaChatCmdTabCompleter;
import net.ironingot.kanachat.command.KanaChatCommand;
import net.ironingot.kanachat.listener.AsyncPlayerChatListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class KanaChat extends JavaPlugin {
    public static final Logger logger = Logger.getLogger("Minecraft");

    public void onEnable() {
        saveDefaultConfig();

        PluginCommand command = getCommand("kanachat");
        Objects.requireNonNull(command).setAliases(Arrays.asList("japanize", "jc"));
        Objects.requireNonNull(command).setTabCompleter(new KanaChatCmdTabCompleter());
        command.setExecutor(new KanaChatCommand(this));

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);

        logger.info(getDescription().getName() + "-" + getDescription().getVersion() + " is enabled");
    }

    public void onDisable() {
        saveConfig();
        logger.info(getDescription().getName() + " is disabled");
    }

    public Boolean getUserKanjiConversion(String name) {
        return getConfig().getBoolean("user." + name + ".kanji", true);
    }

    public void setUserKanjiConversion(String name, Boolean value) {
        getConfig().set("user." + name + ".kanji", value);
        saveConfig();
    }

    public Boolean getUserMode(String name) {
        return getConfig().getBoolean("user." + name + ".kanachat", true);
    }

    public void setUserMode(String name, Boolean value) {
        getConfig().set("user." + name + ".kanachat", value);
        saveConfig();
    }
}
