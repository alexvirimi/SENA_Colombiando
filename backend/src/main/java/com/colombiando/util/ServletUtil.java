package com.colombiando.util;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Utilidades para leer y validar parámetros de peticiones HTTP en los Servlets.
 *
 * <p>Centraliza la extracción segura de parámetros, evitando
 * {@code NullPointerException} y errores de conversión dispersos.</p>
 */
public final class ServletUtil {

    private ServletUtil() {}

    // ── Lectura de parámetros ────────────────────────────────────────────────

    /**
     * Lee un parámetro String del request, retornando {@code ""} si es nulo.
     */
    public static String getString(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        return (valor != null) ? valor.trim() : "";
    }

    /**
     * Lee un parámetro String; retorna {@code defecto} si está vacío o es nulo.
     */
    public static String getString(HttpServletRequest req, String nombre, String defecto) {
        String valor = getString(req, nombre);
        return valor.isEmpty() ? defecto : valor;
    }

    /**
     * Lee un parámetro entero. Retorna {@code 0} si no existe o no es un número.
     */
    public static int getInt(HttpServletRequest req, String nombre) {
        try {
            String valor = getString(req, nombre);
            return valor.isEmpty() ? 0 : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Lee un parámetro entero. Retorna {@code defecto} si no existe o es inválido.
     */
    public static int getInt(HttpServletRequest req, String nombre, int defecto) {
        try {
            String valor = getString(req, nombre);
            return valor.isEmpty() ? defecto : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return defecto;
        }
    }

    /**
     * Lee un parámetro como {@link BigDecimal}. Retorna {@code null} si falla.
     */
    public static BigDecimal getBigDecimal(HttpServletRequest req, String nombre) {
        try {
            String valor = getString(req, nombre);
            return valor.isEmpty() ? null : new BigDecimal(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Lee un parámetro de fecha en formato {@code yyyy-MM-dd}.
     * Retorna {@code null} si el formato es incorrecto o está vacío.
     */
    public static LocalDate getLocalDate(HttpServletRequest req, String nombre) {
        try {
            String valor = getString(req, nombre);
            return valor.isEmpty() ? null : LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // ── Validaciones ─────────────────────────────────────────────────────────

    /**
     * Verifica que un String no sea nulo ni vacío.
     */
    public static boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /**
     * Verifica que un correo tenga formato básico válido.
     */
    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Verifica que un teléfono colombiano tenga formato válido (10 dígitos).
     */
    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && telefono.matches("^[0-9]{10}$");
    }

    /**
     * Escapa caracteres HTML para prevenir XSS en la salida.
     */
    public static String escaparHtml(String valor) {
        if (valor == null) return "";
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    /**
     * Construye una URL con parámetros para redirección.
     *
     * @param base     ruta base (p. ej. "clientes")
     * @param params   pares clave=valor alternados
     * @return URL construida
     */
    public static String buildUrl(String base, String... params) {
        if (params.length == 0) return base;
        StringBuilder sb = new StringBuilder(base).append("?");
        for (int i = 0; i < params.length - 1; i += 2) {
            if (i > 0) sb.append("&");
            sb.append(params[i]).append("=").append(params[i + 1]);
        }
        return sb.toString();
    }
}
