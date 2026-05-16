package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

/**
 * Controlador para la entidad {@link Estudiante}.
 * Orquesta la comunicación entre la Vista y el Servicio.
 * No contiene lógica de negocio ni de persistencia.
 */
public class ControladorEstudiante {

    private final EstudianteService estudianteService;

    public ControladorEstudiante(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    // ── Acciones CRUD ─────────────────────────────────────────────────────────

    /**
     * Registra un nuevo estudiante.
     *
     * @param nombre   nombre del estudiante.
     * @param apellido apellido del estudiante.
     * @param email    correo electrónico único.
     * @return mensaje de resultado para la vista.
     */
    public String registrarEstudiante(String nombre, String apellido, String email) {
        try {
            Estudiante estudiante = new Estudiante(nombre.trim(), apellido.trim(), email.trim());
            estudianteService.registrarEstudiante(estudiante);
            return "✔ Estudiante registrado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    /**
     * Retorna todos los estudiantes registrados.
     */
    public List<Estudiante> listarEstudiantes() {
        return estudianteService.obtenerTodosLosEstudiantes();
    }

    /**
     * Busca un estudiante por id.
     */
    public Optional<Estudiante> buscarEstudiante(int id) {
        return estudianteService.buscarEstudiantePorId(id);
    }

    /**
     * Actualiza un estudiante existente.
     *
     * @return mensaje de resultado para la vista.
     */
    public String actualizarEstudiante(int id, String nombre, String apellido, String email) {
        try {
            Estudiante estudiante = new Estudiante(id, nombre.trim(), apellido.trim(), email.trim());
            estudianteService.actualizarEstudiante(estudiante);
            return "✔ Estudiante actualizado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    /**
     * Elimina un estudiante por id.
     *
     * @return mensaje de resultado para la vista.
     */
    public String eliminarEstudiante(int id) {
        try {
            estudianteService.eliminarEstudiante(id);
            return "✔ Estudiante eliminado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }
}