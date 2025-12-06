package com.miempresa.gestortareas.negocio;

import com.miempresa.gestortareas.dao.TareaDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicioTareas {

    private List<Tarea> lista_tareas = new ArrayList<>();
    private TareaDAO tareaDAO = new TareaDAO();

    public List<Tarea> listar_tareas() {
        return lista_tareas;
    }

    public void agregar_tarea(Tarea tarea) throws Exception {
        int nuevo_id = 1;
        if (!lista_tareas.isEmpty()) {
            nuevo_id = lista_tareas.get(lista_tareas.size() - 1).getId() + 1;
        }
        tarea.setId(nuevo_id);
        lista_tareas.add(tarea);
        guardar_cambios();
    }

    public void editar_tarea(Tarea tarea_editada) throws Exception {
        Optional<Tarea> encontrada = lista_tareas.stream()
                .filter(t -> t.getId() == tarea_editada.getId())
                .findFirst();
        if (encontrada.isPresent()) {
            Tarea t = encontrada.get();
            t.setNombre_tarea(tarea_editada.getNombre_tarea());
            t.setDescripcion(tarea_editada.getDescripcion());
            t.setFecha_vencimiento(tarea_editada.getFecha_vencimiento());
            t.setEstado_tarea(tarea_editada.getEstado_tarea());
            guardar_cambios();
        } else {
            throw new Exception("Tarea no encontrada para editar");
        }
    }

    public void eliminar_tarea(int id) throws Exception {
        boolean removed = lista_tareas.removeIf(t -> t.getId() == id);
        if (!removed) throw new Exception("No se pudo eliminar la tarea (no encontrada)");
        guardar_cambios();
    }

    public List<Tarea> buscar_por_nombre(String texto) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : lista_tareas) {
            if (t.getNombre_tarea().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public void guardar_cambios() throws Exception {
        tareaDAO.guardar_tareas(lista_tareas);
    }

    public void cargar_tareas() {
        try {
            lista_tareas = tareaDAO.cargar_tareas();
        } catch (Exception e) {
            System.err.println("No se pudieron cargar las tareas: " + e.getMessage());
            lista_tareas = new ArrayList<>();
        }
    }
}
