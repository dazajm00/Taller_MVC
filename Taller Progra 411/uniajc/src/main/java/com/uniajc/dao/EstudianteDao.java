package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionDatabase;
import com.uniajc.modelo.Estudiante;

/**
 * Capa de acceso a datos para {@link Estudiante}.
 * Opera sobre la tabla {@code practica_mvc.estudiantes}.
 */
public class EstudianteDao {

    // ── CREATE ─────────────────────────────────────────────────────────────────

    /**
     * Inserta un nuevo estudiante en la base de datos.
     *
     * @param estudiante objeto con los datos a persistir (id ignorado).
     */
    public void guardar(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes (name, lastname, email) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar estudiante: " + e.getMessage(), e);
        }
    }

    // ── READ ALL ───────────────────────────────────────────────────────────────

    /**
     * Retorna la lista completa de estudiantes registrados.
     */
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email, created_at FROM estudiantes ORDER BY id";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEstudiante(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener estudiantes: " + e.getMessage(), e);
        }
        return lista;
    }

    // ── READ BY ID ─────────────────────────────────────────────────────────────

    /**
     * Busca un estudiante por su clave primaria.
     *
     * @param id identificador del estudiante.
     * @return {@link Optional} con el estudiante, o vacío si no existe.
     */
    public Optional<Estudiante> buscarPorId(int id) {
        String sql = "SELECT id, name, lastname, email, created_at FROM estudiantes WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar estudiante por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    /**
     * Busca un estudiante por email.
     */
    public Optional<Estudiante> buscarPorEmail(String email) {
        String sql = "SELECT id, name, lastname, email, created_at FROM estudiantes WHERE email = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar estudiante por email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    /**
     * Actualiza los datos de un estudiante existente.
     *
     * @param estudiante objeto con id y nuevos valores.
     */
    public void actualizar(Estudiante estudiante) {
        String sql = "UPDATE estudiantes SET name = ?, lastname = ?, email = ? WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getEmail());
            pstmt.setInt(4, estudiante.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Estudiante no encontrado con ID: " + estudiante.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estudiante: " + e.getMessage(), e);
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    /**
     * Elimina un estudiante por su id.
     *
     * @param id identificador del estudiante a eliminar.
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Estudiante no encontrado con ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar estudiante: " + e.getMessage(), e);
        }
    }

    // ── HELPER PRIVADO ─────────────────────────────────────────────────────────

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link Estudiante}.
     */
    private Estudiante mapearEstudiante(ResultSet rs) throws SQLException {
        Estudiante e = new Estudiante();
        e.setId(rs.getInt("id"));
        e.setNombre(rs.getString("name"));
        e.setApellido(rs.getString("lastname"));
        e.setEmail(rs.getString("email"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            e.setFechaCreacion(ts.toLocalDateTime());
        }
        return e;
    }
}