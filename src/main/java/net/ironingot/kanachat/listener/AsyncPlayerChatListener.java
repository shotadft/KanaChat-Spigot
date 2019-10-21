package net.ironingot.kanachat.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import biscotte.kana.Kana;
import net.ironingot.kanachat.KanaChat;
import net.ironingot.translator.KanaKanjiTranslator;

public class AsyncPlayerChatListener implements Listener {
    public final KanaChat plugin;

    private static final String excludeMatchString = "\u00a7|u00a74u00a75u00a73u00a74v|^http";
    private static final Pattern excludePattern = Pattern.compile(excludeMatchString);

    private static final String systemMatchString = "^(#GLOBAL#|>)([ ]*)(.*)";
    private static final Pattern systemPattern = Pattern.compile(systemMatchString);

    private static final String wordMatchString = "([a-z0-9!-/:-@\\[-`\\{-~]*)";
    private static final Pattern wordPattern = Pattern.compile(wordMatchString);

    private static final String prefixMatchString = "^([0-9!-/:-@\\[-`\\{-~]+)(.*?)";
    private static final Pattern prefixPattern = Pattern.compile(prefixMatchString);

    private static final String postfixMatchString = "(.*?)([0-9!-/:-@\\[-`\\{-~]+)$";
    private static final Pattern postfixPattern = Pattern.compile(postfixMatchString);

    public AsyncPlayerChatListener(KanaChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String system = "";
        String space = "";
        String message = event.getMessage();

        if (message.startsWith("/")) {
            return;
        }

        Matcher systemMatcher = systemPattern.matcher(message);
        if (systemMatcher.find(0)) {
            system = systemMatcher.group(1);
            space = systemMatcher.group(2);
            message = systemMatcher.group(3);
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
        event.setMessage(formatMessage(system, dstMessage, message));
    }

    private String formatMessage(String prefix, String dst, String src) {
        StringBuilder stringBuilder = new StringBuilder();

        // [Prefix] <Converted Message> <Source Message>
        if (!prefix.isEmpty()) {
            stringBuilder.append(prefix).append(" ");
        }

        return stringBuilder.append(dst)
            .append(" ").append(ChatColor.DARK_GRAY)
            .append("(").append(src).append(")").toString();
    }

    public String translateJapanese(String message, Boolean toKanji)
    {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isLastTranslated = true;

        for (String word: message.split(" ")) {
            Matcher excludeMatcher = excludePattern.matcher(word);
            if (excludeMatcher.find()) {
                stringBuilder.append(word);
                continue;
            }

            Matcher wordMatcher = wordPattern.matcher(word);
            if (!wordMatcher.matches()) {
                // with blank
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(word);
                isLastTranslated = false;
                continue;
            }

            // find prefix signs
            Matcher prefixMatcher = prefixPattern.matcher(word);
            String prefix = "";
            if (prefixMatcher.matches()) {
                prefix = prefixMatcher.group(1);
                word = prefixMatcher.group(2);
            }
        
            // find postfix signs
            Matcher postfixMatcher = postfixPattern.matcher(word);
            String postfix = "";
            if (postfixMatcher.matches()) {
                word = postfixMatcher.group(1);
                postfix = postfixMatcher.group(2);
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
                    
                    // with blank
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" ");
                    }
                    isLastTranslated = false;
                } else {
                    translatedWord = KanaKanjiTranslator.translate(translatedWord);
                    // without blank in japanese string

                    if (!isLastTranslated) {
                        stringBuilder.append(" ");
                    }
                    isLastTranslated = true;
                }
            }

            stringBuilder.append(prefix + translatedWord + postfix);
        }

        return stringBuilder.toString();
    }
}
