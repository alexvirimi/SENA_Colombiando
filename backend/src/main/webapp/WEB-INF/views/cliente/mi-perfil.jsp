<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="Mi Perfil" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['tour-card.css','profile-card.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<%-- ══ SALUDO ═══════════════════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <h1 class="fs-title">Hola, ${cliente.nombre}</h1>
    <p class="fs-details" style="margin-top:var(--size-50)">
      ${cliente.correo} &bull; ${cliente.telefono}
    </p>
  </div>
</section>

<%-- ══ TOURS ACTIVOS ════════════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <h2 class="fs-secondary-heading">Tours Activos</h2>
    <div class="three-col-grid padding-block-150">
      <c:choose>
        <c:when test="${empty reservasActivas}">
          <p class="fs-details" style="grid-column:1/-1">
            No tienes tours activos en este momento.
            <a href="${pageContext.request.contextPath}/tours"
               style="color:var(--clr-verde);font-weight:700"> ¡Explora nuestros tours!</a>
          </p>
        </c:when>
        <c:otherwise>
          <c:forEach var="r" items="${reservasActivas}">
            <div class="tour-card">
              <div class="tour-image"><figure>Tour image</figure></div>
              <div class="tour-tags">
                <span class="tag bgr-yellow">${r.fechaReserva}</span>
                <span class="tag bgr-blue">${r.numeroPasajeros} pax</span>
                <span class="tag bgr-green"
                      style="background:var(--clr-verde);color:var(--clr-blanco)">
                  ${r.estado}
                </span>
              </div>
              <div class="tour-info">
                <p class="fs-body-title">Tour #${r.idTour}</p>
                <p class="fs-details">Reserva #${r.idReserva}</p>
                <p class="fs-details">${r.observaciones}</p>
              </div>
              <div style="display:flex;gap:var(--size-50);margin-top:auto">
                <a href="${pageContext.request.contextPath}/pagos?accion=porReserva&id=${r.idReserva}"
                   style="flex:1;padding:8px var(--size-100);background:var(--clr-verde);
                          color:var(--clr-blanco);border-radius:100px;font-weight:700;
                          font-size:var(--fs-200);text-align:center">
                  Ver pagos
                </a>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</section>

<%-- ══ TOURS PASADOS ════════════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <h2 class="fs-secondary-heading">Tours Pasados</h2>
    <div class="three-col-grid padding-block-150">
      <c:choose>
        <c:when test="${empty reservasPasadas}">
          <p class="fs-details" style="grid-column:1/-1">Sin historial de tours pasados.</p>
        </c:when>
        <c:otherwise>
          <c:forEach var="r" items="${reservasPasadas}">
            <div class="tour-card">
              <div class="tour-image"><figure>Tour image</figure></div>
              <div class="tour-tags">
                <span class="tag bgr-red">
                  <c:choose>
                    <c:when test="${r.estado == 'COMPLETADA'}">Esperando Calificación</c:when>
                    <c:otherwise>${r.estado}</c:otherwise>
                  </c:choose>
                </span>
              </div>
              <div class="tour-info">
                <p class="fs-body-title">Tour #${r.idTour}</p>
                <p class="fs-details">${r.fechaReserva}</p>
              </div>
              <c:if test="${r.estado == 'COMPLETADA'}">
                <a href="${pageContext.request.contextPath}/reservas/calificar?id=${r.idReserva}"
                   style="padding:8px;background:var(--clr-amarillo);color:var(--clr-negro);
                          border-radius:100px;font-weight:700;font-size:var(--fs-200);
                          text-align:center;margin-top:auto">
                  ✦ Calificar tour
                </a>
              </c:if>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
