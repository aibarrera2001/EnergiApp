package sistemapanelessolares.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import sistemapanelessolares.logica.SolarService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InicioSessionAdministrativoFX {

    private final SolarService solarServicio;
    private final Connection conexionDB;

    public InicioSessionAdministrativoFX(
            SolarService solarServicio,
            Connection conexionDB
    ) {

        this.solarServicio = solarServicio;
        this.conexionDB = conexionDB;
    }

    public void mostrarVentanaAcceso(Stage stagePrincipal) {

        stagePrincipal.setTitle("EnergiApp - Acceso Administrativo");

        VBox root = new VBox(25);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        root.setStyle("-fx-background-color: #D2D7D4;");

        Label titulo = new Label("🛠️ PANEL DE CONTROL");
        titulo.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #2B2D2F;"
        );

        VBox cardLogin = new VBox(18);

        cardLogin.setAlignment(Pos.CENTER);
        cardLogin.setPadding(new Insets(30));

        cardLogin.setMaxWidth(420);

        cardLogin.setStyle(
                "-fx-background-color: #F3F5F4;" +
                "-fx-background-radius: 15;"
        );

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("admin@energiapp.cor.co");
        txtCorreo.setPrefHeight(40);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setPrefHeight(40);

        Button btnIngresar = new Button("Autenticar Administrador");

        btnIngresar.setPrefWidth(280);
        btnIngresar.setPrefHeight(45);

        btnIngresar.setStyle(
                "-fx-background-color: #2B2D2F;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;"
        );

        btnIngresar.setOnAction(e -> {

            String correo = txtCorreo.getText().trim();
            String pass = txtPassword.getText();

            if (correo.isEmpty() || pass.isEmpty()) {

                mostrarAlerta(
                        "Campos Vacíos",
                        "Complete todos los campos",
                        Alert.AlertType.WARNING
                );

                return;
            }

            if (conexionDB != null) {

                String sql =
                        "SELECT nombre, rol " +
                        "FROM administrador " +
                        "WHERE correo = ? AND contraseña = ?";

                try (
                        PreparedStatement ps =
                                conexionDB.prepareStatement(sql)
                ) {

                    ps.setString(1, correo);
                    ps.setString(2, pass);

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {

                        String nombre = rs.getString("nombre");

                        mostrarAlerta(
                                "Bienvenido",
                                "Acceso concedido a " + nombre,
                                Alert.AlertType.INFORMATION
                        );

                        dashboardadminitradorFX dashboard =
                                new dashboardadminitradorFX(
                                        solarServicio,
                                        conexionDB
                                );

                        dashboard.mostrar(stagePrincipal);

                    } else {

                        mostrarAlerta(
                                "Acceso Denegado",
                                "Correo o contraseña incorrectos",
                                Alert.AlertType.ERROR
                        );
                    }

                } catch (Exception ex) {

                    mostrarAlerta(
                            "Error",
                            ex.getMessage(),
                            Alert.AlertType.ERROR
                    );
                }

            } else {

                if (
                        correo.equalsIgnoreCase("admin@energiapp.cor.co")
                        &&
                        pass.equals("admin123")
                ) {

                    dashboardadminitradorFX dashboard =
                            new dashboardadminitradorFX(
                                    solarServicio,
                                    conexionDB
                            );

                    dashboard.mostrar(stagePrincipal);

                } else {

                    mostrarAlerta(
                            "Error",
                            "Credenciales incorrectas",
                            Alert.AlertType.ERROR
                    );
                }
            }
        });

        Button btnVolver = new Button("↩ Volver");

        btnVolver.setPrefWidth(220);
        btnVolver.setPrefHeight(35);

        btnVolver.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-border-color: #2B2D2F;" +
                "-fx-border-radius: 8;"
        );

        btnVolver.setOnAction(e -> {

            try {

                new IngresoFX().start(stagePrincipal);

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        cardLogin.getChildren().addAll(
                new Label("Credenciales Administrativas"),
                txtCorreo,
                txtPassword,
                btnIngresar
        );

        root.getChildren().addAll(
                titulo,
                cardLogin,
                btnVolver
        );

        Scene scene = new Scene(root, 1200, 700);

        stagePrincipal.setScene(scene);

        stagePrincipal.setMaximized(true);

        stagePrincipal.setMinWidth(1000);
        stagePrincipal.setMinHeight(650);

        stagePrincipal.show();
    }

    private void mostrarAlerta(
            String titulo,
            String contenido,
            Alert.AlertType tipo
    ) {

        Alert alert = new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);

        alert.showAndWait();
    }
}