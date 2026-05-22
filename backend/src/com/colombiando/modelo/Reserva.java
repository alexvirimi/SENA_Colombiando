package com.colombiando.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa una reserva realizada por un {@link Cliente} para un {@link Tour}.
 */
public class Reserva {

    private int           idReserva;
    private int           idCliente;
    private int           idTour;
    private int           numeroPasajeros;
    private LocalDate     fechaReserva;
    private LocalDateTime fechaCreacion;
    private String        estado;    // PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA
    private String        observaciones;

    // ── Constructores ────────────────────────────────────────────────────────

    public Reserva() {}

    public Reserva(int idReserva, int idCliente, int idTour,
                   int numeroPasajeros, LocalDate fechaReserva,
                   LocalDateTime fechaCreacion, String estado,
                   String observaciones) {
        this.idReserva       = idReserva;
        this.idCliente       = idCliente;
        this.idTour          = idTour;
        this.numeroPasajeros = numeroPasajeros;
        this.fechaReserva    = fechaReserva;
        this.fechaCreacion   = fechaCreacion;
        this.estado          = estado;
        this.observaciones   = observaciones;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public int           getIdReserva()     { return idReserva; }
    public void          setIdReserva(int id)   { this.idReserva       = id; }

    public int           getIdCliente()     { return idCliente; }
    public void          setIdCliente(int id)   { this.idCliente       = id; }

    public int           getIdTour()        { return idTour; }
    public void          setIdTour(int id)  { this.idTour          = id; }

    public int           getNumeroPasajeros() { return numeroPasajeros; }
    public void          setNumeroPasajeros(int n){ this.numeroPasajeros = n; }

    public LocalDate     getFechaReserva()  { return fechaReserva; }
    public void          setFechaReserva(LocalDate f) { this.fechaReserva = f; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void          setFechaCreacion(LocalDateTime f){ this.fechaCreacion = f; }

    public String        getEstado()        { return estado; }
    public void          setEstado(String e){ this.estado          = e; }

    public String        getObservaciones() { return observaciones; }
    public void          setObservaciones(String o){ this.observaciones = o; }

    @Override
    public String toString() {
        return String.format("[%d] Cliente %d → Tour %d | Pasajeros: %d | Fecha: %s | Estado: %s",
                idReserva, idCliente, idTour, numeroPasajeros, fechaReserva, estado);
    }
}
