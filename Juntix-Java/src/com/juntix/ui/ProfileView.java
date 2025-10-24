package com.juntix.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * ProfileView
 * Vista de creación/edición de perfil. En el prototipo final se conectará con PerfilService.
 */
public class ProfileView {
    private int usuarioId;
    private VBox root;

    public ProfileView(int usuarioId) {
        this.setUsuarioId(usuarioId);
        construir();
    }

    private void construir() {
        root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(8);

        Label titulo = new Label("Mi perfil (simulación)");
        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.setVgap(8);

        TextField tfNombre = new TextField();
        TextField tfLocal = new TextField();
        TextField tfTel = new TextField();
        TextArea taExp = new TextArea();
        ComboBox<String> cbOficio = new ComboBox<>();
        cbOficio.getItems().addAll("Jardinería", "Pintura", "Plomería", "Electricidad", "Limpieza");

        Button btnGuardar = new Button("Guardar");

        gp.add(new Label("Nombre completo:"), 0, 0); gp.add(tfNombre, 1, 0);
        gp.add(new Label("Oficio:"), 0, 1); gp.add(cbOficio, 1, 1);
        gp.add(new Label("Localidad:"), 0, 2); gp.add(tfLocal, 1, 2);
        gp.add(new Label("Teléfono:"), 0, 3); gp.add(tfTel, 1, 3);
        gp.add(new Label("Experiencia:"), 0, 4); gp.add(taExp, 1, 4);
        gp.add(btnGuardar, 1, 5);

        btnGuardar.setOnAction(e -> {
            if (tfNombre.getText().trim().isEmpty() || cbOficio.getValue() == null || tfTel.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Complete los campos obligatorios (nombre, oficio, teléfono)", ButtonType.OK);
                a.showAndWait();
                return;
            }
            Alert ok = new Alert(Alert.AlertType.INFORMATION, "Perfil guardado (simulación)", ButtonType.OK);
            ok.showAndWait();
        });

        root.getChildren().addAll(titulo, gp);
    }

    public VBox getView() { return root; }

	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}
}
