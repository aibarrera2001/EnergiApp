package sistemapanelessolares.view;
import java.util.Scanner;
import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.validadores.*;


public class Registro {

    private Scanner scanner;

    public Registro() {
        this.scanner = new Scanner(System.in);
    }

    public Usuario registrarNuevoUsuario() {
        System.out.println("--- REGISTRO DE USUARIO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();
        
        System.out.print("Contraseña (min. 6 caracteres): ");
        String pass = scanner.nextLine();

        Usuario nuevoUsuario = new Usuario(1, nombre, apellido, correo, pass);

        // Validar datos con la clase que creamos antes
        try {
            validadorUsuario.validarRegistro(nuevoUsuario);
            System.out.println("✔ Usuario validado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error en el registro: " + e.getMessage());
            return null;
        }

        return nuevoUsuario;
    }

    public Casa registrarCasa() {
        System.out.println("\n--- REGISTRO DE PROPIEDAD ---");
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        
        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();
        
        System.out.print("Consumo mensual en kWh: ");
        double consumo = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Latitud (0 si no conoce): ");
        double lat = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Longitud (0 si no conoce): ");
        double lon = Double.parseDouble(scanner.nextLine());

        return new Casa(direccion, ciudad, consumo, lat, lon);
    }

    public PanelSolar seleccionarPanel() {
        System.out.println("\n--- SELECCIÓN DE PANEL SOLAR ---");
        System.out.println("1. Trina Solar 450W (Eficiencia 21%) - $180.0");
        System.out.println("2. Jinko Solar 400W (Eficiencia 20%) - $160.0");
        System.out.print("Seleccione una opción (1 o 2): ");
        
        int opcion = Integer.parseInt(scanner.nextLine());
        if (opcion == 1) {
            return new PanelSolar("Trina 450W", 450, 0.21, 180.0);
        } else {
            return new PanelSolar("Jinko 400W", 400, 0.20, 160.0);
        }
    }
}


