package sistemapanelessolares.view;

import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import sistemapanelessolares.dao.UsuarioDAO;
import sistemapanelessolares.dao.CasaDAO;
import sistemapanelessolares.dominio.Casa;
import sistemapanelessolares.dominio.Usuario;
import sistemapanelessolares.logica.SolarService;

import java.sql.Connection;
import java.util.List;

public class inicioSessionUsuarioFX {

    private final SolarService solarServicio;
    private final Connection conexionDB;
    private Usuario usuarioLogueado;

    private static final String FONDO_OSCURO  = "#0D1B2A";
    private static final String AZUL_PRIMARIO = "#1565C0";
    private static final String AZUL_HOVER    = "#1E88E5";
    private static final String AZUL_CLARO    = "#90CAF9";
    private static final String TEXTO_BLANCO  = "#E8F4FD";
    private static final String TEXTO_GRIS    = "#B0BEC5";

    private static final String ESTILO_CAMPO =
            "-fx-background-color: rgba(21,101,192,0.15);"
          + "-fx-border-color: rgba(144,202,249,0.4);"
          + "-fx-border-radius: 10; -fx-background-radius: 10;"
          + "-fx-text-fill: #E8F4FD; -fx-prompt-text-fill: #546E7A;"
          + "-fx-font-size: 13px; -fx-padding: 11 16 11 16;";

    private static final String BTN_PRIMARIO =
            "-fx-background-color: #1565C0; -fx-text-fill: white;"
          + "-fx-font-size: 14px; -fx-font-weight: bold;"
          + "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 12 0 12 0;";

    private static final String BTN_SECUNDARIO =
            "-fx-background-color: transparent; -fx-text-fill: #90CAF9;"
          + "-fx-font-size: 14px; -fx-font-weight: bold;"
          + "-fx-border-color: rgba(144,202,249,0.5);"
          + "-fx-border-radius: 12; -fx-background-radius: 12;"
          + "-fx-cursor: hand; -fx-padding: 12 0 12 0;";

    private static final String BTN_VOLVER =
            "-fx-background-color: rgba(21,101,192,0.2);"
          + "-fx-text-fill: #90CAF9; -fx-font-size: 12px; -fx-font-weight: bold;"
          + "-fx-background-radius: 10; -fx-border-color: rgba(144,202,249,0.3);"
          + "-fx-border-radius: 10; -fx-cursor: hand; -fx-padding: 8 20 8 20;";

    public inicioSessionUsuarioFX(SolarService solarServicio, Connection conexionDB) {
        this.solarServicio = solarServicio;
        this.conexionDB    = conexionDB;
    }

    public void mostrarVentanaAcceso(Stage stagePrincipal) {
        stagePrincipal.setTitle("EnergiApp — Acceso de Usuario");

        StackPane fondoPane = new StackPane();
        Rectangle gradBg = new Rectangle();
        gradBg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0D1B2A")), new Stop(1, Color.web("#0A1628"))));
        gradBg.widthProperty().bind(fondoPane.widthProperty());
        gradBg.heightProperty().bind(fondoPane.heightProperty());
        fondoPane.getChildren().add(gradBg);

        VBox tarjeta = new VBox(0);
        tarjeta.setMaxWidth(460);
        tarjeta.setMinWidth(420);
        tarjeta.setStyle(
                "-fx-background-color: rgba(13,27,42,0.95);"
              + "-fx-background-radius: 24;"
              + "-fx-border-color: rgba(144,202,249,0.3);"
              + "-fx-border-radius: 24; -fx-border-width: 1.5;");
        tarjeta.setEffect(new DropShadow(40, 0, 12, Color.color(0, 0, 0, 0.7)));

        // ── Cabecera ──────────────────────────────────────────────────
        VBox cabecera = new VBox(8);
        cabecera.setAlignment(Pos.CENTER);
        cabecera.setPadding(new Insets(36, 36, 24, 36));
        cabecera.setStyle(
                "-fx-background-color: rgba(21,101,192,0.18);"
              + "-fx-background-radius: 22 22 0 0;"
              + "-fx-border-color: transparent transparent rgba(144,202,249,0.2) transparent;"
              + "-fx-border-width: 0 0 1 0;");

        javafx.scene.Node logoNode;
        InputStream logoIs = getClass().getResourceAsStream("/sistemapanelessolares/imagenes/logoEnergiapp.jpeg");
        if (logoIs == null)
            logoIs = getClass().getClassLoader().getResourceAsStream("images/logoEnergiapp.jpeg");
        if (logoIs != null) {
            javafx.scene.image.Image img = new javafx.scene.image.Image(logoIs);
            javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView(img);
            iv.setFitWidth(90); iv.setFitHeight(90); iv.setPreserveRatio(true);
            javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(45, 45, 45);
            iv.setClip(clip);
            iv.setEffect(new DropShadow(18, Color.web("#4CAF50")));
            HBox contenedorLogo = new HBox(iv);
            contenedorLogo.setAlignment(Pos.CENTER);
            logoNode = contenedorLogo;
        } else {
            Label fallback = new Label("⚡");
            fallback.setStyle("-fx-font-size: 36px;");
            logoNode = fallback;
        }

        Label lblTitulo = new Label("Acceso de Usuario");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: 900; -fx-text-fill: " + TEXTO_BLANCO + ";");
        lblTitulo.setEffect(new DropShadow(12, Color.web(AZUL_CLARO)));
        Label lblSub = new Label("Sistema de Gestion de Energia Solar");
        lblSub.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXTO_GRIS + ";");
        cabecera.getChildren().addAll(logoNode, lblTitulo, lblSub);

        // ── Cuerpo ────────────────────────────────────────────────────
        StackPane cuerpo = new StackPane();
        cuerpo.setPadding(new Insets(28, 36, 32, 36));

        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton btnTabLogin    = crearToggleTab("Iniciar Sesion", toggleGroup);
        ToggleButton btnTabRegistro = crearToggleTab("Crear Cuenta",   toggleGroup);
        btnTabLogin.setSelected(true);

        HBox selectorTabs = new HBox(0, btnTabLogin, btnTabRegistro);
        selectorTabs.setStyle(
                "-fx-background-color: rgba(21,101,192,0.12);"
              + "-fx-background-radius: 12;"
              + "-fx-border-color: rgba(144,202,249,0.2);"
              + "-fx-border-radius: 12; -fx-border-width: 1;");
        selectorTabs.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnTabLogin,    Priority.ALWAYS);
        HBox.setHgrow(btnTabRegistro, Priority.ALWAYS);

        // ── Panel Login ───────────────────────────────────────────────
        VBox panelLogin = new VBox(14);
        panelLogin.setPadding(new Insets(20, 0, 0, 0));

        TextField     txtCorreo   = crearCampoTexto("Correo electronico");
        PasswordField txtPassword = crearCampoPass("Contrasena");

        Button btnIngresar = new Button("Ingresar al Sistema");
        btnIngresar.setMaxWidth(Double.MAX_VALUE);
        btnIngresar.setStyle(BTN_PRIMARIO);
        aplicarHoverPrimario(btnIngresar);

        panelLogin.getChildren().addAll(
                crearLabelCampo("Correo"), txtCorreo,
                crearLabelCampo("Contrasena"), txtPassword,
                new Region() {{ setMinHeight(4); }},
                btnIngresar
        );

        btnIngresar.setOnAction(e -> {
            String correo = txtCorreo.getText().trim();
            String pass   = txtPassword.getText();

            if (correo.isEmpty() || pass.isEmpty()) {
                mostrarAlertaEstilizada("Campos Vacios",
                        "Complete todos los campos.", Alert.AlertType.WARNING);
                return;
            }

            if (conexionDB != null) {
                UsuarioDAO dao = new UsuarioDAO();
                Usuario autenticado = dao.buscarPorCorreo(correo);

                if (autenticado != null && autenticado.getContrasena().equals(pass)) {
                    usuarioLogueado = autenticado;

                    // ✅ Cargar casas del usuario desde Supabase
                    System.out.println("ID usuario logueado: " + usuarioLogueado.getIdUsuario());
                    CasaDAO casaDAO = new CasaDAO();
                    List<Casa> casas = casaDAO.listarPorUsuario(usuarioLogueado.getIdUsuario());
                    System.out.println("Casas encontradas: " + casas.size());
                    for (Casa c : casas) {
                        usuarioLogueado.agregarCasa(c);
                    }

                    new DashboardusuarioFx(usuarioLogueado, solarServicio, conexionDB)
                            .mostrar(stagePrincipal);
                } else {
                    mostrarAlertaEstilizada("Acceso Denegado",
                            "Correo o contrasena incorrectos.", Alert.AlertType.ERROR);
                }
            } else {
                usuarioLogueado = new Usuario("Usuario", "Temporal", "000", correo, pass);
                new DashboardusuarioFx(usuarioLogueado, solarServicio, conexionDB)
                        .mostrar(stagePrincipal);
            }
        });

        // ── Panel Registro ────────────────────────────────────────────
        VBox panelRegistro = new VBox(14);
        panelRegistro.setPadding(new Insets(20, 0, 0, 0));
        panelRegistro.setVisible(false);
        panelRegistro.setManaged(false);

        TextField     txtNombre   = crearCampoTexto("Nombre");
        TextField     txtApellido = crearCampoTexto("Apellido");
        TextField     txtTelefono = crearCampoTexto("Telefono");
        TextField     txtEmail    = crearCampoTexto("Correo");
        PasswordField txtPassReg  = crearCampoPass("Contrasena");

        Button btnRegistrar = new Button("Crear Cuenta");
        btnRegistrar.setMaxWidth(Double.MAX_VALUE);
        btnRegistrar.setStyle(BTN_SECUNDARIO);
        aplicarHoverSecundario(btnRegistrar);

        panelRegistro.getChildren().addAll(
                crearLabelCampo("Nombre"),     txtNombre,
                crearLabelCampo("Apellido"),   txtApellido,
                crearLabelCampo("Telefono"),   txtTelefono,
                crearLabelCampo("Correo"),     txtEmail,
                crearLabelCampo("Contrasena"), txtPassReg,
                new Region() {{ setMinHeight(4); }},
                btnRegistrar
        );

        btnRegistrar.setOnAction(e -> {
            String nombre   = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email    = txtEmail.getText().trim();
            String pass     = txtPassReg.getText();

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                mostrarAlertaEstilizada("Campos Requeridos",
                        "Nombre, correo y contrasena son obligatorios.",
                        Alert.AlertType.WARNING);
                return;
            }

            if (conexionDB != null) {
                try {
                    UsuarioDAO dao = new UsuarioDAO();
                    Usuario nuevo  = new Usuario(nombre, apellido, telefono, email, pass);
                    dao.guardar(nuevo);
                    mostrarAlertaEstilizada("Cuenta Creada",
                            "Usuario registrado con exito. Ya puedes iniciar sesion.",
                            Alert.AlertType.INFORMATION);
                    txtNombre.clear(); txtApellido.clear(); txtTelefono.clear();
                    txtEmail.clear();  txtPassReg.clear();
                    btnTabLogin.setSelected(true);
                    panelLogin.setVisible(true);     panelLogin.setManaged(true);
                    panelRegistro.setVisible(false); panelRegistro.setManaged(false);
                    actualizarEstiloTab(btnTabLogin, btnTabRegistro);
                } catch (Exception ex) {
                    mostrarAlertaEstilizada("Error",
                            "No se pudo registrar: " + ex.getMessage(),
                            Alert.AlertType.ERROR);
                }
            } else {
                mostrarAlertaEstilizada("Sin Conexion",
                        "No hay conexion a la base de datos.", Alert.AlertType.ERROR);
            }
        });

        // ── Switch de tabs ────────────────────────────────────────────
        btnTabLogin.setOnAction(e -> {
            panelLogin.setVisible(true);     panelLogin.setManaged(true);
            panelRegistro.setVisible(false); panelRegistro.setManaged(false);
            actualizarEstiloTab(btnTabLogin, btnTabRegistro);
        });
        btnTabRegistro.setOnAction(e -> {
            panelLogin.setVisible(false);   panelLogin.setManaged(false);
            panelRegistro.setVisible(true); panelRegistro.setManaged(true);
            actualizarEstiloTab(btnTabRegistro, btnTabLogin);
        });
        actualizarEstiloTab(btnTabLogin, btnTabRegistro);

        VBox contenidoTabs = new VBox(16, selectorTabs, panelLogin, panelRegistro);
        cuerpo.getChildren().add(contenidoTabs);

        // ── Footer ────────────────────────────────────────────────────
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(0, 36, 28, 36));
        Button btnVolver = new Button("Volver al Inicio");
        btnVolver.setStyle(BTN_VOLVER);
        btnVolver.setOnMouseEntered(ev -> btnVolver.setStyle(
                BTN_VOLVER + "-fx-background-color: rgba(144,202,249,0.15);"));
        btnVolver.setOnMouseExited(ev -> btnVolver.setStyle(BTN_VOLVER));
        btnVolver.setOnAction(e -> {
            try { new IngresoFX().start(stagePrincipal); }
            catch (Exception ex) { ex.printStackTrace(); }
        });
        footer.getChildren().add(btnVolver);

        Region sepFooter = new Region();
        sepFooter.setPrefHeight(1);
        sepFooter.setStyle("-fx-background-color: rgba(144,202,249,0.15);");
        sepFooter.setMaxWidth(Double.MAX_VALUE);

        tarjeta.getChildren().addAll(cabecera, cuerpo, sepFooter, footer);
        StackPane.setAlignment(tarjeta, Pos.CENTER);
        fondoPane.getChildren().add(tarjeta);

        Scene scene = new Scene(fondoPane, 1200, 700);
        stagePrincipal.setScene(scene);
        stagePrincipal.setMaximized(true);
        stagePrincipal.setMinWidth(1000);
        stagePrincipal.setMinHeight(650);
        stagePrincipal.show();
    }

    // ── Helpers ───────────────────────────────────────────────────────

    private TextField crearCampoTexto(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(ESTILO_CAMPO);
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private PasswordField crearCampoPass(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle(ESTILO_CAMPO);
        pf.setMaxWidth(Double.MAX_VALUE);
        return pf;
    }

    private Label crearLabelCampo(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 11px; -fx-font-weight: bold;");
        return lbl;
    }

    private ToggleButton crearToggleTab(String texto, ToggleGroup grupo) {
        ToggleButton tb = new ToggleButton(texto);
        tb.setToggleGroup(grupo);
        tb.setMaxWidth(Double.MAX_VALUE);
        tb.setStyle("-fx-background-color: transparent; -fx-text-fill: #90CAF9;"
                  + "-fx-font-size: 13px; -fx-font-weight: bold;"
                  + "-fx-background-radius: 11; -fx-cursor: hand; -fx-padding: 10 0 10 0;");
        return tb;
    }

    private void actualizarEstiloTab(ToggleButton activo, ToggleButton inactivo) {
        activo.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white;"
                      + "-fx-font-size: 13px; -fx-font-weight: bold;"
                      + "-fx-background-radius: 11; -fx-cursor: hand; -fx-padding: 10 0 10 0;");
        inactivo.setStyle("-fx-background-color: transparent; -fx-text-fill: #90CAF9;"
                        + "-fx-font-size: 13px; -fx-font-weight: bold;"
                        + "-fx-background-radius: 11; -fx-cursor: hand; -fx-padding: 10 0 10 0;");
    }

    private void aplicarHoverPrimario(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(BTN_PRIMARIO + "-fx-background-color: " + AZUL_HOVER + ";"));
        b.setOnMouseExited(e  -> b.setStyle(BTN_PRIMARIO));
        b.setOnMousePressed(e -> b.setStyle(BTN_PRIMARIO + "-fx-scale-x: 0.98; -fx-scale-y: 0.98;"));
        b.setOnMouseReleased(e-> b.setStyle(BTN_PRIMARIO));
    }

    private void aplicarHoverSecundario(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(BTN_SECUNDARIO + "-fx-background-color: rgba(21,101,192,0.2);"));
        b.setOnMouseExited(e  -> b.setStyle(BTN_SECUNDARIO));
    }

    private void mostrarAlertaEstilizada(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        DialogPane dp = alerta.getDialogPane();
        dp.setStyle("-fx-background-color: #0D1B2A;"
                  + "-fx-border-color: rgba(144,202,249,0.4);"
                  + "-fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12;");
        dp.lookup(".content.label").setStyle("-fx-text-fill: #E8F4FD; -fx-font-size: 13px;");
        alerta.showAndWait();
    }
}