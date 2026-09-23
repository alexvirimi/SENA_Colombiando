import { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { authService } from '../services/otrosServices';
import { Button, Input, Alert } from '../components/ui/UI';
import styles from './AuthPages.module.css';

/* Logo reutilizable */
function LogoHeader() {
  return (
    <div className={styles.logoHeader}>
      <Link to="/" className={styles.logo}>Colombiando</Link>
    </div>
  );
}

/* ═══════════════════════════════════════════════════════════
   LoginPage — replica de ingreso.html
   ═══════════════════════════════════════════════════════════ */
export function LoginPage() {
  const { login }    = useAuth();
  const navigate     = useNavigate();
  const location     = useLocation();
  const destino      = location.state?.from?.pathname ?? '/';

  const [form,     setForm]     = useState({ correo: '', contrasena: '' });
  const [cargando, setCargando] = useState(false);
  const [error,    setError]    = useState('');

  const set = k => e => setForm(p => ({ ...p, [k]: e.target.value }));

  async function handleSubmit(e) {
    e.preventDefault();
    if (!form.correo || !form.contrasena) {
      setError('Completa todos los campos.');
      return;
    }
    setCargando(true);
    setError('');
    try {
      const { data } = await authService.login(form.correo, form.contrasena);
      login(data.usuario, data.token);
      navigate(destino, { replace: true });
    } catch (err) {
      setError(err.response?.data?.mensaje || 'Correo o contraseña incorrectos.');
    } finally {
      setCargando(false);
    }
  }

  return (
    <section className="padding-block-250">
      <div className={styles.wrap}>
        <div className={styles.card}>
          <LogoHeader />
          {error && <Alert tipo="error">{error}</Alert>}
          <form onSubmit={handleSubmit} noValidate>
            <Input
              label="Correo"
              id="correo"
              tipo="email"
              placeholder="example@email.com"
              value={form.correo}
              onChange={set('correo')}
              requerido
            />
            <div className={styles.pwdField}>
              <Input
                label="Contraseña"
                id="pwd"
                tipo="password"
                placeholder="Contraseña"
                value={form.contrasena}
                onChange={set('contrasena')}
                requerido
              />
              <Link to="/recuperar" className={styles.linkSmall}>
                Olvidé la contraseña
              </Link>
            </div>
            <Button tipo="submit" ancho disabled={cargando}>
              {cargando ? 'Ingresando…' : 'Ingresar'}
            </Button>
          </form>
          <p className={styles.switchLink}>
            ¿Aún no tienes cuenta?{' '}
            <Link to="/registro" className={styles.link}>Regístrate</Link>
          </p>
        </div>
      </div>
    </section>
  );
}

/* ═══════════════════════════════════════════════════════════
   RegisterPage — replica de registro.html
   ═══════════════════════════════════════════════════════════ */
export function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nombre: '', apellido: '', correo: '', telefono: '',
    numeroDocumento: '', tipoDocumento: '', fechaNacimiento: '',
    nacionalidad: 'Colombiana', contrasena: '', contrasena2: '',
  });
  const [cargando, setCargando] = useState(false);
  const [errores,  setErrores]  = useState({});
  const [apiError, setApiError] = useState('');

  const set = k => e => setForm(p => ({ ...p, [k]: e.target.value }));

  function validar() {
    const err = {};
    if (!form.nombre)           err.nombre          = 'El nombre es obligatorio.';
    if (!form.apellido)         err.apellido        = 'El apellido es obligatorio.';
    if (!/\S+@\S+\.\S+/.test(form.correo)) err.correo = 'Correo inválido.';
    if (!/^[0-9]{10}$/.test(form.telefono)) err.telefono = 'El teléfono debe tener 10 dígitos.';
    if (!form.numeroDocumento)  err.numeroDocumento = 'El documento es obligatorio.';
    if (!form.contrasena)       err.contrasena      = 'La contraseña es obligatoria.';
    if (form.contrasena !== form.contrasena2) err.contrasena2 = 'Las contraseñas no coinciden.';
    setErrores(err);
    return !Object.keys(err).length;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (!validar()) return;
    setCargando(true);
    setApiError('');
    try {
      await authService.registrar(form);
      navigate('/login?registrado=1');
    } catch (err) {
      setApiError(err.response?.data?.mensaje || 'Error al crear la cuenta.');
    } finally {
      setCargando(false);
    }
  }

  return (
    <section className="padding-block-250">
      <div className={`${styles.wrap} ${styles.wrapWide}`}>
        <div className={styles.card}>
          <LogoHeader />
          {apiError && <Alert tipo="error">{apiError}</Alert>}
          <form onSubmit={handleSubmit} noValidate>
            <div className={styles.grid2}>
              <Input label="Nombre" id="nombre" requerido value={form.nombre}
                     onChange={set('nombre')} placeholder="Jane" error={errores.nombre} />
              <Input label="Apellido" id="apellido" requerido value={form.apellido}
                     onChange={set('apellido')} placeholder="Doe" error={errores.apellido} />
            </div>

            <Input label="Correo" id="correo" tipo="email" requerido value={form.correo}
                   onChange={set('correo')} placeholder="example@email.com" error={errores.correo} />

            <div className={styles.phoneRow}>
              <div className={styles.field}>
                <label className={styles.label}>Teléfono <span style={{color:'var(--clr-rojo)'}}>*</span></label>
                <div className={styles.phoneWrap}>
                  <select className={styles.select} defaultValue="+57">
                    <option>+57</option><option>+1</option><option>+34</option>
                  </select>
                  <input
                    type="tel"
                    className={`${styles.input} ${errores.telefono ? styles.inputErr : ''}`}
                    placeholder="3001234567"
                    value={form.telefono}
                    onChange={set('telefono')}
                    maxLength={10}
                  />
                </div>
                {errores.telefono && <p className={styles.err}>{errores.telefono}</p>}
              </div>
            </div>

            <div className={styles.grid2}>
              <div className={styles.field}>
                <label className={styles.label}>Tipo documento</label>
                <select className={styles.select} value={form.tipoDocumento} onChange={set('tipoDocumento')}>
                  <option value="">Seleccionar…</option>
                  <option value="CC">CC — Cédula de ciudadanía</option>
                  <option value="CE">CE — Cédula de extranjería</option>
                  <option value="PASAPORTE">Pasaporte</option>
                </select>
              </div>
              <Input label="Número de documento" id="numDoc" requerido
                     value={form.numeroDocumento} onChange={set('numeroDocumento')}
                     placeholder="1023456789" error={errores.numeroDocumento} />
            </div>

            <div className={styles.grid2}>
              <Input label="Contraseña" id="pwd" tipo="password" requerido
                     value={form.contrasena} onChange={set('contrasena')}
                     placeholder="Mínimo 6 caracteres" error={errores.contrasena} />
              <Input label="Repetir contraseña" id="pwd2" tipo="password" requerido
                     value={form.contrasena2} onChange={set('contrasena2')}
                     placeholder="Contraseña" error={errores.contrasena2} />
            </div>

            <div className={styles.checkRow}>
              <input type="checkbox" id="pol" required />
              <label htmlFor="pol" style={{ fontSize: 'var(--fs-200)' }}>
                Acepto la política de tratamiento de datos personales
              </label>
            </div>

            <Button tipo="submit" ancho disabled={cargando}>
              {cargando ? 'Creando cuenta…' : 'Crear cuenta'}
            </Button>
          </form>
          <p className={styles.switchLink}>
            ¿Ya tienes cuenta?{' '}
            <Link to="/login" className={styles.link}>Inicia sesión</Link>
          </p>
        </div>
      </div>
    </section>
  );
}

/* ═══════════════════════════════════════════════════════════
   RecuperarPage — replica de recuperar-pwd.html
   ═══════════════════════════════════════════════════════════ */
export function RecuperarPage() {
  const [correo,   setCorreo]   = useState('');
  const [enviado,  setEnviado]  = useState(false);
  const [cargando, setCargando] = useState(false);
  const [error,    setError]    = useState('');

  async function handleSubmit(e) {
    e.preventDefault();
    if (!/\S+@\S+\.\S+/.test(correo)) { setError('Ingresa un correo válido.'); return; }
    setCargando(true);
    setError('');
    try {
      await authService.recuperarPassword(correo);
      setEnviado(true);
    } catch {
      setError('No pudimos enviar el correo. Verifica que esté registrado.');
    } finally {
      setCargando(false);
    }
  }

  return (
    <section className="padding-block-250">
      <div className={styles.wrap}>
        <div className={styles.card}>
          <LogoHeader />
          {enviado ? (
            <Alert tipo="success">
              ✓ Revisa tu correo. Enviamos un enlace de recuperación a <strong>{correo}</strong>.
            </Alert>
          ) : (
            <>
              <p style={{ fontSize: 'var(--fs-200)', color: 'var(--clr-gris-600)', marginBottom: 'var(--size-150)', textAlign: 'center' }}>
                Ingresa tu correo para recuperar tu contraseña
              </p>
              {error && <Alert tipo="error">{error}</Alert>}
              <form onSubmit={handleSubmit} noValidate>
                <Input
                  label="Correo"
                  id="correo-rec"
                  tipo="email"
                  placeholder="example@email.com"
                  value={correo}
                  onChange={e => setCorreo(e.target.value)}
                  requerido
                />
                <Button tipo="submit" ancho disabled={cargando}>
                  {cargando ? 'Enviando…' : 'Recuperar Contraseña'}
                </Button>
              </form>
            </>
          )}
          <p className={styles.switchLink}>
            <Link to="/login" className={styles.link}>← Volver a ingresar</Link>
          </p>
        </div>
      </div>
    </section>
  );
}
