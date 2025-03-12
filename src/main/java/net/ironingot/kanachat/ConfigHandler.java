package net.ironingot.kanachat;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ConfigHandler {
    private final File configFile;
    private final YamlConfiguration config;

    public ConfigHandler(File configFile) {
        this.configFile = configFile;

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException ignored) {}
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
        load();
    }

    public void load() {
        ConfigurationSection section = config.getConfigurationSection("KanaChat");

        if (section != null) {
            for (String key : section.getKeys(false)) {
                config.set(key, section.get(key));
            }
        } 

        save();
    }

    public void save() {
        try {
            Objects.requireNonNull(config).save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getUserKanjiConversion(String name) {
        return (Boolean)config.get("user." + name + ".kanji", Boolean.TRUE);
    }

    public void setUserKanjiConversion(String name, Boolean value) {
        config.set("user." + name + ".kanji", value);
        save();
    }

    public Boolean getUserMode(String name) {
        return (Boolean)config.get("user." + name + ".kanachat", Boolean.TRUE);
    }

    public void setUserMode(String name, Boolean value) {
        config.set("user." + name + ".kanachat", value);
        save();
    }
}