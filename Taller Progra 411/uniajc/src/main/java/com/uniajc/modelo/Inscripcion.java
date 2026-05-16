package com.uniajc.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Inscripcion – representa la tabla {@code practica_mvc.inscripciones}.
 */
public class Inscripcion {

    public enum Estado { ACTIVO, APROBADO, REPROBADO, CANCELADO }

    private int id;
    private int idEstudiante;
    private int idGrupo;
    private BigDecimal notaFinal;
    private Estado estado;
    private LocalDateTime fechaCreacion;

    // Campos desnormalizados de apoyo
    private String nombreEstudiante;
    private String infoGrupo;

    // ── Constructores ──────────────────────────────────────────────────────────

    public Inscripcion() {}

    public Inscripcion(int id, int idEstudiante, int idGrupo, BigDecimal notaFinal, Estado estado) {
        this.id           = id;
        this.idEstudiante = idEstudiante;
        this.idGrupo      = idGrupo;
        this.notaFinal    = notaFinal;
        this.estado       = estado;
    }

    public Inscripcion(int idEstudiante, int idGrupo) {
        this.idEstudiante = idEstudiante;
        this.idGrupo      = idGrupo;
        this.estado       = Estado.ACTIVO;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public int getIdEstudiante()                { return idEstudiante; }
    public void setIdEstudiante(int id)         { this.idEstudiante = id; }

    public int getIdGrupo()                     { return idGrupo; }
    public void setIdGrupo(int id)              { this.idGrupo = id; }

    public BigDecimal getNotaFinal()                { return notaFinal; }
    public void setNotaFinal(BigDecimal notaFinal)  { this.notaFinal = notaFinal; }

    public Estado getEstado()                   { return estado; }
    public void setEstado(Estado estado)        { this.estado = estado; }

    public LocalDateTime getFechaCreacion()             { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fecha)   { this.fechaCreacion = fecha; }

    public String getNombreEstudiante()                     { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante){ this.nombreEstudiante = nombreEstudiante; }

    public String getInfoGrupo()                    { return infoGrupo; }
    public void setInfoGrupo(String infoGrupo)      { this.infoGrupo = infoGrupo; }

    @Override
    public String toString() {
        return "Inscripcion{id=" + id + ", idEstudiante=" + idEstudiante
               + ", idGrupo=" + idGrupo + ", estado=" + estado + "}";
    }
}
