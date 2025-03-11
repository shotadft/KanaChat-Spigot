package net.ironingot.kanachat;

import net.ironingot.kanachat.listener.AsyncPlayerChatListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class KanaChat extends JavaPlugin {
    public static final Logger logger = Logger.getLogger("Minecraft");
    private ConfigHandler configHandler;

    public void onEnable() {
        this.configHandler = new ConfigHandler(new File(this.getDataFolder(), "config.yml"));

        PluginCommand command = getCommand("kanachat");
        Objects.requireNonNull(command).setAliases(Arrays.asList("japanize", "jc"));
        command.setExecutor(new KanaChatCommand(this));

        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);

        logger.info(getDescription().getName() + "-" + getDescription().getVersion() + " is enabled");
    }

    public void onDisable() {
        logger.info(getDescription().getName() + " is disabled");
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
