package sistemapanelessolares.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import sistemapanelessolares.bdd.usuarioDAO;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.SolarService;

import java.sql.Connection;

public class inicioSessionUsuarioFX {

    private final SolarService solarServicio;
    private final Connection conexionDB;

    private Usuario usuarioLogueado;

    public inicioSessionUsuarioFX(
            SolarService solarServicio,
            Connection conexionDB
    ) {

        this.solarServicio = solarServicio;
        this.conexionDB = conexionDB;
    }

    public void mostrarVentanaAcceso(Stage stagePrincipal) {

        stagePrincipal.setTitle("EnergiApp - Acceso Usuarios");

        VBox root = new VBox(20);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.setStyle("-fx-background-color: #E1E4E2;");

        Label titulo = new Label("☀️ ENERGIAPP");
        titulo.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        TabPane tabPane = new TabPane();

        Tab tabLogin = new Tab("Iniciar Sesión");
        tabLogin.setClosable(false);

        VBox loginBox = new VBox(15);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(25));

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo electrónico");
        txtCorreo.setPrefWidth(300);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");
        txtPassword.setPrefWidth(300);

        Button btnIngresar = new Button("Ingresar");
        btnIngresar.setPrefWidth(250);
        btnIngresar.setPrefHeight(40);

        btnIngresar.setStyle(
                "-fx-background-color: #7D8C77;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
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

                usuarioDAO dao = new usuarioDAO(conexionDB);

                Usuario autenticado =
                        dao.buscarPorCredenciales(correo, pass);

                if (autenticado != null) {

                    usuarioLogueado = autenticado;

                    DashboardusuarioFx dashboard =
                            new DashboardusuarioFx(
                                    usuarioLogueado,
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

            } else {

                usuarioLogueado = new Usuario(
                        "Usuario",
                        "Temporal",
                        "000",
                        correo,
                        pass
                );

                DashboardusuarioFx dashboard =
                        new DashboardusuarioFx(
                                usuarioLogueado,
                                solarServicio,
                                conexionDB
                        );

                dashboard.mostrar(stagePrincipal);
            }
        });

        loginBox.getChildren().addAll(
                txtCorreo,
                txtPassword,
                btnIngresar
        );

        tabLogin.setContent(loginBox);

        Tab tabRegistro = new Tab("Crear Cuenta");
        tabRegistro.setClosable(false);

        VBox registroBox = new VBox(12);

        registroBox.setPadding(new Insets(25));
        registroBox.setAlignment(Pos.CENTER);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtApellido = new TextField();
        txtApellido.setPromptText("Apellido");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Correo");

        PasswordField txtPassReg = new PasswordField();
        txtPassReg.setPromptText("Contraseña");

        Button btnRegistrar = new Button("Registrar Usuario");

        btnRegistrar.setPrefWidth(250);
        btnRegistrar.setPrefHeight(40);

        btnRegistrar.setStyle(
                "-fx-background-color: #E76F51;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
        );

        registroBox.getChildren().addAll(
                txtNombre,
                txtApellido,
                txtTelefono,
                txtEmail,
                txtPassReg,
                btnRegistrar
        );

        tabRegistro.setContent(registroBox);

        tabPane.getTabs().addAll(tabLogin, tabRegistro);

        Button btnVolver = new Button("Volver");

        btnVolver.setOnAction(e -> {

            try {

                new IngresoFX().start(stagePrincipal);

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(
                titulo,
                tabPane,
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