import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { bookingService } from '../services/otrosServices';
import { Spinner, Alert, EstadoBadge, Button } from '../components/ui/UI';
import styles from './ProfilePage.module.css';

/**
 * ProfilePage — perfil del cliente logueado.
 * Replica mi-perfil.html: saludo → tours activos → tours pasados.
 * Ruta protegida por PrivateRoute.
 */
export default function ProfilePage() {
  const { usuario }  = useAuth();
  const navigate     = useNavigate();
  const [reservas,   setReservas]   = useState([]);
  const [cargando,   setCargando]   = useState(true);
  const [error,      setError]      = useState('');

  useEffect(() => {
    if (!usuario?.idUsuario) return;
    bookingService.listarPorCliente(usuario.idUsuario)
      .then(({ data }) => setReservas(Array.isArray(data) ? data : []))
      .catch(() => setError('No se pudieron cargar tus reservas.'))
      .finally(() => setCargando(false));
  }, [usuario]);

  const activas = reservas.filter(r =>
    ['PENDIENTE', 'CONFIRMADA'].includes(r.estado)
  );
  const pasadas = reservas.filter(r =>
    ['COMPLETADA', 'CANCELADA'].includes(r.estado)
  );

  return (
    <>
      {/* ── SALUDO ────────────────────────────────────── */}
      <section className="padding-block-250">
        <div className="container">
          <h1 className="fs-title">
            Hola, {usuario?.nombre ?? 'Viajero'}
          </h1>
          <p className={styles.sub}>
            {usuario?.correo}
            {usuario?.telefono && ` • ${usuario.telefono}`}
          </p>
        </div>
      </section>

      {/* ── TOURS ACTIVOS ─────────────────────────────── */}
      <section className="padding-block-250">
        <div className="container">
          <h2 className="fs-secondary-heading" style={{ marginBottom: '1.25rem' }}>
            Tours activos
          </h2>

          {cargando && <Spinner />}
          {error    && <Alert tipo="error">{error}</Alert>}

          {!cargando && !error && (
            activas.length === 0 ? (
              <div className={styles.empty}>
                <p>No tienes tours activos en este momento.</p>
                <Button onClick={() => navigate('/tours')}>
                  ¡Explora nuestros tours!
                </Button>
              </div>
            ) : (
              <div className="three-col-grid">
                {activas.map(r => (
                  <ReservaCard
                    key={r.idReserva}
                    reserva={r}
                    onPagar={() => navigate(`/pagos/nueva?idReserva=${r.idReserva}`)}
                  />
                ))}
              </div>
            )
          )}
        </div>
      </section>

      {/* ── TOURS PASADOS ─────────────────────────────── */}
      {!cargando && pasadas.length > 0 && (
        <section className="padding-block-250" style={{ background: 'var(--clr-gris-200)' }}>
          <div className="container">
            <h2 className="fs-secondary-heading" style={{ marginBottom: '1.25rem' }}>
              Tours pasados
            </h2>
            <div className="three-col-grid">
              {pasadas.map(r => (
                <ReservaCard
                  key={r.idReserva}
                  reserva={r}
                  onCalificar={
                    r.estado === 'COMPLETADA'
                      ? () => navigate(`/calificar?idReserva=${r.idReserva}`)
                      : null
                  }
                />
              ))}
            </div>
          </div>
        </section>
      )}
    </>
  );
}

/* ── ReservaCard — tarjeta individual dentro del perfil ── */
function ReservaCard({ reserva, onPagar, onCalificar }) {
  const pesos = n =>
    new Intl.NumberFormat('es-CO', {
      style: 'currency', currency: 'COP', maximumFractionDigits: 0,
    }).format(n);

  return (
    <div className={styles.card}>
      {/* Cabecera */}
      <div className={styles.cardHeader}>
        <figure className={styles.cardFig}>
          Tour #{reserva.idTour}
        </figure>
      </div>

      {/* Cuerpo */}
      <div className={styles.cardBody}>
        <div className={styles.cardTop}>
          <p className={styles.cardTour}>Tour #{reserva.idTour}</p>
          <EstadoBadge estado={reserva.estado} />
        </div>

        <p className={styles.cardMeta}>
          {reserva.fechaReserva} · {reserva.numeroPasajeros} pax
        </p>

        {reserva.totalReserva && (
          <p className={styles.cardTotal}>{pesos(reserva.totalReserva)}</p>
        )}

        {/* Acciones */}
        <div className={styles.cardActions}>
          {onPagar && (
            <Button variante="primary" onClick={onPagar} className={styles.btnSm}>
              Registrar pago
            </Button>
          )}
          {onCalificar && (
            <Button variante="outline" onClick={onCalificar} className={styles.btnSm}>
              ✦ Calificar
            </Button>
          )}
        </div>
      </div>
    </div>
  );
}
