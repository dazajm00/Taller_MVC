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
import com.uniajc.modelo.Docente;

/**
 * Capa de acceso a datos para {@link Docente}.
 * Opera sobre la tabla {@code practica_mvc.docentes}.
 */
public class DocenteDao {

    // ── CREATE ─────────────────────────────────────────────────────────────────

    public void guardar(Docente docente) {
        String sql = "INSERT INTO docentes (name, specialty, email) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getEspecialidad());
            pstmt.setString(3, docente.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar docente: " + e.getMessage(), e);
        }
    }

    // ── READ ALL ───────────────────────────────────────────────────────────────

    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id, name, specialty, email, created_at FROM docentes ORDER BY id";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearDocente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener docentes: " + e.getMessage(), e);
        }
        return lista;
    }

    // ── READ BY ID ─────────────────────────────────────────────────────────────

    public Optional<Docente> buscarPorId(int id) {
        String sql = "SELECT id, name, specialty, email, created_at FROM docentes WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearDocente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar docente por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Docente> buscarPorEmail(String email) {
        String sql = "SELECT id, name, specialty, email, created_at FROM docentes WHERE email = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearDocente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar docente por email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    public void actualizar(Docente docente) {
        String sql = "UPDATE docentes SET name = ?, specialty = ?, email = ? WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getEspecialidad());
            pstmt.setString(3, docente.getEmail());
            pstmt.setInt(4, docente.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Docente no encontrado con ID: " + docente.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar docente: " + e.getMessage(), e);
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    public void eliminar(int id) {
        String sql = "DELETE FROM docentes WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Docente no encontrado con ID: " + id);

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar docente: " + e.getMessage(), e);
        }
    }

    // ── HELPER ────────────────────────────────────────────────────────────────

    private Docente mapearDocente(ResultSet rs) throws SQLException {
        Docente d = new Docente();
        d.setId(rs.getInt("id"));
        d.setNombre(rs.getString("name"));
        d.setEspecialidad(rs.getString("specialty"));
        d.setEmail(rs.getString("email"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) d.setFechaCreacion(ts.toLocalDateTime());
        return d;
    }
}
