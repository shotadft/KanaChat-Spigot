package net.ironingot.nihongochat;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigHandler {
    private File configFile;
    private YamlConfiguration config;

    public ConfigHandler(File configFile) {
        this.configFile = configFile;

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
        load();
    }

    public void load() {
        ConfigurationSection section = config.getConfigurationSection("NihongoChat");

        if (section != null) {
            for (String key : section.getKeys(false)) {
                config.set(key, section.get(key));
            }
        } 

        save();
    }

    public void save() {
        try {
            config.save(configFile);
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
        return (Boolean)config.get("user." + name + ".nihongochat", Boolean.TRUE);
    }

    public void setUserMode(String name, Boolean value) {
        config.set("user." + name + ".nihongochat", value);
        save();
    }
}