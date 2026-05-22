package com.colombiando.modelo;

/**
 * Clase base que representa a un usuario del sistema Colombiando.
 * Cliente y Empleado heredan de esta clase.
 */
public abstract class Usuario {

    private int    idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String contrasena;
    private String tipoUsuario; // "CLIENTE" o "EMPLEADO"

    // ── Constructores ────────────────────────────────────────────────────────

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellido,
                   String correo, String telefono,
                   String contrasena, String tipoUsuario) {
        this.idUsuario   = idUsuario;
        this.nombre      = nombre;
        this.apellido    = apellido;
        this.correo      = correo;
        this.telefono    = telefono;
        this.contrasena  = contrasena;
        this.tipoUsuario = tipoUsuario;
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public int    getIdUsuario()   { return idUsuario; }
    public void   setIdUsuario(int idUsuario)     { this.idUsuario   = idUsuario; }

    public String getNombre()      { return nombre; }
    public void   setNombre(String nombre)        { this.nombre      = nombre; }

    public String getApellido()    { return apellido; }
    public void   setApellido(String apellido)    { this.apellido    = apellido; }

    public String getCorreo()      { return correo; }
    public void   setCorreo(String correo)        { this.correo      = correo; }

    public String getTelefono()    { return telefono; }
    public void   setTelefono(String telefono)    { this.telefono    = telefono; }

    public String getContrasena()  { return contrasena; }
    public void   setContrasena(String contrasena){ this.contrasena  = contrasena; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void   setTipoUsuario(String tipoUsuario){ this.tipoUsuario = tipoUsuario; }

    // ── Método abstracto ─────────────────────────────────────────────────────

    /** Cada subclase muestra su información específica. */
    public abstract String obtenerInformacion();

    @Override
    public String toString() {
        return String.format("[%d] %s %s <%s> | Tel: %s | Tipo: %s",
                idUsuario, nombre, apellido, correo, telefono, tipoUsuario);
    }
}
