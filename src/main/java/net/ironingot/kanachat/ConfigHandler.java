package net.ironingot.kanachat;

public record ConfigHandler(KanaChat plugin) {
    public Boolean getUserKanjiConversion(String name) {
        return plugin.getConfig().getBoolean("user." + name + ".kanji", Boolean.TRUE);
    }

    public void setUserKanjiConversion(String name, Boolean value) {
        plugin.getConfig().set("user." + name + ".kanji", value);
        plugin.saveConfig();
    }

    public Boolean getUserMode(String name) {
        return plugin.getConfig().getBoolean("user." + name + ".kanachat", Boolean.TRUE);
    }

    public void setUserMode(String name, Boolean value) {
        plugin.getConfig().set("user." + name + ".kanachat", value);
        plugin.saveConfig();
    }
}
