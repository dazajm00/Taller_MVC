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
import com.uniajc.modelo.Grupo;

/**
 * Capa de acceso a datos para {@link Grupo}.
 * Opera sobre la tabla {@code practica_mvc.grupos}.
 * Hace JOIN con materias y docentes para obtener los nombres en la vista.
 */
public class GrupoDao {

    private static final String SELECT_BASE =
        "SELECT g.id, g.id_materia, g.id_docente, g.classroom, g.schedule, g.created_at, " +
        "m.name AS nombre_materia, d.name AS nombre_docente " +
        "FROM grupos g " +
        "JOIN materias m ON g.id_materia = m.id " +
        "JOIN docentes d ON g.id_docente = d.id";

    // ── CREATE ─────────────────────────────────────────────────────────────────

    public void guardar(Grupo grupo) {
        String sql = "INSERT INTO grupos (id_materia, id_docente, classroom, schedule) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grupo.getIdMateria());
            pstmt.setInt(2, grupo.getIdDocente());
            pstmt.setString(3, grupo.getAula());
            pstmt.setString(4, grupo.getHorario());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar grupo: " + e.getMessage(), e);
        }
    }

    // ── READ ALL ───────────────────────────────────────────────────────────────

    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY g.id";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) lista.add(mapearGrupo(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener grupos: " + e.getMessage(), e);
        }
        return lista;
    }

    // ── READ BY ID ─────────────────────────────────────────────────────────────

    public Optional<Grupo> buscarPorId(int id) {
        String sql = SELECT_BASE + " WHERE g.id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearGrupo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar grupo: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    public void actualizar(Grupo grupo) {
        String sql = "UPDATE grupos SET id_materia = ?, id_docente = ?, classroom = ?, schedule = ? WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grupo.getIdMateria());
            pstmt.setInt(2, grupo.getIdDocente());
            pstmt.setString(3, grupo.getAula());
            pstmt.setString(4, grupo.getHorario());
            pstmt.setInt(5, grupo.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Grupo no encontrado con ID: " + grupo.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar grupo: " + e.getMessage(), e);
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    public void eliminar(int id) {
        String sql = "DELETE FROM grupos WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Grupo no encontrado con ID: " + id);

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar grupo: " + e.getMessage(), e);
        }
    }

    // ── HELPER ────────────────────────────────────────────────────────────────

    private Grupo mapearGrupo(ResultSet rs) throws SQLException {
        Grupo g = new Grupo();
        g.setId(rs.getInt("id"));
        g.setIdMateria(rs.getInt("id_materia"));
        g.setIdDocente(rs.getInt("id_docente"));
        g.setAula(rs.getString("classroom"));
        g.setHorario(rs.getString("schedule"));
        g.setNombreMateria(rs.getString("nombre_materia"));
        g.setNombreDocente(rs.getString("nombre_docente"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) g.setFechaCreacion(ts.toLocalDateTime());
        return g;
    }
}