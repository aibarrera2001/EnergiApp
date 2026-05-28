package sistemapanelessolares.logica;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Clase de configuración para gestionar las claves API de forma segura.
 * Lee las variables de entorno desde el archivo .env
 */
public class ConfiguracionAPI {
    private static final Dotenv dotenv = Dotenv.load();
    
    /**
     * Obtiene la clave de OpenAI desde las variables de entorno
     * @return La clave de API de OpenAI
     */
    public static String obtenerClaveOpenAI() {
        String clave = dotenv.get("OPENAI_API_KEY");
        if (clave == null || clave.isEmpty()) {
            throw new IllegalArgumentException("OPENAI_API_KEY no está configurada en el archivo .env");
        }
        return clave;
    }
    
    /**
     * Obtiene la URL base de OpenAI
     * @return URL de la API de OpenAI
     */
    public static String obtenerURLOpenAI() {
        return "https://api.openai.com/v1/chat/completions";
    }
    
    /**
     * Obtiene el modelo de OpenAI a usar
     * @return Nombre del modelo (gpt-4, gpt-3.5-turbo, etc.)
     */
    public static String obtenerModeloOpenAI() {
        return dotenv.get("OPENAI_MODEL", "gpt-3.5-turbo");
    }
}
