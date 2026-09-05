<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="Reservas" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['contact-form.css','tour.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">

    <div style="display:flex;justify-content:space-between;align-items:center;
                margin-bottom:var(--size-150)">
      <div>
        <h1 class="fs-primary-heading">Reservas</h1>
        <p class="fs-details">Gestión de reservas turísticas</p>
      </div>
      <a href="${pageContext.request.contextPath}/reservas?accion=nuevo"
         style="padding:12px var(--size-150);background:var(--clr-verde);
                color:var(--clr-blanco);border-radius:100px;font-weight:700">
        + Nueva reserva
      </a>
    </div>

    <%-- Filtros de estado --%>
    <div class="nav-wrapper" style="display:inline-flex;margin-bottom:var(--size-150)">
      <ul role="list" class="nav-list">
        <li><a href="${pageContext.request.contextPath}/reservas"
               class="nav-list-item ${empty filtroEstado ? 'active' : ''}">Todas</a></li>
        <li><a href="${pageContext.request.contextPath}/reservas?estado=PENDIENTE"
               class="nav-list-item ${filtroEstado=='PENDIENTE'  ? 'active' : ''}">Pendiente</a></li>
        <li><a href="${pageContext.request.contextPath}/reservas?estado=CONFIRMADA"
               class="nav-list-item ${filtroEstado=='CONFIRMADA' ? 'active' : ''}">Confirmada</a></li>
        <li><a href="${pageContext.request.contextPath}/reservas?estado=CANCELADA"
               class="nav-list-item ${filtroEstado=='CANCELADA'  ? 'active' : ''}">Cancelada</a></li>
        <li><a href="${pageContext.request.contextPath}/reservas?estado=COMPLETADA"
               class="nav-list-item ${filtroEstado=='COMPLETADA' ? 'active' : ''}">Completada</a></li>
      </ul>
    </div>

    <%-- Tabla --%>
    <div style="overflow-x:auto;border:1.5px solid var(--clr-negro);border-radius:var(--size-100)">
      <table style="width:100%;border-collapse:collapse;font-size:var(--fs-200)">
        <thead>
          <tr style="background:var(--clr-gris-900);color:var(--clr-blanco)">
            <th style="padding:var(--size-100);text-align:left">#</th>
            <th style="padding:var(--size-100);text-align:left">Cliente</th>
            <th style="padding:var(--size-100);text-align:left">Tour</th>
            <th style="padding:var(--size-100);text-align:center">Pax</th>
            <th style="padding:var(--size-100);text-align:left">Fecha</th>
            <th style="padding:var(--size-100);text-align:left">Estado</th>
            <th style="padding:var(--size-100);text-align:center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty reservas}">
              <tr>
                <td colspan="7" style="padding:var(--size-200);text-align:center;
                                       color:var(--clr-gris-400)">
                  No hay reservas
                  <c:if test="${not empty filtroEstado}"> con estado ${filtroEstado}</c:if>.
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="r" items="${reservas}" varStatus="st">
                <tr style="border-top:1px solid var(--clr-gris-200);
                           background:${st.index%2==0?'var(--clr-blanco)':'#f9f9f9'}">
                  <td style="padding:var(--size-100)">
                    <span class="tag bgr-blue">${r.idReserva}</span>
                  </td>
                  <td style="padding:var(--size-100)">
                    Cliente #${r.idCliente}
                  </td>
                  <td style="padding:var(--size-100)">
                    <a href="${pageContext.request.contextPath}/tours?accion=ver&id=${r.idTour}"
                       style="color:var(--clr-verde);font-weight:700">
                      Tour #${r.idTour}
                    </a>
                  </td>
                  <td style="padding:var(--size-100);text-align:center">
                    <span class="tag bgr-azul"
                          style="background:var(--clr-azul)">${r.numeroPasajeros}</span>
                  </td>
                  <td style="padding:var(--size-100)">${r.fechaReserva}</td>
                  <td style="padding:var(--size-100)">
                    <span class="estado-badge
                      ${r.estado=='PENDIENTE'  ? 'estado-pendiente'  :
                        r.estado=='CONFIRMADA' ? 'estado-confirmada' :
                        r.estado=='CANCELADA'  ? 'estado-cancelada'  :
                                                  'estado-completada'}">
                      ${r.estado}
                    </span>
                  </td>
                  <td style="padding:var(--size-100);text-align:center">
                    <div style="display:flex;gap:4px;justify-content:center;flex-wrap:wrap">
                      <a href="${pageContext.request.contextPath}/reservas?accion=editar&id=${r.idReserva}"
                         style="padding:4px 10px;border:1.5px solid var(--clr-negro);
                                border-radius:100px;font-size:var(--fs-100);font-weight:700">
                        Editar
                      </a>
                      <a href="${pageContext.request.contextPath}/pagos?accion=nuevo&idReserva=${r.idReserva}"
                         style="padding:4px 10px;background:var(--clr-verde);
                                color:var(--clr-blanco);border-radius:100px;
                                font-size:var(--fs-100);font-weight:700">
                        Pagar
                      </a>
                      <c:if test="${r.estado=='PENDIENTE'||r.estado=='CONFIRMADA'}">
                        <a href="${pageContext.request.contextPath}/reservas?accion=cancelar&id=${r.idReserva}"
                           style="padding:4px 10px;background:var(--clr-rojo);
                                  color:var(--clr-negro);border-radius:100px;
                                  font-size:var(--fs-100);font-weight:700"
                           onclick="return confirm('¿Cancelar reserva #${r.idReserva}?')">
                          Cancelar
                        </a>
                      </c:if>
                    </div>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>

  </div>
</section>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
