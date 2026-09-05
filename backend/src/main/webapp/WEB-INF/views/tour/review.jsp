<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle"   value="Calificar Tour" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['tour-card.css','profile-card.css','contact-form.css','tour.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">
    <div class="tour-information">

      <%-- Título y tags del tour --%>
      <div class="tour-title">
        <h1 class="fs-primary-heading">
          ${not empty tour ? tour.nombre : 'Tour #'.concat(reserva.idTour)}
        </h1>
        <div class="tour-tags">
          <span class="tag bgr-yellow">${reserva.fechaReserva}</span>
          <c:if test="${not empty tour}">
            <span class="tag bgr-blue">${tour.duracionDias} días</span>
          </c:if>
          <span class="tag bgr-red estado-completada">Completado</span>
        </div>
      </div>

      <%-- Guía asignado --%>
      <c:if test="${not empty guia}">
        <div class="review-card" id="selected-tour-guide">
          <div class="review-header">
            <div class="avatar">
              ${fn:substring(guia.nombre,0,1)}${fn:substring(guia.apellido,0,1)}
            </div>
            <p class="user-name">${guia.nombre} ${guia.apellido}</p>
          </div>
          <div class="review-footer">
            <span>${guia.cargo}</span>
          </div>
        </div>
      </c:if>

    </div>
  </div>
</section>

<section class="padding-block-250">
  <div class="container">
    <div class="contact-form" style="max-width:640px;margin:auto">

      <div class="title-header">
        <h2 class="fs-secondary-heading">Califica tu experiencia</h2>
        <p style="font-size:var(--fs-200);color:var(--clr-gris-600)">
          Tu opinión nos ayuda a mejorar
        </p>
      </div>

      <c:if test="${not empty param.exito}">
        <div class="alert-global alert-success padding-block-100"
             style="margin-bottom:var(--size-150)">
          ✓ ¡Gracias por tu calificación!
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/reservas/calificar"
            method="post" id="formCalificacion">
        <input type="hidden" name="idReserva" value="${reserva.idReserva}">

        <%-- Calificación del tour --%>
        <div style="border-bottom:1px solid var(--clr-gris-200);
                    padding-bottom:var(--size-150);margin-bottom:var(--size-150)">
          <h3 class="fs-body-title" style="margin-bottom:var(--size-100)">
            Calificación del tour
          </h3>

          <div class="field">
            <label for="calificacionTour">Puntuación (1 – 10)</label>
            <input type="number" id="calificacionTour" name="calificacionTour"
                   placeholder="8" min="1" max="10" required>
            <span class="error">Ingresa un valor entre 1 y 10.</span>
          </div>

          <div class="field">
            <label for="comentarioTour">Describe tu experiencia</label>
            <textarea id="comentarioTour" name="comentarioTour"
                      placeholder="¿Qué fue lo que más te gustó del tour?"
                      rows="4"></textarea>
          </div>
        </div>

        <%-- Calificación del guía --%>
        <div style="margin-bottom:var(--size-150)">
          <h3 class="fs-body-title" style="margin-bottom:var(--size-100)">
            Calificación del guía
          </h3>

          <div class="field">
            <label for="calificacionGuia">Puntuación (1 – 10)</label>
            <input type="number" id="calificacionGuia" name="calificacionGuia"
                   placeholder="9" min="1" max="10" required>
            <span class="error">Ingresa un valor entre 1 y 10.</span>
          </div>

          <div class="field">
            <label for="comentarioGuia">Describe tu experiencia con el guía</label>
            <textarea id="comentarioGuia" name="comentarioGuia"
                      placeholder="¿El guía fue claro, puntual y profesional?"
                      rows="4"></textarea>
          </div>
        </div>

        <button type="submit" class="btn-submit">Enviar Reseña</button>
      </form>
    </div>
  </div>
</section>

<script>
  // Validación básica: valores entre 1 y 10
  document.getElementById('formCalificacion').addEventListener('submit', function(e) {
    const t = parseInt(document.getElementById('calificacionTour').value);
    const g = parseInt(document.getElementById('calificacionGuia').value);
    if (isNaN(t) || t < 1 || t > 10 || isNaN(g) || g < 1 || g > 10) {
      e.preventDefault();
      document.querySelectorAll('.error').forEach(el => el.style.display = 'block');
    }
  });
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
