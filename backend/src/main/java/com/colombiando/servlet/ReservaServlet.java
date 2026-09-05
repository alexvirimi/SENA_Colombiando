package com.colombiando.servlet;

import com.colombiando.dao.ClienteDAO;
import com.colombiando.dao.ReservaDAO;
import com.colombiando.dao.TourDAO;
import com.colombiando.modelo.Reserva;
import com.colombiando.servicio.ReservaServicio;
import com.colombiando.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet CRUD para la entidad {@link Reserva}.
 *
 * <pre>
 *  GET  /reservas                       → lista todas las reservas
 *  GET  /reservas?accion=nuevo          → formulario de creación
 *  GET  /reservas?accion=editar&id=X    → formulario de edición
 *  GET  /reservas?accion=cancelar&id=X  → cancela la reserva
 *  POST /reservas?accion=guardar        → crea la reserva (con validación de cupos)
 *  POST /reservas?accion=actualizar     → modifica la reserva
 * </pre>
 */
@WebServlet(name = "ReservaServlet", urlPatterns = "/reservas")
public class ReservaServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReservaServlet.class.getName());
    private static final String VISTA_LISTA   = "/WEB-INF/views/reserva/lista.jsp";
    private static final String VISTA_FORM    = "/WEB-INF/views/reserva/formulario.jsp";
    private static final String VISTA_CONFIRM = "/WEB-INF/views/reserva/confirmacion.jsp";
    private static final String REDIRECT_LIST = "reservas";

    private ReservaDAO     reservaDAO;
    private ReservaServicio reservaServicio;
    private ClienteDAO     clienteDAO;
    private TourDAO        tourDAO;

    @Override
    public void init() throws ServletException {
        reservaDAO     = new ReservaDAO();
        reservaServicio = new ReservaServicio();
        clienteDAO     = new ClienteDAO();
        tourDAO        = new TourDAO();
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "listar");
        try {
            switch (accion) {
                case "nuevo"    -> mostrarFormulario(req, resp);
                case "editar"   -> mostrarFormularioEdicion(req, resp);
                case "cancelar" -> cancelarReserva(req, resp);
                default         -> listarReservas(req, resp);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error ReservaServlet GET.", e);
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
                case "actualizar" -> actualizarReserva(req, resp);
                default           -> guardarReserva(req, resp);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error ReservaServlet POST.", e);
            manejarError(req, resp, e.getMessage());
        }
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    private void listarReservas(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        String filtroEstado = ServletUtil.getString(req, "estado");
        List<Reserva> reservas = filtroEstado.isEmpty()
                ? reservaDAO.listarTodos()
                : reservaDAO.listarPorEstado(filtroEstado);

        req.setAttribute("reservas",      reservas);
        req.setAttribute("filtroEstado",  filtroEstado);
        req.getRequestDispatcher(VISTA_LISTA).forward(req, resp);
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        req.setAttribute("reserva",    new Reserva());
        req.setAttribute("clientes",   clienteDAO.listarTodos());
        req.setAttribute("tours",      tourDAO.listarToursActivos());
        req.setAttribute("modoEdicion",false);
        req.setAttribute("titulo",     "Nueva reserva");
        req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
    }

    private void mostrarFormularioEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int id = ServletUtil.getInt(req, "id");
        Optional<Reserva> opt = reservaDAO.buscarPorId(id);
        if (opt.isEmpty()) { listarReservas(req, resp); return; }

        req.setAttribute("reserva",    opt.get());
        req.setAttribute("clientes",   clienteDAO.listarTodos());
        req.setAttribute("tours",      tourDAO.listarTodos());
        req.setAttribute("modoEdicion",true);
        req.setAttribute("titulo",     "Editar reserva");
        req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
    }

    private void guardarReserva(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {

        int       idCliente  = ServletUtil.getInt(req, "idCliente");
        int       idTour     = ServletUtil.getInt(req, "idTour");
        int       pasajeros  = ServletUtil.getInt(req, "numeroPasajeros", 1);
        LocalDate fecha      = ServletUtil.getLocalDate(req, "fechaReserva");
        String    obs        = ServletUtil.getString(req, "observaciones");

        List<String> errores = new ArrayList<>();
        if (idCliente == 0)  errores.add("Selecciona un cliente.");
        if (idTour    == 0)  errores.add("Selecciona un tour.");
        if (fecha     == null) errores.add("La fecha de reserva es obligatoria.");
        if (pasajeros <= 0)  errores.add("El número de pasajeros debe ser mayor a cero.");

        if (!errores.isEmpty()) {
            req.setAttribute("reserva",    construirReservaDesdeRequest(req));
            req.setAttribute("errores",    errores);
            req.setAttribute("clientes",   clienteDAO.listarTodos());
            req.setAttribute("tours",      tourDAO.listarToursActivos());
            req.setAttribute("modoEdicion",false);
            req.setAttribute("titulo",     "Nueva reserva");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
            return;
        }

        try {
            Reserva reserva = reservaServicio.crearReserva(
                    idCliente, idTour, pasajeros, fecha, obs);
            req.setAttribute("reserva", reserva);
            req.setAttribute("tour",    tourDAO.buscarPorId(idTour).orElse(null));
            req.setAttribute("cliente", clienteDAO.buscarPorId(idCliente).orElse(null));
            req.getRequestDispatcher(VISTA_CONFIRM).forward(req, resp);

        } catch (IllegalArgumentException e) {
            // Regla de negocio: sin cupos, tour inactivo, etc.
            errores.add(e.getMessage());
            req.setAttribute("reserva",    construirReservaDesdeRequest(req));
            req.setAttribute("errores",    errores);
            req.setAttribute("clientes",   clienteDAO.listarTodos());
            req.setAttribute("tours",      tourDAO.listarToursActivos());
            req.setAttribute("modoEdicion",false);
            req.setAttribute("titulo",     "Nueva reserva");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
        }
    }

    private void actualizarReserva(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        Reserva r = construirReservaDesdeRequest(req);
        r.setIdReserva(ServletUtil.getInt(req, "idReserva"));
        reservaDAO.actualizar(r);
        resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LIST
                + "?mensaje=Reserva+actualizada+correctamente&tipo=success");
    }

    private void cancelarReserva(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {
        int id = ServletUtil.getInt(req, "id");
        try {
            reservaServicio.cancelarReserva(id);
            resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LIST
                    + "?mensaje=Reserva+cancelada+correctamente&tipo=success");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/" + REDIRECT_LIST
                    + "?mensaje=" + e.getMessage().replace(" ", "+") + "&tipo=error");
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Reserva construirReservaDesdeRequest(HttpServletRequest req) {
        Reserva r = new Reserva();
        r.setIdCliente(ServletUtil.getInt(req, "idCliente"));
        r.setIdTour(ServletUtil.getInt(req, "idTour"));
        r.setNumeroPasajeros(ServletUtil.getInt(req, "numeroPasajeros", 1));
        r.setFechaReserva(ServletUtil.getLocalDate(req, "fechaReserva"));
        r.setEstado(ServletUtil.getString(req, "estado", "PENDIENTE"));
        r.setObservaciones(ServletUtil.getString(req, "observaciones"));
        return r;
    }

    private void manejarError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
    }
}
