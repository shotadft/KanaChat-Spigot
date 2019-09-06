package net.ironingot.nihongochat;

import java.io.File;
import java.util.logging.Logger;
import java.util.Arrays;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.PluginCommand;

public class NihongoChat extends JavaPlugin {
    public static final Logger logger = Logger.getLogger("Minecraft");
    private ConfigHandler configHandler;

    public void onEnable() {
        this.configHandler = new ConfigHandler(new File(this.getDataFolder(), "config.yml"));

        PluginCommand command = getCommand("nihongochat");
        command.setAliases(Arrays.asList("japanize", "jc"));
        command.setExecutor(new NihongoChatCommand(this));

        getServer().getPluginManager().registerEvents(new NihongoChatAsyncPlayerChatListener(this), this);

        logger.info(getDescription().getName() + "-" + getDescription().getVersion() + " is enabled");
    }

    public void onDisable() {
        logger.info(getDescription().getName() + " is disabled");
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
