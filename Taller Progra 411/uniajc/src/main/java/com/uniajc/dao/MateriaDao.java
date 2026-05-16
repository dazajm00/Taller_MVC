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
import com.uniajc.modelo.Materia;

/**
 * Capa de acceso a datos para {@link Materia}.
 * Opera sobre la tabla {@code practica_mvc.materias}.
 */
public class MateriaDao {

    // ── CREATE ─────────────────────────────────────────────────────────────────

    public void guardar(Materia materia) {
        String sql = "INSERT INTO materias (name, credits) VALUES (?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombre());
            pstmt.setInt(2, materia.getCreditos());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar materia: " + e.getMessage(), e);
        }
    }

    // ── READ ALL ───────────────────────────────────────────────────────────────

    public List<Materia> obtenerTodos() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id, name, credits, created_at FROM materias ORDER BY id";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) lista.add(mapearMateria(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener materias: " + e.getMessage(), e);
        }
        return lista;
    }

    // ── READ BY ID ─────────────────────────────────────────────────────────────

    public Optional<Materia> buscarPorId(int id) {
        String sql = "SELECT id, name, credits, created_at FROM materias WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearMateria(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar materia: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    public void actualizar(Materia materia) {
        String sql = "UPDATE materias SET name = ?, credits = ? WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombre());
            pstmt.setInt(2, materia.getCreditos());
            pstmt.setInt(3, materia.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Materia no encontrada con ID: " + materia.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar materia: " + e.getMessage(), e);
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    public void eliminar(int id) {
        String sql = "DELETE FROM materias WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Materia no encontrada con ID: " + id);

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar materia: " + e.getMessage(), e);
        }
    }

    // ── HELPER ────────────────────────────────────────────────────────────────

    private Materia mapearMateria(ResultSet rs) throws SQLException {
        Materia m = new Materia();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("name"));
        m.setCreditos(rs.getInt("credits"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) m.setFechaCreacion(ts.toLocalDateTime());
        return m;
    }
}