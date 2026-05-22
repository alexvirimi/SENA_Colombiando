package com.colombiando.servicio;

import com.colombiando.dao.DestinoDAO;
import com.colombiando.dao.EmpleadoDAO;
import com.colombiando.dao.ReservaDAO;
import com.colombiando.dao.TourDAO;
import com.colombiando.modelo.Destino;
import com.colombiando.modelo.Empleado;
import com.colombiando.modelo.Tour;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class TourServicio {

    private final TourDAO     tourDAO;
    private final DestinoDAO  destinoDAO;
    private final EmpleadoDAO empleadoDAO;
    private final ReservaDAO  reservaDAO;

    public TourServicio() {
        this.tourDAO     = new TourDAO();
        this.destinoDAO  = new DestinoDAO();
        this.empleadoDAO = new EmpleadoDAO();
        this.reservaDAO  = new ReservaDAO();
    }

    // ── Crear tour ────────────────────────────────────────────────────────────

    /**
     * Crea un tour nuevo y lo persiste en la base de datos.
     *
     * @param tour objeto Tour con todos sus datos
     * @return tour guardado con su ID asignado
     * @throws IllegalArgumentException si los datos del tour son inválidos
     * @throws SQLException             si ocurre un error en la BD
     */
    public Tour crearTour(Tour tour) throws IllegalArgumentException, SQLException {
        validarTour(tour);
        tourDAO.insertar(tour);
        return tour;
    }

    // ── Cancelar tour ─────────────────────────────────────────────────────────

    /**
     * Cancela un tour activo.
     * Si tiene reservas PENDIENTES las cancela también en cascada.
     *
     * @param idTour identificador del tour
     * @return {@code true} si se canceló correctamente
     * @throws IllegalArgumentException si el tour no existe o ya está cancelado
     * @throws SQLException             si ocurre un error en la BD
     */
    public boolean cancelarTour(int idTour) throws IllegalArgumentException, SQLException {
        Tour tour = tourDAO.buscarPorId(idTour)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tour no encontrado: ID " + idTour));

        if ("CANCELADO".equals(tour.getEstado())) {
            throw new IllegalArgumentException("El tour ya está cancelado.");
        }

        // Verificar reservas confirmadas (no se puede cancelar si hay pasajeros confirmados)
        long confirmadas = reservaDAO.listarPorTour(idTour).stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .count();

        if (confirmadas > 0) {
            throw new IllegalArgumentException(
                    "No se puede cancelar: el tour tiene " + confirmadas
                    + " reserva(s) confirmada(s). Gestione los reembolsos primero.");
        }

        // Cancelar reservas PENDIENTES en cascada
        reservaDAO.listarPorTour(idTour).stream()
                .filter(r -> "PENDIENTE".equals(r.getEstado()))
                .forEach(r -> {
                    try {
                        reservaDAO.actualizarEstado(r.getIdReserva(), "CANCELADA");
                    } catch (SQLException e) {
                        throw new RuntimeException("Error al cancelar reserva: " + r.getIdReserva(), e);
                    }
                });

        tour.setEstado("CANCELADO");
        return tourDAO.actualizar(tour);
    }

    // ── Obtener tour con detalles ─────────────────────────────────────────────

    /**
     * Retorna un tour con sus destinos y empleados precargados.
     *
     * @param idTour identificador del tour
     * @return Optional con el tour y sus relaciones, o vacío si no existe
     * @throws SQLException si ocurre un error en la BD
     */
    public Optional<Tour> obtenerTourCompleto(int idTour) throws SQLException {
        Optional<Tour> optTour = tourDAO.buscarPorId(idTour);
        if (optTour.isPresent()) {
            Tour tour = optTour.get();
            List<Destino>  destinos  = destinoDAO.listarDestinosPorTour(idTour);
            List<Empleado> empleados = empleadoDAO.listarEmpleadosPorTour(idTour);
            tour.setDestinos(destinos);
            tour.setEmpleados(empleados);
        }
        return optTour;
    }

    // ── Listar tours disponibles ──────────────────────────────────────────────

    public List<Tour> listarToursDisponibles() throws SQLException {
        return tourDAO.listarToursActivos();
    }

    public List<Tour> listarTodos() throws SQLException {
        return tourDAO.listarTodos();
    }

    // ── Asignar destino / empleado ────────────────────────────────────────────

    public boolean asignarDestino(int idTour, int idDestino) throws SQLException {
        tourDAO.buscarPorId(idTour)
                .orElseThrow(() -> new IllegalArgumentException("Tour no encontrado."));
        destinoDAO.buscarPorId(idDestino)
                .orElseThrow(() -> new IllegalArgumentException("Destino no encontrado."));
        return tourDAO.agregarDestino(idTour, idDestino);
    }

    public boolean asignarEmpleado(int idTour, int idEmpleado) throws SQLException {
        tourDAO.buscarPorId(idTour)
                .orElseThrow(() -> new IllegalArgumentException("Tour no encontrado."));
        empleadoDAO.buscarPorId(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado."));
        return tourDAO.agregarEmpleado(idTour, idEmpleado);
    }

    // ── Validación interna ────────────────────────────────────────────────────

    private void validarTour(Tour tour) {
        if (tour.getNombre() == null || tour.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del tour es obligatorio.");
        }
        if (tour.getPrecio() == null || tour.getPrecio().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio del tour debe ser mayor a cero.");
        }
        if (tour.getCapacidadMaxima() <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a cero.");
        }
        if (tour.getFechaSalida() == null || tour.getFechaRegreso() == null) {
            throw new IllegalArgumentException("Las fechas de salida y regreso son obligatorias.");
        }
        if (tour.getFechaRegreso().isBefore(tour.getFechaSalida())) {
            throw new IllegalArgumentException("La fecha de regreso no puede ser anterior a la salida.");
        }
    }
}
