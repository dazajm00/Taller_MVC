package com.uniajc.modelo;

import java.time.LocalDateTime;

/**
 * Entidad Estudiante – representa la tabla {@code practica_mvc.estudiantes}.
 */
public class Estudiante {

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDateTime fechaCreacion;

    // ── Constructores ──────────────────────────────────────────────────────────

    public Estudiante() {}

    public Estudiante(int id, String nombre, String apellido, String email) {
        this.id       = id;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.email    = email;
    }

    public Estudiante(String nombre, String apellido, String email) {
        this.nombre   = nombre;
        this.apellido = apellido;
        this.email    = email;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public String getNombre()               { return nombre; }
    public void setNombre(String nombre)    { this.nombre = nombre; }

    public String getApellido()             { return apellido; }
    public void setApellido(String apellido){ this.apellido = apellido; }

    public String getEmail()                { return email; }
    public void setEmail(String email)      { this.email = email; }

    public LocalDateTime getFechaCreacion()             { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fecha)   { this.fechaCreacion = fecha; }

    @Override
    public String toString() {
        return "Estudiante{id=" + id + ", nombre='" + nombre + " " + apellido
               + "', email='" + email + "'}";
    }
}