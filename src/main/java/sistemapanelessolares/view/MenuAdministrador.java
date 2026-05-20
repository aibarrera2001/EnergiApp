package sistemapanelessolares.view;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import sistemapanelessolares.dominio.Administrativo;
import sistemapanelessolares.dominio.PanelSolar;
import sistemapanelessolares.logica.GestorPaneles;

public class MenuAdministrador {

    private final Scanner scanner;
    private final GestorPaneles gestorPaneles;
    private final Administrativo admin;
 
    public MenuAdministrador(Administrativo admin, GestorPaneles gestorPaneles) {
        this.admin = admin;
        this.gestorPaneles = gestorPaneles;
        this.scanner = new Scanner(System.in);
    }
 
    // ----------------------------------------------------------------
    //  Punto de entrada
    // ----------------------------------------------------------------
 
    public void iniciar() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   Panel de Administración - EnergiApp  ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("  Bienvenido, " + admin.getNombre() + " (" + admin.getRol() + ")");
 
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1": listarPaneles();       break;
                case "2": flujoAgregarPanel();   break;
                case "3": flujoModificarPanel(); break;
                case "4": flujoEliminarPanel();  break;
                case "5": buscarPorTipo();       break;
                case "6": salir = true;
                          System.out.println("Sesión administrativa cerrada."); break;
                default:  System.out.println("⚠ Opción no válida. Intente de nuevo.");
            }
        }
    }
 
    // ----------------------------------------------------------------
    //  Menú
    // ----------------------------------------------------------------
 
    private void mostrarMenu() {
        System.out.println("\n--- GESTIÓN DE PANELES SOLARES ---");
        System.out.println("1. Listar paneles disponibles");
        System.out.println("2. Añadir nuevo panel");
        System.out.println("3. Modificar panel existente");
        System.out.println("4. Eliminar panel");
        System.out.println("5. Buscar paneles por tipo");
        System.out.println("6. Salir del panel de administración");
        System.out.print("Opción: ");
    }
 
    // ----------------------------------------------------------------
    //  Operaciones
    // ----------------------------------------------------------------
 
    private void listarPaneles() {
        List<PanelSolar> lista = gestorPaneles.listarPorPrecioAscendente();
        if (lista.isEmpty()) {
            System.out.println("⚠ No hay paneles registrados.");
            return;
        }
        System.out.println("\n══ Catálogo de Paneles (ordenado por precio) ══");
        for (PanelSolar p : lista) {
            imprimirPanel(p);
        }
    }
 
    private void flujoAgregarPanel() {
        System.out.println("\n── Añadir nuevo panel ──");
        try {
            PanelSolar nuevo = leerDatosPanel(null);
            PanelSolar guardado = gestorPaneles.agregarPanel(nuevo);
            System.out.println("✔ Panel añadido con id " + guardado.getId() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Error al añadir panel: " + e.getMessage());
        }
    }
 
    private void flujoModificarPanel() {
        System.out.println("\n── Modificar panel ──");
        listarPaneles();
        System.out.print("ID del panel a modificar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<PanelSolar> opt = gestorPaneles.buscarPorId(id);
            if (opt.isEmpty()) {
                System.out.println("✘ No existe un panel con id " + id + ".");
                return;
            }
            System.out.println("Panel actual: ");
            imprimirPanel(opt.get());
            System.out.println("Ingrese los nuevos datos (Enter = dejar igual):");
            PanelSolar actualizado = leerDatosPanel(opt.get());
            gestorPaneles.modificarPanel(id, actualizado);
            System.out.println("✔ Panel id " + id + " actualizado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("✘ ID inválido. Debe ser un número entero.");
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Error al modificar: " + e.getMessage());
        }
    }
 
    private void flujoEliminarPanel() {
        System.out.println("\n── Eliminar panel ──");
        listarPaneles();
        System.out.print("ID del panel a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("¿Confirma la eliminación del panel id " + id + "? (s/n): ");
            String confirma = scanner.nextLine().trim();
            if ("s".equalsIgnoreCase(confirma)) {
                gestorPaneles.eliminarPanel(id);
                System.out.println("✔ Panel eliminado.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✘ ID inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }
 
    private void buscarPorTipo() {
        System.out.println("\nTipos disponibles: Monocristalino, Policristalino, Thin-Film, Bifacial, PERC");
        System.out.print("Tipo a buscar: ");
        String tipo = scanner.nextLine().trim();
        List<PanelSolar> resultado = gestorPaneles.buscarPorTipo(tipo);
        if (resultado.isEmpty()) {
            System.out.println("⚠ No se encontraron paneles del tipo '" + tipo + "'.");
        } else {
            resultado.forEach(this::imprimirPanel);
        }
    }
 
    // ----------------------------------------------------------------
    //  Lectura de datos desde consola
    // ----------------------------------------------------------------
 
    /**
     * Solicita los datos de un panel por consola.
     * Si se pasa un panel base (modo modificación), muestra el valor actual
     * y lo conserva si el usuario no ingresa nada.
     */
    private PanelSolar leerDatosPanel(PanelSolar base) {
        PanelSolar p = new PanelSolar();
 
        p.setNombre(leerTexto("Nombre del panel",
                base != null ? base.getNombre() : null));
 
        System.out.println("  Tipos válidos: Monocristalino, Policristalino, Thin-Film, Bifacial, PERC");
        p.setTipo(leerTexto("Tipo de panel",
                base != null ? base.getTipo() : null));
 
        p.setPotenciaWatts(leerDouble("Potencia (W)",
                base != null ? base.getPotenciaWatts() : null));
 
        p.setEficiencia(leerDouble("Eficiencia (%)",
                base != null ? base.getEficiencia() : null));
 
        p.setCostoUnidad(leerDouble("Costo por unidad ($)",
                base != null ? base.getCostoUnidad() : null));
 
        p.setCostoInstalacion(leerDouble("Costo de instalación ($)",
                base != null ? base.getCostoInstalacion() : null));
 
        p.setGarantiaAnios(leerTexto("Garantía (años)",
                base != null ? base.getGarantiaAnios() : null));
 
        p.setDescripcion(leerTexto("Descripción",
                base != null ? base.getDescripcion() : null));
 
        return p;
    }
 
    private String leerTexto(String campo, String valorActual) {
        if (valorActual != null) {
            System.out.print(campo + " [" + valorActual + "]: ");
        } else {
            System.out.print(campo + ": ");
        }
        String entrada = scanner.nextLine().trim();
        return entrada.isEmpty() && valorActual != null ? valorActual : entrada;
    }
 
    private double leerDouble(String campo, Double valorActual) {
        while (true) {
            if (valorActual != null) {
                System.out.print(campo + " [" + valorActual + "]: ");
            } else {
                System.out.print(campo + ": ");
            }
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty() && valorActual != null) {
                return valorActual;
            }
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("  ✘ Valor numérico inválido. Intente de nuevo.");
            }
        }
    }
 
    // ----------------------------------------------------------------
    //  Presentación
    // ----------------------------------------------------------------
 
    private void imprimirPanel(PanelSolar p) {
        System.out.println("┌─────────────────────────────────────────");
        System.out.printf("│ [ID: %d]  %s%n", p.getId(), p.getNombre());
        System.out.printf("│ Tipo: %-20s Potencia: %.0f W%n", p.getTipo(), p.getPotenciaWatts());
        System.out.printf("│ Eficiencia: %.1f %%          Garantía: %s años%n", p.getEficiencia(), p.getGarantiaAnios());
        System.out.printf("│ Costo/unidad: $%-10.2f  Instalación: $%.2f%n", p.getCostoUnidad(), p.getCostoInstalacion());
        System.out.printf("│ Descripción: %s%n", p.getDescripcion());
        System.out.println("└─────────────────────────────────────────");
    }
}
