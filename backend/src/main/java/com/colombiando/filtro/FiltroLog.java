package com.colombiando.filtro;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Filtro que registra cada petición HTTP con su método,
 * URI, parámetros y tiempo de procesamiento.
 */
@WebFilter("/*")
public class FiltroLog implements Filter {

    private static final Logger LOGGER = Logger.getLogger(FiltroLog.class.getName());
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        long               ini  = System.currentTimeMillis();

        // Excluir recursos estáticos del log
        String uri = req.getRequestURI();
        boolean esEstatico = uri.endsWith(".css") || uri.endsWith(".js")
                          || uri.endsWith(".png") || uri.endsWith(".ico");

        if (!esEstatico) {
            LOGGER.info(String.format("[%s] %s %s",
                    LocalDateTime.now().format(FMT),
                    req.getMethod(),
                    uri));
        }

        chain.doFilter(request, response);

        if (!esEstatico) {
            long tiempo = System.currentTimeMillis() - ini;
            LOGGER.fine(String.format("  → Completado en %d ms", tiempo));
        }
    }

    @Override
    public void destroy() {}
}
