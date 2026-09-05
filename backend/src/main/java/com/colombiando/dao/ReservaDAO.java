package com.colombiando.dao;

import com.colombiando.conexion.ConexionDB;
import com.colombiando.modelo.Reserva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Reserva}.
 */
public class ReservaDAO implements GenericoDAO<Reserva, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ReservaDAO.class.getName());

    private static final String SQL_INSERTAR =
            "INSERT INTO reserva (id_cliente, id_tour, numero_pasajeros, " +
            "fecha_reserva, fecha_creacion, estado, observaciones) " +
            "VALUES (?, ?, ?, ?, NOW(), ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE reserva SET numero_pasajeros=?, fecha_reserva=?, estado=?, " +
            "observaciones=? WHERE id_reserva=?";

    private static final String SQL_ACTUALIZAR_ESTADO =
            "UPDATE reserva SET estado=? WHERE id_reserva=?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM reserva WHERE id_reserva=?";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM reserva WHERE id_reserva=?";

    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM reserva ORDER BY fecha_creacion DESC";

    private static final String SQL_LISTAR_POR_CLIENTE =
            "SELECT * FROM reserva WHERE id_cliente=? ORDER BY fecha_reserva DESC";

    private static final String SQL_LISTAR_POR_TOUR =
            "SELECT * FROM reserva WHERE id_tour=? ORDER BY fecha_reserva";

    private static final String SQL_LISTAR_POR_ESTADO =
            "SELECT * FROM reserva WHERE estado=? ORDER BY fecha_creacion DESC";

    private static final String SQL_CONTAR_PASAJEROS_EN_TOUR =
            "SELECT COALESCE(SUM(numero_pasajeros), 0) FROM reserva " +
            "WHERE id_tour=? AND estado NOT IN ('CANCELADA')";

    @Override
    public boolean insertar(Reserva reserva) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, reserva.getIdCliente());
            ps.setInt(2, reserva.getIdTour());
            ps.setInt(3, reserva.getNumeroPasajeros());
            ps.setDate(4, Date.valueOf(reserva.getFechaReserva()));
            ps.setString(5, reserva.getEstado());
            ps.setString(6, reserva.getObservaciones());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) reserva.setIdReserva(gk.getInt(1));
                }
                LOGGER.info("✅ Reserva insertada con ID: " + reserva.getIdReserva());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al insertar reserva.", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean actualizar(Reserva reserva) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setInt(1, reserva.getNumeroPasajeros());
            ps.setDate(2, Date.valueOf(reserva.getFechaReserva()));
            ps.setString(3, reserva.getEstado());
            ps.setString(4, reserva.getObservaciones());
            ps.setInt(5, reserva.getIdReserva());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("✅ Reserva actualizada: ID " + reserva.getIdReserva());
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al actualizar reserva.", e);
            throw e;
        }
    }

    public boolean actualizarEstado(int idReserva, String nuevoEstado) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReserva);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("🗑️ Reserva eliminada: ID " + id);
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al eliminar reserva.", e);
            throw e;
        }
    }

    @Override
    public Optional<Reserva> buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearReserva(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al buscar reserva.", e);
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public List<Reserva> listarTodos() throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapearReserva(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al listar reservas.", e);
            throw e;
        }
        return lista;
    }

    public List<Reserva> listarPorCliente(int idCliente) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_CLIENTE)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearReserva(rs));
            }
        }
        return lista;
    }

    public List<Reserva> listarPorTour(int idTour) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_TOUR)) {
            ps.setInt(1, idTour);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearReserva(rs));
            }
        }
        return lista;
    }

    public List<Reserva> listarPorEstado(String estado) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_ESTADO)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearReserva(rs));
            }
        }
        return lista;
    }

    public int contarPasajerosEnTour(int idTour) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_CONTAR_PASAJEROS_EN_TOUR)) {
            ps.setInt(1, idTour);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setIdReserva(rs.getInt("id_reserva"));
        r.setIdCliente(rs.getInt("id_cliente"));
        r.setIdTour(rs.getInt("id_tour"));
        r.setNumeroPasajeros(rs.getInt("numero_pasajeros"));
        Date fr = rs.getDate("fecha_reserva");
        if (fr != null) r.setFechaReserva(fr.toLocalDate());
        Timestamp fc = rs.getTimestamp("fecha_creacion");
        if (fc != null) r.setFechaCreacion(fc.toLocalDateTime());
        r.setEstado(rs.getString("estado"));
        r.setObservaciones(rs.getString("observaciones"));
        return r;
    }
}
