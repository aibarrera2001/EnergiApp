package sistemapanelessolares.view;

import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.*;
import java.util.Scanner;
public class Ingreso {


    private Scanner scanner;
    private SolarService solarServicio;
    private Registro registro;

    public Ingreso() {
        this.scanner = new Scanner(System.in);
        this.solarServicio = new SolarService();
        this.registro = new Registro();
    }

    public void iniciarApp() {
        System.out.println("******************************************");
        System.out.println("* Bienvenido a EnergiApp - Sistema Solar *");
        System.out.println("******************************************");

        Usuario usuario = registro.registrarNuevoUsuario();
        if (usuario == null) return; // Si falla la validación, salimos

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Agregar una casa");
            System.out.println("2. Seleccionar/Cambiar Panel Solar");
            System.out.println("3. Generar reporte de costos y paneles");
            System.out.println("4. Salir");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    usuario.agregarCasa(registro.registrarCasa());
                    System.out.println("✔ Casa agregada.");
                    break;
                case "2":
                    usuario.setPanelSeleccionado(registro.seleccionarPanel());
                    System.out.println("✔ Panel seleccionado.");
                    break;
                case "3":
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
                case "4":
                    salir = true;
                    System.out.println("Gracias por usar EnergiApp. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}


