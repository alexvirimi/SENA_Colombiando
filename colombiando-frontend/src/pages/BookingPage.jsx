import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useTourDetalle } from '../hooks/useTours';
import { useBooking } from '../hooks/useBooking';
import { useAuth } from '../context/AuthContext';
import { BookingForm, BookingSummary, ConfirmationBanner } from '../components/reserva/Forms';
import { Spinner, Alert } from '../components/ui/UI';
import styles from './BookingPage.module.css';

/**
 * BookingPage — flujo de nueva reserva.
 * Lee ?idTour=X de la URL.
 * Layout: formulario (izquierda) + resumen sticky (derecha).
 * Tras el éxito muestra ConfirmationBanner.
 */
export default function BookingPage() {
  const [searchParams]      = useSearchParams();
  const idTour              = Number(searchParams.get('idTour'));
  const navigate            = useNavigate();
  const { usuario }         = useAuth();
  const { tour, cargando: cargandoTour, error: errorTour } = useTourDetalle(idTour);
  const { crearReserva, reserva, cargando, error }         = useBooking();

  /* Estado local del formulario para sincronizar con BookingSummary */
  const [paxActual,   setPaxActual]   = useState(1);
  const [fechaActual, setFechaActual] = useState('');

  useEffect(() => {
    if (tour) setFechaActual(tour.fechaSalida);
  }, [tour]);

  /* Cliente simulado desde sesión — en producción viene de la API */
  const clientes = usuario
    ? [{ idUsuario: usuario.idUsuario, nombre: usuario.nombre,
         apellido: usuario.apellido, numeroDocumento: usuario.numeroDocumento }]
    : [];

  async function handleSubmit(datos) {
    setPaxActual(datos.numeroPasajeros);
    setFechaActual(datos.fechaReserva);
    await crearReserva({ ...datos, idTour });
  }

  /* Confirmación */
  if (reserva) {
    return (
      <section className="padding-block-250">
        <div className="container">
          <ConfirmationBanner
            reserva={reserva}
            tour={tour}
            cliente={usuario}
          />
        </div>
      </section>
    );
  }

  if (cargandoTour) return <div className="padding-block-250 container"><Spinner /></div>;
  if (errorTour)    return <div className="padding-block-250 container"><Alert tipo="error">{errorTour}</Alert></div>;

  return (
    <section className="padding-block-250">
      <div className="container">

        {/* Migas */}
        <button className={styles.volver} onClick={() => navigate(-1)}>
          ← Volver al tour
        </button>

        <h1 className={`${styles.titulo} fs-primary-heading`}>Nueva reserva</h1>

        <div className={styles.layout}>
          {/* Formulario */}
          <div>
            <BookingForm
              tour={tour}
              clientes={clientes}
              onSubmit={handleSubmit}
              cargando={cargando}
              error={error}
            />
          </div>

          {/* Resumen sticky */}
          <BookingSummary
            tour={tour}
            pax={paxActual}
            fecha={fechaActual}
          />
        </div>

      </div>
    </section>
  );
}
