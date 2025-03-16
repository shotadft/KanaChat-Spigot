package net.ironingot.translator;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.concurrent.TimeUnit;

public class KanaKanjiTranslator {
    private static final long EXPIRE_AT = TimeUnit.DAYS.toMillis(3); // 3days

    private static final PassiveExpiringMap<String, String> cache = new PassiveExpiringMap<>(EXPIRE_AT);

    public static String translate(String str) {
        String cached = cache.get(str);
        if (cached != null) {
            return cache.put(str, cached); // update expiring date
        }

        String result = GoogleTranslatorAPI.translate(str);
        cache.put(str, result);
        return result;
    }
}
