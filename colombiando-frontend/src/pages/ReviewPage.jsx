import { useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { Button, Input, Alert } from '../components/ui/UI';
import styles from './ReviewPage.module.css';

/**
 * ReviewPage — calificación de tour y guía post-experiencia.
 * Replica tour-review.html.
 * Lee ?idReserva=X de la URL.
 */
export default function ReviewPage() {
  const [searchParams] = useSearchParams();
  const idReserva      = searchParams.get('idReserva');
  const navigate       = useNavigate();

  const [form, setForm] = useState({
    calificacionTour: '', comentarioTour: '',
    calificacionGuia: '', comentarioGuia: '',
  });
  const [enviado,  setEnviado]  = useState(false);
  const [cargando, setCargando] = useState(false);
  const [errores,  setErrores]  = useState({});

  const set = k => e => setForm(p => ({ ...p, [k]: e.target.value }));

  function validar() {
    const err = {};
    const t = Number(form.calificacionTour);
    const g = Number(form.calificacionGuia);
    if (!t || t < 1 || t > 10) err.calificacionTour = 'Ingresa un valor entre 1 y 10.';
    if (!g || g < 1 || g > 10) err.calificacionGuia = 'Ingresa un valor entre 1 y 10.';
    setErrores(err);
    return !Object.keys(err).length;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (!validar()) return;
    setCargando(true);
    /* Aquí iría la llamada a la API: reviewService.crear({ idReserva, ...form }) */
    await new Promise(r => setTimeout(r, 800)); // simulado
    setCargando(false);
    setEnviado(true);
  }

  if (enviado) return (
    <section className="padding-block-250">
      <div className={`container ${styles.exitoWrap}`}>
        <div className={styles.exitoIcon}>⭐</div>
        <h1 className="fs-primary-heading" style={{ color: 'var(--clr-verde)' }}>
          ¡Gracias por tu reseña!
        </h1>
        <p style={{ color: 'var(--clr-gris-600)' }}>
          Tu opinión ayuda a mejorar la experiencia para futuros viajeros.
        </p>
        <div className={styles.exitoBtns}>
          <Button onClick={() => navigate('/perfil')}>Ver mis tours</Button>
          <Button variante="outline" onClick={() => navigate('/tours')}>Explorar más tours</Button>
        </div>
      </div>
    </section>
  );

  return (
    <section className="padding-block-250">
      <div className="container">

        <button className={styles.volver} onClick={() => navigate(-1)}>
          ← Volver
        </button>

        <div className={styles.layout}>

          {/* Formulario de reseña */}
          <div>
            <h1 className={`${styles.titulo} fs-primary-heading`}>
              Califica tu experiencia
            </h1>
            <p className={styles.subtitulo}>
              Tu opinión nos ayuda a seguir mejorando.
            </p>

            <form
              className={styles.form}
              onSubmit={handleSubmit}
              noValidate
            >
              {/* Calificación del tour */}
              <div className={styles.bloque}>
                <h3 className="fs-body-title" style={{ marginBottom: '1rem' }}>
                  Calificación del tour
                </h3>
                <div className={styles.stars}>
                  {[1,2,3,4,5,6,7,8,9,10].map(n => (
                    <button
                      key={n}
                      type="button"
                      className={`${styles.star} ${Number(form.calificacionTour) >= n ? styles.starOn : ''}`}
                      onClick={() => setForm(p => ({ ...p, calificacionTour: String(n) }))}
                    >
                      {n}
                    </button>
                  ))}
                </div>
                {errores.calificacionTour && (
                  <p className={styles.err}>{errores.calificacionTour}</p>
                )}
                <div className={styles.field}>
                  <label className={styles.label}>Describe tu experiencia</label>
                  <textarea
                    className={styles.textarea}
                    rows={4}
                    placeholder="¿Qué fue lo que más te gustó del tour?"
                    value={form.comentarioTour}
                    onChange={set('comentarioTour')}
                  />
                </div>
              </div>

              <hr className={styles.sep} />

              {/* Calificación del guía */}
              <div className={styles.bloque}>
                <h3 className="fs-body-title" style={{ marginBottom: '1rem' }}>
                  Calificación del guía
                </h3>
                <div className={styles.stars}>
                  {[1,2,3,4,5,6,7,8,9,10].map(n => (
                    <button
                      key={n}
                      type="button"
                      className={`${styles.star} ${Number(form.calificacionGuia) >= n ? styles.starOn : ''}`}
                      onClick={() => setForm(p => ({ ...p, calificacionGuia: String(n) }))}
                    >
                      {n}
                    </button>
                  ))}
                </div>
                {errores.calificacionGuia && (
                  <p className={styles.err}>{errores.calificacionGuia}</p>
                )}
                <div className={styles.field}>
                  <label className={styles.label}>Describe tu experiencia con el guía</label>
                  <textarea
                    className={styles.textarea}
                    rows={4}
                    placeholder="¿El guía fue claro, puntual y profesional?"
                    value={form.comentarioGuia}
                    onChange={set('comentarioGuia')}
                  />
                </div>
              </div>

              <Button tipo="submit" ancho disabled={cargando}>
                {cargando ? 'Enviando reseña…' : 'Enviar Reseña'}
              </Button>
            </form>
          </div>

          {/* Info de la reserva */}
          <aside className={styles.infoPanel}>
            <h3 className={styles.infoPanelTitle}>Reserva #{idReserva}</h3>
            <p className={styles.infoItem}>
              Gracias por completar este tour con Colombiando.
              Tu reseña será visible para otros viajeros.
            </p>
            <div className={styles.infoTip}>
              <span className={styles.infoTipIcon}>💡</span>
              <p>Las reseñas honestas ayudan a mejorar la calidad de nuestros tours.</p>
            </div>
          </aside>

        </div>
      </div>
    </section>
  );
}
