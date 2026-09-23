import { NavLink } from 'react-router-dom';
import styles from './Footer.module.css';

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <nav>
        <div className={styles.logoDiv}>
          <NavLink to="/" className={styles.logoFull}>Colombiando</NavLink>
        </div>
        <ul role="list" className={styles.navList}>
          {[['/', 'Inicio'], ['/tours', 'Servicios'], ['/acerca', 'Acerca de']].map(([path, label]) => (
            <li key={path} className={styles.item}>
              <NavLink to={path}>{label}</NavLink>
            </li>
          ))}
        </ul>
        <p className={styles.copy}>Colombiando © 2026</p>
      </nav>
    </footer>
  );
}
