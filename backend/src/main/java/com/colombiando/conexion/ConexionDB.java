package com.colombiando.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestiona la conexión JDBC a MySQL para Colombiando Web.
 *
 * <p>Las credenciales se leen desde {@code db.properties} ubicado en el
 * classpath, lo que evita datos sensibles en el código fuente.</p>
 *
 * <p>Implementa el patrón <b>Singleton</b> con re-conexión automática
 * cuando la conexión ha sido cerrada o invalidada.</p>
 */
public final class ConexionDB {

    private static final Logger     LOGGER     = Logger.getLogger(ConexionDB.class.getName());
    private static final String     PROPS_FILE = "db.properties";
    private static       Properties propiedades;
    private static       Connection instancia;

    static {
        cargarPropiedades();
    }

    private ConexionDB() {}

    // ── Carga de propiedades ─────────────────────────────────────────────────

    private static void cargarPropiedades() {
        propiedades = new Properties();
        try (InputStream in = ConexionDB.class
                .getClassLoader()
                .getResourceAsStream(PROPS_FILE)) {

            if (in == null) {
                throw new RuntimeException(
                        "❌ No se encontró el archivo: " + PROPS_FILE);
            }
            propiedades.load(in);
            // Registrar driver una sola vez
            Class.forName(propiedades.getProperty("db.driver"));
            LOGGER.info("✅ Propiedades JDBC cargadas correctamente.");

        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "❌ Error cargando propiedades de BD.", e);
            throw new RuntimeException("Error al inicializar ConexionDB.", e);
        }
    }

    // ── Obtener conexión (Singleton con auto-reconexión) ─────────────────────

    /**
     * Retorna la conexión activa, creándola o reconectándola si es necesario.
     *
     * @return objeto {@link Connection} listo para usar
     * @throws SQLException si no se puede establecer la conexión
     */
    public static synchronized Connection obtenerConexion() throws SQLException {
        try {
            if (instancia == null || instancia.isClosed() || !instancia.isValid(2)) {
                String url      = propiedades.getProperty("db.url");
                String usuario  = propiedades.getProperty("db.usuario");
                String password = propiedades.getProperty("db.contrasena");

                instancia = DriverManager.getConnection(url, usuario, password);
                instancia.setAutoCommit(true);
                LOGGER.info("✅ Conexión MySQL establecida.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al conectar con MySQL: " + e.getMessage(), e);
            throw e;
        }
        return instancia;
    }

    /**
     * Cierra la conexión activa de forma segura.
     */
    public static synchronized void cerrarConexion() {
        if (instancia != null) {
            try {
                if (!instancia.isClosed()) {
                    instancia.close();
                    LOGGER.info("🔌 Conexión MySQL cerrada.");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "⚠️ Error al cerrar conexión.", e);
            } finally {
                instancia = null;
            }
        }
    }

    /**
     * Verifica si la conexión actual es válida.
     *
     * @return {@code true} si la conexión está activa y responde
     */
    public static boolean isConectado() {
        try {
            return instancia != null && !instancia.isClosed() && instancia.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }
}
