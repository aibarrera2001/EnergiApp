package sistemapanelessolares.logica;

import sistemapanelessolares.dominio.ChatBoot;
import sistemapanelessolares.dominio.PanelSolar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de Chat para EnergiApp
 * Gestiona la comunicación con OpenAI usando un contexto específico de EnergiApp
 * Integración con base de datos para información en tiempo real
 */
public class ChatController {

    private final ChatBoot chatBoot;
    private final SolarService solarService;

    /**
     * Inicializa el controlador de chat con el sistema prompt de EnergiApp
     * @param solarService Servicio para acceso a base de datos
     */
    public ChatController(SolarService solarService) {
        this.solarService = solarService;
        String systemPrompt = generarSystemPromptDinamico();
        this.chatBoot = new ChatBoot(systemPrompt);
    }

    /**
     * Constructor sin SolarService (solo con datos estáticos)
     */
    public ChatController() {
        this.solarService = null;
        String systemPrompt = generarSystemPromptEstatico();
        this.chatBoot = new ChatBoot(systemPrompt);
    }

    /**
     * Genera el system prompt dinámico con información de la BD
     */
    private String generarSystemPromptDinamico() {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("""
            Eres el chatbot de EnergiApp, una aplicación de energía solar en Colombia.
            Tienes acceso a una base de datos en tiempo real para proporcionar información actualizada.
            
            **DATOS ESTÁTICOS DE PRECIOS:**
            - Precio panel solar 400W: $350,000 COP
            - Precio panel solar 450W: $390,000 COP
            - Costo instalación: $500,000 COP
            - Fórmula: costo_total = (número_panels × precio_panel) + instalación
            - Ahorro anual promedio: 60-70% de factura eléctrica
            - Retorno de inversión: 5-7 años
            """);
        
        // Agregar información de paneles disponibles
        if (solarService != null) {
            try {
                List<PanelSolar> paneles = solarService.obtenerPanelesParaCatalogo();
                if (paneles != null && !paneles.isEmpty()) {
                    prompt.append("\n**PANELES SOLARES DISPONIBLES EN LA BD:**\n");
                    for (PanelSolar panel : paneles) {
                        prompt.append(String.format(
                            "- %s (%s): %dW, Eficiencia: %.1f%%, Precio: $%.2f USD\n",
                            panel.getNombre(),
                            panel.getTipo(),
                            panel.getPotencia(),
                            panel.getEficiencia(),
                            panel.getPrecioDolar()
                        ));
                    }
                }
            } catch (Exception e) {
                // Silent fail - usar datos estáticos
            }
        }
        
        prompt.append("""
            
            **INSTRUCCIONES CRÍTICAS:**
            1. Responde SIEMPRE en español y de forma profesional
            2. Utiliza SOLO información de EnergiApp, no información genérica
            3. Si el usuario pregunta sobre costos, calcula usando LA FÓRMULA EXACTA
            4. Si preguntan sobre paneles disponibles, menciona los que están en la BD
            5. Si no puedes responder algo, sugiere contactar al equipo de soporte
            6. Proporciona recomendaciones personalizadas basadas en los datos de EnergiApp
            7. Si el usuario pregunta sobre su historial, indica que puede consultarlo en su perfil
            
            **CAPACIDADES DE LA BD QUE PUEDES MENCIONAR:**
            - Información actualizada de paneles solares disponibles
            - Historial de consultas del usuario
            - Casas registradas por usuario
            - Estimaciones de ahorro personalizadas
            - Registro de instalaciones
            
            **EJEMPLOS DE RESPUESTAS:**
            - Usuario: "¿Cuánto cuesta instalar 5 paneles de 400W?"
              Respuesta: "Para instalar 5 paneles de 400W en tu casa:
              Costo total = (5 × $350,000) + $500,000 = $2,250,000 COP
              Con tu factura estimada, recuperarías la inversión en 5-7 años ahorrando 60-70%."
            
            - Usuario: "¿Qué paneles tienen mejor eficiencia?"
              Respuesta: "Según nuestro catálogo actualizado, los paneles con mejor eficiencia son..."
            """);
        
        return prompt.toString();
    }

    /**
     * Genera el system prompt estático (sin acceso a BD)
     */
    private String generarSystemPromptEstatico() {
        return """
            Eres el chatbot de EnergiApp, una aplicación de energía solar en Colombia.
            
            Contexto de EnergiApp:
            - Precio panel solar 400W: $350,000 COP
            - Precio panel solar 450W: $390,000 COP
            - Costo instalación: $500,000 COP
            - Fórmula: costo_total = (número_panels × precio_panel) + instalación
            - Ahorro anual promedio: 60-70% de factura eléctrica
            - Retorno de inversión: 5-7 años
            
            Instrucciones:
            1. Responde SIEMPRE usando el contexto de EnergiApp, no información genérica
            2. Si el usuario pregunta sobre costos, calcula EXACTAMENTE usando la fórmula arriba
            3. Sé amable y profesional en español
            4. Si no sabes algo, sugiere contactar con el equipo de soporte
            5. Proporciona recomendaciones basadas en los datos de EnergiApp
            
            Ejemplos de respuestas correctas:
            - Usuario: "¿Cuánto cuesta 5 paneles de 400W?"
              Respuesta: "Para 5 paneles de 400W: (5 × $350,000) + $500,000 = $2,250,000 COP"
            
            - Usuario: "¿Cuánto ahorro con paneles solares?"
              Respuesta: "Con EnergiApp, puedes ahorrar entre 60-70% en tu factura eléctrica, 
              con retorno de inversión estimado entre 5-7 años"
            """;
    }

    /**
     * Envía un mensaje al chatbot de EnergiApp
     * @param mensaje El mensaje del usuario
     * @return La respuesta contextualizada de EnergiApp
     */
    public String procesarMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return "Por favor, escribe un mensaje válido.";
        }
        
        // Procesar el mensaje con contexto de BD si está disponible
        String mensajeEnriquecido = enriquecerMensaje(mensaje);
        return chatBoot.enviarMensaje(mensajeEnriquecido);
    }

    /**
     * Enriquece el mensaje del usuario con información de la BD si es relevante
     */
    private String enriquecerMensaje(String mensaje) {
        if (solarService == null) {
            return mensaje;
        }

        String mensajeLower = mensaje.toLowerCase();

        // Si pregunta sobre paneles disponibles
        if (mensajeLower.contains("panel") || mensajeLower.contains("disponible") || 
            mensajeLower.contains("catálogo") || mensajeLower.contains("tipo")) {
            try {
                List<PanelSolar> paneles = solarService.obtenerPanelesParaCatalogo();
                if (!paneles.isEmpty()) {
                    String listaPaneles = paneles.stream()
                        .map(p -> String.format("%s (%s, %dW)", p.getNombre(), p.getTipo(), p.getPotencia()))
                        .collect(Collectors.joining(", "));
                    return mensaje + "\n[Contexto BD: Paneles disponibles: " + listaPaneles + "]";
                }
            } catch (Exception e) {
                // Ignorar error de BD
            }
        }

        return mensaje;
    }

    /**
     * Obtiene la lista de paneles disponibles de la BD
     * @return String formateado con información de paneles
     */
    public String obtenerListaPanelesDisponibles() {
        if (solarService == null) {
            return "Base de datos no disponible en este momento.";
        }
        
        try {
            List<PanelSolar> paneles = solarService.obtenerPanelesParaCatalogo();
            if (paneles == null || paneles.isEmpty()) {
                return "No hay paneles disponibles en el catálogo.";
            }

            StringBuilder sb = new StringBuilder("📊 **PANELES SOLARES DISPONIBLES EN ENERGIAPP**\n\n");
            for (PanelSolar panel : paneles) {
                sb.append(String.format(
                    "✓ %s\n" +
                    "  - Tipo: %s\n" +
                    "  - Potencia: %dW\n" +
                    "  - Eficiencia: %.1f%%\n" +
                    "  - Precio: $%.2f USD\n" +
                    "  - Costo instalación: $%.2f USD\n\n",
                    panel.getNombre(),
                    panel.getTipo(),
                    panel.getPotencia(),
                    panel.getEficiencia(),
                    panel.getPrecioDolar(),
                    panel.getCostoInstalacion()
                ));
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error al consultar paneles de la BD: " + e.getMessage();
        }
    }

    /**
     * Obtiene información de un panel específico por ID
     * @param idPanel ID del panel
     * @return Información del panel
     */
    public String obtenerDetallePanel(int idPanel) {
        if (solarService == null) {
            return "Base de datos no disponible.";
        }

        try {
            PanelSolar panel = solarService.buscarPanelPorId(idPanel);
            if (panel == null) {
                return "Panel no encontrado en la BD.";
            }

            return String.format(
                "📋 **DETALLES DEL PANEL**\n\n" +
                "**Nombre:** %s\n" +
                "**Tipo:** %s\n" +
                "**Potencia:** %dW\n" +
                "**Eficiencia:** %.1f%%\n" +
                "**Precio USD:** $%.2f\n" +
                "**Precio COP:** $%.2f\n" +
                "**Costo Instalación:** $%.2f\n" +
                "**Garantía:** %s\n" +
                "**Descripción:** %s",
                panel.getNombre(),
                panel.getTipo(),
                panel.getPotencia(),
                panel.getEficiencia(),
                panel.getPrecioDolar(),
                panel.getPrecioDolar() * 4200, // Conversión aproximada a COP
                panel.getCostoInstalacion(),
                panel.getGarantia(),
                panel.getDescripcion()
            );
        } catch (Exception e) {
            return "Error al consultar panel: " + e.getMessage();
        }
    }

    /**
     * Obtiene recomendación de paneles basado en consumo
     * @param consumoDiarioKWh Consumo diario en KWh
     * @return Recomendación personalizada
     */
    public String obtenerRecomendacionPersonalizada(double consumoDiarioKWh) {
        if (solarService == null) {
            return "No es posible hacer recomendaciones sin acceso a la BD.";
        }

        try {
            List<PanelSolar> paneles = solarService.obtenerPanelesParaCatalogo();
            if (paneles == null || paneles.isEmpty()) {
                return "No hay paneles para recomendar.";
            }

            // Seleccionar panel con mejor relación eficiencia-precio
            PanelSolar panelRecomendado = paneles.stream()
                .max((p1, p2) -> Double.compare(p1.getEficiencia(), p2.getEficiencia()))
                .orElse(paneles.get(0));

            int numPaneles = (int) Math.ceil(consumoDiarioKWh / (panelRecomendado.getPotencia() / 1000.0));

            return String.format(
                "⚡ **RECOMENDACIÓN PERSONALIZADA**\n\n" +
                "Para tu consumo de %.2f KWh/día:\n\n" +
                "**Panel Recomendado:** %s (%s)\n" +
                "**Cantidad sugerida:** %d paneles\n" +
                "**Potencia total:** %dW\n" +
                "**Costo estimado:** $%,.0f COP\n" +
                "**Ahorro anual:** $%,.0f - $%,.0f COP\n" +
                "**ROI:** 5-7 años",
                consumoDiarioKWh,
                panelRecomendado.getNombre(),
                panelRecomendado.getTipo(),
                numPaneles,
                numPaneles * panelRecomendado.getPotencia(),
                (numPaneles * panelRecomendado.getPrecioDolar() + 500) * 4200,
                consumoDiarioKWh * 12 * 30 * 4200 * 0.60,
                consumoDiarioKWh * 12 * 30 * 4200 * 0.70
            );
        } catch (Exception e) {
            return "Error al generar recomendación: " + e.getMessage();
        }
    }

    /**
     * Obtiene el SolarService asociado
     * @return SolarService o null si no está disponible
     */
    public SolarService getSolarService() {
        return solarService;
    }
}
