package com.colombiando.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa un pago asociado a una {@link Reserva}.
 */
public class Pago {

    private int          idPago;
    private int          idReserva;
    private BigDecimal   monto;
    private LocalDateTime fechaPago;
    private String       metodoPago;   // EFECTIVO, TARJETA, TRANSFERENCIA, PSE
    private String       estado;       // PENDIENTE, APROBADO, RECHAZADO, REEMBOLSADO
    private String       referencia;   // Número de transacción externo
    private String       observaciones;

    // ── Constructores ────────────────────────────────────────────────────────

    public Pago() {}

    public Pago(int idPago, int idReserva, BigDecimal monto,
                LocalDateTime fechaPago, String metodoPago,
                String estado, String referencia, String observaciones) {
        this.idPago        = idPago;
        this.idReserva     = idReserva;
        this.monto         = monto;
        this.fechaPago     = fechaPago;
        this.metodoPago    = metodoPago;
        this.estado        = estado;
        this.referencia    = referencia;
        this.observaciones = observaciones;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public int          getIdPago()       { return idPago; }
    public void         setIdPago(int id) { this.idPago        = id; }

    public int          getIdReserva()    { return idReserva; }
    public void         setIdReserva(int id){ this.idReserva   = id; }

    public BigDecimal   getMonto()        { return monto; }
    public void         setMonto(BigDecimal m){ this.monto     = m; }

    public LocalDateTime getFechaPago()   { return fechaPago; }
    public void          setFechaPago(LocalDateTime f){ this.fechaPago = f; }

    public String       getMetodoPago()   { return metodoPago; }
    public void         setMetodoPago(String m){ this.metodoPago = m; }

    public String       getEstado()       { return estado; }
    public void         setEstado(String e){ this.estado       = e; }

    public String       getReferencia()   { return referencia; }
    public void         setReferencia(String r){ this.referencia = r; }

    public String       getObservaciones(){ return observaciones; }
    public void         setObservaciones(String o){ this.observaciones = o; }

    @Override
    public String toString() {
        return String.format("[%d] Reserva %d | $%s | %s | %s | Ref: %s",
                idPago, idReserva, monto, metodoPago, estado, referencia);
    }
}
