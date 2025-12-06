package com.miempresa.gestortareas.dao;

import com.miempresa.gestortareas.negocio.Tarea;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {

    private final Path ruta_archivo = Paths.get("data", "tareas.dat");

    public TareaDAO() {
        try {
            Path carpeta = ruta_archivo.getParent();
            if (carpeta != null && !Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }
            if (!Files.exists(ruta_archivo)) {
                try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(ruta_archivo, StandardOpenOption.CREATE))) {
                    oos.writeObject(new ArrayList<Tarea>());
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo crear archivo de datos: " + e.getMessage());
        }
    }

    public void guardar_tareas(List<Tarea> lista) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(ruta_archivo, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(new ArrayList<>(lista));
        } catch (IOException e) {
            throw new Exception("Error guardando tareas: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Tarea> cargar_tareas() throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(ruta_archivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Tarea>) obj;
            } else {
                return new ArrayList<>();
            }
        } catch (EOFException eof) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error cargando tareas: " + e.getMessage(), e);
        }
    }
}
