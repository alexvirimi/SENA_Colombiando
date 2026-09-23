import { useParams, useNavigate } from 'react-router-dom';
import { useTourDetalle } from '../hooks/useTours';
import { GuideCard, Itinerary } from '../components/tour/TourExtras';
import { Button, Badge, Spinner, Alert } from '../components/ui/UI';
import styles from './TourDetailPage.module.css';

const pesos = n =>
  new Intl.NumberFormat('es-CO', {
    style: 'currency', currency: 'COP', maximumFractionDigits: 0,
  }).format(n);

/**
 * TourDetailPage — detalle completo de un tour.
 * Replica tour.html: hero → título + CTA → descripción → destinos →
 *   guías → itinerario → CTA final.
 * Lee :idTour de la URL con useParams.
 */
export default function TourDetailPage() {
  const { idTour }  = useParams();
  const navigate    = useNavigate();
  const { tour, cargando, error } = useTourDetalle(Number(idTour));

  if (cargando) return <div className="padding-block-250 container"><Spinner /></div>;
  if (error)    return <div className="padding-block-250 container"><Alert tipo="error">{error}</Alert></div>;
  if (!tour)    return null;

  return (
    <>
      {/* ── HERO ───────────────────────────────────────── */}
      <section
        className={styles.hero}
        style={tour.imagenUrl ? { backgroundImage: `url(${tour.imagenUrl})` } : {}}
      >
        <div className={`container ${styles.heroContent}`}>
          <button className={styles.volver} onClick={() => navigate(-1)}>
            ← Volver a tours
          </button>
          <h1 className="fs-title" style={{ color: '#fff', textShadow: '0 2px 8px rgba(0,0,0,.25)' }}>
            {tour.nombre}
          </h1>
          <div className="tour-tags" style={{ marginBottom: '1.25rem' }}>
            <Badge color="yellow">{tour.fechaSalida}</Badge>
            {tour.duracionDias && <Badge color="blue">{tour.duracionDias} días</Badge>}
            {tour.region && <Badge color="outline">{tour.region}</Badge>}
            <Badge color={tour.estado === 'ACTIVO' ? 'green' : 'red'}>{tour.estado}</Badge>
          </div>
          {tour.estado === 'ACTIVO' && (
            <Button onClick={() => navigate(`/reservas/nueva?idTour=${tour.idTour}`)}>
              ¡Reserva Ya! →
            </Button>
          )}
        </div>
      </section>

      {/* ── DESCRIPCIÓN + PRECIO ───────────────────────── */}
      <section className="padding-block-250">
        <div className={`container ${styles.mainGrid}`}>

          {/* Descripción */}
          <div>
            <h2 className="fs-secondary-heading" style={{ marginBottom: '1rem' }}>
              Descripción
            </h2>
            <p className={styles.descripcion}>{tour.descripcion}</p>
          </div>

          {/* Panel de precio */}
          <aside className={styles.pricePanel}>
            <p className={styles.precioPor}>Precio por persona</p>
            <p className={styles.precio}>{pesos(tour.precio)}</p>
            <hr className={styles.sep} />
            <p className={styles.detalle}><strong>{tour.duracionDias}</strong> días</p>
            <p className={styles.detalle}><strong>{tour.capacidadMaxima}</strong> personas máx.</p>
            <p className={styles.detalle}>Salida: <strong>{tour.fechaSalida}</strong></p>
            <p className={styles.detalle}>Regreso: <strong>{tour.fechaRegreso}</strong></p>
            {tour.estado === 'ACTIVO' && (
              <Button
                ancho
                onClick={() => navigate(`/reservas/nueva?idTour=${tour.idTour}`)}
                className={styles.pricePanelBtn}
              >
                Reservar ahora
              </Button>
            )}
          </aside>
        </div>
      </section>

      {/* ── DESTINOS ───────────────────────────────────── */}
      {tour.destinos?.length > 0 && (
        <section className="padding-block-250" style={{ background: 'var(--clr-gris-200)' }}>
          <div className="container">
            <h2 className="fs-secondary-heading" style={{ marginBottom: '1rem' }}>
              Destinos incluidos
            </h2>
            <div className={styles.destinosList}>
              {tour.destinos.map(d => (
                <div key={d.idDestino} className={styles.destinoItem}>
                  <span className={styles.destinoPin}>●</span>
                  <div>
                    <p className={styles.destinoNombre}>{d.nombre}</p>
                    <p className={styles.destinoSub}>{d.municipio}, {d.departamento}</p>
                  </div>
                  {d.clima && <Badge color="yellow">{d.clima}</Badge>}
                </div>
              ))}
            </div>
          </div>
        </section>
      )}

      {/* ── GUÍAS ──────────────────────────────────────── */}
      {tour.empleados?.length > 0 && (
        <section className="padding-block-250">
          <div className="container">
            <h2 className="fs-secondary-heading" style={{ marginBottom: '1rem' }}>Guías</h2>
            <div className="three-col-grid">
              {tour.empleados.map(e => (
                <GuideCard key={e.idUsuario} empleado={e} />
              ))}
            </div>
          </div>
        </section>
      )}

      {/* ── ITINERARIO ─────────────────────────────────── */}
      <section className="padding-block-250" style={{ background: 'var(--clr-gris-200)' }}>
        <div className="container">
          <h2 className="fs-secondary-heading" style={{ marginBottom: '1rem' }}>Itinerario</h2>
          <Itinerary actividades={tour.actividades ?? []} />
        </div>
      </section>

      {/* ── CTA FINAL ──────────────────────────────────── */}
      {tour.estado === 'ACTIVO' && (
        <section className="padding-block-250">
          <div className={`container ${styles.ctaFinal}`}>
            <Button
              onClick={() => navigate(`/reservas/nueva?idTour=${tour.idTour}`)}
              className={styles.ctaBtn}
            >
              ¡Reserva Ya!
            </Button>
          </div>
        </section>
      )}
    </>
  );
}
