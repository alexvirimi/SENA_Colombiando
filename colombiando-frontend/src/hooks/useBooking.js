import { useState } from 'react';
import { bookingService, paymentService } from '../services/otrosServices';

export function useBooking() {
  const [reserva,  setReserva]  = useState(null);
  const [cargando, setCargando] = useState(false);
  const [error,    setError]    = useState(null);
  const [exito,    setExito]    = useState(false);

  async function crearReserva(datos) {
    setCargando(true); setError(null);
    try {
      const { data } = await bookingService.crear(datos);
      setReserva(data);
      return data;
    } catch (e) {
      const msg = e.response?.data?.mensaje || 'Error al crear la reserva.';
      setError(msg); throw new Error(msg);
    } finally { setCargando(false); }
  }

  async function registrarPago(datosPago) {
    setCargando(true); setError(null);
    try {
      await paymentService.registrar(datosPago);
      setExito(true);
    } catch (e) {
      setError(e.response?.data?.mensaje || 'Error al registrar el pago.');
    } finally { setCargando(false); }
  }

  async function cancelarReserva(id) {
    setCargando(true);
    try { await bookingService.cancelar(id); }
    catch (e) { setError(e.response?.data?.mensaje || 'No se pudo cancelar.'); }
    finally { setCargando(false); }
  }

  function reiniciar() { setReserva(null); setError(null); setExito(false); }

  return { reserva, cargando, error, exito, crearReserva, registrarPago, cancelarReserva, reiniciar };
}
