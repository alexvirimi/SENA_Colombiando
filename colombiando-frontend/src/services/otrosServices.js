import api from './api';

export const authService = {
  login:             (correo, contrasena) => api.post('/auth/login', { correo, contrasena }),
  registrar:         (datos)              => api.post('/clientes?accion=guardar', datos),
  recuperarPassword: (correo)             => api.post('/auth/recuperar', { correo }),
};

export const bookingService = {
  crear:            (datos) => api.post('/reservas?accion=guardar', datos),
  listarPorCliente: (id)    => api.get(`/reservas?idCliente=${id}`),
  obtenerPorId:     (id)    => api.get(`/reservas?accion=ver&id=${id}`),
  actualizar:       (datos) => api.put('/reservas?accion=actualizar', datos),
  cancelar:         (id)    => api.get(`/reservas?accion=cancelar&id=${id}`),
};

export const paymentService = {
  registrar:        (datos) => api.post('/pagos?accion=guardar', datos),
  listarPorReserva: (id)    => api.get(`/pagos?accion=porReserva&id=${id}`),
  totalPorReserva:  (id)    => api.get(`/pagos?accion=total&id=${id}`),
};

export const destinoService = {
  listarTodos:  ()     => api.get('/destinos'),
  obtenerPorId: (id)   => api.get(`/destinos?accion=ver&id=${id}`),
  crear:        (d)    => api.post('/destinos', d),
  actualizar:   (d)    => api.put('/destinos', d),
  eliminar:     (id)   => api.delete(`/destinos?accion=eliminar&id=${id}`),
};
