import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
  timeout: 15000,
});

// Inyectar token JWT en cada request
api.interceptors.request.use(config => {
  const token = sessionStorage.getItem('col_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Redirigir al login si el backend devuelve 401
api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      sessionStorage.removeItem('col_user');
      sessionStorage.removeItem('col_token');
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

export default api;
