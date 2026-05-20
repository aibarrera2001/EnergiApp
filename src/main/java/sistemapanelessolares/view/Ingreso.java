package sistemapanelessolares.view;

import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.*;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.*;
import sistemapanelessolares.validadores.validadorAdministrativo;
import java.util.Scanner;

public class Ingreso {

    private Scanner scanner;
    private SolarService solarServicio;
    private Registro registro;
    private ChatBoot chatBoot;

    public Ingreso() {
        this.scanner = new Scanner(System.in);
        this.solarServicio = new SolarService();
        this.registro = new Registro();
        this.chatBoot = new ChatBoot(
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent",
            "AIzaSyDzqWnEJ88hcwYFglpoOjWgos0drWCff30"
        );
    }

    // ----------------------------------------------------------------
    //  Punto de entrada
    // ----------------------------------------------------------------

    public void iniciarApp() {
        System.out.println("|----------------------------------------|");
        System.out.println("| Bienvenido a EnergiApp - Sistema Solar |");
        System.out.println("|----------------------------------------|");

        // Selección de tipo de ingreso
        String tipoIngreso = seleccionarTipoIngreso();

        if ("2".equals(tipoIngreso)) {
            iniciarSesionAdministrativo();
        } else {
            iniciarSesionUsuario();
        }
    }

    // ----------------------------------------------------------------
    //  Selección de rol
    // ----------------------------------------------------------------

    private String seleccionarTipoIngreso() {
        while (true) {
            System.out.println("\n¿Cómo deseas ingresar?");
            System.out.println("1. Como Usuario");
            System.out.println("2. Como Administrador");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine().trim();
            if ("1".equals(opcion) || "2".equals(opcion)) {
                return opcion;
            }
            System.out.println("⚠ Opción no válida. Ingrese 1 o 2.");
        }
    }

    // ----------------------------------------------------------------
    //  Flujo Usuario
    // ----------------------------------------------------------------

    private void iniciarSesionUsuario() {
        Usuario usuario = registro.registrarNuevoUsuario();
        if (usuario == null) return;

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Agregar una casa");
            System.out.println("2. Seleccionar Panel Solar");
            System.out.println("3. Cambiar Panel Solar");
            System.out.println("4. Generar reporte de costos y paneles");
            System.out.println("5. Consultar asistente AI");
            System.out.println("6. Salir");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    usuario.agregarCasa(registro.registrarCasa());
                    System.out.println("✔ Casa agregada.");
                    break;
                case "2":
                    if (usuario.getPanelSeleccionado() == null) {
                        usuario.setPanelSeleccionado(registro.seleccionarPanel());
                        System.out.println("✔ Panel seleccionado.");
                    } else {
                        System.out.println("⚠ Ya tienes un panel seleccionado. Usa la opción 3 para cambiarlo.");
                    }
                    break;
                case "3":
                    if (usuario.getPanelSeleccionado() != null) {
                        System.out.println("Panel actual: " + usuario.getPanelSeleccionado().toString());
                        usuario.setPanelSeleccionado(registro.seleccionarPanel());
                        System.out.println("✔ Panel cambiado.");
                    } else {
                        System.out.println("⚠ No tienes un panel seleccionado. Usa la opción 2 para seleccionar uno.");
                    }
                    break;
                case "4":
                    if (usuario.getCasas().isEmpty()) {
                        System.out.println("⚠ Primero debe agregar al menos una casa.");
                    } else if (usuario.getPanelSeleccionado() == null) {
                        System.out.println("⚠ Primero debe seleccionar un panel.");
                    } else {
                        System.out.print("Ingrese costo adicional de instalación (inversor, mano de obra, etc): ");
                        double costoExtra = Double.parseDouble(scanner.nextLine());
                        System.out.println("\n" + solarServicio.generarResumenTodasLasCasas(usuario, costoExtra));
                    }
                    break;
                case "5":
                    consultarAsistenteAI(usuario);
                    break;
                case "6":
                    salir = true;
                    System.out.println("Gracias por usar EnergiApp. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("⚠ Opción no válida.");
            }
        }
    }

    // ----------------------------------------------------------------
    //  Flujo Administrativo
    // ----------------------------------------------------------------

    private void iniciarSesionAdministrativo() {
        System.out.println("\n--- ACCESO ADMINISTRATIVO ---");
        Administrativo admin = registrarAdministrativo();
        if (admin == null) return;

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ ADMINISTRATIVO ---");
            System.out.println("1. Gestionar catálogo de paneles solares");
            System.out.println("2. Salir");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    admin.abrirMenuGestion();
                    break;
                case "2":
                    salir = true;
                    System.out.println("Sesión administrativa cerrada. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("⚠ Opción no válida.");
            }
        }
    }

    /**
     * Solicita y valida los datos del administrativo por consola.
     */
    private Administrativo registrarAdministrativo() {
        System.out.println("Ingrese sus datos de administrador:");

        String nombre;
        do {
            System.out.print("Nombre: ");
            nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) break;
            System.out.println("  ✘ El nombre no puede estar vacío.");
        } while (true);

        String apellido;
        do {
            System.out.print("Apellido: ");
            apellido = scanner.nextLine().trim();
            if (!apellido.isEmpty()) break;
            System.out.println("  ✘ El apellido no puede estar vacío.");
        } while (true);

        String telefono;
        do {
            System.out.print("Teléfono: ");
            telefono = scanner.nextLine().trim();
            if (!telefono.isEmpty()) break;
            System.out.println("  ✘ El teléfono no puede estar vacío.");
        } while (true);

        String rol;
        do {
            System.out.print("Rol (ej: Gerente, Supervisor, Técnico): ");
            rol = scanner.nextLine().trim();
            if (!rol.isEmpty()) break;
            System.out.println("  ✘ El rol no puede estar vacío.");
        } while (true);

        String departamento;
        do {
            System.out.print("Departamento (ej: Ventas, Operaciones): ");
            departamento = scanner.nextLine().trim();
            if (!departamento.isEmpty()) break;
            System.out.println("  ✘ El departamento no puede estar vacío.");
        } while (true);

        Administrativo admin = new Administrativo(1, nombre, apellido, telefono, rol, departamento);

        try {
            validadorAdministrativo.validarRegistro(admin);
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Error en los datos del administrador: " + e.getMessage());
            return null;
        }

        System.out.println("✔ Administrador autenticado: " + admin.getNombre()
                + " | " + admin.getRol() + " - " + admin.getDepartamento());
        return admin;
    }

    // ----------------------------------------------------------------
    //  Asistente AI (extraído para no duplicar código)
    // ----------------------------------------------------------------

    private void consultarAsistenteAI(Usuario usuario) {
        System.out.print("Ingrese su consulta para el asistente AI: ");
        String consulta = scanner.nextLine();

        StringBuilder contexto = new StringBuilder();
        contexto.append("Eres un asistente experto en energía solar para la app EnergiApp. ")
                .append("Responde de forma concreta basándote SOLO en estos datos del usuario:\n\n");

        contexto.append("Usuario: ").append(usuario.getNombre()).append(" ").append(usuario.getApellido()).append("\n");

        if (!usuario.getCasas().isEmpty()) {
            contexto.append("Casas registradas:\n");
            for (int i = 0; i < usuario.getCasas().size(); i++) {
                Casa casa = usuario.getCasas().get(i);
                CalculadoraPanels calc = new CalculadoraPanels(
                    casa,
                    usuario.getPanelSeleccionado() != null ? usuario.getPanelSeleccionado() : null,
                    usuario.getPanelSeleccionado() != null ? usuario.getPanelSeleccionado().getCostoInstalacion() : 0
                );
                contexto.append("  Casa ").append(i + 1).append(": ").append(casa.toString()).append("\n");
                if (usuario.getPanelSeleccionado() != null) {
                    contexto.append("    - Paneles necesarios: ").append(calc.calcularNumeroPaneles()).append("\n");
                    contexto.append("    - Costo total estimado: $").append(String.format("%.2f", calc.calcularCostoTotal())).append("\n");
                    contexto.append("    - Horas sol estimadas: ").append(String.format("%.1f", calc.getHorasSolEstimadas())).append(" h\n");
                }
            }
        } else {
            contexto.append("El usuario no tiene casas registradas aún.\n");
        }

        if (usuario.getPanelSeleccionado() != null) {
            contexto.append("Panel seleccionado: ").append(usuario.getPanelSeleccionado().toString()).append("\n");
            contexto.append("Costo instalación adicional: $")
                    .append(String.format("%.2f", usuario.getPanelSeleccionado().getCostoInstalacion())).append("\n");
        } else {
            contexto.append("El usuario no ha seleccionado un panel solar aún.\n");
        }

        contexto.append("\nPregunta del usuario: ").append(consulta);

        String respuesta = chatBoot.enviarMensaje(contexto.toString());
        System.out.println("\n=== Respuesta AI ===\n" + respuesta);
    }
}