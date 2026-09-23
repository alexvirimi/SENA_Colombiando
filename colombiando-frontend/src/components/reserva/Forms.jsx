import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Input, Alert, EstadoBadge } from '../ui/UI';
import { paymentService } from '../../services/otrosServices';
import styles from './Forms.module.css';

const pesos = n =>
  new Intl.NumberFormat('es-CO', {
    style: 'currency', currency: 'COP', maximumFractionDigits: 0,
  }).format(n);

/* ═══════════════════════════════════════════════════════════
   BookingForm — formulario de nueva reserva
   ═══════════════════════════════════════════════════════════ */
export function BookingForm({ tour, clientes = [], onSubmit, cargando, error }) {
  const [form, setForm] = useState({
    idCliente:       '',
    idTour:          tour?.idTour ?? '',
    numeroPasajeros: 1,
    fechaReserva:    tour?.fechaSalida ?? '',
    observaciones:   '',
  });
  const [errores, setErrores] = useState({});
  const set = k => e => setForm(p => ({ ...p, [k]: e.target.value }));

  function validar() {
    const err = {};
    if (!form.idCliente)     err.idCliente      = 'Selecciona un cliente.';
    if (!form.fechaReserva)  err.fechaReserva   = 'La fecha es obligatoria.';
    if (form.numeroPasajeros < 1) err.numeroPasajeros = 'Mínimo 1 pasajero.';
    if (tour && form.numeroPasajeros > tour.capacidadMaxima)
      err.numeroPasajeros = `Máximo ${tour.capacidadMaxima} pasajeros.`;
    setErrores(err);
    return !Object.keys(err).length;
  }

  function handleSubmit(e) {
    e.preventDefault();
    if (validar()) onSubmit(form);
  }

  const total = tour ? tour.precio * form.numeroPasajeros : 0;

  return (
    <form className={styles.form} onSubmit={handleSubmit} noValidate>
      {error && <Alert tipo="error">{error}</Alert>}

      {tour && (
        <div className={styles.tourPreview}>
          <div className={styles.tourPreviewColor} />
          <div>
            <p className={styles.tourPreviewNombre}>{tour.nombre}</p>
            <p className={styles.tourPreviewSub}>
              {tour.duracionDias} días · Máx. {tour.capacidadMaxima} personas
            </p>
          </div>
          <p className={styles.tourPreviewPrecio}>
            {pesos(tour.precio)}<span>/pax</span>
          </p>
        </div>
      )}

      <div className={styles.field}>
        <label className={styles.label}>Cliente <span className={styles.req}>*</span></label>
        <select
          className={`${styles.select} ${errores.idCliente ? styles.inputErr : ''}`}
          value={form.idCliente} onChange={set('idCliente')} required>
          <option value="">Seleccionar cliente…</option>
          {clientes.map(c => (
            <option key={c.idUsuario} value={c.idUsuario}>
              {c.nombre} {c.apellido} — {c.numeroDocumento}
            </option>
          ))}
        </select>
        {errores.idCliente && <p className={styles.err}>{errores.idCliente}</p>}
      </div>

      <div className={styles.row}>
        <div className={styles.field}>
          <label className={styles.label}>Pasajeros <span className={styles.req}>*</span></label>
          <input type="number"
            className={`${styles.input} ${errores.numeroPasajeros ? styles.inputErr : ''}`}
            min={1} max={tour?.capacidadMaxima ?? 50}
            value={form.numeroPasajeros}
            onChange={e => setForm(p => ({ ...p, numeroPasajeros: Math.max(1, +e.target.value) }))} />
          {errores.numeroPasajeros && <p className={styles.err}>{errores.numeroPasajeros}</p>}
        </div>
        <div className={styles.field}>
          <label className={styles.label}>Fecha del tour <span className={styles.req}>*</span></label>
          <input type="date"
            className={`${styles.input} ${errores.fechaReserva ? styles.inputErr : ''}`}
            value={form.fechaReserva} min={tour?.fechaSalida ?? ''}
            onChange={set('fechaReserva')} required />
          {errores.fechaReserva && <p className={styles.err}>{errores.fechaReserva}</p>}
        </div>
      </div>

      {total > 0 && (
        <div className={styles.costoPreview}>
          <span>Costo estimado</span>
          <strong style={{ color: 'var(--clr-verde)' }}>{pesos(total)}</strong>
        </div>
      )}

      <div className={styles.field}>
        <label className={styles.label}>Observaciones / Notas especiales</label>
        <textarea className={styles.textarea} rows={3}
          placeholder="Ej: cliente con movilidad reducida, solicita habitación doble…"
          value={form.observaciones} onChange={set('observaciones')} />
      </div>

      <Button tipo="submit" ancho disabled={cargando}>
        {cargando ? 'Creando reserva…' : 'Crear reserva'}
      </Button>
    </form>
  );
}

/* ═══════════════════════════════════════════════════════════
   BookingSummary — panel lateral de resumen (solo presentación)
   ═══════════════════════════════════════════════════════════ */
export function BookingSummary({ tour, pax = 1, fecha }) {
  if (!tour) return null;
  const total = tour.precio * pax;
  return (
    <aside className={styles.summary}>
      <h3 className={styles.summaryTitle}>Resumen</h3>
      <div className={styles.summaryTourBanner}>
        <p className={styles.summaryTourNombre}>{tour.nombre}</p>
        <p className={styles.summaryTourSub}>{tour.duracionDias} días · {tour.estado}</p>
      </div>
      {[
        ['Precio por persona', pesos(tour.precio)],
        ['Pasajeros',          pax],
        ['Fecha',              fecha || tour.fechaSalida],
        ['Capacidad máxima',  `${tour.capacidadMaxima} pax`],
      ].map(([k, v]) => (
        <div key={k} className={styles.summaryRow}>
          <span>{k}</span><span>{v}</span>
        </div>
      ))}
      <div className={styles.summaryTotal}>
        <span>Total estimado</span>
        <strong style={{ color: 'var(--clr-verde)' }}>{pesos(total)}</strong>
      </div>
      <p className={styles.summaryNote}>
        La reserva se confirma automáticamente al recibir el pago completo.
      </p>
    </aside>
  );
}

/* ═══════════════════════════════════════════════════════════
   PaymentForm — formulario de registro de pago
   ═══════════════════════════════════════════════════════════ */
export function PaymentForm({ idReserva, totalPendiente, onExito }) {
  const [form, setForm] = useState({
    monto: totalPendiente ?? '',
    metodoPago: '',
    referencia: '',
    observaciones: '',
  });
  const [cargando, setCargando] = useState(false);
  const [error,    setError]    = useState('');
  const set = k => e => setForm(p => ({ ...p, [k]: e.target.value }));

  const METODOS = [
    { value: 'EFECTIVO',      label: '💵  Efectivo' },
    { value: 'TARJETA',       label: '💳  Tarjeta débito / crédito' },
    { value: 'TRANSFERENCIA', label: '🏦  Transferencia bancaria' },
    { value: 'PSE',           label: '🌐  PSE' },
    { value: 'NEQUI',         label: '📱  Nequi / Daviplata' },
  ];

  async function handleSubmit(e) {
    e.preventDefault();
    if (!form.metodoPago || !form.monto) {
      setError('Monto y método de pago son obligatorios.'); return;
    }
    setCargando(true); setError('');
    try {
      await paymentService.registrar({ ...form, idReserva });
      onExito?.();
    } catch (err) {
      setError(err.response?.data?.mensaje || 'Error al registrar el pago.');
    } finally {
      setCargando(false);
    }
  }

  return (
    <form className={styles.form} onSubmit={handleSubmit} noValidate>
      {error && <Alert tipo="error">{error}</Alert>}

      <Input label="Monto (COP)" id="monto" tipo="number" requerido
        value={form.monto} onChange={set('monto')} placeholder="1800000" />

      <div className={styles.field}>
        <label className={styles.label}>Método de pago <span className={styles.req}>*</span></label>
        <select className={styles.select} value={form.metodoPago}
          onChange={set('metodoPago')} required>
          <option value="">Seleccionar…</option>
          {METODOS.map(m => <option key={m.value} value={m.value}>{m.label}</option>)}
        </select>
      </div>

      <Input label="Referencia / Nº de transacción" id="referencia"
        value={form.referencia} onChange={set('referencia')}
        placeholder="TXN-20260615-001" />

      <div className={styles.field}>
        <label className={styles.label}>Observaciones</label>
        <textarea className={styles.textarea} rows={2}
          placeholder="Notas adicionales del pago…"
          value={form.observaciones} onChange={set('observaciones')} />
      </div>

      <Button tipo="submit" ancho disabled={cargando}>
        {cargando ? 'Procesando…' : 'Confirmar pago'}
      </Button>
    </form>
  );
}

/* ═══════════════════════════════════════════════════════════
   ConfirmationBanner — reserva creada con éxito
   ═══════════════════════════════════════════════════════════ */
export function ConfirmationBanner({ reserva, tour, cliente }) {
  const navigate = useNavigate();
  if (!reserva) return null;
  const total = tour ? tour.precio * reserva.numeroPasajeros : 0;

  return (
    <div className={styles.confirm}>
      <div className={styles.confirmIcon}>🇨🇴</div>
      <h1 className={styles.confirmTitle}>¡Reserva creada exitosamente!</h1>
      <p className={styles.confirmSub}>
        Tu reserva está pendiente de pago. Confírmala registrando el pago.
      </p>

      <div className={styles.confirmCard}>
        {[
          ['Nº de reserva', `#${reserva.idReserva}`],
          ['Cliente',  cliente ? `${cliente.nombre} ${cliente.apellido}` : `#${reserva.idCliente}`],
          ['Tour',     tour?.nombre ?? `Tour #${reserva.idTour}`],
          ['Pasajeros', reserva.numeroPasajeros],
          ['Fecha del tour', reserva.fechaReserva],
          ['Total estimado', total > 0 ? pesos(total) : '—'],
        ].map(([k, v]) => (
          <div key={k} className={styles.confirmRow}>
            <span className={styles.confirmKey}>{k}</span>
            <strong>{v}</strong>
          </div>
        ))}
        <div className={styles.confirmEstado}>
          <EstadoBadge estado={reserva.estado} />
        </div>
      </div>

      <div className={styles.confirmActions}>
        <Button onClick={() => navigate(`/pagos/nueva?idReserva=${reserva.idReserva}`)}>
          Registrar pago ahora
        </Button>
        <Button variante="outline" onClick={() => navigate('/reservas')}>
          Ver todas las reservas
        </Button>
        <Button variante="outline" onClick={() => navigate('/tours')}>
          Explorar más tours
        </Button>
      </div>
    </div>
  );
}

/* ═══════════════════════════════════════════════════════════
   ContactForm — formulario de contacto reutilizable
   Aparece en: HomePage, ToursPage, AboutPage
   ═══════════════════════════════════════════════════════════ */
export function ContactForm() {
  const [form,  setForm]  = useState({ nombre: '', correo: '', mensaje: '' });
  const [sent,  setSent]  = useState(false);
  const [error, setError] = useState('');
  const set = k => e => setForm(p => ({ ...p, [k]: e.target.value }));

  function handleSubmit(e) {
    e.preventDefault();
    if (!form.nombre || !form.correo || !form.mensaje) {
      setError('Completa todos los campos obligatorios.'); return;
    }
    setSent(true);
  }

  if (sent) return (
    <div className={styles.contactWrap}>
      <Alert tipo="success">✓ Mensaje enviado. Te responderemos pronto.</Alert>
      <Button variante="outline"
        onClick={() => { setSent(false); setForm({ nombre: '', correo: '', mensaje: '' }); }}>
        Enviar otro mensaje
      </Button>
    </div>
  );

  return (
    <form className={styles.contactWrap} onSubmit={handleSubmit} noValidate>
      <div className={styles.contactHeader}>
        <p style={{ color: 'var(--clr-gris-600)', fontSize: 'var(--fs-200)' }}>¿Quieres saber más?</p>
        <h2 className="fs-secondary-heading">Contáctanos</h2>
      </div>
      {error && <Alert tipo="error">{error}</Alert>}
      <div className={styles.row}>
        <Input label="Nombre Completo" id="c-nombre" requerido
          value={form.nombre} onChange={set('nombre')} placeholder="Jane Doe" />
        <Input label="Correo" id="c-correo" tipo="email" requerido
          value={form.correo} onChange={set('correo')} placeholder="example@email.com" />
      </div>
      <div className={styles.field}>
        <label className={styles.label}>Mensaje</label>
        <textarea className={styles.textarea} rows={4} placeholder="Mensaje"
          value={form.mensaje} onChange={set('mensaje')} required />
      </div>
      <div className={styles.checkRow}>
        <input type="checkbox" id="politica" required />
        <label htmlFor="politica" style={{ fontSize: 'var(--fs-200)' }}>
          Acepto la política de tratamiento de datos personales
        </label>
      </div>
      <Button tipo="submit" ancho>Enviar Mensaje</Button>
    </form>
  );
}
