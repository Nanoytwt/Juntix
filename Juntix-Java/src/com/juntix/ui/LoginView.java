package com.juntix.ui;

import com.juntix.service.IAuthService;
import com.juntix.exception.ServiceException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;

/**
 * LoginView
 * Vista JavaFX para iniciar sesión. Captura excepciones y muestra mensajes amigables.
 */
public class LoginView {
    private IAuthService authService;
    private BiConsumer<Integer, String> onSuccess;
    private VBox root;

    public LoginView(IAuthService authService, BiConsumer<Integer, String> onSuccess) {
        this.authService = authService;
        this.onSuccess = onSuccess;
        construir();
    }

    private void construir() {
        root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);

        Label titulo = new Label("Iniciar sesión");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(8);

        Label lEmail = new Label("Email:");
        TextField tfEmail = new TextField();
        Label lPass = new Label("Contraseña:");
        PasswordField pf = new PasswordField();

        Button btnLogin = new Button("Ingresar");
        Button btnHelp = new Button("Ayuda");

        grid.add(lEmail, 0, 0); grid.add(tfEmail, 1, 0);
        grid.add(lPass, 0, 1); grid.add(pf, 1, 1);
        grid.add(btnLogin, 1, 2);
        grid.add(btnHelp, 1, 3);

        root.getChildren().addAll(titulo, grid);

        btnLogin.setOnAction(ev -> {
            String email = tfEmail.getText().trim();
            String pass = pf.getText();
            try {
                var usuario = authService.iniciarSesion(email, pass);
                Alert ok = new Alert(Alert.AlertType.INFORMATION, "Inicio de sesión exitoso", ButtonType.OK);
                ok.showAndWait();
                if (onSuccess != null) onSuccess.accept(usuario.getUsuarioId(), usuario.getRol());
            } catch (ServiceException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                a.showAndWait();
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Error interno: " + ex.getMessage(), ButtonType.OK);
                a.showAndWait();
            }
        });

        btnHelp.setOnAction(ev -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION, "Ingrese las credenciales válidas. Si no tiene cuenta, seleccione 'Registrarse' en el menú.", ButtonType.OK);
            info.showAndWait();
        });
    }

    public VBox getView() {
        return root;
    }
}
