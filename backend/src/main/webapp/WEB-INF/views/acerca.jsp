<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle"   value="Acerca de" />
<c:set var="currentPage" value="acerca" />
<c:set var="extraCss"    value="${['contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">
    <h1 class="fs-title">Colombiando</h1>
    <p style="max-width:720px;margin-top:var(--size-100);line-height:1.8;
              color:var(--clr-gris-600)">
      Colombiando es una plataforma de reservas turísticas dedicada a mostrar la
      riqueza cultural y natural de Colombia. Conectamos viajeros con experiencias
      auténticas guiadas por expertos locales, desde las playas del Caribe hasta
      la selva amazónica, pasando por el Eje Cafetero y los Llanos Orientales.
    </p>
  </div>
</section>

<section class="padding-block-250">
  <div class="container">
    <h2 class="fs-secondary-heading">FAQ</h2>
    <div class="padding-block-150"
         style="display:flex;flex-direction:column;gap:var(--size-100);max-width:720px">

      <details style="border:1.5px solid var(--clr-negro);border-radius:10px;padding:var(--size-100)">
        <summary style="font-weight:700;cursor:pointer">¿Cómo reservo un tour?</summary>
        <p style="margin-top:var(--size-50);color:var(--clr-gris-600)">
          Navega a la sección Servicios, selecciona el tour que te interesa y
          haz clic en "Reserva Ya!". Completa el formulario y confirma con el pago.
        </p>
      </details>

      <details style="border:1.5px solid var(--clr-negro);border-radius:10px;padding:var(--size-100)">
        <summary style="font-weight:700;cursor:pointer">¿Cuáles son los métodos de pago?</summary>
        <p style="margin-top:var(--size-50);color:var(--clr-gris-600)">
          Aceptamos efectivo, tarjeta débito/crédito, transferencia bancaria,
          PSE y Nequi/Daviplata.
        </p>
      </details>

      <details style="border:1.5px solid var(--clr-negro);border-radius:10px;padding:var(--size-100)">
        <summary style="font-weight:700;cursor:pointer">¿Puedo cancelar mi reserva?</summary>
        <p style="margin-top:var(--size-50);color:var(--clr-gris-600)">
          Sí, puedes cancelar reservas PENDIENTES o CONFIRMADAS desde tu perfil.
          Consulta nuestra política de cancelación para conocer los reembolsos aplicables.
        </p>
      </details>

      <details style="border:1.5px solid var(--clr-negro);border-radius:10px;padding:var(--size-100)">
        <summary style="font-weight:700;cursor:pointer">¿Los tours incluyen transporte?</summary>
        <p style="margin-top:var(--size-50);color:var(--clr-gris-600)">
          Cada tour detalla qué está incluido. En general, los paquetes incluyen
          transporte local, guía certificado y actividades descritas en el itinerario.
        </p>
      </details>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/views/layout/contact-section.jsp" %>
<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
