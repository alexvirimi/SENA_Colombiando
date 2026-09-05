package com.colombiando.servlet;

import com.colombiando.dao.ClienteDAO;
import com.colombiando.modelo.Cliente;
import com.colombiando.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet que gestiona el CRUD completo de {@link Cliente}s.
 *
 * <pre>
 *  GET  /clientes             → lista todos los clientes
 *  GET  /clientes?accion=nuevo       → formulario de registro
 *  GET  /clientes?accion=editar&id=X → formulario de edición
 *  GET  /clientes?accion=eliminar&id=X → elimina y redirige
 *  POST /clientes?accion=guardar     → inserta nuevo cliente
 *  POST /clientes?accion=actualizar  → actualiza cliente
 * </pre>
 */
@WebServlet(name = "ClienteServlet", urlPatterns = "/clientes")
public class ClienteServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ClienteServlet.class.getName());
    private static final String VISTA_LISTA     = "/WEB-INF/views/cliente/lista.jsp";
    private static final String VISTA_FORMULARIO= "/WEB-INF/views/cliente/formulario.jsp";
    private static final String REDIRECT_LISTA  = "clientes";

    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        clienteDAO = new ClienteDAO();
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "listar");

        try {
            switch (accion) {
                case "nuevo"     -> mostrarFormularioNuevo(req, resp);
                case "editar"    -> mostrarFormularioEdicion(req, resp);
                case "eliminar"  -> eliminarCliente(req, resp);
                default          -> listarClientes(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en ClienteServlet GET.", e);
            manejarError(req, resp, "Error al procesar la solicitud: " + e.getMessage());
        }
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "guardar");

        try {
            switch (accion) {
                case "actualizar" -> actualizarCliente(req, resp);
                default           -> guardarCliente(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error en ClienteServlet POST.", e);
            manejarError(req, resp, "Error al guardar los datos: " + e.getMessage());
        }
    }

    // ── Acciones GET ─────────────────────────────────────────────────────────

    private void listarClientes(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        List<Cliente> clientes = clienteDAO.listarTodos();
        req.setAttribute("clientes", clientes);
        req.setAttribute("totalClientes", clientes.size());
        req.getRequestDispatcher(VISTA_LISTA).forward(req, resp);
    }

    private void mostrarFormularioNuevo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("cliente", new Cliente());
        req.setAttribute("modoEdicion", false);
        req.setAttribute("titulo", "Registrar nuevo cliente");
        req.getRequestDispatcher(VISTA_FORMULARIO).forward(req, resp);
    }

    private void mostrarFormularioEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int id = ServletUtil.getInt(req, "id");
        Optional<Cliente> opt = clienteDAO.buscarPorId(id);

        if (opt.isEmpty()) {
            req.setAttribute("mensaje", "Cliente no encontrado con ID: " + id);
            listarClientes(req, resp);
            return;
        }

        req.setAttribute("cliente", opt.get());
        req.setAttribute("modoEdicion", true);
        req.setAttribute("titulo", "Editar cliente");
        req.getRequestDispatcher(VISTA_FORMULARIO).forward(req, resp);
    }

    private void eliminarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {

        int id = ServletUtil.getInt(req, "id");
        try {
            if (clienteDAO.eliminar(id)) {
                resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                        + "?mensaje=Cliente+eliminado+correctamente&tipo=success");
            } else {
                resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                        + "?mensaje=No+se+pudo+eliminar+el+cliente&tipo=error");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "No se puede eliminar cliente ID " + id, e);
            resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                    + "?mensaje=No+se+puede+eliminar:+tiene+reservas+asociadas&tipo=error");
        }
    }

    // ── Acciones POST ────────────────────────────────────────────────────────

    private void guardarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Cliente cliente = construirClienteDesdeRequest(req);
        List<String> errores = validarCliente(cliente, true);

        if (!errores.isEmpty()) {
            req.setAttribute("cliente",    cliente);
            req.setAttribute("errores",    errores);
            req.setAttribute("modoEdicion",false);
            req.setAttribute("titulo",     "Registrar nuevo cliente");
            req.getRequestDispatcher(VISTA_FORMULARIO).forward(req, resp);
            return;
        }

        clienteDAO.insertar(cliente);
        resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                + "?mensaje=Cliente+registrado+exitosamente&tipo=success");
    }

    private void actualizarCliente(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Cliente cliente = construirClienteDesdeRequest(req);
        cliente.setIdUsuario(ServletUtil.getInt(req, "idUsuario"));

        List<String> errores = validarCliente(cliente, false);

        if (!errores.isEmpty()) {
            req.setAttribute("cliente",    cliente);
            req.setAttribute("errores",    errores);
            req.setAttribute("modoEdicion",true);
            req.setAttribute("titulo",     "Editar cliente");
            req.getRequestDispatcher(VISTA_FORMULARIO).forward(req, resp);
            return;
        }

        clienteDAO.actualizar(cliente);
        resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                + "?mensaje=Cliente+actualizado+correctamente&tipo=success");
    }

    // ── Construcción y validación ────────────────────────────────────────────

    private Cliente construirClienteDesdeRequest(HttpServletRequest req) {
        Cliente c = new Cliente();
        c.setNombre(ServletUtil.getString(req, "nombre"));
        c.setApellido(ServletUtil.getString(req, "apellido"));
        c.setCorreo(ServletUtil.getString(req, "correo"));
        c.setTelefono(ServletUtil.getString(req, "telefono"));
        c.setContrasena(ServletUtil.getString(req, "contrasena"));
        c.setTipoDocumento(ServletUtil.getString(req, "tipoDocumento"));
        c.setNumeroDocumento(ServletUtil.getString(req, "numeroDocumento"));
        c.setNacionalidad(ServletUtil.getString(req, "nacionalidad", "Colombiana"));

        LocalDate fechaNac = ServletUtil.getLocalDate(req, "fechaNacimiento");
        c.setFechaNacimiento(fechaNac);
        return c;
    }

    private List<String> validarCliente(Cliente c, boolean esNuevo) {
        List<String> errores = new java.util.ArrayList<>();

        if (ServletUtil.esVacio(c.getNombre()))
            errores.add("El nombre es obligatorio.");
        if (ServletUtil.esVacio(c.getApellido()))
            errores.add("El apellido es obligatorio.");
        if (!ServletUtil.esCorreoValido(c.getCorreo()))
            errores.add("El correo electrónico no tiene un formato válido.");
        if (!ServletUtil.esTelefonoValido(c.getTelefono()))
            errores.add("El teléfono debe tener 10 dígitos.");
        if (esNuevo && ServletUtil.esVacio(c.getContrasena()))
            errores.add("La contraseña es obligatoria para nuevos clientes.");
        if (ServletUtil.esVacio(c.getNumeroDocumento()))
            errores.add("El número de documento es obligatorio.");
        if (c.getFechaNacimiento() == null)
            errores.add("La fecha de nacimiento es obligatoria.");

        return errores;
    }

    private void manejarError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
    }
}
