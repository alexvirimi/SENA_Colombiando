package com.colombiando.dao;

import com.colombiando.conexion.ConexionDB;
import com.colombiando.modelo.Destino;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Destino}.
 */
public class DestinoDAO implements GenericoDAO<Destino, Integer> {

    private static final Logger LOGGER = Logger.getLogger(DestinoDAO.class.getName());

    private static final String SQL_INSERTAR =
            "INSERT INTO destino (nombre, departamento, municipio, descripcion, clima, imagen_url) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE destino SET nombre=?, departamento=?, municipio=?, " +
            "descripcion=?, clima=?, imagen_url=? WHERE id_destino=?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM destino WHERE id_destino=?";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM destino WHERE id_destino=?";

    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM destino ORDER BY departamento, nombre";

    private static final String SQL_BUSCAR_POR_DEPARTAMENTO =
            "SELECT * FROM destino WHERE departamento=? ORDER BY nombre";

    private static final String SQL_LISTAR_POR_TOUR =
            "SELECT d.* FROM destino d " +
            "JOIN tour_destino td ON d.id_destino = td.id_destino " +
            "WHERE td.id_tour=?";

    @Override
    public boolean insertar(Destino destino) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, destino.getNombre());
            ps.setString(2, destino.getDepartamento());
            ps.setString(3, destino.getMunicipio());
            ps.setString(4, destino.getDescripcion());
            ps.setString(5, destino.getClima());
            ps.setString(6, destino.getImagenUrl());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) destino.setIdDestino(gk.getInt(1));
                }
                LOGGER.info("✅ Destino insertado: " + destino.getNombre());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al insertar destino.", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean actualizar(Destino destino) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, destino.getNombre());
            ps.setString(2, destino.getDepartamento());
            ps.setString(3, destino.getMunicipio());
            ps.setString(4, destino.getDescripcion());
            ps.setString(5, destino.getClima());
            ps.setString(6, destino.getImagenUrl());
            ps.setInt(7, destino.getIdDestino());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("✅ Destino actualizado: ID " + destino.getIdDestino());
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al actualizar destino.", e);
            throw e;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("🗑️ Destino eliminado: ID " + id);
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al eliminar destino.", e);
            throw e;
        }
    }

    @Override
    public Optional<Destino> buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearDestino(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al buscar destino.", e);
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public List<Destino> listarTodos() throws SQLException {
        List<Destino> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapearDestino(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al listar destinos.", e);
            throw e;
        }
        return lista;
    }

    public List<Destino> listarPorDepartamento(String departamento) throws SQLException {
        List<Destino> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_DEPARTAMENTO)) {
            ps.setString(1, departamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearDestino(rs));
            }
        }
        return lista;
    }

    public List<Destino> listarDestinosPorTour(int idTour) throws SQLException {
        List<Destino> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_TOUR)) {
            ps.setInt(1, idTour);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearDestino(rs));
            }
        }
        return lista;
    }

    private Destino mapearDestino(ResultSet rs) throws SQLException {
        Destino d = new Destino();
        d.setIdDestino(rs.getInt("id_destino"));
        d.setNombre(rs.getString("nombre"));
        d.setDepartamento(rs.getString("departamento"));
        d.setMunicipio(rs.getString("municipio"));
        d.setDescripcion(rs.getString("descripcion"));
        d.setClima(rs.getString("clima"));
        d.setImagenUrl(rs.getString("imagen_url"));
        return d;
    }
}
