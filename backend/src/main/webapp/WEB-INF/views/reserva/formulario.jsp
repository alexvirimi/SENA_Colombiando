<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="${titulo}" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['contact-form.css','tour-card.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">

    <div class="padding-block-100">
      <a href="${pageContext.request.contextPath}/tours"
         style="color:var(--clr-verde);font-size:var(--fs-200);font-weight:700">
        ← Volver a tours
      </a>
    </div>

    <div class="contact-form" style="max-width:720px;margin:auto">
      <div class="title-header">
        <h2 class="fs-secondary-heading">${titulo}</h2>
        <p style="font-size:var(--fs-200);color:var(--clr-gris-600)">
          Completa los datos para reservar tu experiencia en Colombia
        </p>
      </div>

      <c:if test="${not empty errores}">
        <div class="alert-global alert-error padding-block-100"
             style="margin-bottom:var(--size-150)">
          <ul style="margin-left:var(--size-100)">
            <c:forEach var="e" items="${errores}"><li>${e}</li></c:forEach>
          </ul>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/reservas" method="post"
            id="formReserva">
        <input type="hidden" name="accion"
               value="${modoEdicion ? 'actualizar' : 'guardar'}">
        <c:if test="${modoEdicion}">
          <input type="hidden" name="idReserva" value="${reserva.idReserva}">
        </c:if>

        <div class="form-two-col">

          <div class="field form-full-col">
            <label for="idCliente">Cliente *</label>
            <select id="idCliente" name="idCliente" required>
              <option value="">Seleccionar cliente…</option>
              <c:forEach var="c" items="${clientes}">
                <option value="${c.idUsuario}"
                        ${reserva.idCliente == c.idUsuario ? 'selected':''}>
                  ${c.nombre} ${c.apellido} — ${c.numeroDocumento}
                </option>
              </c:forEach>
            </select>
          </div>

          <div class="field form-full-col">
            <label for="idTour">Tour *</label>
            <select id="idTour" name="idTour" required
                    onchange="actualizarPrecio(this)">
              <option value="">Seleccionar tour…</option>
              <c:forEach var="t" items="${tours}">
                <option value="${t.idTour}"
                        data-precio="${t.precio}"
                        data-capacidad="${t.capacidadMaxima}"
                        ${reserva.idTour == t.idTour ? 'selected':''}>
                  ${t.nombre} —
                  <fmt:formatNumber value="${t.precio}" type="currency"
                                    currencySymbol="$" maxFractionDigits="0"/>
                  (sal: ${t.fechaSalida})
                </option>
              </c:forEach>
            </select>
          </div>

          <div class="field">
            <label for="numeroPasajeros">Nº de pasajeros *</label>
            <input type="number" id="numeroPasajeros" name="numeroPasajeros"
                   value="${not empty reserva.numeroPasajeros ? reserva.numeroPasajeros : 1}"
                   required min="1" max="50"
                   onchange="calcularTotal()">
            <span class="error">Mínimo 1 pasajero.</span>
          </div>

          <div class="field">
            <label for="fechaReserva">Fecha del tour *</label>
            <input type="date" id="fechaReserva" name="fechaReserva"
                   value="${reserva.fechaReserva}" required>
          </div>

          <c:if test="${modoEdicion}">
            <div class="field">
              <label for="estado">Estado</label>
              <select id="estado" name="estado">
                <option value="PENDIENTE"  ${reserva.estado=='PENDIENTE'  ?'selected':''}>PENDIENTE</option>
                <option value="CONFIRMADA" ${reserva.estado=='CONFIRMADA' ?'selected':''}>CONFIRMADA</option>
                <option value="CANCELADA"  ${reserva.estado=='CANCELADA'  ?'selected':''}>CANCELADA</option>
                <option value="COMPLETADA" ${reserva.estado=='COMPLETADA' ?'selected':''}>COMPLETADA</option>
              </select>
            </div>
          </c:if>

          <div class="field form-full-col">
            <label for="observaciones">Observaciones / Notas especiales</label>
            <textarea id="observaciones" name="observaciones"
                      placeholder="Ej: cliente con movilidad reducida, solicita habitación doble…">${reserva.observaciones}</textarea>
          </div>

        </div>

        <%-- Resumen de costo --%>
        <div id="resumenCosto"
             style="display:none;padding:var(--size-100) var(--size-150);
                    background:var(--clr-azul);border-radius:10px;
                    margin-bottom:var(--size-150);font-size:var(--fs-200)">
          <strong>Costo estimado:</strong>
          <span id="textoCosto">—</span>
        </div>

        <button type="submit" class="btn-submit">
          ${modoEdicion ? 'Guardar cambios' : 'Crear reserva'}
        </button>
      </form>
    </div>
  </div>
</section>

<script>
  const hoy = new Date().toISOString().split('T')[0];
  document.getElementById('fechaReserva').min = hoy;

  function actualizarPrecio(sel) { calcularTotal(); }

  function calcularTotal() {
    const sel   = document.getElementById('idTour');
    const opt   = sel.options[sel.selectedIndex];
    const precio= parseFloat(opt?.dataset?.precio) || 0;
    const pax   = parseInt(document.getElementById('numeroPasajeros').value) || 1;
    if (precio > 0) {
      const total = precio * pax;
      document.getElementById('textoCosto').textContent =
        pax + ' persona(s) × $' + precio.toLocaleString('es-CO')
        + ' = $' + total.toLocaleString('es-CO');
      document.getElementById('resumenCosto').style.display = 'block';
    }
  }
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
