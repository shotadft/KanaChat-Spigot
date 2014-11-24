package net.ironingot.nihongochat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import biscotte.kana.Kana;

import net.ironingot.translator.KanaKanjiTranslator;

public class NihongoChatAsyncPlayerChatListener implements Listener {
    public NihongoChat plugin;

    private static final String avoidingString = 
            "[^\u0020-\u007E]|\u00a7|u00a74u00a75u00a73u00a74v|^http|^[A-Z]";
    private static final Pattern avoidingPattern = Pattern.compile(avoidingString);

    private static final String prefixString = "([#GLOBAL#|>])(.*)";
    private static final Pattern prefixPattern = Pattern.compile(prefixString);

    public NihongoChatAsyncPlayerChatListener(NihongoChat plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String prefix = "";
        String message = event.getMessage();
        Player player = event.getPlayer();

        if (message.startsWith("/")) {
            return;
        }

        Matcher prefixMatcher = prefixPattern.matcher(message);
        if (prefixMatcher.find(0)) {
            prefix = prefixMatcher.group(1);
            message = prefixMatcher.group(2);
        }

        if (plugin.getConfigHandler().getUserMode(player.getName()).equals(Boolean.FALSE)) {
            return;
        }

        Matcher avoidingMatcher = avoidingPattern.matcher(message);
        if (!avoidingMatcher.find(0)) {
            Kana kana = new Kana();
            kana.setLine(message);
            kana.convert();
            String kanaMessage = kana.getLine();

            if (message.equals(kanaMessage)) {
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            String kanjiMessage = kanaMessage;
            stringBuilder.append(prefix);

            if (plugin.getConfigHandler().getUserKanjiConversion(player.getName()).equals(Boolean.TRUE)) {
                kanjiMessage = KanaKanjiTranslator.translate(kanaMessage);
            }

            if (kanjiMessage.length() > 0) {
                stringBuilder.append(kanjiMessage);
            } else {
                stringBuilder.append(kanaMessage);
            } 

            stringBuilder.append(ChatColor.GRAY).append(" ").append(message);

            event.setMessage(stringBuilder.toString());
        }
    }
}
