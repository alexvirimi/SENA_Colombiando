package com.colombiando.servlet;

import com.colombiando.dao.EmpleadoDAO;
import com.colombiando.dao.IdiomaDAO;
import com.colombiando.modelo.Empleado;
import com.colombiando.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet CRUD para la entidad {@link Empleado}.
 *
 * <pre>
 *  GET  /empleados                       → lista todos
 *  GET  /empleados?accion=nuevo          → formulario nuevo
 *  GET  /empleados?accion=editar&id=X    → formulario edición
 *  GET  /empleados?accion=eliminar&id=X  → elimina
 *  POST /empleados?accion=guardar        → crea empleado
 *  POST /empleados?accion=actualizar     → actualiza empleado
 *  POST /empleados?accion=asignarIdioma  → asigna idioma
 * </pre>
 */
@WebServlet(name = "EmpleadoServlet", urlPatterns = "/empleados")
public class EmpleadoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EmpleadoServlet.class.getName());
    private static final String VISTA_LISTA = "/WEB-INF/views/empleado/lista.jsp";
    private static final String VISTA_FORM  = "/WEB-INF/views/empleado/formulario.jsp";

    private EmpleadoDAO empleadoDAO;
    private IdiomaDAO   idiomaDAO;

    @Override
    public void init() throws ServletException {
        empleadoDAO = new EmpleadoDAO();
        idiomaDAO   = new IdiomaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "listar");
        try {
            switch (accion) {
                case "nuevo"    -> mostrarFormulario(req, resp, null, false);
                case "editar"   -> {
                    int id = ServletUtil.getInt(req, "id");
                    Optional<Empleado> opt = empleadoDAO.buscarPorId(id);
                    mostrarFormulario(req, resp, opt.orElse(new Empleado()), true);
                }
                case "eliminar" -> {
                    int id = ServletUtil.getInt(req, "id");
                    empleadoDAO.eliminar(id);
                    resp.sendRedirect(req.getContextPath()
                            + "/empleados?mensaje=Empleado+eliminado&tipo=success");
                }
                default         -> listar(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error EmpleadoServlet GET.", e);
            manejarError(req, resp, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "guardar");
        try {
            switch (accion) {
                case "actualizar"    -> actualizar(req, resp);
                case "asignarIdioma" -> asignarIdioma(req, resp);
                default              -> guardar(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error EmpleadoServlet POST.", e);
            manejarError(req, resp, e.getMessage());
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("empleados", empleadoDAO.listarTodos());
        req.getRequestDispatcher(VISTA_LISTA).forward(req, resp);
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp,
                                   Empleado empleado, boolean modoEdicion)
            throws SQLException, ServletException, IOException {
        req.setAttribute("empleado",   empleado != null ? empleado : new Empleado());
        req.setAttribute("idiomas",    idiomaDAO.listarTodos());
        req.setAttribute("modoEdicion",modoEdicion);
        req.setAttribute("titulo",     modoEdicion ? "Editar empleado" : "Registrar empleado");
        req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        Empleado e = construirDesdeRequest(req);
        List<String> errores = validar(e, true);
        if (!errores.isEmpty()) {
            req.setAttribute("empleado",   e);
            req.setAttribute("errores",    errores);
            req.setAttribute("idiomas",    idiomaDAO.listarTodos());
            req.setAttribute("modoEdicion",false);
            req.setAttribute("titulo",     "Registrar empleado");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
            return;
        }
        empleadoDAO.insertar(e);
        resp.sendRedirect(req.getContextPath()
                + "/empleados?mensaje=Empleado+registrado+exitosamente&tipo=success");
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        Empleado e = construirDesdeRequest(req);
        e.setIdUsuario(ServletUtil.getInt(req, "idUsuario"));
        List<String> errores = validar(e, false);
        if (!errores.isEmpty()) {
            req.setAttribute("empleado",   e);
            req.setAttribute("errores",    errores);
            req.setAttribute("idiomas",    idiomaDAO.listarTodos());
            req.setAttribute("modoEdicion",true);
            req.setAttribute("titulo",     "Editar empleado");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
            return;
        }
        empleadoDAO.actualizar(e);
        resp.sendRedirect(req.getContextPath()
                + "/empleados?mensaje=Empleado+actualizado+correctamente&tipo=success");
    }

    private void asignarIdioma(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int    idEmp    = ServletUtil.getInt(req, "idEmpleado");
        int    idIdioma = ServletUtil.getInt(req, "idIdioma");
        String nivel    = ServletUtil.getString(req, "nivel", "BASICO");
        idiomaDAO.asignarIdiomaAEmpleado(idEmp, idIdioma, nivel);
        resp.sendRedirect(req.getContextPath()
                + "/empleados?accion=editar&id=" + idEmp
                + "&mensaje=Idioma+asignado+correctamente&tipo=success");
    }

    private Empleado construirDesdeRequest(HttpServletRequest req) {
        Empleado e = new Empleado();
        e.setNombre(ServletUtil.getString(req, "nombre"));
        e.setApellido(ServletUtil.getString(req, "apellido"));
        e.setCorreo(ServletUtil.getString(req, "correo"));
        e.setTelefono(ServletUtil.getString(req, "telefono"));
        e.setContrasena(ServletUtil.getString(req, "contrasena"));
        e.setCargo(ServletUtil.getString(req, "cargo"));
        e.setSalario(ServletUtil.getBigDecimal(req, "salario"));
        e.setFechaContratacion(ServletUtil.getLocalDate(req, "fechaContratacion"));
        return e;
    }

    private List<String> validar(Empleado e, boolean esNuevo) {
        List<String> err = new ArrayList<>();
        if (ServletUtil.esVacio(e.getNombre()))    err.add("El nombre es obligatorio.");
        if (ServletUtil.esVacio(e.getApellido()))  err.add("El apellido es obligatorio.");
        if (!ServletUtil.esCorreoValido(e.getCorreo())) err.add("Correo inválido.");
        if (ServletUtil.esVacio(e.getCargo()))     err.add("El cargo es obligatorio.");
        if (e.getSalario() == null || e.getSalario().compareTo(BigDecimal.ZERO) <= 0)
            err.add("El salario debe ser mayor a cero.");
        if (e.getFechaContratacion() == null)      err.add("La fecha de contratación es obligatoria.");
        if (esNuevo && ServletUtil.esVacio(e.getContrasena())) err.add("La contraseña es obligatoria.");
        return err;
    }

    private void manejarError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
    }
}
