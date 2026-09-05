<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle"   value="${titulo}" />
<c:set var="currentPage" value="servicios" />
<c:set var="extraCss"    value="${['contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">

    <div class="padding-block-100">
      <a href="${pageContext.request.contextPath}/tours"
         style="color:var(--clr-verde);font-size:var(--fs-200);font-weight:700">
        ← Volver a tours
      </a>
    </div>

    <div class="contact-form" style="max-width:860px;margin:auto">
      <div class="title-header">
        <h2 class="fs-secondary-heading">${titulo}</h2>
      </div>

      <c:if test="${not empty errores}">
        <div class="alert-global alert-error padding-block-100" style="margin-bottom:var(--size-150)">
          <ul style="margin-left:var(--size-100)">
            <c:forEach var="e" items="${errores}"><li>${e}</li></c:forEach>
          </ul>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/tours" method="post">
        <input type="hidden" name="accion" value="${modoEdicion ? 'actualizar' : 'guardar'}">
        <c:if test="${modoEdicion}">
          <input type="hidden" name="idTour" value="${tour.idTour}">
        </c:if>

        <div class="form-two-col">

          <div class="field form-full-col">
            <label for="nombre">Nombre del tour *</label>
            <input type="text" id="nombre" name="nombre"
                   value="${tour.nombre}" required maxlength="120"
                   placeholder="Ej: Cartagena Mágica — 4 días">
            <span class="error">El nombre es obligatorio.</span>
          </div>

          <div class="field form-full-col">
            <label for="descripcion">Descripción</label>
            <textarea id="descripcion" name="descripcion"
                      placeholder="Describe el recorrido, actividades incluidas…">${tour.descripcion}</textarea>
          </div>

          <div class="field">
            <label for="precio">Precio por persona (COP) *</label>
            <input type="number" id="precio" name="precio"
                   value="${tour.precio}" required min="1" step="1000"
                   placeholder="1800000">
          </div>

          <div class="field">
            <label for="duracionDias">Duración (días) *</label>
            <input type="number" id="duracionDias" name="duracionDias"
                   value="${tour.duracionDias}" required min="1" max="365"
                   placeholder="4">
          </div>

          <div class="field">
            <label for="capacidadMaxima">Capacidad máxima *</label>
            <input type="number" id="capacidadMaxima" name="capacidadMaxima"
                   value="${tour.capacidadMaxima}" required min="1" max="500"
                   placeholder="20">
          </div>

          <div class="field">
            <label for="estado">Estado</label>
            <select id="estado" name="estado">
              <option value="ACTIVO"    ${tour.estado == 'ACTIVO'    || empty tour.estado ? 'selected':''}> ACTIVO</option>
              <option value="CANCELADO" ${tour.estado == 'CANCELADO' ? 'selected':''}> CANCELADO</option>
              <option value="COMPLETO"  ${tour.estado == 'COMPLETO'  ? 'selected':''}> COMPLETO</option>
            </select>
          </div>

          <div class="field">
            <label for="fechaSalida">Fecha de salida *</label>
            <input type="date" id="fechaSalida" name="fechaSalida"
                   value="${tour.fechaSalida}" required>
          </div>

          <div class="field">
            <label for="fechaRegreso">Fecha de regreso *</label>
            <input type="date" id="fechaRegreso" name="fechaRegreso"
                   value="${tour.fechaRegreso}" required>
          </div>

        </div><!-- /form-two-col -->

        <button type="submit" class="btn-submit">
          ${modoEdicion ? 'Guardar cambios' : 'Crear tour'}
        </button>
      </form>
    </div>
  </div>
</section>

<script>
  // Fecha de regreso >= fecha de salida
  const salida  = document.getElementById('fechaSalida');
  const regreso = document.getElementById('fechaRegreso');
  if (salida && regreso) {
    const hoy = new Date().toISOString().split('T')[0];
    salida.min = hoy;
    salida.addEventListener('change', () => {
      regreso.min = salida.value;
      if (regreso.value && regreso.value < salida.value) regreso.value = salida.value;
    });
  }
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
