<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Error del servidor | Colombiando</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/reset.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base.css" />
</head>
<body>
  <div style="min-height:100vh;display:flex;align-items:center;
              justify-content:center;text-align:center;padding:var(--size-200)">
    <div style="max-width:520px">
      <div style="font-size:5rem;margin-bottom:var(--size-100)">⚙️</div>
      <h1 class="fs-primary-heading" style="color:var(--clr-rojo);margin-bottom:var(--size-50)">500</h1>
      <h2 class="fs-secondary-heading" style="margin-bottom:var(--size-100);
                                               color:var(--clr-gris-600)">
        Error interno del servidor
      </h2>
      <p style="color:var(--clr-gris-400);margin-bottom:var(--size-100);line-height:1.6">
        Ocurrió un problema al procesar tu solicitud. Por favor intenta de nuevo.
      </p>
      <c:if test="${not empty error}">
        <div class="alert-global alert-error padding-block-100"
             style="text-align:left;margin-bottom:var(--size-150);font-size:var(--fs-100)">
          ${error}
        </div>
      </c:if>
      <div style="display:flex;gap:var(--size-100);justify-content:center;flex-wrap:wrap">
        <a href="javascript:history.back()"
           style="padding:12px var(--size-150);border:1.5px solid var(--clr-negro);
                  border-radius:100px;font-weight:700">
          ← Volver
        </a>
        <a href="${pageContext.request.contextPath}/index"
           style="padding:12px var(--size-150);background:var(--clr-verde);
                  color:var(--clr-blanco);border-radius:100px;font-weight:700">
          Inicio
        </a>
      </div>
    </div>
  </div>
</body>
</html>
