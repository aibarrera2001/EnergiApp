package sistemapanelessolares.logica;

import sistemapanelessolares.dominio.ChatBoot;
import sistemapanelessolares.dominio.PanelSolar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de Chat para EnergiApp
 * Gestiona la comunicación con el modelo usando exclusivamente los valores en COP de la Base de Datos
 */
public class ChatController {

    private final ChatBoot chatBoot;
    private final SolarService solarService;

    /**
     * Inicializa el controlador inyectando los paneles reales de la BD en el System Prompt
     */
    public ChatController(SolarService solarService) {
        this.solarService = solarService;
        String systemPrompt = generarSystemPromptDinamico();
        this.chatBoot = new ChatBoot(systemPrompt);
    }

    /**
     * Constructor de respaldo si la base de datos no está disponible
     */
    public ChatController() {
        this.solarService = null;
        String systemPrompt = generarSystemPromptEstatico();
        this.chatBoot = new ChatBoot(systemPrompt);
    }

    /**
     * Genera el prompt del sistema usando los precios nativos en COP de los objetos PanelSolar
     */
    private String generarSystemPromptDinamico() {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("""
            Eres el chatbot experto de EnergiApp, una aplicación de energía solar en Colombia.
            Tu misión es asesorar y cotizar proyectos usando UNICAMENTE los paneles de nuestro catálogo real.
            
            **REGLAS DE NEGOCIO OBLIGATORIAS:**
            1. Todos los precios del catálogo ya están expresados en Pesos Colombianos (COP). No apliques ninguna conversión monetaria adicional.
            2. FÓRMULA DE COTIZACIÓN EN COP (Sigue este desglose estricto):
               - Costo de Módulos = Cantidad × PRECIO PANEL (COP)
               - Costo de Instalación = Cantidad × INSTALACIÓN UNITARIA (COP)
               - Costo Total del Proyecto = Costo de Módulos + Costo de Instalación
            3. Métricas de Rendimiento: Ahorro estimado del 60% al 70% en la factura de energía y Retorno de Inversión (ROI) entre 5 y 7 años.
            """);
        
        // Inyección dinámica de los registros de la Base de Datos
        if (solarService != null) {
            try {
                List<PanelSolar> paneles = solarService.obtenerPanelesParaCatalogo();
                if (paneles != null && !paneles.isEmpty()) {
                    prompt.append("\n**CATÁLOGO OFICIAL DISPONIBLE (Precios directos de la BD en COP):**\n");
                    for (PanelSolar panel : paneles) {
                        // Se toman los costos directos ya que vienen parametrizados en COP
                        double precioPanelCOP = panel.getCostoUnidad();
                        double instalacionCOP = panel.getCostoInstalacion();
                        
                        prompt.append(String.format(
                            "- %s [%s]: Potencia %.0fW, Eficiencia %.1f%%, PRECIO PANEL: $%,.0f COP, INSTALACIÓN UNITARIA: $%,.0f COP (Garantía: %s años) - Descripción: %s\n",
                            panel.getNombre(),
                            panel.getTipo(),
                            panel.getPotenciaWatts(),
                            panel.getEficiencia(),
                            precioPanelCOP,
                            instalacionCOP,
                            panel.getGarantiaAnios(),
                            panel.getDescripcion()
                        ));
                    }
                } else {
                    prompt.append("\n[ALERTA SISTEMA: No hay paneles registrados en la base de datos].\n");
                }
            } catch (Exception e) {
                prompt.append("\n[ALERTA SISTEMA: Error al conectar con el repositorio de datos].\n");
            }
        }
        
        prompt.append("""
            
            **INSTRUCCIONES CRÍTICAS DE COMPORTAMIENTO:**
            - Usa única y exclusivamente los valores monetarios exactos provistos en el catálogo anterior. Queda PROHIBIDO inventar valores fijos como $350,000 o $390,000 COP si no corresponden a los paneles reales de la lista.
            - Si el usuario te pide una cotización genérica, lístale ordenadamente las marcas reales de nuestro catálogo y pídele que escoja una para proceder.
            - Al realizar la cotización, haz la matemática en público mostrando detalladamente el costo total de los paneles y el de la instalación por separado en una tabla o lista clara.
            
            **EJEMPLO DE RESPUESTA FORMATEADA:**
            Usuario: "Quiero cotizar 10 paneles de Canadian Solar"
            Respuesta: "¡Excelente elección! Basado en nuestro stock actual de 'Canadian Solar HiKu', el desglose de tu cotización es:
            • **Costo de 10 paneles:** 10 × $210,000 COP = $2,100,000 COP
            • **Costo de instalación técnica:** 10 × $60,000 COP = $600,000 COP
            • **VALOR TOTAL ESTIMADO:** $2,700,000 COP
            Con esta configuración, reducirás tu factura de luz entre un 60% y un 70%. ¿Te gustaría que registremos esta solicitud?"
            """);
        
        return prompt.toString();
    }

    /**
     * Fallback estático en caso de que SolarService falle por completo
     */
    private String generarSystemPromptEstatico() {
        return """
            Eres el chatbot de EnergiApp. En este momento el sistema de inventario centralizado no responde.
            Por políticas de seguridad, no debes simular cotizaciones ni inventar marcas.
            Informa amablemente al usuario que el catálogo se encuentra en actualización técnica y sugiérele contactar a soporte desde su panel de usuario.
            """;
    }

    public String procesarMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return "Por favor, escribe un mensaje válido.";
        }
        
        String mensajeEnriquecido = enriquecerMensaje(mensaje);
        return chatBoot.enviarMensaje(mensajeEnriquecido);
    }

    /**
     * Enriquecimiento del mensaje en tiempo de ejecución: Refuerza la persistencia 
     * inyectando los datos exactos del catálogo en COP al final del mensaje del usuario.
     */
    private String enriquecerMensaje(String mensaje) {
        if (solarService == null) {
            return mensaje;
        }

        String mensajeLower = mensaje.toLowerCase();

        if (mensajeLower.contains("panel") || mensajeLower.contains("cotizar") || 
            mensajeLower.contains("cuánto") || mensajeLower.contains("precio") || 
            mensajeLower.contains("inventario") || mensajeLower.contains("catálogo")) {
            try {
                List<PanelSolar> paneles = solarService.obtenerPanelesParaCatalogo();
                if (paneles != null && !paneles.isEmpty()) {
                    String infoInyeccion = paneles.stream()
                        .map(p -> String.format("%s (Valor: $%,.0f COP | Inst: $%,.0f COP)", 
                                p.getNombre(), p.getCostoUnidad(), p.getCostoInstalacion()))
                        .collect(Collectors.joining(" || "));
                    
                    // Doble candado de seguridad para guiar al modelo con valores nativos en COP
                    return mensaje + "\n[DATOS TIEMPO REAL BD COP: " + infoInyeccion + "]";
                }
            } catch (Exception e) {
                // Fail silent
            }
        }

        return mensaje;
    }
}