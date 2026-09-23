import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import styles from "./Navbar.module.css";

export default function Navbar() {
  const { estaAutenticado, usuario, logout } = useAuth();
  const navigate = useNavigate();

  const iniciales = usuario
    ? `${usuario.nombre?.[0] ?? ""}${usuario.apellido?.[0] ?? ""}`
    : "";

  function manejarLogout() {
    logout();
    navigate("/");
  }

  return (
    <header className={styles.header}>
      <div className="container">
        <nav className={styles.nav} aria-label="Navegación principal">
          <NavLink to="/" className={styles.logo}>
            Colombiando
          </NavLink>

          <div className={styles.navWrapper}>
            <ul role="list" className={styles.navList}>
              {[
                ["/", "Inicio", true],
                ["/tours", "Servicios", false],
                ["/acerca", "Acerca de", false],
              ].map(([path, label, end]) => (
                <li key={path}>
                  <NavLink
                    to={path}
                    end={end}
                    className={({ isActive }) =>
                      `${styles.navItem} ${isActive ? styles.active : ""}`
                    }
                  >
                    {label}
                  </NavLink>
                </li>
              ))}
            </ul>
          </div>

          <div className={styles.profileArea}>
            {estaAutenticado ? (
              <>
                <NavLink
                  to="/perfil"
                  className={styles.avatar}
                  title="Mi perfil"
                >
                  {iniciales}
                </NavLink>
                <button className={styles.logoutBtn} onClick={manejarLogout}>
                  Salir
                </button>
              </>
            ) : (
              <NavLink
                to="/login"
                className={styles.avatar}
                title="Ingresar"
              ></NavLink>
            )}
          </div>
        </nav>
      </div>
    </header>
  );
}
