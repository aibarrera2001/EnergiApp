/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Energic.app;
import Energic_dominio.*; // La 'E' coincide con tu carpeta
import java.time.LocalDate;
import java.util.ArrayList;
/**
 *
 * @author USUARIO
 */
public class Main {
 
    public static void main(String[] args) {

        System.out.println("=== EnergyIQ — Prueba de clases de dominio ===\n");

        // 1. USUARIO
        Usuario usuario = new Usuario(1, "Juan", "Pérez",
                "juan@correo.com", "1234", "Valledupar");

        System.out.println("✔ Usuario creado:");
        System.out.println("  " + usuario);

        // 2. ARTÍCULO
        Articulo nevera = new Articulo(1, "Nevera Samsung", 150.0,
                "Refrigeración", usuario.getIdUsuario());
        nevera.setUsuario(usuario);
        usuario.agregarArticulo(nevera); // Asegúrate de tener este método en Usuario

        Articulo televisor = new Articulo(2, "Televisor LG 55\"", 120.0,
                "Entretenimiento", usuario.getIdUsuario());
        televisor.setUsuario(usuario);
        usuario.agregarArticulo(televisor);

        System.out.println("\n✔ Artículos registrados: " + (usuario.getArticulos() != null ? usuario.getArticulos().size() : 0));

        // 3. USO
        Uso usoNevera = new Uso(1, 24.0, 7, nevera.getIdArticulo());
        usoNevera.setArticulo(nevera);
        nevera.agregarUso(usoNevera);

        Uso usoTv = new Uso(2, 4.0, 5, televisor.getIdArticulo());
        usoTv.setArticulo(televisor);
        televisor.agregarUso(usoTv);

        System.out.println("\n✔ Uso nevera  -> " + usoNevera.getHorasPorMes() + " h/mes");
        System.out.println("✔ Uso TV      -> " + usoTv.getHorasPorMes() + " h/mes");

        // 4. CONSUMO (Requiere que calcularKwh sea public static en la clase Consumo)
        double kwhNevera = Consumo.calcularKwh(nevera.getPotenciaWatts(), usoNevera.getHorasPorMes());
        double kwhTv     = Consumo.calcularKwh(televisor.getPotenciaWatts(), usoTv.getHorasPorMes());

        Consumo consumoNevera = new Consumo(1, kwhNevera, LocalDate.now(), usoNevera.getIdUso());
        consumoNevera.setUso(usoNevera);

        Consumo consumoTv = new Consumo(2, kwhTv, LocalDate.now(), usoTv.getIdUso());
        consumoTv.setUso(usoTv);

        System.out.println("\n✔ Consumo nevera  -> " + String.format("%.2f", kwhNevera) + " kWh/mes");
        System.out.println("✔ Consumo TV      -> " + String.format("%.2f", kwhTv) + " kWh/mes");

        // 5. COSTO
        double tarifa = 750.0; // COP/kWh

        Costo costoNevera = new Costo(1, tarifa, consumoNevera.getIdConsumo());
        costoNevera.setConsumo(consumoNevera);
        costoNevera.recalcularValor(); 

        Costo costoTv = new Costo(2, tarifa, consumoTv.getIdConsumo());
        costoTv.setConsumo(consumoTv);
        costoTv.recalcularValor();

        System.out.println("\n✔ Costo total estimado: $" + String.format("%.0f", (costoNevera.getValor() + costoTv.getValor())) + " COP");

        // 6. RECOMENDACIÓN (Corregido a Recomendaciones_usuario)
        Recomendaciones_usuario rec = new Recomendaciones_usuario(
                1,
                "Reduce el uso del televisor a 2h/día y ahorra energía.",
                Recomendaciones_usuario.Tipo.REDUCIR_USO, // Ajusta si es un Enum en tu clase
                kwhTv * 0.5 * tarifa,
                usuario.getIdUsuario()
        );
        rec.setUsuario(usuario);
        
        System.out.println("\n✔ Recomendación: " + rec.getDescripcion());

        // 7. SOLAR ESTIMACIÓN
        SolarEstimacion solar = new SolarEstimacion(
                1, 6, 300.0, 5.5, tarifa, usuario.getIdUsuario()
        );
        solar.setUsuario(usuario);
        solar.calcularCobertura(kwhNevera + kwhTv);

        System.out.println("\n✔ Generación solar estimada: " + String.format("%.2f", solar.getKwhGeneradosMes()) + " kWh/mes");

        // 8. CHAT SESIÓN (Corregido a ChatSession)
        ChatSession sesion = new ChatSession(1, usuario.getIdUsuario());
        sesion.setUsuario(usuario);

        System.out.println("\n✔ Sesión de Chat creada ID: " + sesion.getIdSesion());
        
        System.out.println("\n--- PRUEBA FINALIZADA CON ÉXITO ---");
    }
}


