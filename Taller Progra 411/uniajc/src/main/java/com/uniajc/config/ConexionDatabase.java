package com.uniajc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuración de conexión a MySQL para el Sistema de Gestión Académica UNIAJC.
 * Gestiona la conexión JDBC con la base de datos MySQL.
 */
public class ConexionDatabase {

    // ── Parámetros de conexión ─────────────────────────────────────────────────
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "practica_mvc";   
    private static final String USER     = "root";           
    private static final String PASSWORD = "Kamila1234*";               

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota";

    // Constructor privado: clase de utilidad, no se instancia
    private ConexionDatabase() {}

    /**
     * Devuelve una nueva conexión JDBC a MySQL.
     * El llamador es responsable de cerrarla (preferiblemente en un try-with-resources).
     *
     * @return {@link Connection} abierta.
     * @throws RuntimeException si el driver no se carga o la conexión falla.
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Driver MySQL no encontrado. Asegúrate de agregar mysql-connector-j al pom.xml.", e);
        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo conectar a MySQL: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica que la conexión esté activa (útil para pruebas de arranque).
     */
    public static boolean probarConexion() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            return false;
        }
    }
}