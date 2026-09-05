<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="${tour.nombre}" />
<c:set var="currentPage" value="servicios" />
<c:set var="extraCss"    value="${['tour-card.css','profile-card.css','contact-form.css','tour.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<%-- ══ HERO imagen del tour ════════════════════════════════════════════════ --%>
<section class="first-padding-block-250">
  <div class="">
    <div class="secondary-header">
      <div><figure>Imagen del tour ${tour.nombre}</figure></div>
    </div>
  </div>
</section>

<%-- ══ TÍTULO Y BOTÓN RESERVA ════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <div class="tour-title">
      <h1 class="fs-title">${tour.nombre}</h1>
      <div class="tour-features">
        <div class="tour-tags">
          <span class="tag bgr-yellow">${tour.fechaSalida}</span>
          <span class="tag bgr-blue">${tour.duracionDias} días</span>
          <span class="tag bgr-red">${tour.estado}</span>
        </div>
        <c:if test="${tour.estado == 'ACTIVO'}">
          <div class="btn-reserva">
            <a href="${pageContext.request.contextPath}/reservas?accion=nuevo&idTour=${tour.idTour}">
              ¡Reserva Ya!
            </a>
          </div>
        </c:if>
      </div>
    </div>
  </div>
</section>

<%-- ══ DESCRIPCIÓN ════════════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <p class="tour-description">${tour.descripcion}</p>
  </div>
</section>

<%-- ══ PRECIO Y DETALLES ══════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <div class="tour-information">
      <div>
        <h2 class="fs-secondary-heading">Detalles del tour</h2>
        <div class="tour-tags padding-block-100">
          <span class="tag bgr-yellow">
            Salida: ${tour.fechaSalida}
          </span>
          <span class="tag bgr-blue">
            Regreso: ${tour.fechaRegreso}
          </span>
          <span class="tag bgr-green" style="background:var(--clr-verde);color:var(--clr-blanco)">
            Máx. ${tour.capacidadMaxima} personas
          </span>
        </div>
      </div>
      <div class="payment-details" style="flex-direction:column;align-items:flex-end;gap:var(--size-50)">
        <p class="fs-details">Precio por persona</p>
        <p class="fs-primary-heading" style="color:var(--clr-verde)">
          <fmt:formatNumber value="${tour.precio}" type="currency"
                            currencySymbol="$" maxFractionDigits="0"/>
        </p>
        <c:if test="${tour.estado == 'ACTIVO'}">
          <div class="btn-reserva" style="min-width:180px">
            <a href="${pageContext.request.contextPath}/reservas?accion=nuevo&idTour=${tour.idTour}">
              Reservar ahora
            </a>
          </div>
        </c:if>
      </div>
    </div>
  </div>
</section>

<%-- ══ DESTINOS ═══════════════════════════════════════════════════════════ --%>
<c:if test="${not empty tour.destinos}">
  <section class="padding-block-250">
    <div class="container">
      <h2 class="fs-secondary-heading">Destinos incluidos</h2>
      <div class="three-col-grid padding-block-150">
        <c:forEach var="d" items="${tour.destinos}">
          <div class="review-card" style="gap:var(--size-50)">
            <div class="review-header" style="flex-direction:row;align-items:center">
              <div class="avatar" style="background:var(--clr-amarillo);font-size:1.2rem">🗺️</div>
              <p class="user-name">${d.nombre}</p>
            </div>
            <p class="review-body" style="text-align:left">
              ${d.municipio}, ${d.departamento}
            </p>
            <div class="review-footer">
              <span class="tag bgr-yellow" style="padding:.25rem .75rem;border-radius:8px">${d.clima}</span>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </section>
</c:if>

<%-- ══ GUÍAS ════════════════════════════════════════════════════════════════ --%>
<c:if test="${not empty tour.empleados}">
  <section class="padding-block-250">
    <div class="container">
      <h2 class="fs-secondary-heading">Guías</h2>
      <div class="three-col-grid padding-block-150">
        <c:forEach var="e" items="${tour.empleados}">
          <div class="review-card" id="selected-tour-guide">
            <div class="review-header">
              <div class="avatar">
                ${fn:substring(e.nombre,0,1)}${fn:substring(e.apellido,0,1)}
              </div>
              <p class="user-name">${e.nombre} ${e.apellido}</p>
            </div>
            <div class="review-footer">
              <span>${e.cargo}</span>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </section>
</c:if>

<%-- ══ ITINERARIO ═════════════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <h2 class="fs-secondary-heading">Itinerario</h2>
    <div class="padding-block-150">
      <table class="itinerario">
        <tr>
          <td class="it-activity-no">Día 1</td>
          <td class="it-activity">Llegada y bienvenida — orientación del grupo</td>
          <td class="it-time">08:00 AM</td>
        </tr>
        <tr>
          <td class="it-activity-no">Día 1</td>
          <td class="it-activity">Recorrido inicial por el destino principal</td>
          <td class="it-time">10:00 AM</td>
        </tr>
        <tr>
          <td class="it-activity-no">Día 2</td>
          <td class="it-activity">Actividades de aventura y naturaleza</td>
          <td class="it-time">07:00 AM</td>
        </tr>
        <c:if test="${tour.duracionDias > 2}">
          <tr>
            <td class="it-activity-no">Día 3+</td>
            <td class="it-activity">Exploración de destinos secundarios</td>
            <td class="it-time">09:00 AM</td>
          </tr>
        </c:if>
        <tr>
          <td class="it-activity-no">Día ${tour.duracionDias}</td>
          <td class="it-activity">Regreso y despedida</td>
          <td class="it-time">06:00 PM</td>
        </tr>
      </table>
    </div>
  </div>
</section>

<%-- ══ CTA FINAL ══════════════════════════════════════════════════════════ --%>
<c:if test="${tour.estado == 'ACTIVO'}">
  <section class="padding-block-250">
    <div class="container">
      <div class="btn-reserva" style="max-width:300px;margin:auto">
        <a href="${pageContext.request.contextPath}/reservas?accion=nuevo&idTour=${tour.idTour}">
          ¡Reserva Ya!
        </a>
      </div>
    </div>
  </section>
</c:if>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
