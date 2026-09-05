<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="Registrar Pago" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">

    <div class="padding-block-100">
      <a href="${pageContext.request.contextPath}/reservas"
         style="color:var(--clr-verde);font-size:var(--fs-200);font-weight:700">
        ← Volver a reservas
      </a>
    </div>

    <%-- Resumen de la reserva --%>
    <c:if test="${not empty reserva}">
      <div style="max-width:600px;margin:0 auto var(--size-150);
                  padding:var(--size-100) var(--size-150);
                  border-left:4px solid var(--clr-verde);
                  background:var(--clr-azul);border-radius:0 10px 10px 0">
        <div style="display:flex;justify-content:space-between;align-items:center">
          <div>
            <p style="font-size:var(--fs-100);color:var(--clr-gris-600)">Reserva</p>
            <strong>#${reserva.idReserva}</strong> —
            Cliente #${reserva.idCliente} |
            Tour #${reserva.idTour} |
            ${reserva.numeroPasajeros} pax
          </div>
          <span class="estado-badge
            ${reserva.estado=='PENDIENTE'  ? 'estado-pendiente'  :
              reserva.estado=='CONFIRMADA' ? 'estado-confirmada' :
              reserva.estado=='CANCELADA'  ? 'estado-cancelada'  :
                                             'estado-completada'}">
            ${reserva.estado}
          </span>
        </div>
        <c:if test="${not empty totalPagado}">
          <p style="margin-top:var(--size-50);font-size:var(--fs-100);color:var(--clr-gris-600)">
            Ya pagado:
            <strong style="color:var(--clr-verde)">
              <fmt:formatNumber value="${totalPagado}" type="currency"
                                currencySymbol="$" maxFractionDigits="0"/>
            </strong>
          </p>
        </c:if>
      </div>
    </c:if>

    <div class="contact-form" style="max-width:600px;margin:auto">
      <div class="title-header">
        <h2 class="fs-secondary-heading">Registrar pago</h2>
      </div>

      <c:if test="${not empty errores}">
        <div class="alert-global alert-error padding-block-100"
             style="margin-bottom:var(--size-150)">
          <ul style="margin-left:var(--size-100)">
            <c:forEach var="e" items="${errores}"><li>${e}</li></c:forEach>
          </ul>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/pagos" method="post">
        <input type="hidden" name="accion"    value="guardar">
        <input type="hidden" name="idReserva"
               value="${not empty reserva ? reserva.idReserva : param.idReserva}">

        <div class="field">
          <label for="monto">Monto (COP) *</label>
          <input type="number" id="monto" name="monto"
                 required min="1" step="1000"
                 placeholder="1800000"
                 style="font-size:var(--fs-400);font-weight:700;padding:var(--size-100)">
          <span class="error">El monto debe ser mayor a cero.</span>
        </div>

        <div class="field">
          <label for="metodoPago">Método de pago *</label>
          <select id="metodoPago" name="metodoPago" required>
            <option value="">Seleccionar…</option>
            <option value="EFECTIVO">💵  Efectivo</option>
            <option value="TARJETA">💳  Tarjeta débito / crédito</option>
            <option value="TRANSFERENCIA">🏦  Transferencia bancaria</option>
            <option value="PSE">🌐  PSE</option>
            <option value="NEQUI">📱  Nequi / Daviplata</option>
          </select>
          <span class="error">Selecciona un método de pago.</span>
        </div>

        <div class="field">
          <label for="referencia">Referencia / Nº de transacción</label>
          <input type="text" id="referencia" name="referencia"
                 maxlength="100"
                 placeholder="TXN-20250615-001">
        </div>

        <div class="field">
          <label for="observaciones">Observaciones</label>
          <textarea id="observaciones" name="observaciones"
                    placeholder="Notas adicionales del pago…"></textarea>
        </div>

        <button type="submit" class="btn-submit">Confirmar pago</button>
      </form>
    </div>

  </div>
</section>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
