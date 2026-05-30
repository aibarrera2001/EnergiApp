package sistemapanelessolares.view;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import sistemapanelessolares.logica.SolarService;

public class InicioSessionAdministrativoFX {

    private final SolarService solarServicio;
    private final Connection   conexionDB;

    // ── PALETA ────────────────────────────────────────────────────────
    private static final String TEXTO_BLANCO = "#E8F4FD";
    private static final String TEXTO_GRIS   = "#B0BEC5";
    private static final String AZUL_CLARO   = "#90CAF9";
    private static final String AZUL_PRIMARY = "#1565C0";
    private static final String AZUL_HOVER   = "#1E88E5";

    private static final String ESTILO_CAMPO =
            "-fx-background-color: rgba(21, 101, 192, 0.15);"
          + "-fx-border-color: rgba(144, 202, 249, 0.4);"
          + "-fx-border-radius: 10; -fx-background-radius: 10;"
          + "-fx-text-fill: #E8F4FD; -fx-prompt-text-fill: #546E7A;"
          + "-fx-font-size: 13px; -fx-padding: 11 16 11 16;";

    private static final String BTN_PRIMARIO =
            "-fx-background-color: #1565C0; -fx-text-fill: white;"
          + "-fx-font-size: 14px; -fx-font-weight: bold;"
          + "-fx-background-radius: 12; -fx-cursor: hand;"
          + "-fx-padding: 12 0 12 0;";

    private static final String BTN_VOLVER =
            "-fx-background-color: rgba(21, 101, 192, 0.2);"
          + "-fx-text-fill: #90CAF9; -fx-font-size: 12px; -fx-font-weight: bold;"
          + "-fx-background-radius: 10;"
          + "-fx-border-color: rgba(144, 202, 249, 0.3);"
          + "-fx-border-radius: 10; -fx-cursor: hand;"
          + "-fx-padding: 8 20 8 20;";

    public InicioSessionAdministrativoFX(SolarService solarServicio, Connection conexionDB) {
        this.solarServicio = solarServicio;
        this.conexionDB    = conexionDB;
    }

    public void mostrarVentanaAcceso(Stage stagePrincipal) {

        stagePrincipal.setTitle("EnergiApp — Acceso Administrativo");

        // ── FONDO CON DEGRADADO ───────────────────────────────────────
        StackPane fondoPane = new StackPane();
        Rectangle gradBg = new Rectangle();
        gradBg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0D1B2A")),
                new Stop(1, Color.web("#0A1628"))));
        gradBg.widthProperty().bind(fondoPane.widthProperty());
        gradBg.heightProperty().bind(fondoPane.heightProperty());
        fondoPane.getChildren().add(gradBg);

        // ── TARJETA CENTRAL ───────────────────────────────────────────
        VBox tarjeta = new VBox(0);
        tarjeta.setMaxWidth(460);
        tarjeta.setMinWidth(420);
        tarjeta.setStyle(
                "-fx-background-color: rgba(13, 27, 42, 0.95);"
              + "-fx-background-radius: 24;"
              + "-fx-border-color: rgba(144, 202, 249, 0.3);"
              + "-fx-border-radius: 24; -fx-border-width: 1.5;");
        tarjeta.setEffect(new DropShadow(40, 0, 12, Color.color(0, 0, 0, 0.7)));

        // ── CABECERA ──────────────────────────────────────────────────
        VBox cabecera = new VBox(8);
        cabecera.setAlignment(Pos.CENTER);
        cabecera.setPadding(new Insets(36, 36, 24, 36));
        cabecera.setStyle(
                "-fx-background-color: rgba(21, 101, 192, 0.18);"
              + "-fx-background-radius: 22 22 0 0;"
              + "-fx-border-color: transparent transparent rgba(144, 202, 249, 0.2) transparent;"
              + "-fx-border-width: 0 0 1 0;");

        // Logo
        javafx.scene.Node logoNode;
        InputStream logoIs = getClass().getResourceAsStream(
                "/sistemapanelessolares/imagenes/logoEnergiapp.jpeg");
        if (logoIs == null)
            logoIs = getClass().getClassLoader().getResourceAsStream("images/logoEnergiapp.jpeg");

        if (logoIs != null) {
            ImageView iv = new ImageView(new Image(logoIs));
            iv.setFitWidth(90); iv.setFitHeight(90);
            iv.setPreserveRatio(true); iv.setSmooth(true);
            Circle clip = new Circle(45, 45, 45);
            iv.setClip(clip);
            DropShadow glow = new DropShadow(18, Color.web("#4CAF50"));
            glow.setSpread(0.07);
            iv.setEffect(glow);
            HBox box = new HBox(iv);
            box.setAlignment(Pos.CENTER);
            logoNode = box;
        } else {
            Label fb = new Label("🛠️");
            fb.setStyle("-fx-font-size: 36px;");
            logoNode = fb;
        }

        Label lblTitulo = new Label("Panel Administrativo");
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: 900;"
                         + "-fx-text-fill: " + TEXTO_BLANCO + ";");
        lblTitulo.setEffect(new DropShadow(12, Color.web(AZUL_CLARO)));

        Label lblSub = new Label("Acceso restringido — Solo administradores");
        lblSub.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXTO_GRIS + ";");

        // Badge ADMIN
        Label lblBadge = new Label("⬡  ADMIN MODE");
        lblBadge.setStyle(
                "-fx-background-color: rgba(198, 40, 40, 0.25);"
              + "-fx-text-fill: #EF9A9A; -fx-font-size: 10px; -fx-font-weight: bold;"
              + "-fx-background-radius: 20; -fx-padding: 3 12 3 12;"
              + "-fx-border-color: rgba(239,154,154,0.4); -fx-border-radius: 20;");

        cabecera.getChildren().addAll(logoNode, lblTitulo, lblSub, lblBadge);

        // ── CUERPO ────────────────────────────────────────────────────
        VBox cuerpo = new VBox(14);
        cuerpo.setPadding(new Insets(28, 36, 8, 36));

        TextField     txtCorreo   = crearCampoTexto("✉  admin@energiapp.cor.co");
        PasswordField txtPassword = crearCampoPass("🔒  Contraseña");

        Button btnIngresar = new Button("Autenticar Administrador");
        btnIngresar.setMaxWidth(Double.MAX_VALUE);
        btnIngresar.setStyle(BTN_PRIMARIO);
        aplicarHoverPrimario(btnIngresar);

        btnIngresar.setOnAction(e -> {
            String correo = txtCorreo.getText().trim();
            String pass   = txtPassword.getText();

            if (correo.isEmpty() || pass.isEmpty()) {
                mostrarAlertaEstilizada("Campos Vacíos",
                        "Complete todos los campos para continuar.", Alert.AlertType.WARNING);
                return;
            }

            if (conexionDB != null) {
                String sql = "SELECT nombre, rol FROM administrador WHERE correo = ? AND contraseña = ?";
                try (PreparedStatement ps = conexionDB.prepareStatement(sql)) {
                    ps.setString(1, correo);
                    ps.setString(2, pass);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        String nombre = rs.getString("nombre");
                        mostrarAlertaEstilizada("Bienvenido",
                                "Acceso concedido a " + nombre, Alert.AlertType.INFORMATION);
                        new dashboardadminitradorFX(solarServicio, conexionDB)
                                .mostrar(stagePrincipal);
                    } else {
                        mostrarAlertaEstilizada("Acceso Denegado",
                                "Correo o contraseña incorrectos.", Alert.AlertType.ERROR);
                    }
                } catch (Exception ex) {
                    mostrarAlertaEstilizada("Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                if (correo.equalsIgnoreCase("admin@energiapp.cor.co") && pass.equals("admin123")) {
                    new dashboardadminitradorFX(solarServicio, conexionDB).mostrar(stagePrincipal);
                } else {
                    mostrarAlertaEstilizada("Acceso Denegado",
                            "Credenciales incorrectas.", Alert.AlertType.ERROR);
                }
            }
        });

        cuerpo.getChildren().addAll(
                lbl("Correo Administrativo"), txtCorreo,
                lbl("Contraseña"),            txtPassword,
                new Region() {{ setMinHeight(4); }},
                btnIngresar
        );

        // ── FOOTER ────────────────────────────────────────────────────
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 36, 28, 36));

        Button btnVolver = new Button("← Volver al Inicio");
        btnVolver.setStyle(BTN_VOLVER);
        btnVolver.setOnMouseEntered(ev -> btnVolver.setStyle(
                BTN_VOLVER + "-fx-background-color: rgba(144,202,249,0.15);"));
        btnVolver.setOnMouseExited(ev  -> btnVolver.setStyle(BTN_VOLVER));
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

        // Texto decorativo lateral
        Label lblDeco = new Label("SYSTEM CORE v2.6  //  ADMIN MODE");
        lblDeco.setStyle("-fx-text-fill: rgba(144,202,249,0.18); -fx-font-size: 11px;"
                       + "-fx-font-family: 'Courier New'; -fx-font-weight: bold;");
        lblDeco.setRotate(-90);
        StackPane.setAlignment(lblDeco, Pos.CENTER_LEFT);
        lblDeco.setTranslateX(-180);

        StackPane.setAlignment(tarjeta, Pos.CENTER);
        fondoPane.getChildren().addAll(tarjeta, lblDeco);

        Scene scene = new Scene(fondoPane, 1200, 700);
        stagePrincipal.setScene(scene);
        stagePrincipal.setMaximized(true);
        stagePrincipal.setMinWidth(1000);
        stagePrincipal.setMinHeight(650);
        stagePrincipal.show();
    }

    // ── HELPERS ───────────────────────────────────────────────────────

    private TextField crearCampoTexto(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(ESTILO_CAMPO);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.focusedProperty().addListener((obs, o, focused) ->
            tf.setStyle(ESTILO_CAMPO + (focused
                ? "-fx-border-color: #42A5F5; -fx-border-width: 1.5;" : "")));
        return tf;
    }

    private PasswordField crearCampoPass(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle(ESTILO_CAMPO);
        pf.setMaxWidth(Double.MAX_VALUE);
        pf.focusedProperty().addListener((obs, o, focused) ->
            pf.setStyle(ESTILO_CAMPO + (focused
                ? "-fx-border-color: #42A5F5; -fx-border-width: 1.5;" : "")));
        return pf;
    }

    private Label lbl(String texto) {
        Label l = new Label(texto);
        l.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 11px; -fx-font-weight: bold;");
        return l;
    }

    private void aplicarHoverPrimario(Button b) {
        DropShadow glow = new DropShadow(16, Color.web(AZUL_PRIMARY));
        b.setOnMouseEntered(e -> { b.setStyle(BTN_PRIMARIO + "-fx-background-color:" + AZUL_HOVER + ";"); b.setEffect(glow); });
        b.setOnMouseExited(e  -> { b.setStyle(BTN_PRIMARIO); b.setEffect(null); });
        b.setOnMousePressed(e -> b.setStyle(BTN_PRIMARIO + "-fx-scale-x:0.98;-fx-scale-y:0.98;"));
        b.setOnMouseReleased(e-> b.setStyle(BTN_PRIMARIO));
    }

    private void mostrarAlertaEstilizada(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        DialogPane dp = alerta.getDialogPane();
        dp.setStyle(
                "-fx-background-color: #0D1B2A;"
              + "-fx-border-color: rgba(144,202,249,0.4);"
              + "-fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12;");
        dp.lookup(".content.label").setStyle("-fx-text-fill: #E8F4FD; -fx-font-size: 13px;");
        alerta.showAndWait();
    }
}