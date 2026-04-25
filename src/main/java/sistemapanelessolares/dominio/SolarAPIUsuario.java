package sistemapanelessolares.dominio;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class SolarAPIUsuario {


    /**
     * Obtiene las horas de sol promedio diarias estimadas para una ubicación.
     * Consulta la radiación solar de la última semana y saca un promedio.
     */
    public static double obtenerHorasSolPico(double latitud, double longitud) {
       
    try {
        String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&daily=shortwave_radiation_sum&timezone=auto", latitud, longitud);
        
        // LOG 1: Verificar la URL generada
        System.out.println("Conectando a: " + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // LOG 2: Verificar el código de estado (200 es OK)
        System.out.println("Respuesta recibida. Código de estado: " + response.statusCode());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            double radiacion = jsonResponse.get("daily").getAsJsonObject()
                                          .get("shortwave_radiation_sum").getAsJsonArray()
                                          .get(0).getAsDouble();
            
            double hsp = radiacion / 3.6;
            // LOG 3: Verificar el dato final calculado
            System.out.println("Horas Sol Pico calculadas: " + hsp);
            return hsp;
        }
    } catch (Exception e) {
        // LOG 4: Capturar el error específico (ej. si no hay internet)
        System.err.println("¡ERROR DE CONEXIÓN!: " + e.getMessage());
    }
    return 5.0; 
      
    }
}

