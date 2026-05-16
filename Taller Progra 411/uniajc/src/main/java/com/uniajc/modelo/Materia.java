package com.uniajc.modelo;

import java.time.LocalDateTime;

/**
 * Entidad Materia – representa la tabla {@code practica_mvc.materias}.
 */
public class Materia {

    private int id;
    private String nombre;
    private int creditos;
    private LocalDateTime fechaCreacion;

    // ── Constructores ──────────────────────────────────────────────────────────

    public Materia() {}

    public Materia(int id, String nombre, int creditos) {
        this.id       = id;
        this.nombre   = nombre;
        this.creditos = creditos;
    }

    public Materia(String nombre, int creditos) {
        this.nombre   = nombre;
        this.creditos = creditos;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public String getNombre()               { return nombre; }
    public void setNombre(String nombre)    { this.nombre = nombre; }

    public int getCreditos()                { return creditos; }
    public void setCreditos(int creditos)   { this.creditos = creditos; }

    public LocalDateTime getFechaCreacion()             { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fecha)   { this.fechaCreacion = fecha; }

    @Override
    public String toString() {
        return "Materia{id=" + id + ", nombre='" + nombre + "', creditos=" + creditos + "}";
    }
}