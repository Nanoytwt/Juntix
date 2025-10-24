package com.juntix.ui;

import com.juntix.service.IAuthService;
import com.juntix.exception.ServiceException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * RegisterView
 * Vista JavaFX para registrar un nuevo usuario.
 */
public class RegisterView {
    private IAuthService authService;
    private VBox root;

    public RegisterView(IAuthService authService) {
        this.authService = authService;
        construir();
    }

    private void construir() {
        root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);

        Label titulo = new Label("Registrar nuevo usuario");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(8);

        TextField tfEmail = new TextField();
        PasswordField pf1 = new PasswordField();
        PasswordField pf2 = new PasswordField();
        ComboBox<String> cbRol = new ComboBox<>();
        cbRol.getItems().addAll("CLIENTE", "TRABAJADOR");
        TextField tfTelefono = new TextField();

        Button btnRegister = new Button("Registrar");

        grid.add(new Label("Email:"), 0, 0); grid.add(tfEmail, 1, 0);
        grid.add(new Label("Contraseña:"), 0, 1); grid.add(pf1, 1, 1);
        grid.add(new Label("Confirmar contraseña:"), 0, 2); grid.add(pf2, 1, 2);
        grid.add(new Label("Rol:"), 0, 3); grid.add(cbRol, 1, 3);
        grid.add(new Label("Teléfono (opcional):"), 0, 4); grid.add(tfTelefono, 1, 4);
        grid.add(btnRegister, 1, 5);

        root.getChildren().addAll(titulo, grid);

        btnRegister.setOnAction(ev -> {
            try {
                String email = tfEmail.getText().trim();
                String p1 = pf1.getText();
                String p2 = pf2.getText();
                String rol = cbRol.getValue();
                String tel = tfTelefono.getText().trim();

                if (email.isEmpty() || p1.isEmpty() || p2.isEmpty() || rol == null) {
                    throw new ServiceException("Complete todos los campos obligatorios");
                }
                if (!p1.equals(p2)) throw new ServiceException("Las contraseñas no coinciden");
                if (p1.length() < 6) throw new ServiceException("La contraseña debe tener al menos 6 caracteres");

                var usuario = authService.registrar(email, p1, rol, tel);
                Alert ok = new Alert(Alert.AlertType.INFORMATION, "Registro exitoso. ID: " + usuario.getUsuarioId(), ButtonType.OK);
                ok.showAndWait();
            } catch (ServiceException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
                a.showAndWait();
            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Error interno: " + ex.getMessage(), ButtonType.OK);
                a.showAndWait();
            }
        });
    }

    public VBox getView() { return root; }
}
