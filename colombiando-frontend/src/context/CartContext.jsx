import { createContext, useContext, useState } from 'react';

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const [tourSeleccionado, setTourSeleccionado] = useState(null);
  const [datosReserva,     setDatosReserva]     = useState(null);

  return (
    <CartContext.Provider value={{
      tourSeleccionado,
      datosReserva,
      seleccionarTour:     setTourSeleccionado,
      guardarDatosReserva: setDatosReserva,
      limpiarCarrito:      () => { setTourSeleccionado(null); setDatosReserva(null); },
    }}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart debe usarse dentro de <CartProvider>');
  return ctx;
}
