package com.miempresa.gestortareas;

import com.miempresa.gestortareas.negocio.ServicioTareas;
import com.miempresa.gestortareas.vistas.VistaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private ServicioTareas servicio_tareas = new ServicioTareas();

    @Override
    public void start(Stage primaryStage) {
        servicio_tareas.cargar_tareas();

        VistaPrincipal vista_principal = new VistaPrincipal(servicio_tareas);
        Scene scene = new Scene(vista_principal.getRoot(), 820, 520);

        primaryStage.setTitle("Gestor de Tareas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
