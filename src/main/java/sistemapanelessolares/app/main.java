package sistemapanelessolares.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import sistemapanelessolares.view.IngresoFX;

public class main {
    public static void main(String[] args) {
        // 1. Parámetros de tu base de datos
        String url = "jdbc:postgresql://localhost:5432/Base_proyecto_ahorro";
        String usuario = "postgres";
        String contrasena = "root"; 

        System.out.println("Iniciando EnergiApp y conectando a la Base de Datos...");

        Connection conexion = null;

        try {
            // Forzamos la carga del driver de Postgres
            Class.forName("org.postgresql.Driver");
            
            // 2. Abrimos la conexión física
            conexion = DriverManager.getConnection(url, usuario, contrasena);
            conexion.setAutoCommit(true); // Guarda todo inmediatamente en pgAdmin
            
            System.out.println("¡Conexión establecida con éxito para todo el sistema!");

        } catch (ClassNotFoundException e) {
            System.err.println(" Error: No se encontró el driver de PostgreSQL.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a pgAdmin en el inicio: " + e.getMessage());
            System.err.println("La aplicación iniciará sin persistencia en Base de Datos (Modo memoria).");
        }

        // 3. PASAMOS LA CONEXIÓN A LA INTERFAZ GRÁFICA Y LA INICIAMOS
        // Le enviamos la conexión establecida (o null si falló) a la vista de JavaFX
        IngresoFX.setConexionDB(conexion);

        System.out.println("Desplegando Interfaz Gráfica con Plantilla Nórdica...");
        
        // Este método 'launch' inicia el ciclo de vida de las ventanas en VS Code de manera segura
        Application.launch(IngresoFX.class, args);
    }
}