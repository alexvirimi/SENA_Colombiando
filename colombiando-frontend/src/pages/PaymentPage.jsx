import { useSearchParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import { bookingService, paymentService } from "../services/otrosServices";
import { PaymentForm } from "../components/reserva/Forms";
import { Spinner, Alert, EstadoBadge, Button } from "../components/ui/UI";
import styles from "./PaymentPage.module.css";

const pesos = (n) =>
  new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0,
  }).format(n);

/**
 * PaymentPage — registro de pago para una reserva.
 * Lee ?idReserva=X de la URL.
 */
export default function PaymentPage() {
  const [searchParams] = useSearchParams();
  const idReserva = Number(searchParams.get("idReserva"));
  const navigate = useNavigate();

  const [reserva, setReserva] = useState(null);
  const [pagos, setPagos] = useState([]);
  const [total, setTotal] = useState(0);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");
  const [exitoPago, setExitoPago] = useState(false);

  useEffect(() => {
    if (!idReserva) {
      setError("ID de reserva no válido.");
      setCargando(false);
      return;
    }
    Promise.all([
      bookingService.obtenerPorId(idReserva),
      paymentService.listarPorReserva(idReserva),
      paymentService.totalPorReserva(idReserva),
    ])
      .then(([r, p, t]) => {
        setReserva(r.data);
        setPagos(p.data ?? []);
        setTotal(t.data?.total ?? 0);
      })
      .catch(() => setError("No se pudo cargar la información de la reserva."))
      .finally(() => setCargando(false));
  }, [idReserva]);

  if (cargando)
    return (
      <div className="padding-block-250 container">
        <Spinner />
      </div>
    );
  if (error)
    return (
      <div className="padding-block-250 container">
        <Alert tipo="error">{error}</Alert>
      </div>
    );

  if (exitoPago)
    return (
      <section className="padding-block-250">
        <div className={`container ${styles.exitoWrap}`}>
          <div className={styles.exitoIcon}>✅</div>
          <h1
            className="fs-primary-heading"
            style={{ color: "var(--clr-verde)" }}
          >
            ¡Pago registrado exitosamente!
          </h1>
          <p style={{ color: "var(--clr-gris-600)" }}>
            Tu reserva #{idReserva} ha sido actualizada.
          </p>
          <div className={styles.exitoBtns}>
            <Button onClick={() => navigate("/perfil")}>
              Ver mis reservas
            </Button>
            <Button variante="outline" onClick={() => navigate("/tours")}>
              Explorar más tours
            </Button>
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

        <h1 className={`${styles.titulo} fs-primary-heading`}>
          Registrar pago
        </h1>

        <div className={styles.layout}>
          {/* Formulario de pago */}
          <PaymentForm
            idReserva={idReserva}
            totalPendiente={reserva?.totalReserva - total}
            onExito={() => setExitoPago(true)}
          />

          {/* Panel de info de la reserva */}
          <aside className={styles.infoPanel}>
            <h3 className={styles.panelTitle}>Reserva #{idReserva}</h3>
            {reserva && (
              <div className={styles.reservaInfo}>
                {[
                  ["Tour", `Tour #${reserva.idTour}`],
                  ["Pasajeros", reserva.numeroPasajeros],
                  ["Fecha", reserva.fechaReserva],
                  ["Estado", null],
                ].map(([k, v]) => (
                  <div key={k} className={styles.infoRow}>
                    <span className={styles.infoKey}>{k}</span>
                    {k === "Estado" ? (
                      <EstadoBadge estado={reserva.estado} />
                    ) : (
                      <span>{v}</span>
                    )}
                  </div>
                ))}
              </div>
            )}

            {/* Pagos previos */}
            {pagos.length > 0 && (
              <>
                <h4 className={styles.pagosTitle}>Pagos anteriores</h4>
                {pagos.map((p) => (
                  <div key={p.idPago} className={styles.pagoItem}>
                    <span className={styles.pagoMetodo}>{p.metodoPago}</span>
                    <span className={styles.pagoMonto}>{pesos(p.monto)}</span>
                    <EstadoBadge estado={p.estado} />
                  </div>
                ))}
                <div className={styles.totalRow}>
                  <span>Total pagado</span>
                  <strong style={{ color: "var(--clr-verde)" }}>
                    {pesos(total)}
                  </strong>
                </div>
              </>
            )}
          </aside>
        </div>
      </div>
    </section>
  );
}
