import { ContactForm } from '../components/reserva/Forms';
import styles from './AboutPage.module.css';

const FAQS = [
  {
    pregunta: '¿Cómo reservo un tour?',
    respuesta: 'Navega a Servicios, selecciona el tour que te interesa y haz clic en "Reserva Ya!". Completa el formulario con tus datos y confirma registrando el pago.',
  },
  {
    pregunta: '¿Cuáles son los métodos de pago?',
    respuesta: 'Aceptamos efectivo, tarjeta débito/crédito, transferencia bancaria, PSE y Nequi/Daviplata.',
  },
  {
    pregunta: '¿Puedo cancelar mi reserva?',
    respuesta: 'Sí. Puedes cancelar reservas con estado PENDIENTE o CONFIRMADA desde tu perfil. Consulta nuestra política de cancelación para conocer los reembolsos aplicables según la antelación.',
  },
  {
    pregunta: '¿Los tours incluyen transporte?',
    respuesta: 'Cada tour detalla sus inclusiones. En general los paquetes incluyen transporte local, guía certificado y las actividades descritas en el itinerario.',
  },
  {
    pregunta: '¿Qué pasa si el tour se cancela?',
    respuesta: 'Si Colombiando cancela el tour por causas externas, te ofrecemos reprogramación sin costo o reembolso completo del monto pagado.',
  },
];

/**
 * AboutPage — página acerca de Colombiando.
 * Replica acerca-de.html: texto + FAQ interactivo + contacto.
 */
export default function AboutPage() {
  return (
    <>
      {/* ── ENCABEZADO ──────────────────────────────── */}
      <section className={styles.hero}>
        <div className="container">
          <h1 className="fs-title">Colombiando</h1>
          <p className={styles.intro}>
            Somos una plataforma de reservas turísticas dedicada a mostrar la
            riqueza cultural y natural de Colombia. Conectamos viajeros con
            experiencias auténticas guiadas por expertos locales, desde las
            playas del Caribe hasta la selva amazónica, pasando por el Eje
            Cafetero y los Llanos Orientales.
          </p>
        </div>
      </section>

      {/* ── FAQ ─────────────────────────────────────── */}
      <section className="padding-block-250">
        <div className="container">
          <h2 className="fs-secondary-heading" style={{ marginBottom: '1.25rem' }}>
            FAQ
          </h2>
          <div className={styles.faqList}>
            {FAQS.map((item, i) => (
              <details key={i} className={styles.faqItem}>
                <summary className={styles.faqSummary}>{item.pregunta}</summary>
                <p className={styles.faqRespuesta}>{item.respuesta}</p>
              </details>
            ))}
          </div>
        </div>
      </section>

      {/* ── CONTACTO ────────────────────────────────── */}
      <section className="padding-block-250">
        <div className="container">
          <ContactForm />
        </div>
      </section>
    </>
  );
}
