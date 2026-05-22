package com.colombiando.servicio;

import com.colombiando.dao.PagoDAO;
import com.colombiando.dao.ReservaDAO;
import com.colombiando.dao.TourDAO;
import com.colombiando.modelo.Pago;
import com.colombiando.modelo.Reserva;
import com.colombiando.modelo.Tour;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Capa de servicio que centraliza la lógica de negocio
 * relacionada con las reservas y los pagos de la plataforma Colombiando.
 *
 * <p>Reglas aplicadas:
 * <ul>
 *   <li>No se puede reservar un tour sin cupos disponibles.</li>
 *   <li>No se puede confirmar una reserva sin al menos un pago aprobado.</li>
 *   <li>Solo se puede cancelar una reserva PENDIENTE o CONFIRMADA.</li>
 * </ul>
 */
public class ReservaServicio {

    private final ReservaDAO reservaDAO;
    private final PagoDAO    pagoDAO;
    private final TourDAO    tourDAO;

    public ReservaServicio() {
        this.reservaDAO = new ReservaDAO();
        this.pagoDAO    = new PagoDAO();
        this.tourDAO    = new TourDAO();
    }

    // ── Crear reserva ────────────────────────────────────────────────────────

    /**
     * Crea una nueva reserva verificando disponibilidad de cupos.
     *
     * @param idCliente        identificador del cliente
     * @param idTour           identificador del tour
     * @param numeroPasajeros  cantidad de pasajeros
     * @param fechaReserva     fecha deseada del tour
     * @param observaciones    notas opcionales
     * @return reserva creada con su ID asignado
     * @throws IllegalArgumentException si no hay cupos disponibles
     * @throws SQLException             si ocurre un error en la BD
     */
    public Reserva crearReserva(int idCliente, int idTour, int numeroPasajeros,
                                LocalDate fechaReserva, String observaciones)
            throws IllegalArgumentException, SQLException {

        // 1. Verificar que el tour existe y está activo
        Tour tour = tourDAO.buscarPorId(idTour)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tour no encontrado: ID " + idTour));

        if (!"ACTIVO".equals(tour.getEstado())) {
            throw new IllegalArgumentException(
                    "El tour '" + tour.getNombre() + "' no está disponible.");
        }

        // 2. Verificar cupos disponibles
        int pasajerosActuales = reservaDAO.contarPasajerosEnTour(idTour);
        int cuposDisponibles  = tour.getCapacidadMaxima() - pasajerosActuales;

        if (numeroPasajeros > cuposDisponibles) {
            throw new IllegalArgumentException(String.format(
                    "Solo hay %d cupos disponibles para este tour.", cuposDisponibles));
        }

        // 3. Crear la reserva
        Reserva reserva = new Reserva();
        reserva.setIdCliente(idCliente);
        reserva.setIdTour(idTour);
        reserva.setNumeroPasajeros(numeroPasajeros);
        reserva.setFechaReserva(fechaReserva);
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setEstado("PENDIENTE");
        reserva.setObservaciones(observaciones);

        reservaDAO.insertar(reserva);
        return reserva;
    }

    // ── Registrar pago ───────────────────────────────────────────────────────

    /**
     * Registra un pago para una reserva y confirma la reserva automáticamente
     * si el monto cubre el precio total del tour.
     *
     * @param idReserva   identificador de la reserva
     * @param monto       monto a pagar
     * @param metodoPago  método de pago utilizado
     * @param referencia  referencia externa de la transacción
     * @return pago registrado
     * @throws IllegalArgumentException si la reserva no existe o ya fue pagada
     * @throws SQLException             si ocurre un error en la BD
     */
    public Pago registrarPago(int idReserva, BigDecimal monto,
                              String metodoPago, String referencia)
            throws IllegalArgumentException, SQLException {

        Reserva reserva = reservaDAO.buscarPorId(idReserva)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reserva no encontrada: ID " + idReserva));

        if ("CANCELADA".equals(reserva.getEstado())) {
            throw new IllegalArgumentException("No se puede pagar una reserva cancelada.");
        }

        // Registrar pago
        Pago pago = new Pago();
        pago.setIdReserva(idReserva);
        pago.setMonto(monto);
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodoPago(metodoPago);
        pago.setEstado("APROBADO");
        pago.setReferencia(referencia);

        pagoDAO.insertar(pago);

        // Verificar si el total pagado cubre el precio del tour
        Tour tour = tourDAO.buscarPorId(reserva.getIdTour()).orElse(null);
        if (tour != null) {
            BigDecimal totalPagado  = pagoDAO.obtenerTotalPagadoPorReserva(idReserva);
            BigDecimal totalReserva = tour.getPrecio()
                    .multiply(BigDecimal.valueOf(reserva.getNumeroPasajeros()));

            if (totalPagado.compareTo(totalReserva) >= 0
                    && "PENDIENTE".equals(reserva.getEstado())) {
                reservaDAO.actualizarEstado(idReserva, "CONFIRMADA");
            }
        }

        return pago;
    }

    // ── Cancelar reserva ─────────────────────────────────────────────────────

    /**
     * Cancela una reserva existente si su estado lo permite.
     *
     * @param idReserva identificador de la reserva
     * @return {@code true} si se canceló exitosamente
     * @throws IllegalArgumentException si la reserva no puede cancelarse
     * @throws SQLException             si ocurre un error en la BD
     */
    public boolean cancelarReserva(int idReserva)
            throws IllegalArgumentException, SQLException {

        Reserva reserva = reservaDAO.buscarPorId(idReserva)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reserva no encontrada: ID " + idReserva));

        if ("COMPLETADA".equals(reserva.getEstado())
                || "CANCELADA".equals(reserva.getEstado())) {
            throw new IllegalArgumentException(
                    "La reserva ya está en estado: " + reserva.getEstado());
        }

        return reservaDAO.actualizarEstado(idReserva, "CANCELADA");
    }

    // ── Consultas ────────────────────────────────────────────────────────────

    public Optional<Reserva> obtenerReservaPorId(int idReserva) throws SQLException {
        return reservaDAO.buscarPorId(idReserva);
    }

    public List<Reserva> listarReservasPorCliente(int idCliente) throws SQLException {
        return reservaDAO.listarPorCliente(idCliente);
    }

    public List<Reserva> listarReservasActivas() throws SQLException {
        return reservaDAO.listarPorEstado("CONFIRMADA");
    }

    public int obtenerCuposDisponibles(int idTour) throws SQLException {
        Tour tour = tourDAO.buscarPorId(idTour)
                .orElseThrow(() -> new IllegalArgumentException("Tour no encontrado."));
        int ocupados = reservaDAO.contarPasajerosEnTour(idTour);
        return tour.getCapacidadMaxima() - ocupados;
    }
}
