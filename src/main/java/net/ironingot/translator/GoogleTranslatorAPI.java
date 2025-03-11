package net.ironingot.translator;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class GoogleTranslatorAPI {
    private static final String baseURL = "https://www.google.com/transliterate";
    private static final String from = "ja-Hira";
    private static final String to = "ja";

    private static final HttpClient client = HttpClient.newHttpClient();

    private static String makeURLString(String text) {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
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
        StringBuilder stringBuilder = new StringBuilder();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .header("Accept", "text/plain")
                .header("User-Agent", "Java SE HttpClient")
                .timeout(Duration.ofMillis(5000))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                stringBuilder.append(response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}