import { useNavigate } from 'react-router-dom';
import { Button } from '../components/ui/UI';
import styles from './NotFoundPage.module.css';

/**
 * NotFoundPage — página 404.
 * Se muestra para cualquier ruta no definida en el Router.
 */
export default function NotFoundPage() {
  const navigate = useNavigate();
  return (
    <section className="padding-block-250">
      <div className={styles.wrap}>
        <div className={styles.icon}>🗺️</div>
        <h1 className={styles.code}>404</h1>
        <h2 className="fs-secondary-heading">Destino no encontrado</h2>
        <p className={styles.texto}>
          La página que buscas no existe o fue movida a otro destino turístico.
        </p>
        <div className={styles.btns}>
          <Button onClick={() => navigate('/')}>← Volver al inicio</Button>
          <Button variante="outline" onClick={() => navigate('/tours')}>
            Ver tours disponibles
          </Button>
        </div>
      </div>
    </section>
  );
}
