package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;

/**
 * Controlador para la entidad {@link Docente}.
 */
public class ControladorDocente {

    private final DocenteService docenteService;

    public ControladorDocente(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    public String registrarDocente(String nombre, String especialidad, String email) {
        try {
            Docente docente = new Docente(nombre.trim(), especialidad.trim(), email.trim());
            docenteService.registrarDocente(docente);
            return "✔ Docente registrado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public List<Docente> listarDocentes() {
        return docenteService.obtenerTodosLosDocentes();
    }

    public Optional<Docente> buscarDocente(int id) {
        return docenteService.buscarDocentePorId(id);
    }

    public String actualizarDocente(int id, String nombre, String especialidad, String email) {
        try {
            Docente docente = new Docente(id, nombre.trim(), especialidad.trim(), email.trim());
            docenteService.actualizarDocente(docente);
            return "✔ Docente actualizado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }

    public String eliminarDocente(int id) {
        try {
            docenteService.eliminarDocente(id);
            return "✔ Docente eliminado exitosamente.";
        } catch (IllegalArgumentException e) {
            return "⚠ " + e.getMessage();
        } catch (Exception e) {
            return "✘ Error inesperado: " + e.getMessage();
        }
    }
}
