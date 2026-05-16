package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Materia;
import com.uniajc.servicios.MateriaService;

/**
 * Controlador para la entidad {@link Materia}.
 */
public class ControladorMateria {

    private final MateriaService materiaService;

    public ControladorMateria(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    public String registrarMateria(String nombre, int creditos) {
        try {
            Materia materia = new Materia(nombre.trim(), creditos);
            materiaService.registrarMateria(materia);
            return "✔ Materia registrada exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public List<Materia> listarMaterias() {
        return materiaService.obtenerTodasLasMaterias();
    }

    public Optional<Materia> buscarMateria(int id) {
        return materiaService.buscarMateriaPorId(id);
    }

    public String actualizarMateria(int id, String nombre, int creditos) {
        try {
            Materia materia = new Materia(id, nombre.trim(), creditos);
            materiaService.actualizarMateria(materia);
            return "✔ Materia actualizada exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public String eliminarMateria(int id) {
        try {
            materiaService.eliminarMateria(id);
            return "✔ Materia eliminada exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }
}
