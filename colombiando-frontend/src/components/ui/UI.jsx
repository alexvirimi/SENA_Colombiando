import styles from './UI.module.css';

/* ── Button ── variantes: 'primary' | 'outline' | 'ghost' */
export function Button({ children, variante = 'primary', tipo = 'button', onClick, disabled = false, ancho = false, className = '' }) {
  return (
    <button type={tipo} onClick={onClick} disabled={disabled}
      className={[styles.btn, styles[`btn--${variante}`], ancho ? styles['btn--full'] : '', className].join(' ')}>
      {children}
    </button>
  );
}

/* ── Badge / Tag ── color: 'yellow'|'blue'|'red'|'green'|'outline' */
export function Badge({ children, color = 'yellow' }) {
  return <span className={`${styles.badge} ${styles[`badge--${color}`]}`}>{children}</span>;
}

/* ── EstadoBadge ── color semántico según el estado */
export function EstadoBadge({ estado }) {
  const mapa = { PENDIENTE: 'yellow', CONFIRMADA: 'green', CANCELADA: 'red', COMPLETADA: 'blue', APROBADO: 'green', RECHAZADO: 'red', REEMBOLSADO: 'blue' };
  return <Badge color={mapa[estado] ?? 'outline'}>{estado}</Badge>;
}

/* ── Spinner ── */
export function Spinner({ texto = 'Cargando…' }) {
  return (
    <div className={styles.spinnerWrap} role="status" aria-live="polite">
      <span className={styles.spinner} aria-hidden="true" />
      <span className={styles.spinnerText}>{texto}</span>
    </div>
  );
}

/* ── Alert ── tipo: 'success'|'error'|'info' */
export function Alert({ tipo = 'info', children, onCerrar }) {
  return (
    <div className={`alert alert-${tipo} ${styles.alertWrap}`} role="alert">
      <span>{children}</span>
      {onCerrar && <button className={styles.alertClose} onClick={onCerrar} aria-label="Cerrar">✕</button>}
    </div>
  );
}

/* ── Input ── campo con label y error */
export function Input({ label, id, tipo = 'text', placeholder, value, onChange, error, requerido = false, className = '', ...rest }) {
  return (
    <div className={`${styles.field} ${className}`}>
      {label && <label htmlFor={id} className={styles.fieldLabel}>{label} {requerido && <span className={styles.req}>*</span>}</label>}
      <input id={id} type={tipo} placeholder={placeholder} value={value} onChange={onChange}
        required={requerido} className={`${styles.fieldInput} ${error ? styles.fieldInputErr : ''}`} {...rest} />
      {error && <p className={styles.fieldError}>{error}</p>}
    </div>
  );
}

/* ── Modal ── */
export function Modal({ abierto, onCerrar, titulo, children }) {
  if (!abierto) return null;
  return (
    <div className={styles.overlay} role="dialog" aria-modal="true">
      <div className={styles.modal}>
        <div className={styles.modalHeader}>
          <h2 className={styles.modalTitulo}>{titulo}</h2>
          <button className={styles.modalClose} onClick={onCerrar} aria-label="Cerrar">✕</button>
        </div>
        <div className={styles.modalBody}>{children}</div>
      </div>
    </div>
  );
}
