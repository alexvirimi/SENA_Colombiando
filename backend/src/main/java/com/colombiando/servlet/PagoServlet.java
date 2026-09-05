package com.colombiando.servlet;

import com.colombiando.dao.PagoDAO;
import com.colombiando.dao.ReservaDAO;
import com.colombiando.modelo.Pago;
import com.colombiando.servicio.ReservaServicio;
import com.colombiando.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet CRUD para la entidad {@link Pago}.
 *
 * <pre>
 *  GET  /pagos                         → lista todos los pagos
 *  GET  /pagos?accion=nuevo&idReserva=X → formulario de pago
 *  GET  /pagos?accion=porReserva&id=X  → pagos de una reserva
 *  POST /pagos?accion=guardar          → registra el pago
 * </pre>
 */
@WebServlet(name = "PagoServlet", urlPatterns = "/pagos")
public class PagoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(PagoServlet.class.getName());
    private static final String VISTA_LISTA = "/WEB-INF/views/pago/lista.jsp";
    private static final String VISTA_FORM  = "/WEB-INF/views/pago/formulario.jsp";

    private PagoDAO         pagoDAO;
    private ReservaDAO      reservaDAO;
    private ReservaServicio reservaServicio;

    @Override
    public void init() throws ServletException {
        pagoDAO         = new PagoDAO();
        reservaDAO      = new ReservaDAO();
        reservaServicio = new ReservaServicio();
    }

    // ── GET ──────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "listar");
        try {
            switch (accion) {
                case "nuevo"       -> mostrarFormulario(req, resp);
                case "porReserva"  -> pagosPorReserva(req, resp);
                default            -> listarPagos(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error PagoServlet GET.", e);
            manejarError(req, resp, e.getMessage());
        }
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            registrarPago(req, resp);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error PagoServlet POST.", e);
            manejarError(req, resp, e.getMessage());
        }
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    private void listarPagos(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        req.setAttribute("pagos", pagoDAO.listarTodos());
        req.getRequestDispatcher(VISTA_LISTA).forward(req, resp);
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int idReserva = ServletUtil.getInt(req, "idReserva");
        req.setAttribute("reserva",    reservaDAO.buscarPorId(idReserva).orElse(null));
        req.setAttribute("totalPagado",pagoDAO.obtenerTotalPagadoPorReserva(idReserva));
        req.setAttribute("titulo",     "Registrar pago");
        req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
    }

    private void pagosPorReserva(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {

        int idReserva = ServletUtil.getInt(req, "id");
        req.setAttribute("pagos",       pagoDAO.listarPorReserva(idReserva));
        req.setAttribute("reserva",     reservaDAO.buscarPorId(idReserva).orElse(null));
        req.setAttribute("totalPagado", pagoDAO.obtenerTotalPagadoPorReserva(idReserva));
        req.getRequestDispatcher(VISTA_LISTA).forward(req, resp);
    }

    private void registrarPago(HttpServletRequest req, HttpServletResponse resp)
            throws Exception {

        int        idReserva = ServletUtil.getInt(req, "idReserva");
        BigDecimal monto     = ServletUtil.getBigDecimal(req, "monto");
        String     metodo    = ServletUtil.getString(req, "metodoPago");
        String     ref       = ServletUtil.getString(req, "referencia");

        List<String> errores = new ArrayList<>();
        if (idReserva == 0)   errores.add("La reserva es obligatoria.");
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
                               errores.add("El monto debe ser mayor a cero.");
        if (metodo.isEmpty())  errores.add("El método de pago es obligatorio.");

        if (!errores.isEmpty()) {
            req.setAttribute("reserva", reservaDAO.buscarPorId(idReserva).orElse(null));
            req.setAttribute("errores", errores);
            req.setAttribute("titulo",  "Registrar pago");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
            return;
        }

        try {
            Pago pago = reservaServicio.registrarPago(idReserva, monto, metodo, ref);
            resp.sendRedirect(req.getContextPath() + "/pagos?accion=porReserva&id="
                    + idReserva + "&mensaje=Pago+registrado+exitosamente&tipo=success");
        } catch (IllegalArgumentException e) {
            errores.add(e.getMessage());
            req.setAttribute("reserva", reservaDAO.buscarPorId(idReserva).orElse(null));
            req.setAttribute("errores", errores);
            req.setAttribute("titulo",  "Registrar pago");
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
        }
    }

    private void manejarError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
    }
}
