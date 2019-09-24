package net.ironingot.kanachat.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import biscotte.kana.Kana;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ironingot.kanachat.KanaChat;
import net.ironingot.translator.KanaKanjiTranslator;

public class AsyncPlayerChatListener implements Listener {
    public final KanaChat plugin;

    private static final String excludeMatchString = "\u00a7|u00a74u00a75u00a73u00a74v|^http";
    private static final Pattern excludePattern = Pattern.compile(excludeMatchString);

    private static final String prefixMatchString = "^(#GLOBAL#|>)(.*)";
    private static final Pattern prefixPattern = Pattern.compile(prefixMatchString);

    private static final String wordMatchString = "([a-z]*)";
    private static final Pattern wordPattern = Pattern.compile(wordMatchString);

    public AsyncPlayerChatListener(KanaChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String prefix = "";
        String message = event.getMessage();

        if (message.startsWith("/")) {
            return;
        }

        Matcher prefixMatcher = prefixPattern.matcher(message);
        if (prefixMatcher.find(0)) {
            prefix = prefixMatcher.group(1);
            message = prefixMatcher.group(2);
        }

        Player player = event.getPlayer();
        Boolean toKana = plugin.getConfigHandler().getUserMode(player.getName());
        Boolean toKanji = plugin.getConfigHandler().getUserKanjiConversion(player.getName());

        if (toKana.equals(Boolean.FALSE)) {
            return;
        }

        String dstMessage = translateJapanese(message, toKanji);

        if (dstMessage.equals(message)) {
            return;
        }
        event.setMessage(formatMessage(prefix, dstMessage, message));
    }

    private String formatMessage(String prefix, String dst, String src) {
        StringBuilder stringBuilder = new StringBuilder();

        // [Prefix] <Converted Message> <Source Message>
        if (!prefix.isEmpty()) {
            stringBuilder.append(prefix).append(" ");
        }

        return stringBuilder.append(dst).append(" ")
            .append(ChatColor.GRAY).append(src).toString();
    }

    public String translateJapanese(String message, Boolean toKanji)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (String word: message.split(" ")) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" ");
            }

            Matcher excludeMatcher = excludePattern.matcher(word);
            if (excludeMatcher.find()) {
                stringBuilder.append(word);
                continue;
            }

            Matcher wordMatcher = wordPattern.matcher(word);
            if (!wordMatcher.matches()) {
                stringBuilder.append(word);
                continue;
            }
        
            // Roma-Ji -> Hiragana translation
            Kana kana = new Kana();
            kana.setLine(word);
            kana.convert();
            String translatedWord = kana.getLine();

            // Hiragana -> Kanji translation
            if (toKanji.equals(Boolean.TRUE)) {
                int wordLength = word.length();
                int headLength = wordLength < 2 ? wordLength : 2;
                int footLength = wordLength < 2 ? wordLength : 2;

                if (translatedWord.startsWith(word.substring(0, headLength)) ||
                    translatedWord.endsWith(word.substring(wordLength - footLength, wordLength))) {
                    // its not roma-ji may be.
                    translatedWord = word;
                } else {
                    translatedWord = KanaKanjiTranslator.translate(translatedWord);
                }
            }

            stringBuilder.append(translatedWord);
        }

        return stringBuilder.toString();
    }
}
