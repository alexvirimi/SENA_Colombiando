package com.colombiando.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestiona la conexión JDBC a la base de datos MySQL de Colombiando.
 * Implementa el patrón Singleton para reutilizar una única conexión.
 *
 * <p>Configuración: ajusta las constantes {@code URL}, {@code USUARIO}
 * y {@code CONTRASENA} según tu entorno, o extrae los valores a un
 * archivo {@code db.properties} para evitar credenciales en el código.</p>
 */
public final class ConexionDB {

    private static final Logger LOGGER = Logger.getLogger(ConexionDB.class.getName());

    // ── Parámetros de conexión ───────────────────────────────────────────────
    private static final String DRIVER    = "com.mysql.cj.jdbc.Driver";
    private static final String HOST      = "localhost";
    private static final int    PUERTO    = 3306;
    private static final String BASE_DATOS = "colombiando_db";
    private static final String URL       = String.format(
            "jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=America/Bogota&useUnicode=true&characterEncoding=UTF-8",
            HOST, PUERTO, BASE_DATOS);
    private static final String USUARIO   = "root";          // ← cambia según tu entorno
    private static final String CONTRASENA = "121314"; // ← cambia según tu entorno

    private static Connection instancia = null;

    // Constructor privado: impide instanciación directa
    private ConexionDB() {}

    /**
     * Retorna la conexión única (Singleton).
     * Si la conexión está cerrada o es nula, la crea nuevamente.
     *
     * @return objeto {@link Connection} activo
     * @throws SQLException si no se puede establecer la conexión
     */
    public static Connection obtenerConexion() throws SQLException {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName(DRIVER);
                instancia = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                instancia.setAutoCommit(true);
                LOGGER.info("Conexión establecida con la base de datos: " + BASE_DATOS);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver JDBC no encontrado: " + DRIVER, e);
            throw new SQLException("Driver JDBC no encontrado.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos.", e);
            throw e;
        }
        return instancia;
    }

    /**
     * Cierra la conexión activa si existe y está abierta.
     */
    public static void cerrarConexion() {
        if (instancia != null) {
            try {
                if (!instancia.isClosed()) {
                    instancia.close();
                    LOGGER.info("🔌 Conexión cerrada correctamente.");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar la conexión.", e);
            } finally {
                instancia = null;
            }
        }
    }

    /**
     * Verifica si la conexión actual sigue activa.
     *
     * @return {@code true} si la conexión es válida, {@code false} en caso contrario
     */
    public static boolean isConectado() {
        try {
            return instancia != null && !instancia.isClosed() && instancia.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
