package com.miempresa.gestortareas.controladores;

import com.miempresa.gestortareas.negocio.ServicioTareas;
import com.miempresa.gestortareas.negocio.Tarea;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ControladorTareaForm {

    private ServicioTareas servicio_tareas;
    private Tarea tarea_actual;

    public ControladorTareaForm(ServicioTareas servicio_tareas, Tarea tarea_actual) {
        this.servicio_tareas = servicio_tareas;
        this.tarea_actual = tarea_actual;
    }

    public Optional<Boolean> mostrarDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(tarea_actual == null ? "Nueva tarea" : "Editar tarea");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        TextField txt_nombre = new TextField();
        TextArea txt_descripcion = new TextArea();
        txt_descripcion.setPrefRowCount(3);
        TextField txt_fecha_venc = new TextField();
        ChoiceBox<String> choice_estado = new ChoiceBox<>();
        choice_estado.getItems().addAll("PENDIENTE", "COMPLETADA");
        choice_estado.setValue("PENDIENTE");

        gp.add(new Label("Nombre:"), 0, 0);
        gp.add(txt_nombre, 1, 0);
        gp.add(new Label("Descripción:"), 0, 1);
        gp.add(txt_descripcion, 1, 1);
        gp.add(new Label("Vencimiento:"), 0, 2);
        gp.add(txt_fecha_venc, 1, 2);
        gp.add(new Label("Estado:"), 0, 3);
        gp.add(choice_estado, 1, 3);

        if (tarea_actual != null) {
            txt_nombre.setText(tarea_actual.getNombre_tarea());
            txt_descripcion.setText(tarea_actual.getDescripcion());
            txt_fecha_venc.setText(tarea_actual.getFecha_vencimiento());
            choice_estado.setValue(tarea_actual.getEstado_tarea());
        }

        dialog.getDialogPane().setContent(gp);

        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, ae -> {
            String nombre = txt_nombre.getText();
            if (nombre == null || nombre.trim().isEmpty()) {
                ae.consume();
                Alert a = new Alert(Alert.AlertType.ERROR, "El nombre es obligatorio.", ButtonType.OK);
                a.showAndWait();
            } else {
                try {
                    String descripcion = txt_descripcion.getText();
                    String fecha = txt_fecha_venc.getText();
                    String estado = choice_estado.getValue();
                    if (tarea_actual == null) {
                        Tarea nueva = new Tarea();
                        nueva.setNombre_tarea(nombre);
                        nueva.setDescripcion(descripcion);
                        nueva.setFecha_vencimiento(fecha);
                        nueva.setEstado_tarea(estado);
                        servicio_tareas.agregar_tarea(nueva);
                    } else {
                        tarea_actual.setNombre_tarea(nombre);
                        tarea_actual.setDescripcion(descripcion);
                        tarea_actual.setFecha_vencimiento(fecha);
                        tarea_actual.setEstado_tarea(estado);
                        servicio_tareas.editar_tarea(tarea_actual);
                    }
                } catch (Exception e) {
                    ae.consume();
                    Alert a = new Alert(Alert.AlertType.ERROR, "Error guardando: " + e.getMessage(), ButtonType.OK);
                    a.showAndWait();
                }
            }
        });

        Optional<ButtonType> res = dialog.showAndWait();
        return res.map(bt -> bt == ButtonType.OK);
    }
}
