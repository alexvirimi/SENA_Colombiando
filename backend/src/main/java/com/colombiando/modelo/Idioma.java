package com.colombiando.modelo;

/**
 * Representa un idioma que puede hablar un {@link Empleado}.
 */
public class Idioma {

    private int    idIdioma;
    private String nombre;       // Español, Inglés, Francés…
    private String codigoIso;    // es, en, fr…
    private String nivelRequerido; // BASICO, INTERMEDIO, AVANZADO, NATIVO

    // ── Constructores ────────────────────────────────────────────────────────

    public Idioma() {}

    public Idioma(int idIdioma, String nombre, String codigoIso, String nivelRequerido) {
        this.idIdioma       = idIdioma;
        this.nombre         = nombre;
        this.codigoIso      = codigoIso;
        this.nivelRequerido = nivelRequerido;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public int    getIdIdioma()        { return idIdioma; }
    public void   setIdIdioma(int id)  { this.idIdioma       = id; }

    public String getNombre()          { return nombre; }
    public void   setNombre(String n)  { this.nombre         = n; }

    public String getCodigoIso()       { return codigoIso; }
    public void   setCodigoIso(String c){ this.codigoIso     = c; }

    public String getNivelRequerido()  { return nivelRequerido; }
    public void   setNivelRequerido(String nivel) { this.nivelRequerido = nivel; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) — Nivel: %s",
                idIdioma, nombre, codigoIso, nivelRequerido);
    }
}
