package com.colombiando.modelo;

/**
 * Representa un destino turístico dentro de Colombia al que apunta un Tour.
 */
public class Destino {

    private int    idDestino;
    private String nombre;
    private String departamento;
    private String municipio;
    private String descripcion;
    private String clima;          // Cálido, Frío, Templado…
    private String imagenUrl;

    // ── Constructores ────────────────────────────────────────────────────────

    public Destino() {}

    public Destino(int idDestino, String nombre, String departamento,
                   String municipio, String descripcion,
                   String clima, String imagenUrl) {
        this.idDestino    = idDestino;
        this.nombre       = nombre;
        this.departamento = departamento;
        this.municipio    = municipio;
        this.descripcion  = descripcion;
        this.clima        = clima;
        this.imagenUrl    = imagenUrl;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public int    getIdDestino()    { return idDestino; }
    public void   setIdDestino(int id)           { this.idDestino    = id; }

    public String getNombre()       { return nombre; }
    public void   setNombre(String nombre)       { this.nombre       = nombre; }

    public String getDepartamento() { return departamento; }
    public void   setDepartamento(String dep)    { this.departamento = dep; }

    public String getMunicipio()    { return municipio; }
    public void   setMunicipio(String mun)       { this.municipio    = mun; }

    public String getDescripcion()  { return descripcion; }
    public void   setDescripcion(String desc)    { this.descripcion  = desc; }

    public String getClima()        { return clima; }
    public void   setClima(String clima)         { this.clima        = clima; }

    public String getImagenUrl()    { return imagenUrl; }
    public void   setImagenUrl(String url)       { this.imagenUrl    = url; }

    @Override
    public String toString() {
        return String.format("[%d] %s — %s, %s | Clima: %s",
                idDestino, nombre, municipio, departamento, clima);
    }
}
