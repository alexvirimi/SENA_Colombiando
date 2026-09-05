package com.colombiando.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un paquete turístico ofrecido en la plataforma Colombiando.
 * Un Tour puede tener varios {@link Destino}s y {@link Empleado}s asignados.
 */
public class Tour {

    private int        idTour;
    private String     nombre;
    private String     descripcion;
    private BigDecimal precio;
    private int        duracionDias;
    private int        capacidadMaxima;
    private LocalDate  fechaSalida;
    private LocalDate  fechaRegreso;
    private String     estado;          // ACTIVO, CANCELADO, COMPLETO
    private List<Destino>  destinos;
    private List<Empleado> empleados;

    // ── Constructores ────────────────────────────────────────────────────────

    public Tour() {
        this.destinos  = new ArrayList<>();
        this.empleados = new ArrayList<>();
    }

    public Tour(int idTour, String nombre, String descripcion,
                BigDecimal precio, int duracionDias, int capacidadMaxima,
                LocalDate fechaSalida, LocalDate fechaRegreso, String estado) {
        this.idTour          = idTour;
        this.nombre          = nombre;
        this.descripcion     = descripcion;
        this.precio          = precio;
        this.duracionDias    = duracionDias;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaSalida     = fechaSalida;
        this.fechaRegreso    = fechaRegreso;
        this.estado          = estado;
        this.destinos        = new ArrayList<>();
        this.empleados       = new ArrayList<>();
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public int        getIdTour()         { return idTour; }
    public void       setIdTour(int id)   { this.idTour          = id; }

    public String     getNombre()         { return nombre; }
    public void       setNombre(String n) { this.nombre          = n; }

    public String     getDescripcion()    { return descripcion; }
    public void       setDescripcion(String d) { this.descripcion = d; }

    public BigDecimal getPrecio()         { return precio; }
    public void       setPrecio(BigDecimal p)  { this.precio     = p; }

    public int        getDuracionDias()   { return duracionDias; }
    public void       setDuracionDias(int d)   { this.duracionDias = d; }

    public int        getCapacidadMaxima(){ return capacidadMaxima; }
    public void       setCapacidadMaxima(int c){ this.capacidadMaxima = c; }

    public LocalDate  getFechaSalida()    { return fechaSalida; }
    public void       setFechaSalida(LocalDate f){ this.fechaSalida  = f; }

    public LocalDate  getFechaRegreso()   { return fechaRegreso; }
    public void       setFechaRegreso(LocalDate f){ this.fechaRegreso = f; }

    public String     getEstado()         { return estado; }
    public void       setEstado(String e) { this.estado          = e; }

    public List<Destino>  getDestinos()   { return destinos; }
    public void           setDestinos(List<Destino> destinos)   { this.destinos  = destinos; }

    public List<Empleado> getEmpleados()  { return empleados; }
    public void           setEmpleados(List<Empleado> empleados){ this.empleados = empleados; }

    public void agregarDestino(Destino destino)   { this.destinos.add(destino); }
    public void agregarEmpleado(Empleado empleado){ this.empleados.add(empleado); }

    @Override
    public String toString() {
        return String.format("[%d] %s | Precio: $%s | Días: %d | Salida: %s | Estado: %s",
                idTour, nombre, precio, duracionDias, fechaSalida, estado);
    }
}
