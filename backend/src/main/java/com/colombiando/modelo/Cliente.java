package com.colombiando.modelo;

import java.time.LocalDate;

/**
 * Representa a un cliente que puede realizar reservas de tours turísticos.
 * Hereda los atributos generales de {@link Usuario}.
 */
public class Cliente extends Usuario {

    private String     numeroDocumento;
    private String     tipoDocumento;   // CC, CE, PASAPORTE
    private LocalDate  fechaNacimiento;
    private String     nacionalidad;

    // ── Constructores ────────────────────────────────────────────────────────

    public Cliente() {
        super();
        setTipoUsuario("CLIENTE");
    }

    public Cliente(int idUsuario, String nombre, String apellido,
                   String correo, String telefono, String contrasena,
                   String numeroDocumento, String tipoDocumento,
                   LocalDate fechaNacimiento, String nacionalidad) {
        super(idUsuario, nombre, apellido, correo, telefono, contrasena, "CLIENTE");
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento   = tipoDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad    = nacionalidad;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public String    getNumeroDocumento()  { return numeroDocumento; }
    public void      setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String    getTipoDocumento()    { return tipoDocumento; }
    public void      setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LocalDate getFechaNacimiento()  { return fechaNacimiento; }
    public void      setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String    getNacionalidad()     { return nacionalidad; }
    public void      setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    // ── Implementación abstracta ─────────────────────────────────────────────

    @Override
    public String obtenerInformacion() {
        return String.format("CLIENTE — %s %s | Doc: %s %s | Nac: %s | País: %s",
                getNombre(), getApellido(),
                tipoDocumento, numeroDocumento,
                fechaNacimiento, nacionalidad);
    }
}
