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
        
        String nombre;
        do {
            System.out.print("Nombre: ");
            nombre = scanner.nextLine();
            try {
                validadorUsuario.validarNombre(nombre);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
            }
        } while (true);
        
        String apellido;
        do {
            System.out.print("Apellido: ");
            apellido = scanner.nextLine();
            try {
                validadorUsuario.validarApellido(apellido);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
            }
        } while (true);
        
        String telefono;
        do {
            System.out.print("Teléfono: ");
            telefono = scanner.nextLine();
            try {
                validadorUsuario.validarTelefono(telefono);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
            }
        } while (true);
        
        String correo;
        do {
            System.out.print("Correo electrónico: ");
            correo = scanner.nextLine();
            try {
                validadorUsuario.validarCorreo(correo);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
            }
        } while (true);
        
        String pass;
        do {
            System.out.print("Contraseña (min. 6 caracteres): ");
            pass = scanner.nextLine();
            try {
                validadorUsuario.validarContrasena(pass);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
            }
        } while (true);

        Usuario nuevoUsuario = new Usuario(1, nombre, apellido, telefono, correo, pass);

        System.out.println("✔ Usuario registrado correctamente.");
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
        System.out.println("1. Triada Solar 450W (Eficiencia 22%) - $2,100,000");
        System.out.println("2. Heinsen Solar 400W (Eficiencia 21%) - $1,900,000");
        System.out.print("Seleccione una opción (1 o 2): ");
        
        int opcion = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Ingrese costo adicional de instalación (inversor, mano de obra, etc): ");
        double costoInstalacion = Double.parseDouble(scanner.nextLine());

        if (opcion == 1) {
            return new PanelSolar("Triada 450W", 450, 0.22, 2100000.0, costoInstalacion);
        } else {
            return new PanelSolar("Heinsen 400W", 400, 0.21, 1900000.0, costoInstalacion);
        }
    }
}


