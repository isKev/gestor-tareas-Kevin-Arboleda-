package com.miempresa.gestortareas.negocio;

import java.io.Serializable;

public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre_tarea;
    private String descripcion;
    private String fecha_vencimiento;
    private String estado_tarea;

    public Tarea() {}

    public Tarea(int id, String nombre_tarea, String descripcion, String fecha_vencimiento, String estado_tarea) {
        this.id = id;
        this.nombre_tarea = nombre_tarea;
        this.descripcion = descripcion;
        this.fecha_vencimiento = fecha_vencimiento;
        this.estado_tarea = estado_tarea;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre_tarea() { return nombre_tarea; }
    public void setNombre_tarea(String nombre_tarea) { this.nombre_tarea = nombre_tarea; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getFecha_vencimiento() { return fecha_vencimiento; }
    public void setFecha_vencimiento(String fecha_vencimiento) { this.fecha_vencimiento = fecha_vencimiento; }
    public String getEstado_tarea() { return estado_tarea; }
    public void setEstado_tarea(String estado_tarea) { this.estado_tarea = estado_tarea; }
}
