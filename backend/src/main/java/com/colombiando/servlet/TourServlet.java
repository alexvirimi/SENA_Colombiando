package com.colombiando.servlet;

import com.colombiando.dao.DestinoDAO;
import com.colombiando.dao.EmpleadoDAO;
import com.colombiando.dao.TourDAO;
import com.colombiando.modelo.Tour;
import com.colombiando.servicio.TourServicio;
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
 * Servlet CRUD para la entidad {@link Tour}.
 *
 * <pre>
 *  GET  /tours                        → lista todos los tours
 *  GET  /tours?accion=nuevo           → formulario de creación
 *  GET  /tours?accion=editar&id=X     → formulario de edición
 *  GET  /tours?accion=ver&id=X        → detalle completo del tour
 *  GET  /tours?accion=eliminar&id=X   → elimina y redirige
 *  POST /tours?accion=guardar         → crea tour
 *  POST /tours?accion=actualizar      → actualiza tour
 * </pre>
 */
@WebServlet(name = "TourServlet", urlPatterns = "/tours")
public class TourServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TourServlet.class.getName());
    private static final String VISTA_LISTA      = "/WEB-INF/views/tour/lista.jsp";
    private static final String VISTA_FORM       = "/WEB-INF/views/tour/formulario.jsp";
    private static final String VISTA_DETALLE    = "/WEB-INF/views/tour/detalle.jsp";
    private static final String REDIRECT_LISTA   = "tours";

    private TourDAO      tourDAO;
    private TourServicio tourServicio;
    private DestinoDAO   destinoDAO;
    private EmpleadoDAO  empleadoDAO;

    @Override
    public void init() throws ServletException {
        tourDAO      = new TourDAO();
        tourServicio = new TourServicio();
        destinoDAO   = new DestinoDAO();
        empleadoDAO  = new EmpleadoDAO();
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "listar");
        try {
            switch (accion) {
                case "nuevo"    -> mostrarFormularioNuevo(req, resp);
                case "editar"   -> mostrarFormularioEdicion(req, resp);
                case "ver"      -> mostrarDetalle(req, resp);
                case "eliminar" -> eliminarTour(req, resp);
                default         -> listarTours(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error TourServlet GET.", e);
            manejarError(req, resp, e.getMessage());
        }
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "guardar");
        try {
            switch (accion) {
                case "actualizar"      -> actualizarTour(req, resp);
                case "asignarDestino"  -> asignarDestino(req, resp);
                case "asignarEmpleado" -> asignarEmpleado(req, resp);
                default                -> guardarTour(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error TourServlet POST.", e);
            manejarError(req, resp, e.getMessage());
        }
    }

    // ── Acciones GET ─────────────────────────────────────────────────────────

    private void listarTours(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        req.setAttribute("tours", tourDAO.listarTodos());
        req.getRequestDispatcher(VISTA_LISTA).forward(req, resp);
    }

    private void mostrarFormularioNuevo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("tour",       new Tour());
        req.setAttribute("modoEdicion",false);
        req.setAttribute("titulo",     "Crear nuevo tour");
        req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
    }

    private void mostrarFormularioEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int id = ServletUtil.getInt(req, "id");
        Optional<Tour> opt = tourDAO.buscarPorId(id);
        if (opt.isEmpty()) {
            listarTours(req, resp);
            return;
        }
        req.setAttribute("tour",       opt.get());
        req.setAttribute("modoEdicion",true);
        req.setAttribute("titulo",     "Editar tour");
        req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
    }

    private void mostrarDetalle(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int id = ServletUtil.getInt(req, "id");
        Optional<Tour> opt = tourServicio.obtenerTourCompleto(id);
        if (opt.isEmpty()) {
            listarTours(req, resp);
            return;
        }
        req.setAttribute("tour",       opt.get());
        req.setAttribute("destinos",   destinoDAO.listarTodos());
        req.setAttribute("empleados",  empleadoDAO.listarTodos());
        req.getRequestDispatcher(VISTA_DETALLE).forward(req, resp);
    }

    private void eliminarTour(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = ServletUtil.getInt(req, "id");
        try {
            tourDAO.eliminar(id);
            resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                    + "?mensaje=Tour+eliminado+correctamente&tipo=success");
        } catch (SQLException e) {
            resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                    + "?mensaje=No+se+puede+eliminar:+tiene+reservas&tipo=error");
        }
    }

    // ── Acciones POST ────────────────────────────────────────────────────────

    private void guardarTour(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Tour tour = construirTourDesdeRequest(req);
        List<String> errores = validarTour(tour);

        if (!errores.isEmpty()) {
            req.setAttribute("tour",       tour);
            req.setAttribute("errores",    errores);
            req.setAttribute("modoEdicion",false);
            req.setAttribute("titulo",     "Crear nuevo tour");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
            return;
        }

        tourServicio.crearTour(tour);
        resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                + "?mensaje=Tour+creado+exitosamente&tipo=success");
    }

    private void actualizarTour(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Tour tour = construirTourDesdeRequest(req);
        tour.setIdTour(ServletUtil.getInt(req, "idTour"));
        List<String> errores = validarTour(tour);

        if (!errores.isEmpty()) {
            req.setAttribute("tour",       tour);
            req.setAttribute("errores",    errores);
            req.setAttribute("modoEdicion",true);
            req.setAttribute("titulo",     "Editar tour");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
            return;
        }

        tourDAO.actualizar(tour);
        resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                + "?mensaje=Tour+actualizado+correctamente&tipo=success");
    }

    private void asignarDestino(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int idTour    = ServletUtil.getInt(req, "idTour");
        int idDestino = ServletUtil.getInt(req, "idDestino");
        tourDAO.agregarDestino(idTour, idDestino);
        resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                + "?accion=ver&id=" + idTour + "&mensaje=Destino+asignado&tipo=success");
    }

    private void asignarEmpleado(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException {
        int idTour     = ServletUtil.getInt(req, "idTour");
        int idEmpleado = ServletUtil.getInt(req, "idEmpleado");
        tourDAO.agregarEmpleado(idTour, idEmpleado);
        resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LISTA
                + "?accion=ver&id=" + idTour + "&mensaje=Empleado+asignado&tipo=success");
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Tour construirTourDesdeRequest(HttpServletRequest req) {
        Tour t = new Tour();
        t.setNombre(ServletUtil.getString(req, "nombre"));
        t.setDescripcion(ServletUtil.getString(req, "descripcion"));

        BigDecimal precio = ServletUtil.getBigDecimal(req, "precio");
        t.setPrecio(precio);
        t.setDuracionDias(ServletUtil.getInt(req, "duracionDias"));
        t.setCapacidadMaxima(ServletUtil.getInt(req, "capacidadMaxima"));
        t.setFechaSalida(ServletUtil.getLocalDate(req, "fechaSalida"));
        t.setFechaRegreso(ServletUtil.getLocalDate(req, "fechaRegreso"));
        t.setEstado(ServletUtil.getString(req, "estado", "ACTIVO"));
        return t;
    }

    private List<String> validarTour(Tour t) {
        List<String> errores = new ArrayList<>();
        if (ServletUtil.esVacio(t.getNombre()))
            errores.add("El nombre del tour es obligatorio.");
        if (t.getPrecio() == null || t.getPrecio().compareTo(BigDecimal.ZERO) <= 0)
            errores.add("El precio debe ser mayor a cero.");
        if (t.getDuracionDias() <= 0)
            errores.add("La duración en días debe ser mayor a cero.");
        if (t.getCapacidadMaxima() <= 0)
            errores.add("La capacidad máxima debe ser mayor a cero.");
        if (t.getFechaSalida() == null)
            errores.add("La fecha de salida es obligatoria.");
        if (t.getFechaRegreso() == null)
            errores.add("La fecha de regreso es obligatoria.");
        if (t.getFechaSalida() != null && t.getFechaRegreso() != null
                && t.getFechaRegreso().isBefore(t.getFechaSalida()))
            errores.add("La fecha de regreso no puede ser anterior a la salida.");
        return errores;
    }

    private void manejarError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
    }
}
