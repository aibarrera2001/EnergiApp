package sistemapanelessolares.dominio;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ChatBoot {

    private final String apiUrl;
    private final String apiKey;
    private final HttpClient httpClient;

    public ChatBoot(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

   public String enviarMensaje(String mensaje) {
    try {

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", mensaje);

        com.google.gson.JsonArray parts = new com.google.gson.JsonArray();
        parts.add(textPart);

        JsonObject contentPart = new JsonObject();
        contentPart.add("parts", parts);

        com.google.gson.JsonArray contents = new com.google.gson.JsonArray();
        contents.add(contentPart);

        JsonObject payload = new JsonObject();
        payload.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        payload.toString(),
                        StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200
                && response.statusCode() < 300) {

            JsonObject json =
                    JsonParser.parseString(response.body())
                            .getAsJsonObject();

            if (json.has("candidates")
                    && json.getAsJsonArray("candidates").size() > 0) {

                JsonObject firstCandidate =
                        json.getAsJsonArray("candidates")
                                .get(0)
                                .getAsJsonObject();

                if (firstCandidate.has("content")) {

                    JsonObject content =
                            firstCandidate.getAsJsonObject("content");

                    if (content.has("parts")) {

                        com.google.gson.JsonArray partsArray =
                                content.getAsJsonArray("parts");

                        if (partsArray.size() > 0) {

                            JsonObject firstPart =
                                    partsArray.get(0).getAsJsonObject();

                            if (firstPart.has("text")) {
                                return firstPart
                                        .get("text")
                                        .getAsString();
                            }
                        }
                    }
                }
            }

            return response.body();
        }

        return "Error en API: "
                + response.statusCode()
                + " - "
                + response.body();

    } catch (Exception e) {

        e.printStackTrace();

        return "Error de conexión al enviar mensaje: "
                + e.getMessage();
    }
}

    public String obtenerApiUrl() {
        return apiUrl;
    }

    public String obtenerApiKey() {
        return apiKey;
    }
}
