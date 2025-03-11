package net.ironingot.translator;

import org.apache.commons.collections4.map.PassiveExpiringMap;
// import net.ironingot.kanachat.KanaChat;

public class KanaKanjiTranslator {
    private static final long EXPIRE_AT = 1000 * 60 * 60 * 24 * 3; // 3days

    private static final PassiveExpiringMap<String, String> cache = new PassiveExpiringMap<>(EXPIRE_AT);

    public static String translate(String str) {
        String cached = cache.get(str);
        if (cached != null) {
            // KanaChat.logger.info("KanaKanjiTranslator: Cache Hit [" + str + "] : [" + cached + "]");
            return cache.put(str, cached); // update expiring date
        }

        String result = GoogleTranslatorAPI.translate(str);
        cache.put(str, result);
        // KanaChat.logger.info("KanaKanjiTranslator: Translate - [" + str + "] : [" + result + "]");
        return result;
    }
}
