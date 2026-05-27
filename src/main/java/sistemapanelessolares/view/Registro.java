package sistemapanelessolares.view;

import java.sql.Connection;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.bdd.usuarioDAO;
import sistemapanelessolares.bdd.casaDAO;

public class Registro {

    private final Connection conexionDB;
    private final usuarioDAO userDAO;
    private final casaDAO homeDAO;

    public Registro(Connection conexionDB) {
        this.conexionDB = conexionDB;
        if (conexionDB != null) {
            this.userDAO = new usuarioDAO(conexionDB);
            this.homeDAO = new casaDAO(conexionDB);
        } else {
            this.userDAO = null;
            this.homeDAO = null;
        }
    }

    // ----------------------------------------------------------------
    // 🖥️ MODAL GRÁFICO: Registrar un Nuevo Usuario (JavaFX)
    // ----------------------------------------------------------------
    public Optional<Usuario> mostrarModalRegistroUsuario() {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("EnergiApp - Registro de Cuenta");
        dialog.setHeaderText("Cree su cuenta de usuario para el sistema proyectivo:");

        ButtonType btnGuardarTipo = new ButtonType("Registrarse", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardarTipo, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        TextField txtApellido = new TextField(); txtApellido.setPromptText("Apellido");
        TextField txtTelefono = new TextField(); txtTelefono.setPromptText("Teléfono");
        TextField txtCorreo = new TextField(); txtCorreo.setPromptText("ejemplo@mail.com");
        PasswordField txtPassword = new PasswordField(); txtPassword.setPromptText("Contraseña");

        grid.add(new Label("Nombre:"), 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(new Label("Apellido:"), 0, 1); grid.add(txtApellido, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2); grid.add(txtTelefono, 1, 2);
        grid.add(new Label("Correo:"), 0, 3); grid.add(txtCorreo, 1, 3);
        grid.add(new Label("Contraseña:"), 0, 4); grid.add(txtPassword, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardarTipo) {
                if (txtNombre.getText().trim().isEmpty() || txtCorreo.getText().trim().isEmpty() || txtPassword.getText().isEmpty()) {
                    mostrarAlerta("Campos vacíos", "El nombre, correo y contraseña son obligatorios.", Alert.AlertType.WARNING);
                    return null;
                }
                return new Usuario(
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtCorreo.getText().trim(),
                    txtPassword.getText()
                );
            }
            return null;
        });

        Optional<Usuario> resultado = dialog.showAndWait();
        
        if (resultado.isPresent() && conexionDB != null && userDAO != null) {
            try {
                Usuario guardado = userDAO.guardar(resultado.get());
                if (guardado != null && guardado.getIdUsuario() > 0) {
                    mostrarAlerta("Éxito", "Usuario registrado permanentemente en la Base de Datos.", Alert.AlertType.INFORMATION);
                    return Optional.of(guardado);
                } else {
                    mostrarAlerta("Error", "No se pudo registrar en la base de datos.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                mostrarAlerta("Error Crítico", "Error en pgAdmin: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        return resultado;
    }

    // ----------------------------------------------------------------
    // 🏠 MODAL GRÁFICO: Registrar una Casa / Propiedad (JavaFX)
    // ----------------------------------------------------------------
    public Optional<Casa> mostrarModalRegistroCasa(int idUsuario) {
        Dialog<Casa> dialog = new Dialog<>();
        dialog.setTitle("EnergiApp - Registrar Propiedad");
        dialog.setHeaderText("Ingrese los datos de consumo de la vivienda:");

        ButtonType btnGuardarTipo = new ButtonType("Guardar Propiedad", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardarTipo, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtDireccion = new TextField(); txtDireccion.setPromptText("Ej: Calle 15 #4-12");
        TextField txtCiudad = new TextField("Valledupar");
        TextField txtConsumo = new TextField(); txtConsumo.setPromptText("Ej: 450.5");
        TextField txtLat = new TextField("0");
        TextField txtLon = new TextField("0");

        grid.add(new Label("Dirección:"), 0, 0); grid.add(txtDireccion, 1, 0);
        grid.add(new Label("Ciudad:"), 0, 1); grid.add(txtCiudad, 1, 1);
        grid.add(new Label("Consumo Mensual (kWh):"), 0, 2); grid.add(txtConsumo, 1, 2);
        grid.add(new Label("Latitud (Opcional):"), 0, 3); grid.add(txtLat, 1, 3);
        grid.add(new Label("Longitud (Opcional):"), 0, 4); grid.add(txtLon, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardarTipo) {
                try {
                    double consumo = Double.parseDouble(txtConsumo.getText().trim());
                    double lat = Double.parseDouble(txtLat.getText().trim());
                    double lon = Double.parseDouble(txtLon.getText().trim());
                    
                    return new Casa(txtDireccion.getText().trim(), txtCiudad.getText().trim(), consumo, lat, lon);
                } catch (NumberFormatException e) {
                    mostrarAlerta("Formato Incorrecto", "El consumo, la latitud y longitud deben ser números válidos.", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<Casa> resultado = dialog.showAndWait();

        if (resultado.isPresent() && conexionDB != null && homeDAO != null) {
            boolean exito = homeDAO.guardarCasa(resultado.get(), idUsuario);
            if (exito) {
                mostrarAlerta("Propiedad Registrada", "Vivienda enlazada con éxito a su cuenta en pgAdmin.", Alert.AlertType.INFORMATION);
            }
        }
        return resultado;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}