package com.uniajc.controlador;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Inscripcion;
import com.uniajc.modelo.Inscripcion.Estado;
import com.uniajc.servicios.InscripcionService;

/**
 * Controlador para la entidad {@link Inscripcion}.
 */
public class ControladorInscripcion {

    private final InscripcionService inscripcionService;

    public ControladorInscripcion(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    public String registrarInscripcion(int idEstudiante, int idGrupo) {
        try {
            Inscripcion inscripcion = new Inscripcion(idEstudiante, idGrupo);
            inscripcionService.registrarInscripcion(inscripcion);
            return "✔ Inscripción registrada exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public List<Inscripcion> listarInscripciones() {
        return inscripcionService.obtenerTodasLasInscripciones();
    }

    public Optional<Inscripcion> buscarInscripcion(int id) {
        return inscripcionService.buscarInscripcionPorId(id);
    }

    /**
     * Actualiza una inscripción. notaFinalStr puede ser "" o null si no se asigna nota aún.
     */
    public String actualizarInscripcion(int id, int idEstudiante, int idGrupo,
                                        String notaFinalStr, String estadoStr) {
        try {
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setId(id);
            inscripcion.setIdEstudiante(idEstudiante);
            inscripcion.setIdGrupo(idGrupo);

            if (notaFinalStr != null && !notaFinalStr.isBlank()) {
                inscripcion.setNotaFinal(new BigDecimal(notaFinalStr.trim().replace(",", ".")));
            }

            if (estadoStr != null && !estadoStr.isBlank()) {
                try {
                    inscripcion.setEstado(Estado.valueOf(estadoStr.trim().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    return "⚠ Estado inválido. Use: ACTIVO, APROBADO, REPROBADO o CANCELADO.";
                }
            } else {
                inscripcion.setEstado(Estado.ACTIVO);
            }

            inscripcionService.actualizarInscripcion(inscripcion);
            return "✔ Inscripción actualizada exitosamente.";
        } catch (NumberFormatException e) {
            return "⚠ La nota debe ser un número decimal válido (ej: 4.5).";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public String eliminarInscripcion(int id) {
        try {
            inscripcionService.eliminarInscripcion(id);
            return "✔ Inscripción eliminada exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }
}
