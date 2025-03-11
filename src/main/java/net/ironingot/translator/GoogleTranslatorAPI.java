package net.ironingot.translator;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    private static final Charset codec = StandardCharsets.UTF_8;

    private static final HttpClient client = HttpClient.newHttpClient();

    private static String makeURLString(String text) {
        return baseURL + "?langpair=" + from + "|" + to + "&text=" + text;
    }

    public static String translate(String text) {
        String result;
        String encodedText = URLEncoder.encode(text, codec);
        String response = callWebAPI(makeURLString(encodedText));
        result = pickupFirstCandidate(response);
        return result;
    }

    private static String pickupFirstCandidate(String response) {
        StringBuilder stringBuilder = new StringBuilder();
        JSONParser parser = new JSONParser();

        try {
            JSONArray responseArray = (JSONArray)parser.parse(response);

            for (Object o : responseArray) {
                String partString = "";
                try {
                    JSONArray partArray = (JSONArray) o;
                    partString = (String) partArray.get(0);
                    partString = (String) ((JSONArray) partArray.get(1)).getFirst();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(partString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static String callWebAPI(String urlString) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .header("User-Agent", "Java SE HttpClient")
                .timeout(Duration.ofMillis(5000))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return "";
            }

            stringBuilder.append(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}