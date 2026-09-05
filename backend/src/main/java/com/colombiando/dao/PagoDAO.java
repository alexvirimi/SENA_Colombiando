package com.colombiando.dao;

import com.colombiando.conexion.ConexionDB;
import com.colombiando.modelo.Pago;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Pago}.
 */
public class PagoDAO implements GenericoDAO<Pago, Integer> {

    private static final Logger LOGGER = Logger.getLogger(PagoDAO.class.getName());

    private static final String SQL_INSERTAR =
            "INSERT INTO pago (id_reserva, monto, fecha_pago, metodo_pago, " +
            "estado, referencia, observaciones) VALUES (?, ?, NOW(), ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE pago SET monto=?, metodo_pago=?, estado=?, referencia=?, " +
            "observaciones=? WHERE id_pago=?";

    private static final String SQL_ACTUALIZAR_ESTADO =
            "UPDATE pago SET estado=? WHERE id_pago=?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM pago WHERE id_pago=?";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM pago WHERE id_pago=?";

    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM pago ORDER BY fecha_pago DESC";

    private static final String SQL_LISTAR_POR_RESERVA =
            "SELECT * FROM pago WHERE id_reserva=? ORDER BY fecha_pago DESC";

    private static final String SQL_TOTAL_PAGADO_POR_RESERVA =
            "SELECT COALESCE(SUM(monto), 0) FROM pago " +
            "WHERE id_reserva=? AND estado='APROBADO'";

    @Override
    public boolean insertar(Pago pago) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pago.getIdReserva());
            ps.setBigDecimal(2, pago.getMonto());
            ps.setString(3, pago.getMetodoPago());
            ps.setString(4, pago.getEstado());
            ps.setString(5, pago.getReferencia());
            ps.setString(6, pago.getObservaciones());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) pago.setIdPago(gk.getInt(1));
                }
                LOGGER.info("✅ Pago insertado con ID: " + pago.getIdPago());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al insertar pago.", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean actualizar(Pago pago) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setBigDecimal(1, pago.getMonto());
            ps.setString(2, pago.getMetodoPago());
            ps.setString(3, pago.getEstado());
            ps.setString(4, pago.getReferencia());
            ps.setString(5, pago.getObservaciones());
            ps.setInt(6, pago.getIdPago());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("✅ Pago actualizado: ID " + pago.getIdPago());
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al actualizar pago.", e);
            throw e;
        }
    }

    public boolean actualizarEstado(int idPago, String nuevoEstado) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPago);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("🗑️ Pago eliminado: ID " + id);
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al eliminar pago.", e);
            throw e;
        }
    }

    @Override
    public Optional<Pago> buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearPago(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al buscar pago.", e);
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public List<Pago> listarTodos() throws SQLException {
        List<Pago> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapearPago(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al listar pagos.", e);
            throw e;
        }
        return lista;
    }

    public List<Pago> listarPorReserva(int idReserva) throws SQLException {
        List<Pago> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_RESERVA)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearPago(rs));
            }
        }
        return lista;
    }

    public BigDecimal obtenerTotalPagadoPorReserva(int idReserva) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_PAGADO_POR_RESERVA)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setIdPago(rs.getInt("id_pago"));
        p.setIdReserva(rs.getInt("id_reserva"));
        p.setMonto(rs.getBigDecimal("monto"));
        Timestamp fp = rs.getTimestamp("fecha_pago");
        if (fp != null) p.setFechaPago(fp.toLocalDateTime());
        p.setMetodoPago(rs.getString("metodo_pago"));
        p.setEstado(rs.getString("estado"));
        p.setReferencia(rs.getString("referencia"));
        p.setObservaciones(rs.getString("observaciones"));
        return p;
    }
}
