package com.colombiando.dao;

import com.colombiando.conexion.ConexionDB;
import com.colombiando.modelo.Idioma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Idioma}.
 * También gestiona la tabla de unión {@code habla} (Empleado ↔ Idioma).
 */
public class IdiomaDAO implements GenericoDAO<Idioma, Integer> {

    private static final Logger LOGGER = Logger.getLogger(IdiomaDAO.class.getName());

    private static final String SQL_INSERTAR =
            "INSERT INTO idioma (nombre, codigo_iso, nivel_requerido) VALUES (?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE idioma SET nombre=?, codigo_iso=?, nivel_requerido=? WHERE id_idioma=?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM idioma WHERE id_idioma=?";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM idioma WHERE id_idioma=?";

    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM idioma ORDER BY nombre";

    // Tabla HABLA (empleado_idioma)
    private static final String SQL_ASIGNAR_IDIOMA_EMPLEADO =
            "INSERT IGNORE INTO habla (id_empleado, id_idioma, nivel) VALUES (?, ?, ?)";

    private static final String SQL_QUITAR_IDIOMA_EMPLEADO =
            "DELETE FROM habla WHERE id_empleado=? AND id_idioma=?";

    private static final String SQL_LISTAR_IDIOMAS_EMPLEADO =
            "SELECT i.*, h.nivel FROM idioma i " +
            "JOIN habla h ON i.id_idioma = h.id_idioma " +
            "WHERE h.id_empleado=?";

    private static final String SQL_LISTAR_EMPLEADOS_POR_IDIOMA =
            "SELECT id_empleado FROM habla WHERE id_idioma=?";

    @Override
    public boolean insertar(Idioma idioma) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, idioma.getNombre());
            ps.setString(2, idioma.getCodigoIso());
            ps.setString(3, idioma.getNivelRequerido());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) idioma.setIdIdioma(gk.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al insertar idioma.", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean actualizar(Idioma idioma) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, idioma.getNombre());
            ps.setString(2, idioma.getCodigoIso());
            ps.setString(3, idioma.getNivelRequerido());
            ps.setInt(4, idioma.getIdIdioma());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al actualizar idioma.", e);
            throw e;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al eliminar idioma.", e);
            throw e;
        }
    }

    @Override
    public Optional<Idioma> buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearIdioma(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Idioma> listarTodos() throws SQLException {
        List<Idioma> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearIdioma(rs));
        }
        return lista;
    }

    // ── Tabla HABLA ──────────────────────────────────────────────────────────

    public boolean asignarIdiomaAEmpleado(int idEmpleado, int idIdioma, String nivel) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ASIGNAR_IDIOMA_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idIdioma);
            ps.setString(3, nivel);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean quitarIdiomaDeEmpleado(int idEmpleado, int idIdioma) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_QUITAR_IDIOMA_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idIdioma);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Idioma> listarIdiomasDeEmpleado(int idEmpleado) throws SQLException {
        List<Idioma> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_IDIOMAS_EMPLEADO)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Idioma idioma = mapearIdioma(rs);
                    // El nivel específico del empleado sobreescribe el nivel requerido
                    idioma.setNivelRequerido(rs.getString("nivel"));
                    lista.add(idioma);
                }
            }
        }
        return lista;
    }

    private Idioma mapearIdioma(ResultSet rs) throws SQLException {
        Idioma i = new Idioma();
        i.setIdIdioma(rs.getInt("id_idioma"));
        i.setNombre(rs.getString("nombre"));
        i.setCodigoIso(rs.getString("codigo_iso"));
        i.setNivelRequerido(rs.getString("nivel_requerido"));
        return i;
    }
}
