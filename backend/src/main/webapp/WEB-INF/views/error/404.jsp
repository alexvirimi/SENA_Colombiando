<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>404 — Destino no encontrado | Colombiando</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/reset.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base.css" />
</head>
<body>
  <div style="min-height:100vh;display:flex;align-items:center;
              justify-content:center;text-align:center;padding:var(--size-200)">
    <div style="max-width:480px">
      <div style="font-size:5rem;margin-bottom:var(--size-100)">🗺️</div>
      <h1 class="fs-primary-heading" style="margin-bottom:var(--size-50)">404</h1>
      <h2 class="fs-secondary-heading" style="margin-bottom:var(--size-100);
                                               color:var(--clr-gris-600)">
        Destino no encontrado
      </h2>
      <p style="color:var(--clr-gris-400);margin-bottom:var(--size-200);line-height:1.6">
        La página que buscas no existe o fue movida a otro destino turístico.
      </p>
      <a href="${pageContext.request.contextPath}/index"
         style="display:inline-block;padding:12px var(--size-200);
                background:var(--clr-verde);color:var(--clr-blanco);
                border-radius:100px;font-weight:700">
        ← Volver al inicio
      </a>
    </div>
  </div>
</body>
</html>
