package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Grupo;
import com.uniajc.servicios.GrupoService;

/**
 * Controlador para la entidad {@link Grupo}.
 */
public class ControladorGrupo {

    private final GrupoService grupoService;

    public ControladorGrupo(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    public String registrarGrupo(int idMateria, int idDocente, String aula, String horario) {
        try {
            Grupo grupo = new Grupo(idMateria, idDocente, aula.trim(), horario.trim());
            grupoService.registrarGrupo(grupo);
            return "✔ Grupo registrado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public List<Grupo> listarGrupos() {
        return grupoService.obtenerTodosLosGrupos();
    }

    public Optional<Grupo> buscarGrupo(int id) {
        return grupoService.buscarGrupoPorId(id);
    }

    public String actualizarGrupo(int id, int idMateria, int idDocente, String aula, String horario) {
        try {
            Grupo grupo = new Grupo(id, idMateria, idDocente, aula.trim(), horario.trim());
            grupoService.actualizarGrupo(grupo);
            return "✔ Grupo actualizado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public String eliminarGrupo(int id) {
        try {
            grupoService.eliminarGrupo(id);
            return "✔ Grupo eliminado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }
}
