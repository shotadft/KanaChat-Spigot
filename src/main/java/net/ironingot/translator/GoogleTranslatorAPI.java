package net.ironingot.translator;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GoogleTranslatorAPI {
    private static final String baseURL = "https://www.google.com/transliterate";
    private static final String from = "ja-Hira";
    private static final String to = "ja";
    private static final Charset codec = StandardCharsets.UTF_8;

    private static String makeURLString(String text) {
        String encodedText = URLEncoder.encode(text, codec);
        return baseURL + "?langpair=" + from + "%7C" + to + "&text=" + encodedText;
    }

    public static String translate(String text) {
        String response = callWebAPI(makeURLString(text));
        return pickupFirstCandidate(response);
    }

    private static String pickupFirstCandidate(String response) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            JsonArray responseArray = JsonParser.parseString(response).getAsJsonArray();

            for (Object o : responseArray) {
                String partString = "";
                try {
                    JsonArray partArray = (JsonArray) o;
                    partString = partArray.get(0).getAsString();
                    partString = partArray.get(1).getAsJsonArray().get(0).getAsString();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(partString);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static String callWebAPI(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            URI uri = URI.create(urlString);
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/plain");
            connection.setRequestProperty("User-Agent", "Java SE HttpClient");
            connection.setConnectTimeout(5000);
            connection.connect();

            bufferedReader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream(), codec));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ignored) {}

            if (connection != null) {
                connection.disconnect();
            }
        }

        return stringBuilder.toString();
    }
}