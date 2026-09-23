import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Badge } from '../ui/UI';
import styles from './TourExtras.module.css';

/* ── TourCarousel ── */
export function TourCarousel({ tours = [] }) {
  const [idx, setIdx] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    if (tours.length <= 1) return;
    const t = setInterval(() => setIdx(i => (i + 1) % tours.length), 5000);
    return () => clearInterval(t);
  }, [tours.length]);

  if (!tours.length) return null;
  const tour = tours[idx];

  return (
    <div className={styles.carrousel}>
      <div className={styles.bg} style={tour.imagenUrl ? { backgroundImage: `url(${tour.imagenUrl})` } : {}} />
      <div className={styles.content}>
        <button className={styles.btn} style={{ left: 0 }}
          onClick={() => setIdx(i => (i - 1 + tours.length) % tours.length)} aria-label="Anterior">‹</button>
        <div className={styles.infoCard}>
          <p className={styles.region}>{tour.region} • Colombia</p>
          <h2 className={styles.nombre}>{tour.nombre}</h2>
          <div className="tour-tags" style={{ justifyContent: 'center' }}>
            <Badge color="yellow">{tour.fechaSalida}</Badge>
            <Badge color="blue">{tour.duracionDias} días</Badge>
          </div>
          <button className={styles.verBtn} onClick={() => navigate(`/tours/${tour.idTour}`)}>Ver tour →</button>
        </div>
        <button className={styles.btn} style={{ right: 0 }}
          onClick={() => setIdx(i => (i + 1) % tours.length)} aria-label="Siguiente">›</button>
      </div>
      <div className={styles.dots}>
        {tours.map((_, i) => (
          <button key={i} onClick={() => setIdx(i)}
            className={`${styles.dot} ${i === idx ? styles.dotOn : ''}`} aria-label={`Tour ${i + 1}`} />
        ))}
      </div>
    </div>
  );
}

/* ── TourFilters ── */
const REGIONES = ['Todos', 'Caribe', 'Andina', 'Orinoquía', 'Amazonía', 'Pacífica'];

export function TourFilters({ regionActiva, busqueda, onRegion, onBusqueda }) {
  return (
    <nav className={styles.filtersNav} aria-label="Filtros de tours">
      <div className={styles.navWrapper}>
        <ul role="list" className={styles.navList}>
          {REGIONES.map(r => (
            <li key={r}>
              <button className={`${styles.navItem} ${regionActiva === r ? styles.navOn : ''}`} onClick={() => onRegion(r)}>{r}</button>
            </li>
          ))}
        </ul>
      </div>
      <label className={styles.search}>
        <svg width="18" height="18" viewBox="0 0 32 32" fill="none">
          <path d="M30.25 30.25L20.5 20.5M1 12.375a11.375 11.375 0 1 0 22.75 0A11.375 11.375 0 0 0 1 12.375Z"
            stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
        </svg>
        <input type="search" placeholder="Buscar tour…" value={busqueda}
          onChange={e => onBusqueda(e.target.value)} aria-label="Buscar tour" />
      </label>
    </nav>
  );
}

/* ── GuideCard ── */
export function GuideCard({ empleado }) {
  const ini = `${empleado.nombre?.[0] ?? ''}${empleado.apellido?.[0] ?? ''}`;
  return (
    <div className={styles.guideCard}>
      <div className={styles.avatar}>{ini}</div>
      <div>
        <p className={styles.guideName}>{empleado.nombre} {empleado.apellido}</p>
        <p className={styles.guideCargo}>{empleado.cargo}</p>
      </div>
    </div>
  );
}

/* ── ReviewCard ── */
export function ReviewCard({ review }) {
  const ini = review.nombreUsuario?.split(' ').map(p => p[0]).slice(0,2).join('') ?? '??';
  return (
    <div className={styles.reviewCard}>
      <div className={styles.reviewHeader}>
        <div className={styles.avatar}>{ini}</div>
        <p className={styles.reviewNombre}>{review.nombreUsuario}</p>
      </div>
      <p className={styles.reviewTexto}>{review.comentario}</p>
      <div className={styles.reviewFooter}>
        <span>{review.tourNombre}</span>
        <span>{review.guiaNombre}</span>
        <span>{review.fecha}</span>
      </div>
    </div>
  );
}

/* ── Itinerary ── */
export function Itinerary({ actividades = [] }) {
  if (!actividades.length) return (
    <p style={{ color: 'var(--clr-gris-400)', fontSize: 'var(--fs-200)' }}>
      El itinerario estará disponible próximamente.
    </p>
  );
  return (
    <table className={styles.itinerary}>
      <tbody>
        {actividades.map((a, i) => (
          <tr key={i} className={styles.itRow}>
            <td className={styles.itNo}>{a.numeroActividad}</td>
            <td className={styles.itAct}>{a.descripcion}</td>
            <td className={styles.itTime}>{a.hora}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
