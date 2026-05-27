package sistemapanelessolares.dominio;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray; 
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
            // 1. Construcción limpia del árbol JSON para Gemini
            JsonObject textPart = new JsonObject();
            textPart.addProperty("text", mensaje);

            JsonArray parts = new JsonArray();
            parts.add(textPart);

            JsonObject contentPart = new JsonObject();
            contentPart.add("parts", parts);

            JsonArray contents = new JsonArray();
            contents.add(contentPart);

            JsonObject payload = new JsonObject();
            payload.add("contents", contents);

            // 2. Configuración del Request HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("X-Goog-Api-Key", apiKey) // ¡CORREGIDO!: Letras mayúsculas en 'Goog'
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                    .build();

            // 3. Envío y recepción de respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Procesamiento de la respuesta HTTP
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

                // Validaciones seguras capa por capa para extraer el texto de respuesta de la IA
                if (json.has("candidates") && !json.getAsJsonArray("candidates").isEmpty()) {
                    JsonObject firstCandidate = json.getAsJsonArray("candidates").get(0).getAsJsonObject();

                    if (firstCandidate.has("content")) {
                        JsonObject content = firstCandidate.getAsJsonObject("content");

                        if (content.has("parts")) {
                            JsonArray partsArray = content.getAsJsonArray("parts");

                            if (!partsArray.isEmpty()) {
                                JsonObject firstPart = partsArray.get(0).getAsJsonObject();

                                if (firstPart.has("text")) {
                                    return firstPart.get("text").getAsString();
                                }
                            }
                        }
                    }
                }
                return response.body();
            }

            return "⚠ Error en API de Google: " + response.statusCode() + " - " + response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return " Error de conexión al enviar mensaje: " + e.getMessage();
        }
    }

    public String obtenerApiUrl() {
        return apiUrl;
    }

    public String obtenerApiKey() {
        return apiKey;
    }
}