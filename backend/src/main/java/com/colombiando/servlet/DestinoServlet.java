package com.colombiando.servlet;

import com.colombiando.dao.DestinoDAO;
import com.colombiando.modelo.Destino;
import com.colombiando.util.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet CRUD para la entidad {@link Destino}.
 */
@WebServlet(name = "DestinoServlet", urlPatterns = "/destinos")
public class DestinoServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DestinoServlet.class.getName());
    private static final String VISTA_LISTA = "/WEB-INF/views/destino/lista.jsp";
    private static final String VISTA_FORM  = "/WEB-INF/views/destino/formulario.jsp";

    private DestinoDAO destinoDAO;

    @Override
    public void init() throws ServletException {
        destinoDAO = new DestinoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = ServletUtil.getString(req, "accion", "listar");
        try {
            switch (accion) {
                case "nuevo"    -> { req.setAttribute("destino", new Destino());
                                    req.setAttribute("modoEdicion", false);
                                    req.getRequestDispatcher(VISTA_FORM).forward(req, resp); }
                case "editar"   -> {
                    int id = ServletUtil.getInt(req, "id");
                    Optional<Destino> opt = destinoDAO.buscarPorId(id);
                    req.setAttribute("destino",    opt.orElse(new Destino()));
                    req.setAttribute("modoEdicion",true);
                    req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
                }
                case "eliminar" -> {
                    destinoDAO.eliminar(ServletUtil.getInt(req, "id"));
                    resp.sendRedirect(req.getContextPath()
                            + "/destinos?mensaje=Destino+eliminado&tipo=success");
                }
                default -> {
                    req.setAttribute("destinos", destinoDAO.listarTodos());
                    req.getRequestDispatcher(VISTA_LISTA).forward(req, resp);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error DestinoServlet.", e);
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Destino d = construirDesdeRequest(req);
        List<String> errores = new ArrayList<>();

        if (ServletUtil.esVacio(d.getNombre()))       errores.add("El nombre es obligatorio.");
        if (ServletUtil.esVacio(d.getDepartamento())) errores.add("El departamento es obligatorio.");
        if (ServletUtil.esVacio(d.getMunicipio()))    errores.add("El municipio es obligatorio.");

        if (!errores.isEmpty()) {
            req.setAttribute("destino",    d);
            req.setAttribute("errores",    errores);
            req.setAttribute("modoEdicion",d.getIdDestino() > 0);
            req.getRequestDispatcher(VISTA_FORM).forward(req, resp);
            return;
        }

        try {
            if (d.getIdDestino() > 0) {
                destinoDAO.actualizar(d);
                resp.sendRedirect(req.getContextPath()
                        + "/destinos?mensaje=Destino+actualizado&tipo=success");
            } else {
                destinoDAO.insertar(d);
                resp.sendRedirect(req.getContextPath()
                        + "/destinos?mensaje=Destino+creado+exitosamente&tipo=success");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error guardando destino.", e);
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
        }
    }

    private Destino construirDesdeRequest(HttpServletRequest req) {
        Destino d = new Destino();
        d.setIdDestino(ServletUtil.getInt(req, "idDestino"));
        d.setNombre(ServletUtil.getString(req, "nombre"));
        d.setDepartamento(ServletUtil.getString(req, "departamento"));
        d.setMunicipio(ServletUtil.getString(req, "municipio"));
        d.setDescripcion(ServletUtil.getString(req, "descripcion"));
        d.setClima(ServletUtil.getString(req, "clima"));
        d.setImagenUrl(ServletUtil.getString(req, "imagenUrl"));
        return d;
    }
}
