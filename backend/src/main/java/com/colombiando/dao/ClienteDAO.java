package com.colombiando.dao;

import com.colombiando.conexion.ConexionDB;
import com.colombiando.modelo.Cliente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Cliente}.
 * Centraliza todas las operaciones CRUD contra la tabla {@code cliente}.
 */
public class ClienteDAO implements GenericoDAO<Cliente, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ClienteDAO.class.getName());

    // ── SQL ──────────────────────────────────────────────────────────────────
    private static final String SQL_INSERTAR =
            "INSERT INTO cliente (nombre, apellido, correo, telefono, contrasena, " +
            "numero_documento, tipo_documento, fecha_nacimiento, nacionalidad) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE cliente SET nombre=?, apellido=?, correo=?, telefono=?, " +
            "numero_documento=?, tipo_documento=?, fecha_nacimiento=?, nacionalidad=? " +
            "WHERE id_usuario=?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM cliente WHERE id_usuario=?";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM cliente WHERE id_usuario=?";

    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM cliente ORDER BY apellido, nombre";

    private static final String SQL_BUSCAR_POR_CORREO =
            "SELECT * FROM cliente WHERE correo=?";

    private static final String SQL_BUSCAR_POR_DOCUMENTO =
            "SELECT * FROM cliente WHERE numero_documento=?";

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public boolean insertar(Cliente cliente) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getContrasena());
            ps.setString(6, cliente.getNumeroDocumento());
            ps.setString(7, cliente.getTipoDocumento());
            ps.setDate(8,   Date.valueOf(cliente.getFechaNacimiento()));
            ps.setString(9, cliente.getNacionalidad());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generadas = ps.getGeneratedKeys()) {
                    if (generadas.next()) {
                        cliente.setIdUsuario(generadas.getInt(1));
                    }
                }
                LOGGER.info("✅ Cliente insertado con ID: " + cliente.getIdUsuario());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al insertar cliente.", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getNumeroDocumento());
            ps.setString(6, cliente.getTipoDocumento());
            ps.setDate(7,   Date.valueOf(cliente.getFechaNacimiento()));
            ps.setString(8, cliente.getNacionalidad());
            ps.setInt(9,    cliente.getIdUsuario());

            boolean actualizado = ps.executeUpdate() > 0;
            if (actualizado) LOGGER.info("✅ Cliente actualizado: ID " + cliente.getIdUsuario());
            return actualizado;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al actualizar cliente.", e);
            throw e;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            boolean eliminado = ps.executeUpdate() > 0;
            if (eliminado) LOGGER.info("🗑️ Cliente eliminado: ID " + id);
            return eliminado;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al eliminar cliente ID: " + id, e);
            throw e;
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al buscar cliente por ID: " + id, e);
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al listar clientes.", e);
            throw e;
        }
        return clientes;
    }

    // ── Consultas adicionales ────────────────────────────────────────────────

    public Optional<Cliente> buscarPorCorreo(String correo) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_CORREO)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearCliente(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Cliente> buscarPorDocumento(String numeroDocumento) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_DOCUMENTO)) {

            ps.setString(1, numeroDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearCliente(rs));
            }
        }
        return Optional.empty();
    }

    // ── Mapeo ResultSet → Cliente ────────────────────────────────────────────

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdUsuario(rs.getInt("id_usuario"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setCorreo(rs.getString("correo"));
        c.setTelefono(rs.getString("telefono"));
        c.setContrasena(rs.getString("contrasena"));
        c.setNumeroDocumento(rs.getString("numero_documento"));
        c.setTipoDocumento(rs.getString("tipo_documento"));

        Date fechaNac = rs.getDate("fecha_nacimiento");
        if (fechaNac != null) c.setFechaNacimiento(fechaNac.toLocalDate());

        c.setNacionalidad(rs.getString("nacionalidad"));
        return c;
    }
}
