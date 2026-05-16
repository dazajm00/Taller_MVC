package com.uniajc.modelo;

import java.time.LocalDateTime;

/**
 * Entidad Grupo – representa la tabla {@code practica_mvc.grupos}.
 * Un grupo asocia una materia con un docente, aula y horario.
 */
public class Grupo {

    private int id;
    private int idMateria;
    private int idDocente;
    private String aula;
    private String horario;
    private LocalDateTime fechaCreacion;

    // Campos desnormalizados de apoyo (para mostrar en la vista)
    private String nombreMateria;
    private String nombreDocente;

    // ── Constructores ──────────────────────────────────────────────────────────

    public Grupo() {}

    public Grupo(int id, int idMateria, int idDocente, String aula, String horario) {
        this.id        = id;
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.aula      = aula;
        this.horario   = horario;
    }

    public Grupo(int idMateria, int idDocente, String aula, String horario) {
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.aula      = aula;
        this.horario   = horario;
    }

    // ── Getters y Setters ──────────────────────────────────────────────────────

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public int getIdMateria()               { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }

    public int getIdDocente()               { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public String getAula()                 { return aula; }
    public void setAula(String aula)        { this.aula = aula; }

    public String getHorario()              { return horario; }
    public void setHorario(String horario)  { this.horario = horario; }

    public LocalDateTime getFechaCreacion()             { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fecha)   { this.fechaCreacion = fecha; }

    public String getNombreMateria()                    { return nombreMateria; }
    public void setNombreMateria(String nombreMateria)  { this.nombreMateria = nombreMateria; }

    public String getNombreDocente()                    { return nombreDocente; }
    public void setNombreDocente(String nombreDocente)  { this.nombreDocente = nombreDocente; }

    @Override
    public String toString() {
        return "Grupo{id=" + id + ", idMateria=" + idMateria
               + ", idDocente=" + idDocente + ", aula='" + aula
               + "', horario='" + horario + "'}";
    }
}