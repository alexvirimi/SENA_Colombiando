import { useNavigate } from 'react-router-dom';
import { Badge } from '../ui/UI';
import styles from './TourCard.module.css';

const pesos = n => new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(n);

export default function TourCard({ tour }) {
  const navigate = useNavigate();
  return (
    <article className={styles.card}
      onClick={() => navigate(`/tours/${tour.idTour}`)}
      role="button" tabIndex={0}
      onKeyDown={e => e.key === 'Enter' && navigate(`/tours/${tour.idTour}`)}
      aria-label={`Ver detalles del tour ${tour.nombre}`}>

      <div className={styles.imagen}>
        {tour.imagenUrl
          ? <img src={tour.imagenUrl} alt={tour.nombre} />
          : <span className={styles.placeholder}>{tour.nombre}</span>}
      </div>

      <div className="tour-tags">
        <Badge color="yellow">{tour.fechaSalida}</Badge>
        {tour.duracionDias && <Badge color="blue">{tour.duracionDias} días</Badge>}
        <Badge color={tour.estado === 'ACTIVO' ? 'green' : 'red'}>{tour.estado}</Badge>
      </div>

      <div className={styles.info}>
        <p className={styles.nombre}>{tour.nombre}</p>
        <p className={styles.precio}>{pesos(tour.precio)}<span className={styles.pax}> / persona</span></p>
        {tour.descripcion && (
          <p className={styles.resumen}>
            {tour.descripcion.length > 90 ? tour.descripcion.slice(0, 90) + '…' : tour.descripcion}
          </p>
        )}
      </div>
    </article>
  );
}
