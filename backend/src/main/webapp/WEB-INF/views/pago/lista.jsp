<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="Pagos" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['tour.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">

    <div style="display:flex;justify-content:space-between;align-items:center;
                margin-bottom:var(--size-150)">
      <div>
        <h1 class="fs-primary-heading">
          <c:choose>
            <c:when test="${not empty reserva}">Pagos — Reserva #${reserva.idReserva}</c:when>
            <c:otherwise>Todos los pagos</c:otherwise>
          </c:choose>
        </h1>
        <c:if test="${not empty reserva}">
          <p class="fs-details">
            Cliente #${reserva.idCliente} | Tour #${reserva.idTour} |
            <span class="estado-badge
              ${reserva.estado=='PENDIENTE'  ? 'estado-pendiente'  :
                reserva.estado=='CONFIRMADA' ? 'estado-confirmada' :
                reserva.estado=='CANCELADA'  ? 'estado-cancelada'  :
                                               'estado-completada'}">
              ${reserva.estado}
            </span>
          </p>
        </c:if>
      </div>
      <c:if test="${not empty reserva}">
        <a href="${pageContext.request.contextPath}/pagos?accion=nuevo&idReserva=${reserva.idReserva}"
           style="padding:12px var(--size-150);background:var(--clr-verde);
                  color:var(--clr-blanco);border-radius:100px;font-weight:700">
          + Registrar pago
        </a>
      </c:if>
    </div>

    <%-- Tarjeta total pagado --%>
    <c:if test="${not empty totalPagado}">
      <div style="display:inline-flex;align-items:center;gap:var(--size-100);
                  padding:var(--size-100) var(--size-150);
                  background:var(--clr-verde);color:var(--clr-blanco);
                  border-radius:var(--size-100);margin-bottom:var(--size-150)">
        <span style="font-size:1.5rem">💰</span>
        <div>
          <p style="font-size:var(--fs-100);opacity:.8">Total pagado (aprobado)</p>
          <p style="font-size:var(--fs-500);font-weight:700">
            <fmt:formatNumber value="${totalPagado}" type="currency"
                              currencySymbol="$" maxFractionDigits="0"/>
          </p>
        </div>
      </div>
    </c:if>

    <%-- Tabla de pagos --%>
    <div style="overflow-x:auto;border:1.5px solid var(--clr-negro);border-radius:var(--size-100)">
      <table style="width:100%;border-collapse:collapse;font-size:var(--fs-200)">
        <thead>
          <tr style="background:var(--clr-gris-900);color:var(--clr-blanco)">
            <th style="padding:var(--size-100);text-align:left">ID</th>
            <th style="padding:var(--size-100);text-align:left">Reserva</th>
            <th style="padding:var(--size-100);text-align:right">Monto</th>
            <th style="padding:var(--size-100);text-align:left">Fecha</th>
            <th style="padding:var(--size-100);text-align:left">Método</th>
            <th style="padding:var(--size-100);text-align:left">Referencia</th>
            <th style="padding:var(--size-100);text-align:left">Estado</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty pagos}">
              <tr>
                <td colspan="7"
                    style="padding:var(--size-200);text-align:center;color:var(--clr-gris-400)">
                  No hay pagos registrados.
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="p" items="${pagos}" varStatus="st">
                <tr style="border-top:1px solid var(--clr-gris-200);
                           background:${st.index%2==0?'var(--clr-blanco)':'#f9f9f9'}">
                  <td style="padding:var(--size-100)">
                    <span class="tag bgr-blue">${p.idPago}</span>
                  </td>
                  <td style="padding:var(--size-100)">
                    <a href="${pageContext.request.contextPath}/pagos?accion=porReserva&id=${p.idReserva}"
                       style="color:var(--clr-verde);font-weight:700">
                      #${p.idReserva}
                    </a>
                  </td>
                  <td style="padding:var(--size-100);text-align:right;font-weight:700;
                             color:var(--clr-verde)">
                    <fmt:formatNumber value="${p.monto}" type="currency"
                                      currencySymbol="$" maxFractionDigits="0"/>
                  </td>
                  <td style="padding:var(--size-100)">
                    <span class="fs-details">${p.fechaPago}</span>
                  </td>
                  <td style="padding:var(--size-100)">
                    <span class="tag bgr-yellow">
                      <c:choose>
                        <c:when test="${p.metodoPago=='EFECTIVO'}">💵</c:when>
                        <c:when test="${p.metodoPago=='TARJETA'}">💳</c:when>
                        <c:when test="${p.metodoPago=='PSE'}">🌐</c:when>
                        <c:when test="${p.metodoPago=='NEQUI'}">📱</c:when>
                        <c:otherwise>🏦</c:otherwise>
                      </c:choose>
                      ${p.metodoPago}
                    </span>
                  </td>
                  <td style="padding:var(--size-100)">
                    <span class="fs-details">${p.referencia}</span>
                  </td>
                  <td style="padding:var(--size-100)">
                    <span class="estado-badge
                      ${p.estado=='APROBADO'    ? 'estado-confirmada' :
                        p.estado=='PENDIENTE'   ? 'estado-pendiente'  :
                        p.estado=='RECHAZADO'   ? 'estado-cancelada'  :
                                                   'estado-completada'}">
                      ${p.estado}
                    </span>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>

    <c:if test="${not empty reserva}">
      <div style="margin-top:var(--size-150);text-align:right">
        <a href="${pageContext.request.contextPath}/reservas"
           style="color:var(--clr-verde);font-weight:700;font-size:var(--fs-200)">
          ← Volver a reservas
        </a>
      </div>
    </c:if>

  </div>
</section>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
