
package sistemapanelessolares.view;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import sistemapanelessolares.logica.SolarService;


public class chatBootFX {

    private final SolarService solarServicio;

    public chatBootFX(SolarService solarServicio) {
        this.solarServicio = solarServicio;
    }

    public void mostrar() {

        Stage stage = new Stage();

        stage.setTitle("Asistente IA");

        VBox root = new VBox(15);

        root.setPadding(new Insets(20));

        TextArea areaChat = new TextArea();

        areaChat.setEditable(false);
        areaChat.setWrapText(true);

        VBox.setVgrow(areaChat, Priority.ALWAYS);

        HBox caja = new HBox(10);

        TextField input = new TextField();
        input.setPromptText("Escribe una pregunta...");

        HBox.setHgrow(input, Priority.ALWAYS);

        Button btnEnviar = new Button("Enviar");

        btnEnviar.setOnAction(e -> {

            String pregunta = input.getText();

            if (pregunta.isEmpty()) {
                return;
            }

            String respuesta;

            if (solarServicio != null &&
                solarServicio.getChatBoot() != null) {

                respuesta = solarServicio
                        .getChatBoot()
                        .enviarMensaje(pregunta);

            } else {

                respuesta = "Asistente IA no disponible.";
            }

            areaChat.appendText("Tú: " + pregunta + "\n\n");
            areaChat.appendText("IA: " + respuesta + "\n\n");

            input.clear();
        });

        caja.getChildren().addAll(
                input,
                btnEnviar
        );

        root.getChildren().addAll(
                areaChat,
                caja
        );

        Scene scene = new Scene(root, 700, 500);

        stage.setScene(scene);

        stage.setMinWidth(600);
        stage.setMinHeight(450);

        stage.show();
    }
}

