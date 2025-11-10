package com.juntix.ui;

import com.juntix.model.PerfilTrabajador;
import com.juntix.service.IPerfilService;
import com.juntix.service.PerfilServiceImpl;
import com.juntix.exception.ServiceException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * SearchView
 * Vista de búsqueda REAL de trabajadores desde la base de datos.
 * Usa PerfilServiceImpl → PerfilDAOImpl → MySQL.
 */
public class SearchView {
    private VBox root;
    private int page = 0;
    private final int pageSize = 5;

    private final IPerfilService perfilService = new PerfilServiceImpl();

    public SearchView() {
        construir();
    }

    private void construir() {
        root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(8);
        Label titulo = new Label("Buscar trabajadores por oficio");
        TextField tfTerm = new TextField();
        tfTerm.setPromptText("Ej. Plomería, Electricidad...");
        TextField tfLocal = new TextField();
        tfLocal.setPromptText("Localidad (opcional)");
        Button btnBuscar = new Button("Buscar");

        ListView<String> resultados = new ListView<>();
        Button btnPrev = new Button("Anterior");
        Button btnNext = new Button("Siguiente");

        btnBuscar.setOnAction(e -> {
            page = 0;
            try {
				realizarBusqueda(tfTerm.getText().trim(), tfLocal.getText().trim(), resultados);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        });

        btnPrev.setOnAction(e -> {
            if (page > 0) {
                page--;
                try {
					realizarBusqueda(tfTerm.getText().trim(), tfLocal.getText().trim(), resultados);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            }
        });

        btnNext.setOnAction(e -> {
            page++;
            try {
				realizarBusqueda(tfTerm.getText().trim(), tfLocal.getText().trim(), resultados);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        });

        root.getChildren().addAll(titulo, tfTerm, tfLocal, btnBuscar, resultados, new ToolBar(btnPrev, btnNext));
    }

    private void realizarBusqueda(String term, String localidad, ListView<String> resultados) throws Exception {
        resultados.getItems().clear();
        if (term.isEmpty()) {
            resultados.getItems().add("⚠ Ingrese un oficio para buscar.");
            return;
        }

        try {
            // Buscar el ID del oficio según el nombre (usamos un pequeño helper SQL interno)
            int oficioId = obtenerOficioIdPorNombre(term);
            if (oficioId == -1) {
                resultados.getItems().add("❌ No se encontró el oficio: " + term);
                return;
            }

            List<PerfilTrabajador> perfiles = perfilService.buscarPorOficio(oficioId, localidad, page, pageSize);

            if (perfiles.isEmpty()) {
                resultados.getItems().add("No se encontraron resultados para '" + term + "' en '" + localidad + "'");
            } else {
                for (PerfilTrabajador p : perfiles) {
                    resultados.getItems().add(
                            p.getNombreCompleto() + " - " + p.getLocalidad() +
                            " - Tel: " + p.getTelefono() +
                            " - Experiencia: " + p.getExperiencia()
                    );
                }
            }

        } catch (ServiceException ex) {
            resultados.getItems().add("Error al buscar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Busca el ID del oficio en la base de datos según su nombre.
     */
    private int obtenerOficioIdPorNombre(String nombre) {
        String sql = "SELECT oficio_id FROM Oficio WHERE LOWER(nombre) = LOWER(?)";
        try (var c = com.juntix.util.DBConnection.getConexion();
             var ps = c.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("oficio_id");
            }
        } catch (Exception e) {
            System.err.println("Error buscando oficio_id: " + e.getMessage());
        }
        return -1;
    }

    public VBox getView() {
        return root;
    }
}
