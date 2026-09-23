import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';
import Layout from './components/layout/Layout';
import PrivateRoute from './components/auth/PrivateRoute';

/* Páginas */
import HomePage        from './pages/HomePage';
import ToursPage       from './pages/ToursPage';
import TourDetailPage  from './pages/TourDetailPage';
import AboutPage       from './pages/AboutPage';
import { LoginPage, RegisterPage, RecuperarPage } from './pages/AuthPages';
import BookingPage     from './pages/BookingPage';
import PaymentPage     from './pages/PaymentPage';
import ProfilePage     from './pages/ProfilePage';
import ReviewPage      from './pages/ReviewPage';
import NotFoundPage    from './pages/NotFoundPage';

/**
 * App — componente raíz.
 *
 * Árbol de providers:
 *   BrowserRouter → AuthProvider → CartProvider → Routes
 *
 * Rutas públicas:  / | /tours | /tours/:idTour | /acerca
 *                  /login | /registro | /recuperar
 * Rutas privadas:  /reservas/nueva | /pagos/nueva
 *                  /perfil | /calificar
 */
export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CartProvider>
          <Routes>

            {/* ── Layout compartido (Navbar + Footer) ── */}
            <Route element={<Layout />}>

              {/* Públicas */}
              <Route index          element={<HomePage />} />
              <Route path="tours"   element={<ToursPage />} />
              <Route path="tours/:idTour" element={<TourDetailPage />} />
              <Route path="acerca"  element={<AboutPage />} />

              {/* Autenticación */}
              <Route path="login"    element={<LoginPage />} />
              <Route path="registro" element={<RegisterPage />} />
              <Route path="recuperar" element={<RecuperarPage />} />

              {/* Privadas */}
              <Route
                path="reservas/nueva"
                element={
                  <PrivateRoute>
                    <BookingPage />
                  </PrivateRoute>
                }
              />
              <Route
                path="pagos/nueva"
                element={
                  <PrivateRoute>
                    <PaymentPage />
                  </PrivateRoute>
                }
              />
              <Route
                path="perfil"
                element={
                  <PrivateRoute>
                    <ProfilePage />
                  </PrivateRoute>
                }
              />
              <Route
                path="calificar"
                element={
                  <PrivateRoute>
                    <ReviewPage />
                  </PrivateRoute>
                }
              />

              {/* 404 */}
              <Route path="*" element={<NotFoundPage />} />
            </Route>

          </Routes>
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
