<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="Inicio" />
<c:set var="currentPage" value="inicio" />
<c:set var="extraCss"    value="${['tour-card.css','carrousel.css','profile-card.css','contact-form.css','index.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<%-- ══ HERO ════════════════════════════════════════════════════════════════ --%>
<section class="first-padding-block-250">
  <div class="container">
    <div class="secondary-header">
      <div>
        <h1>
          <span class="fs-primary-heading">No hay mejor forma de viajar que</span>
          <span class="fs-title">Colombiando</span>
        </h1>
        <a href="${pageContext.request.contextPath}/tours">
          Conoce nuestros planes
        </a>
      </div>
    </div>
  </div>
</section>

<%-- ══ CARRUSEL tours destacados ══════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <div class="carrousel" id="carrusel">
      <div class="tour-exhibited" id="carruselContenido">
        <c:choose>
          <c:when test="${not empty ultimosTours}">
            <c:forEach var="t" items="${ultimosTours}" varStatus="st">
              <div class="tour-info-card" style="${st.index > 0 ? 'display:none' : ''}"
                   data-index="${st.index}">
                <h2 class="fs-body-title">${t.nombre}</h2>
                <span class="description fs-details">
                  Colombia &bull; ${t.fechaSalida}
                </span>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <div class="tour-info-card">
              <h2 class="fs-body-title">Próximamente</h2>
              <span class="description fs-details">Nuevos tours disponibles</span>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
      <button class="btn-before" id="btnAnterior" aria-label="Anterior">
        <img src="${pageContext.request.contextPath}/static/img/before.svg" alt="anterior" />
      </button>
      <button class="btn-next" id="btnSiguiente" aria-label="Siguiente">
        <img src="${pageContext.request.contextPath}/static/img/after.svg" alt="siguiente" />
      </button>
    </div>
  </div>
</section>

<%-- ══ PRÓXIMOS TOURS ══════════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <h2 class="fs-secondary-heading">Próximos Tours</h2>
    <div class="three-col-grid padding-block-150">
      <c:choose>
        <c:when test="${empty ultimosTours}">
          <p class="fs-details" style="grid-column:1/-1">No hay tours disponibles por el momento.</p>
        </c:when>
        <c:otherwise>
          <c:forEach var="t" items="${ultimosTours}">
            <a href="${pageContext.request.contextPath}/tours?accion=ver&id=${t.idTour}"
               class="tour-card-link">
              <div class="tour-card">
                <div class="tour-image">
                  <figure>Imagen del tour</figure>
                </div>
                <div class="tour-tags">
                  <span class="tag bgr-yellow">${t.fechaSalida}</span>
                  <span class="tag bgr-blue">${t.duracionDias} días</span>
                  <span class="tag bgr-red
                    ${t.estado == 'ACTIVO' ? '' : ''}">
                    ${t.estado}
                  </span>
                </div>
                <div class="tour-info">
                  <p class="fs-body-title">${t.nombre}</p>
                  <p class="tour-price">
                    <fmt:formatNumber value="${t.precio}" type="currency"
                                      currencySymbol="$" maxFractionDigits="0"/>
                  </p>
                  <p class="fs-details">${t.descripcion}</p>
                </div>
              </div>
            </a>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</section>

<%-- ══ RESEÑAS ═════════════════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <div class="title-header">
      <p>No nos tomes la palabra,</p>
      <h2 class="fs-secondary-heading">lee a nuestros usuarios</h2>
    </div>
    <div class="three-col-grid">
      <div class="review-card">
        <div class="review-header">
          <div class="avatar">MG</div>
          <p class="user-name">María González</p>
        </div>
        <p class="review-body">
          "Una experiencia increíble. El guía fue muy profesional y el recorrido
          por Cartagena superó todas mis expectativas."
        </p>
        <div class="review-footer">
          <span>Cartagena Mágica</span>
          <span>Carlos Mendoza</span>
          <span>Jun 2025</span>
        </div>
      </div>
      <div class="review-card">
        <div class="review-header">
          <div class="avatar">JA</div>
          <p class="user-name">James Anderson</p>
        </div>
        <p class="review-body">
          "The Tayrona and Ciudad Perdida tour was absolutely breathtaking.
          Would recommend Colombiando to anyone visiting Colombia."
        </p>
        <div class="review-footer">
          <span>Tayrona y Ciudad Perdida</span>
          <span>Andrés Herrera</span>
          <span>Jul 2025</span>
        </div>
      </div>
      <div class="review-card">
        <div class="review-header">
          <div class="avatar">LP</div>
          <p class="user-name">Laura Restrepo</p>
        </div>
        <p class="review-body">
          "Caño Cristales es un lugar mágico y Colombiando lo hace aún más especial.
          Organización perfecta de principio a fin."
        </p>
        <div class="review-footer">
          <span>Caño Cristales</span>
          <span>Diana Ospina</span>
          <span>Sep 2025</span>
        </div>
      </div>
    </div>
  </div>
</section>

<%-- ══ FORMULARIO CONTACTO ════════════════════════════════════════════════ --%>
<%@ include file="/WEB-INF/views/layout/contact-section.jsp" %>

<script>
  // Carrusel simple
  const cards = document.querySelectorAll('.tour-info-card');
  let idx = 0;
  function mostrar(n) {
    cards.forEach(c => c.style.display = 'none');
    idx = (n + cards.length) % cards.length;
    if (cards[idx]) cards[idx].style.display = '';
  }
  document.getElementById('btnAnterior')?.addEventListener('click', () => mostrar(idx - 1));
  document.getElementById('btnSiguiente')?.addEventListener('click', () => mostrar(idx + 1));
  if (cards.length > 1) setInterval(() => mostrar(idx + 1), 5000);
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
