<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="Reserva Confirmada" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['tour-card.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">
    <div style="max-width:600px;margin:auto;text-align:center">

      <%-- Ícono éxito --%>
      <div style="font-size:4rem;margin-bottom:var(--size-100)">🇨🇴</div>
      <h1 class="fs-primary-heading" style="color:var(--clr-verde)">
        ¡Reserva creada exitosamente!
      </h1>
      <p style="margin-top:var(--size-50);color:var(--clr-gris-600)">
        Tu reserva está pendiente de pago. Confírmala registrando el pago.
      </p>

      <%-- Resumen --%>
      <div style="border:1.5px solid var(--clr-negro);border-radius:var(--size-100);
                  padding:var(--size-200);margin-top:var(--size-200);text-align:left">

        <div style="display:flex;justify-content:space-between;
                    border-bottom:1px solid var(--clr-gris-200);padding-bottom:var(--size-100);
                    margin-bottom:var(--size-100)">
          <span class="fs-details">Nº de reserva</span>
          <strong>#${reserva.idReserva}</strong>
        </div>

        <c:if test="${not empty cliente}">
          <div style="display:flex;justify-content:space-between;margin-bottom:var(--size-50)">
            <span class="fs-details">Cliente</span>
            <strong>${cliente.nombre} ${cliente.apellido}</strong>
          </div>
        </c:if>

        <c:if test="${not empty tour}">
          <div style="display:flex;justify-content:space-between;margin-bottom:var(--size-50)">
            <span class="fs-details">Tour</span>
            <strong>${tour.nombre}</strong>
          </div>
          <div style="display:flex;justify-content:space-between;margin-bottom:var(--size-50)">
            <span class="fs-details">Fecha de salida</span>
            <strong>${tour.fechaSalida}</strong>
          </div>
        </c:if>

        <div style="display:flex;justify-content:space-between;margin-bottom:var(--size-50)">
          <span class="fs-details">Pasajeros</span>
          <strong>${reserva.numeroPasajeros}</strong>
        </div>
        <div style="display:flex;justify-content:space-between;margin-bottom:var(--size-50)">
          <span class="fs-details">Fecha del tour</span>
          <strong>${reserva.fechaReserva}</strong>
        </div>

        <c:if test="${not empty tour}">
          <div style="display:flex;justify-content:space-between;
                      border-top:1px solid var(--clr-gris-200);padding-top:var(--size-100);
                      margin-top:var(--size-100)">
            <span>Total estimado</span>
            <strong style="font-size:var(--fs-400);color:var(--clr-verde)">
              <fmt:formatNumber value="${tour.precio * reserva.numeroPasajeros}"
                                type="currency" currencySymbol="$" maxFractionDigits="0"/>
            </strong>
          </div>
        </c:if>

        <div style="margin-top:var(--size-100)">
          <span class="tag bgr-yellow">Estado: ${reserva.estado}</span>
        </div>
      </div>

      <%-- Acciones --%>
      <div style="display:flex;gap:var(--size-100);justify-content:center;
                  flex-wrap:wrap;margin-top:var(--size-200)">
        <a href="${pageContext.request.contextPath}/pagos?accion=nuevo&idReserva=${reserva.idReserva}"
           style="padding:12px var(--size-150);background:var(--clr-verde);
                  color:var(--clr-blanco);border-radius:100px;font-weight:700">
          Registrar pago ahora
        </a>
        <a href="${pageContext.request.contextPath}/reservas"
           style="padding:12px var(--size-150);border:1.5px solid var(--clr-negro);
                  border-radius:100px;font-weight:700">
          Ver todas las reservas
        </a>
      </div>

    </div>
  </div>
</section>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
