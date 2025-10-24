package com.juntix.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * SearchView
 * Vista de búsqueda con paginación simulada.
 * Demuestra estructuras repetitivas (bucle para resultados) y condicionales para paginación.
 */
public class SearchView {
    private VBox root;
    private int page = 0;
    private final int pageSize = 5;

    public SearchView() {
        construir();
    }

    private void construir() {
        root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(8);
        Label titulo = new Label("Buscar trabajadores por oficio");
        TextField tfTerm = new TextField();
        tfTerm.setPromptText("Ej. Jardinería");
        TextField tfLocal = new TextField();
        tfLocal.setPromptText("Localidad (opcional)");
        Button btnBuscar = new Button("Buscar");

        ListView<String> resultados = new ListView<>();
        Button btnPrev = new Button("Anterior");
        Button btnNext = new Button("Siguiente");

        btnBuscar.setOnAction(e -> {
            page = 0;
            realizarBusqueda(tfTerm.getText().trim(), tfLocal.getText().trim(), resultados);
        });

        btnPrev.setOnAction(e -> {
            if (page > 0) {
                page--;
                realizarBusqueda(tfTerm.getText().trim(), tfLocal.getText().trim(), resultados);
            }
        });

        btnNext.setOnAction(e -> {
            page++;
            realizarBusqueda(tfTerm.getText().trim(), tfLocal.getText().trim(), resultados);
        });

        root.getChildren().addAll(titulo, tfTerm, tfLocal, btnBuscar, resultados, new ToolBar(btnPrev, btnNext));
    }

    private void realizarBusqueda(String term, String localidad, ListView<String> resultados) {
        resultados.getItems().clear();
        for (int i = 1; i <= pageSize; i++) {
            int idx = page * pageSize + i;
            String loc = (localidad == null || localidad.isEmpty()) ? "Varias localidades" : localidad;
            resultados.getItems().add("Trabajador " + idx + " - " + term + " - " + loc + " - Tel: 351000" + idx);
        }
    }

    public VBox getView() { return root; }
}
