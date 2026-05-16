package com.uniajc.modelo;

import java.time.LocalDateTime;

/**
 * Entidad Docente – representa la tabla {@code practica_mvc.docentes}.
 */
public class Docente {

    private int id;
    private String nombre;
    private String especialidad;
    private String email;
    private LocalDateTime fechaCreacion;

    // ── Constructores ──────────────────────────────────────────────────────────

    public Docente() {}

    public Docente(int id, String nombre, String especialidad, String email) {
        this.id           = id;
        this.nombre       = nombre;
        this.especialidad = especialidad;
        this.email        = email;
    }

    public Docente(String nombre, String especialidad, String email) {
        this.nombre       = nombre;
        this.especialidad = especialidad;
        this.email        = email;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }

    public String getNombre()                       { return nombre; }
    public void setNombre(String nombre)            { this.nombre = nombre; }

    public String getEspecialidad()                 { return especialidad; }
    public void setEspecialidad(String especialidad){ this.especialidad = especialidad; }

    public String getEmail()                        { return email; }
    public void setEmail(String email)              { this.email = email; }

    public LocalDateTime getFechaCreacion()                 { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion){ this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return "Docente{id=" + id + ", nombre='" + nombre
               + "', especialidad='" + especialidad + "', email='" + email + "'}";
    }
}