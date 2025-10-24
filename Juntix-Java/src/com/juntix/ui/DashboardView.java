package com.juntix.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * DashboardView
 * Panel principal simple que muestra botones hacia búsqueda y perfil.
 * Demuestra uso de condicionales en la navegación.
 */
public class DashboardView {
    private int usuarioId;
    private String rol;
    private VBox root;

    public DashboardView(int usuarioId, String rol) {
        this.usuarioId = usuarioId;
        this.rol = rol;
        construir();
    }

    private void construir() {
        root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);
        Label titulo = new Label("Panel principal");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label info = new Label("Usuario ID: " + usuarioId + "  |  Rol: " + rol);

        Button btnBuscar = new Button("Buscar por oficio");
        Button btnPerfil = new Button("Mi perfil");

        btnBuscar.setOnAction(e -> {
            SearchView sv = new SearchView();
            root.getChildren().clear();
            root.getChildren().addAll(titulo, info, sv.getView());
        });

        btnPerfil.setOnAction(e -> {
            ProfileView pv = new ProfileView(usuarioId);
            root.getChildren().clear();
            root.getChildren().addAll(titulo, info, pv.getView());
        });

        root.getChildren().addAll(titulo, info, btnBuscar, btnPerfil);
    }

    public VBox getView() { return root; }
}
