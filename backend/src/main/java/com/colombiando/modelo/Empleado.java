package com.colombiando.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un empleado de la plataforma Colombiando.
 * Puede ser guía, coordinador u otro rol asignado a tours.
 * Hereda los atributos generales de {@link Usuario}.
 */
public class Empleado extends Usuario {

    private String     cargo;
    private BigDecimal salario;
    private LocalDate  fechaContratacion;
    private List<Idioma> idiomas;

    // ── Constructores ────────────────────────────────────────────────────────

    public Empleado() {
        super();
        setTipoUsuario("EMPLEADO");
        this.idiomas = new ArrayList<>();
    }

    public Empleado(int idUsuario, String nombre, String apellido,
                    String correo, String telefono, String contrasena,
                    String cargo, BigDecimal salario, LocalDate fechaContratacion) {
        super(idUsuario, nombre, apellido, correo, telefono, contrasena, "EMPLEADO");
        this.cargo             = cargo;
        this.salario           = salario;
        this.fechaContratacion = fechaContratacion;
        this.idiomas           = new ArrayList<>();
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public String     getCargo()             { return cargo; }
    public void       setCargo(String cargo) { this.cargo = cargo; }

    public BigDecimal getSalario()              { return salario; }
    public void       setSalario(BigDecimal s)  { this.salario = s; }

    public LocalDate  getFechaContratacion()    { return fechaContratacion; }
    public void       setFechaContratacion(LocalDate f) { this.fechaContratacion = f; }

    public List<Idioma> getIdiomas()            { return idiomas; }
    public void         setIdiomas(List<Idioma> idiomas) { this.idiomas = idiomas; }

    public void agregarIdioma(Idioma idioma) {
        this.idiomas.add(idioma);
    }

    // ── Implementación abstracta ─────────────────────────────────────────────

    @Override
    public String obtenerInformacion() {
        return String.format("EMPLEADO — %s %s | Cargo: %s | Salario: $%s | Contratado: %s",
                getNombre(), getApellido(), cargo, salario, fechaContratacion);
    }
}
