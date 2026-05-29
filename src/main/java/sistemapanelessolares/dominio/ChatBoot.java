package sistemapanelessolares.dominio;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray; 
import com.google.gson.JsonParser;
import sistemapanelessolares.logica.ConfiguracionAPI;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * Clase ChatBoot para integración con OpenAI API
 * Proporciona funcionalidad de chat basada en GPT
 */
public class ChatBoot {

    private final String apiUrl;
    private final String apiKey;
    private final String modelo;
    private final String systemPrompt;
    private final HttpClient httpClient;

    /**
     * Constructor que inicializa ChatBoot con la configuración de OpenAI
     * @param apiUrl URL de la API de OpenAI
     * @param apiKey Clave de API de OpenAI
     * @param systemPrompt Prompt del sistema para contextualizar las respuestas
     */
    public ChatBoot(String apiUrl, String apiKey, String systemPrompt) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.modelo = ConfiguracionAPI.obtenerModeloOpenAI();
        this.systemPrompt = systemPrompt;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Constructor alternativo que carga automáticamente la configuración desde variables de entorno
     * @param systemPrompt Prompt del sistema para contextualizar las respuestas
     */
    public ChatBoot(String systemPrompt) {
        this.apiUrl = ConfiguracionAPI.obtenerURLOpenAI();
        this.apiKey = ConfiguracionAPI.obtenerClaveOpenAI();
        this.modelo = ConfiguracionAPI.obtenerModeloOpenAI();
        this.systemPrompt = systemPrompt;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Constructor que carga automáticamente la configuración desde variables de entorno
     * Usa un system prompt vacío por defecto
     */
    public ChatBoot() {
        this.apiUrl = ConfiguracionAPI.obtenerURLOpenAI();
        this.apiKey = ConfiguracionAPI.obtenerClaveOpenAI();
        this.modelo = ConfiguracionAPI.obtenerModeloOpenAI();
        this.systemPrompt = "";
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Envía un mensaje a OpenAI y obtiene la respuesta
     * @param mensaje El mensaje a enviar
     * @return La respuesta de OpenAI
     */
    public String enviarMensaje(String mensaje) {
        try {
            // 1. Construcción del JSON para OpenAI API
            JsonArray messages = new JsonArray();

            // Agregar system prompt si está configurado
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                JsonObject systemMessage = new JsonObject();
                systemMessage.addProperty("role", "system");
                systemMessage.addProperty("content", systemPrompt);
                messages.add(systemMessage);
            }

            // Agregar mensaje del usuario
            JsonObject messageObj = new JsonObject();
            messageObj.addProperty("role", "user");
            messageObj.addProperty("content", mensaje);
            messages.add(messageObj);

            JsonObject payload = new JsonObject();
            payload.addProperty("model", modelo);
            payload.add("messages", messages);
            payload.addProperty("temperature", 0.7);
            payload.addProperty("max_tokens", 500);

            // 2. Configuración del Request HTTP para OpenAI
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                    .build();

            // 3. Envío y recepción de respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Procesamiento de la respuesta HTTP
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

                // Validaciones seguras para extraer el texto de respuesta de OpenAI
                if (json.has("choices") && !json.getAsJsonArray("choices").isEmpty()) {
                    JsonObject firstChoice = json.getAsJsonArray("choices").get(0).getAsJsonObject();

                    if (firstChoice.has("message")) {
                        JsonObject message = firstChoice.getAsJsonObject("message");

                        if (message.has("content")) {
                            return message.get("content").getAsString();
                        }
                    }
                }
                return response.body();
            }

            return "⚠ Error en OpenAI API: " + response.statusCode() + " - " + response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error de conexión al enviar mensaje: " + e.getMessage();
        }
    }

    /**
     * Obtiene la URL de la API
     * @return URL de OpenAI
     */
    public String obtenerApiUrl() {
        return apiUrl;
    }

    /**
     * Obtiene la clave de API (sin mostrar el valor completo por seguridad)
     * @return Indicador de que la clave está configurada
     */
    public String obtenerApiKey() {
        return "***clave_configurada***";
    }

    /**
     * Obtiene el modelo utilizado
     * @return Nombre del modelo
     */
    public String obtenerModelo() {
        return modelo;
    }
}