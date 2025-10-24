package com.juntix.app;

import com.juntix.service.AuthServiceImpl;
import com.juntix.service.IAuthService;
import com.juntix.ui.LoginView;
import com.juntix.ui.RegisterView;
import com.juntix.ui.DashboardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * MainApp
 * Punto de entrada JavaFX. Provee menú superior y navegación entre vistas.
 * Demuestra el uso de estructuras condicionales en la selección de vistas.
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private IAuthService authService;

    @Override
    public void start(Stage primaryStage) {
        this.setPrimaryStage(primaryStage);
        this.authService = new AuthServiceImpl();

        primaryStage.setTitle("JUNTIX - Prototipo");

        rootLayout = new BorderPane();
        Scene scene = new Scene(rootLayout, 1000, 650);

        MenuBar menuBar = new MenuBar();
        Menu menuApp = new Menu("Aplicación");
        MenuItem miLogin = new MenuItem("Iniciar sesión");
        MenuItem miRegister = new MenuItem("Registrarse");
        MenuItem miExit = new MenuItem("Salir");
        menuApp.getItems().addAll(miLogin, miRegister, new SeparatorMenuItem(), miExit);

        Menu menuAyuda = new Menu("Ayuda");
        MenuItem miAbout = new MenuItem("Acerca de");
        menuAyuda.getItems().add(miAbout);

        menuBar.getMenus().addAll(menuApp, menuAyuda);
        rootLayout.setTop(menuBar);

        miLogin.setOnAction(e -> mostrarLogin());
        miRegister.setOnAction(e -> mostrarRegister());
        miExit.setOnAction(e -> primaryStage.close());
        miAbout.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Acerca de Juntix");
            a.setHeaderText("Juntix - Prototipo");
            a.setContentText("Prototipo desktop en JavaFX. Versión educativa.");
            a.showAndWait();
        });

        mostrarLogin();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarLogin() {
        LoginView login = new LoginView(authService, this::onLoginSuccess);
        rootLayout.setCenter(login.getView());
    }

    private void mostrarRegister() {
        RegisterView rv = new RegisterView(authService);
        rootLayout.setCenter(rv.getView());
    }

    private void onLoginSuccess(int usuarioId, String rol) {
        DashboardView dash = new DashboardView(usuarioId, rol);
        rootLayout.setCenter(dash.getView());
    }

    public static void main(String[] args) {
        launch(args);
    }

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
