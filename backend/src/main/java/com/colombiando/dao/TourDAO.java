package com.colombiando.dao;

import com.colombiando.conexion.ConexionDB;
import com.colombiando.modelo.Tour;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Tour}.
 * También gestiona las tablas de unión {@code tour_destino} y {@code tour_empleado}.
 */
public class TourDAO implements GenericoDAO<Tour, Integer> {

    private static final Logger LOGGER = Logger.getLogger(TourDAO.class.getName());

    private static final String SQL_INSERTAR =
            "INSERT INTO tour (nombre, descripcion, precio, duracion_dias, " +
            "capacidad_maxima, fecha_salida, fecha_regreso, estado) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE tour SET nombre=?, descripcion=?, precio=?, duracion_dias=?, " +
            "capacidad_maxima=?, fecha_salida=?, fecha_regreso=?, estado=? " +
            "WHERE id_tour=?";

    private static final String SQL_ELIMINAR        = "DELETE FROM tour WHERE id_tour=?";
    private static final String SQL_BUSCAR_POR_ID   = "SELECT * FROM tour WHERE id_tour=?";
    private static final String SQL_LISTAR_TODOS    = "SELECT * FROM tour ORDER BY fecha_salida";
    private static final String SQL_LISTAR_ACTIVOS  =
            "SELECT * FROM tour WHERE estado='ACTIVO' AND fecha_salida >= CURDATE() ORDER BY fecha_salida";

    private static final String SQL_AGREGAR_DESTINO =
            "INSERT IGNORE INTO tour_destino (id_tour, id_destino) VALUES (?, ?)";
    private static final String SQL_AGREGAR_EMPLEADO =
            "INSERT IGNORE INTO tour_empleado (id_tour, id_empleado) VALUES (?, ?)";
    private static final String SQL_QUITAR_DESTINO =
            "DELETE FROM tour_destino WHERE id_tour=? AND id_destino=?";
    private static final String SQL_QUITAR_EMPLEADO =
            "DELETE FROM tour_empleado WHERE id_tour=? AND id_empleado=?";

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public boolean insertar(Tour tour) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tour.getNombre());
            ps.setString(2, tour.getDescripcion());
            ps.setBigDecimal(3, tour.getPrecio());
            ps.setInt(4, tour.getDuracionDias());
            ps.setInt(5, tour.getCapacidadMaxima());
            ps.setDate(6, Date.valueOf(tour.getFechaSalida()));
            ps.setDate(7, Date.valueOf(tour.getFechaRegreso()));
            ps.setString(8, tour.getEstado());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) tour.setIdTour(gk.getInt(1));
                }
                LOGGER.info("✅ Tour insertado con ID: " + tour.getIdTour());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al insertar tour.", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean actualizar(Tour tour) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, tour.getNombre());
            ps.setString(2, tour.getDescripcion());
            ps.setBigDecimal(3, tour.getPrecio());
            ps.setInt(4, tour.getDuracionDias());
            ps.setInt(5, tour.getCapacidadMaxima());
            ps.setDate(6, Date.valueOf(tour.getFechaSalida()));
            ps.setDate(7, Date.valueOf(tour.getFechaRegreso()));
            ps.setString(8, tour.getEstado());
            ps.setInt(9, tour.getIdTour());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("✅ Tour actualizado: ID " + tour.getIdTour());
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al actualizar tour.", e);
            throw e;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("🗑️ Tour eliminado: ID " + id);
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al eliminar tour.", e);
            throw e;
        }
    }

    @Override
    public Optional<Tour> buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearTour(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al buscar tour por ID.", e);
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public List<Tour> listarTodos() throws SQLException {
        List<Tour> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapearTour(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al listar tours.", e);
            throw e;
        }
        return lista;
    }

    public List<Tour> listarToursActivos() throws SQLException {
        List<Tour> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_ACTIVOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapearTour(rs));
        }
        return lista;
    }

    // ── Relaciones ────────────────────────────────────────────────────────────

    public boolean agregarDestino(int idTour, int idDestino) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_AGREGAR_DESTINO)) {
            ps.setInt(1, idTour);
            ps.setInt(2, idDestino);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean agregarEmpleado(int idTour, int idEmpleado) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_AGREGAR_EMPLEADO)) {
            ps.setInt(1, idTour);
            ps.setInt(2, idEmpleado);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean quitarDestino(int idTour, int idDestino) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_QUITAR_DESTINO)) {
            ps.setInt(1, idTour);
            ps.setInt(2, idDestino);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean quitarEmpleado(int idTour, int idEmpleado) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_QUITAR_EMPLEADO)) {
            ps.setInt(1, idTour);
            ps.setInt(2, idEmpleado);
            return ps.executeUpdate() > 0;
        }
    }

    private Tour mapearTour(ResultSet rs) throws SQLException {
        Tour t = new Tour();
        t.setIdTour(rs.getInt("id_tour"));
        t.setNombre(rs.getString("nombre"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setPrecio(rs.getBigDecimal("precio"));
        t.setDuracionDias(rs.getInt("duracion_dias"));
        t.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
        Date fs = rs.getDate("fecha_salida");
        if (fs != null) t.setFechaSalida(fs.toLocalDate());
        Date fr = rs.getDate("fecha_regreso");
        if (fr != null) t.setFechaRegreso(fr.toLocalDate());
        t.setEstado(rs.getString("estado"));
        return t;
    }
}
