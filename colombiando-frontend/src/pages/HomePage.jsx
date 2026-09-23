import { useTours } from '../hooks/useTours';
import TourCard from '../components/tour/TourCard';
import { TourCarousel, ReviewCard } from '../components/tour/TourExtras';
import { ContactForm } from '../components/reserva/Forms';
import { Spinner, Alert } from '../components/ui/UI';
import styles from './HomePage.module.css';

/* Reseñas estáticas — en producción vendrían de la API */
const REVIEWS_ESTATICAS = [
  {
    nombreUsuario: 'María González',
    comentario: 'Una experiencia que superó todas mis expectativas. Carlos conoce cada rincón de Cartagena con una pasión que se contagia.',
    tourNombre: 'Cartagena Mágica',
    guiaNombre: 'Carlos Mendoza',
    fecha: 'Jun 2025',
  },
  {
    nombreUsuario: 'James Anderson',
    comentario: 'Best trip of my life. Ciudad Perdida is indescribable. Colombiando made every detail effortless.',
    tourNombre: 'Tayrona y Ciudad Perdida',
    guiaNombre: 'Andrés Herrera',
    fecha: 'Jul 2025',
  },
  {
    nombreUsuario: 'Laura Restrepo',
    comentario: 'Caño Cristales es un sueño hecho realidad. La organización fue impecable de principio a fin.',
    tourNombre: 'Caño Cristales',
    guiaNombre: 'Diana Ospina',
    fecha: 'Sep 2025',
  },
];

/**
 * HomePage — página principal pública.
 * Secciones: Carousel hero → Tours próximos → Reseñas → Contacto.
 * Consume useTours() para datos reales del backend.
 */
export default function HomePage() {
  const { tours, cargando, error } = useTours();

  return (
    <>
      {/* ── HERO: carousel de tours activos ─────────────── */}
      <section className={`${styles.heroSection} padding-block-250`}>
        <div className="container">
          {cargando ? (
            <div className={styles.heroPlaceholder}>
              <Spinner texto="Cargando tours…" />
            </div>
          ) : error ? (
            <div className={styles.heroPlaceholder}>
              <p className={styles.heroTagline}>No hay mejor forma de viajar que</p>
              <h1 className="fs-title">Colombiando</h1>
            </div>
          ) : (
            <TourCarousel tours={tours.slice(0, 5)} />
          )}
        </div>
      </section>

      {/* ── PRÓXIMOS TOURS ───────────────────────────────── */}
      <section className="padding-block-250">
        <div className="container">
          <h2 className="fs-secondary-heading" style={{ marginBottom: '1.25rem' }}>
            Próximos Tours
          </h2>

          {cargando && <Spinner />}
          {error    && <Alert tipo="error">{error}</Alert>}

          {!cargando && !error && (
            <>
              <div className="three-col-grid">
                {tours.slice(0, 3).map(t => (
                  <TourCard key={t.idTour} tour={t} />
                ))}
              </div>

              <div className={styles.verTodos}>
                <a href="/tours" className={styles.verTodosBtn}>
                  Ver todos los tours →
                </a>
              </div>
            </>
          )}
        </div>
      </section>

      {/* ── RESEÑAS ──────────────────────────────────────── */}
      <section className={`${styles.reviewsSection} padding-block-250`}>
        <div className="container">
          <div className={styles.reviewsHeader}>
            <p style={{ color: 'var(--clr-gris-600)', fontSize: 'var(--fs-200)' }}>
              No nos tomes la palabra,
            </p>
            <h2 className="fs-secondary-heading">lee a nuestros usuarios</h2>
          </div>
          <div className="three-col-grid">
            {REVIEWS_ESTATICAS.map((r, i) => (
              <ReviewCard key={i} review={r} />
            ))}
          </div>
        </div>
      </section>

      {/* ── CONTACTO ─────────────────────────────────────── */}
      <section className="padding-block-250">
        <div className="container">
          <ContactForm />
        </div>
      </section>
    </>
  );
}
