package com.uniajc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionDatabase;
import com.uniajc.modelo.Inscripcion;
import com.uniajc.modelo.Inscripcion.Estado;

/**
 * Capa de acceso a datos para {@link Inscripcion}.
 * Opera sobre la tabla {@code practica_mvc.inscripciones}.
 */
public class InscripcionDao {

    private static final String SELECT_BASE =
        "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, i.created_at, " +
        "CONCAT(e.name, ' ', e.lastname) AS nombre_estudiante, " +
        "CONCAT(m.name, ' – ', g.classroom) AS info_grupo " +
        "FROM inscripciones i " +
        "JOIN estudiantes e ON i.id_estudiante = e.id " +
        "JOIN grupos g ON i.id_grupo = g.id " +
        "JOIN materias m ON g.id_materia = m.id";

    // ── CREATE ─────────────────────────────────────────────────────────────────

    public void guardar(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripciones (id_estudiante, id_grupo, final_grade, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcion.getIdEstudiante());
            pstmt.setInt(2, inscripcion.getIdGrupo());

            if (inscripcion.getNotaFinal() != null) {
                pstmt.setBigDecimal(3, inscripcion.getNotaFinal());
            } else {
                pstmt.setNull(3, Types.DECIMAL);
            }

            String estadoStr = inscripcion.getEstado() != null
                    ? inscripcion.getEstado().name()
                    : Estado.ACTIVO.name();
            pstmt.setString(4, estadoStr);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar inscripción: " + e.getMessage(), e);
        }
    }

    // ── READ ALL ───────────────────────────────────────────────────────────────

    public List<Inscripcion> obtenerTodos() {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY i.id";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) lista.add(mapearInscripcion(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener inscripciones: " + e.getMessage(), e);
        }
        return lista;
    }

    // ── READ BY ID ─────────────────────────────────────────────────────────────

    public Optional<Inscripcion> buscarPorId(int id) {
        String sql = SELECT_BASE + " WHERE i.id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearInscripcion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar inscripción: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────────

    public void actualizar(Inscripcion inscripcion) {
        String sql = "UPDATE inscripciones SET id_estudiante = ?, id_grupo = ?, final_grade = ?, status = ? WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcion.getIdEstudiante());
            pstmt.setInt(2, inscripcion.getIdGrupo());

            if (inscripcion.getNotaFinal() != null) {
                pstmt.setBigDecimal(3, inscripcion.getNotaFinal());
            } else {
                pstmt.setNull(3, Types.DECIMAL);
            }

            pstmt.setString(4, inscripcion.getEstado().name());
            pstmt.setInt(5, inscripcion.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Inscripción no encontrada con ID: " + inscripcion.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar inscripción: " + e.getMessage(), e);
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────────

    public void eliminar(int id) {
        String sql = "DELETE FROM inscripciones WHERE id = ?";

        try (Connection conn = ConexionDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) throw new RuntimeException("Inscripción no encontrada con ID: " + id);

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar inscripción: " + e.getMessage(), e);
        }
    }

    // ── HELPER ────────────────────────────────────────────────────────────────

    private Inscripcion mapearInscripcion(ResultSet rs) throws SQLException {
        Inscripcion i = new Inscripcion();
        i.setId(rs.getInt("id"));
        i.setIdEstudiante(rs.getInt("id_estudiante"));
        i.setIdGrupo(rs.getInt("id_grupo"));

        BigDecimal nota = rs.getBigDecimal("final_grade");
        if (!rs.wasNull()) i.setNotaFinal(nota);

        String estadoStr = rs.getString("status");
        if (estadoStr != null) {
            try { i.setEstado(Estado.valueOf(estadoStr)); }
            catch (IllegalArgumentException ex) { i.setEstado(Estado.ACTIVO); }
        }

        i.setNombreEstudiante(rs.getString("nombre_estudiante"));
        i.setInfoGrupo(rs.getString("info_grupo"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) i.setFechaCreacion(ts.toLocalDateTime());
        return i;
    }
}