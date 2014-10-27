package net.ironingot.nihongochat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import biscotte.kana.Kana;

import net.ironingot.translator.KanaKanjiTranslator;

public class NihongoChatAsyncPlayerChatListener implements Listener {
    public static final Logger logger = Logger.getLogger("Minecraft");

    public NihongoChat plugin;

    private static final String avoidingString = 
            "[^\u0020-\u007E]|\u00a7|u00a74u00a75u00a73u00a74v|^http|^[A-Z#!<>]";

    private static final Pattern avoidingPattern = Pattern.compile(avoidingString);

    public NihongoChatAsyncPlayerChatListener(NihongoChat plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        logger.info("message: " + message);

        if (message.startsWith("/")) {
            logger.info("start with /");
            return;
        }

        if (plugin.getConfigHandler().getUserMode(player.getName()).equals(Boolean.FALSE)) {
            logger.info("Mode false");
            return;
        }

        Matcher matcher = avoidingPattern.matcher(message);
        if (!matcher.find(0)) {
            Kana kana = new Kana();
            kana.setLine(message);
            kana.convert();

            String kanaMessage = kana.getLine();

            if (message.equals(kanaMessage)) {
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            String kanjiMessage = kanaMessage;

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
        } else {
            logger.info("Matched.");
        }
    }
}
