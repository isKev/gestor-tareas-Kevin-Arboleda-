package com.miempresa.gestortareas.vistas;

import com.miempresa.gestortareas.controladores.ControladorTareaForm;
import com.miempresa.gestortareas.negocio.ServicioTareas;
import com.miempresa.gestortareas.negocio.Tarea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Optional;

public class VistaPrincipal {

    private BorderPane root = new BorderPane();
    private ServicioTareas servicio_tareas;
    private TableView<Tarea> tabla_tareas = new TableView<>();
    private ObservableList<Tarea> datos_tabla = FXCollections.observableArrayList();
    private TextField txt_buscar = new TextField();
    private Label lbl_mensaje = new Label("Listo.");

    public VistaPrincipal(ServicioTareas servicio_tareas) {
        this.servicio_tareas = servicio_tareas;
        construirUI();
        cargarDatosALaTabla();
    }

    public Parent getRoot() {
        return root;
    }

    private void construirUI() {
        HBox top = new HBox();
        top.setPadding(new Insets(10));
        Label titulo = new Label("Gestor de Tareas");
        titulo.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
        top.getChildren().add(titulo);
        root.setTop(top);

        VBox centro = new VBox(10);
        centro.setPadding(new Insets(10));

        HBox hb_busqueda = new HBox(8);
        txt_buscar.setPromptText("Buscar por nombre...");
        Button btn_buscar = new Button("Buscar");
        Button btn_limpiar = new Button("Limpiar");
        hb_busqueda.getChildren().addAll(txt_buscar, btn_buscar, btn_limpiar);

        TableColumn<Tarea, Number> col_id = new TableColumn<>("ID");
        col_id.setPrefWidth(60);
        col_id.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Tarea, String> col_nombre = new TableColumn<>("Nombre");
        col_nombre.setPrefWidth(320);
        col_nombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre_tarea()));

        TableColumn<Tarea, String> col_fecha = new TableColumn<>("Vencimiento");
        col_fecha.setPrefWidth(160);
        col_fecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha_vencimiento()));

        TableColumn<Tarea, String> col_estado = new TableColumn<>("Estado");
        col_estado.setPrefWidth(140);
        col_estado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstado_tarea()));

        tabla_tareas.getColumns().addAll(col_id, col_nombre, col_fecha, col_estado);
        tabla_tareas.setItems(datos_tabla);
        tabla_tareas.setPrefHeight(330);

        HBox hb_botones = new HBox(8);
        Button btn_nueva = new Button("Nueva tarea");
        Button btn_editar = new Button("Editar");
        Button btn_eliminar = new Button("Eliminar");
        hb_botones.getChildren().addAll(btn_nueva, btn_editar, btn_eliminar);

        centro.getChildren().addAll(hb_busqueda, tabla_tareas, hb_botones);
        root.setCenter(centro);

        HBox bottom = new HBox();
        bottom.setPadding(new Insets(10));
        bottom.getChildren().add(lbl_mensaje);
        root.setBottom(bottom);

        btn_nueva.setOnAction(e -> abrirFormulario(null));
        btn_editar.setOnAction(e -> {
            Tarea sel = tabla_tareas.getSelectionModel().getSelectedItem();
            if (sel == null) {
                lbl_mensaje.setText("Seleccione una tarea para editar.");
                return;
            }
            abrirFormulario(sel);
        });
        btn_eliminar.setOnAction(e -> {
            Tarea sel = tabla_tareas.getSelectionModel().getSelectedItem();
            if (sel == null) {
                lbl_mensaje.setText("Seleccione una tarea para eliminar.");
                return;
            }
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar la tarea seleccionada?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> r = a.showAndWait();
            if (r.isPresent() && r.get() == ButtonType.YES) {
                try {
                    servicio_tareas.eliminar_tarea(sel.getId());
                    cargarDatosALaTabla();
                    lbl_mensaje.setText("Tarea eliminada.");
                } catch (Exception ex) {
                    lbl_mensaje.setText("Error eliminando: " + ex.getMessage());
                }
            }
        });

        btn_buscar.setOnAction(e -> {
            String txt = txt_buscar.getText();
            if (txt == null || txt.isEmpty()) {
                cargarDatosALaTabla();
                lbl_mensaje.setText("Mostrando todas las tareas.");
            } else {
                List<Tarea> encontrados = servicio_tareas.buscar_por_nombre(txt);
                datos_tabla.setAll(encontrados);
                lbl_mensaje.setText("Encontradas: " + encontrados.size());
            }
        });

        btn_limpiar.setOnAction(e -> {
            txt_buscar.setText("");
            cargarDatosALaTabla();
            lbl_mensaje.setText("Filtro limpiado.");
        });
    }

    private void abrirFormulario(Tarea tarea_a_editar) {
        ControladorTareaForm form = new ControladorTareaForm(servicio_tareas, tarea_a_editar);
        Optional<Boolean> resultado = form.mostrarDialog();
        if (resultado.isPresent() && resultado.get()) {
            cargarDatosALaTabla();
            lbl_mensaje.setText("Guardado.");
        } else {
            lbl_mensaje.setText("Cancelado.");
        }
    }

    private void cargarDatosALaTabla() {
        datos_tabla.clear();
        datos_tabla.addAll(servicio_tareas.listar_tareas());
    }
}
