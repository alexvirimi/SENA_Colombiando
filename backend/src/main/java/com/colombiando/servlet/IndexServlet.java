package com.colombiando.servlet;

import com.colombiando.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet del panel principal (dashboard) de Colombiando.
 * Carga estadísticas generales y las expone al JSP mediante atributos del request.
 *
 * <p>GET  /index → muestra el dashboard</p>
 */
@WebServlet(name = "IndexServlet", urlPatterns = {"/", "/index"})
public class IndexServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(IndexServlet.class.getName());

    private ClienteDAO  clienteDAO;
    private EmpleadoDAO empleadoDAO;
    private TourDAO     tourDAO;
    private ReservaDAO  reservaDAO;

    @Override
    public void init() throws ServletException {
        clienteDAO  = new ClienteDAO();
        empleadoDAO = new EmpleadoDAO();
        tourDAO     = new TourDAO();
        reservaDAO  = new ReservaDAO();
    }

    // ── GET: mostrar dashboard ───────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Estadísticas del sistema
            req.setAttribute("totalClientes",  clienteDAO.listarTodos().size());
            req.setAttribute("totalEmpleados", empleadoDAO.listarTodos().size());
            req.setAttribute("totalTours",     tourDAO.listarTodos().size());
            req.setAttribute("toursActivos",   tourDAO.listarToursActivos().size());
            req.setAttribute("totalReservas",  reservaDAO.listarTodos().size());
            req.setAttribute("reservasConfirmadas",
                    reservaDAO.listarPorEstado("CONFIRMADA").size());

            // Últimos 5 tours activos para tarjetas de portada
            req.setAttribute("ultimosTours",   tourDAO.listarToursActivos());

            req.getRequestDispatcher("/WEB-INF/views/index.jsp")
               .forward(req, resp);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cargando dashboard.", e);
            req.setAttribute("error", "No se pudieron cargar las estadísticas.");
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp")
               .forward(req, resp);
        }
    }
}
