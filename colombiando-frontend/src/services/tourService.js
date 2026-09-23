import api from './api';

const tourService = {
  listarActivos:   ()    => api.get('/tours'),
  listarTodos:     ()    => api.get('/tours?accion=todos'),
  obtenerPorId:    (id)  => api.get(`/tours?accion=ver&id=${id}`),
  crear:           (d)   => api.post('/tours?accion=guardar', d),
  actualizar:      (d)   => api.put('/tours?accion=actualizar', d),
  eliminar:        (id)  => api.delete(`/tours?accion=eliminar&id=${id}`),
  asignarDestino:  (it, id) => api.post('/tours?accion=asignarDestino',  { idTour: it, idDestino: id }),
  asignarEmpleado: (it, ie) => api.post('/tours?accion=asignarEmpleado', { idTour: it, idEmpleado: ie }),
};

export default tourService;
