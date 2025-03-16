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
    private final ConfigHandler configHandler = new ConfigHandler(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PluginCommand command = getCommand("kanachat");
        Objects.requireNonNull(command).setAliases(Arrays.asList("japanize", "jc"));
        Objects.requireNonNull(command).setTabCompleter(new KanaChatCmdTabCompleter());
        command.setExecutor(new KanaChatCommand(this));

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);

        logger.info(getDescription().getName() + "-" + getDescription().getVersion() + " is enabled");
    }

    @Override
    public void onDisable() {
        saveConfig();
        logger.info(getDescription().getName() + " is disabled");
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
