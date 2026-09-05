package com.colombiando.dao;

import com.colombiando.conexion.ConexionDB;
import com.colombiando.modelo.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para la entidad {@link Empleado}.
 * Centraliza todas las operaciones CRUD contra la tabla {@code empleado}.
 */
public class EmpleadoDAO implements GenericoDAO<Empleado, Integer> {

    private static final Logger LOGGER = Logger.getLogger(EmpleadoDAO.class.getName());

    private static final String SQL_INSERTAR =
            "INSERT INTO empleado (nombre, apellido, correo, telefono, contrasena, " +
            "cargo, salario, fecha_contratacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE empleado SET nombre=?, apellido=?, correo=?, telefono=?, " +
            "cargo=?, salario=?, fecha_contratacion=? WHERE id_usuario=?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM empleado WHERE id_usuario=?";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM empleado WHERE id_usuario=?";

    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM empleado ORDER BY apellido, nombre";

    private static final String SQL_LISTAR_POR_CARGO =
            "SELECT * FROM empleado WHERE cargo=? ORDER BY apellido";

    private static final String SQL_LISTAR_POR_TOUR =
            "SELECT e.* FROM empleado e " +
            "JOIN tour_empleado te ON e.id_usuario = te.id_empleado " +
            "WHERE te.id_tour=?";

    @Override
    public boolean insertar(Empleado empleado) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR,
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setString(3, empleado.getCorreo());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getContrasena());
            ps.setString(6, empleado.getCargo());
            ps.setBigDecimal(7, empleado.getSalario());
            ps.setDate(8, Date.valueOf(empleado.getFechaContratacion()));

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet generadas = ps.getGeneratedKeys()) {
                    if (generadas.next()) empleado.setIdUsuario(generadas.getInt(1));
                }
                LOGGER.info("✅ Empleado insertado con ID: " + empleado.getIdUsuario());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al insertar empleado.", e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean actualizar(Empleado empleado) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellido());
            ps.setString(3, empleado.getCorreo());
            ps.setString(4, empleado.getTelefono());
            ps.setString(5, empleado.getCargo());
            ps.setBigDecimal(6, empleado.getSalario());
            ps.setDate(7, Date.valueOf(empleado.getFechaContratacion()));
            ps.setInt(8, empleado.getIdUsuario());

            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("✅ Empleado actualizado: ID " + empleado.getIdUsuario());
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al actualizar empleado.", e);
            throw e;
        }
    }

    @Override
    public boolean eliminar(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) LOGGER.info("🗑️ Empleado eliminado: ID " + id);
            return ok;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al eliminar empleado.", e);
            throw e;
        }
    }

    @Override
    public Optional<Empleado> buscarPorId(Integer id) throws SQLException {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearEmpleado(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al buscar empleado por ID.", e);
            throw e;
        }
        return Optional.empty();
    }

    @Override
    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mapearEmpleado(rs));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Error al listar empleados.", e);
            throw e;
        }
        return lista;
    }

    public List<Empleado> listarPorCargo(String cargo) throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_CARGO)) {

            ps.setString(1, cargo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearEmpleado(rs));
            }
        }
        return lista;
    }

    public List<Empleado> listarEmpleadosPorTour(int idTour) throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_TOUR)) {

            ps.setInt(1, idTour);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearEmpleado(rs));
            }
        }
        return lista;
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdUsuario(rs.getInt("id_usuario"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setCorreo(rs.getString("correo"));
        e.setTelefono(rs.getString("telefono"));
        e.setContrasena(rs.getString("contrasena"));
        e.setCargo(rs.getString("cargo"));
        e.setSalario(rs.getBigDecimal("salario"));
        Date fc = rs.getDate("fecha_contratacion");
        if (fc != null) e.setFechaContratacion(fc.toLocalDate());
        return e;
    }
}
