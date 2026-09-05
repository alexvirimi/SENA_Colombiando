<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle"   value="Servicios" />
<c:set var="currentPage" value="servicios" />
<c:set var="extraCss"    value="${['tour-card.css','carrousel.css','servicios.css','contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<%-- ══ HERO CARRUSEL ══════════════════════════════════════════════════════ --%>
<section class="first-padding-block-250">
  <div class="">
    <div class="secondary-header">
      <div class="carrousel" id="carrusel">
        <div class="tour-exhibited">
          <c:choose>
            <c:when test="${not empty tours}">
              <c:forEach var="t" items="${tours}" varStatus="st">
                <c:if test="${st.index < 5}">
                  <div class="tour-info-card"
                       style="${st.index > 0 ? 'display:none' : ''}"
                       data-index="${st.index}">
                    <h2 class="fs-body-title">${t.nombre}</h2>
                    <span class="description fs-details">
                      Colombia &bull; ${t.fechaSalida}
                    </span>
                  </div>
                </c:if>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div class="tour-info-card">
                <h2 class="fs-body-title">Explora Colombia</h2>
                <span class="description fs-details">Descubre nuestros tours</span>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
        <button class="btn-before" id="btnAnterior" aria-label="Anterior">
          <img src="${pageContext.request.contextPath}/static/img/before.svg" alt="anterior"/>
        </button>
        <button class="btn-next" id="btnSiguiente" aria-label="Siguiente">
          <img src="${pageContext.request.contextPath}/static/img/after.svg" alt="siguiente"/>
        </button>
      </div>
    </div>
  </div>
</section>

<%-- ══ FILTROS Y BÚSQUEDA ══════════════════════════════════════════════════ --%>
<section class="padding-block-250">
  <div class="container">
    <nav class="secondary-nav">
      <%-- Filtros por región --%>
      <div class="nav-wrapper">
        <ul role="list" class="nav-list">
          <li><a href="${pageContext.request.contextPath}/tours"
                 class="nav-list-item ${empty param.region ? 'active' : ''}">Todos</a></li>
          <li><a href="${pageContext.request.contextPath}/tours?region=Caribe"
                 class="nav-list-item ${param.region == 'Caribe' ? 'active' : ''}">Caribe</a></li>
          <li><a href="${pageContext.request.contextPath}/tours?region=Pacífica"
                 class="nav-list-item ${param.region == 'Pacífica' ? 'active' : ''}">Pacífica</a></li>
          <li><a href="${pageContext.request.contextPath}/tours?region=Andina"
                 class="nav-list-item ${param.region == 'Andina' ? 'active' : ''}">Andina</a></li>
          <li><a href="${pageContext.request.contextPath}/tours?region=Orinoquía"
                 class="nav-list-item ${param.region == 'Orinoquía' ? 'active' : ''}">Orinoquía</a></li>
          <li><a href="${pageContext.request.contextPath}/tours?region=Amazonía"
                 class="nav-list-item ${param.region == 'Amazonía' ? 'active' : ''}">Amazonía</a></li>
        </ul>
      </div>

      <%-- Buscador --%>
      <form action="${pageContext.request.contextPath}/tours" method="get">
        <div class="search-field">
          <label for="buscar">
            <img src="${pageContext.request.contextPath}/static/img/search.svg"
                 alt="Buscar" width="20" height="20"/>
          </label>
          <input type="text" id="buscar" name="q"
                 placeholder="Buscar tour…"
                 value="${param.q}"/>
        </div>
      </form>
    </nav>

    <%-- Imagen representativa de región --%>
    <div class="padding-block-150">
      <div class="region-img-representative">
        <figure>Imagen representativa de
          <c:choose>
            <c:when test="${not empty param.region}">${param.region}</c:when>
            <c:otherwise>Colombia</c:otherwise>
          </c:choose>
        </figure>
      </div>
    </div>

    <%-- Grid de tour cards --%>
    <div class="three-col-grid padding-block-150">
      <c:choose>
        <c:when test="${empty tours}">
          <p class="fs-details" style="grid-column:1/-1;text-align:center;padding:2rem 0">
            No se encontraron tours disponibles.
          </p>
        </c:when>
        <c:otherwise>
          <c:forEach var="t" items="${tours}">
            <a href="${pageContext.request.contextPath}/tours?accion=ver&id=${t.idTour}"
               class="tour-card-link">
              <div class="tour-card">
                <div class="tour-image">
                  <figure>Imagen del tour</figure>
                </div>
                <div class="tour-tags">
                  <span class="tag bgr-yellow">${t.fechaSalida}</span>
                  <span class="tag bgr-red">${t.estado}</span>
                </div>
                <div class="tour-info">
                  <p class="fs-body-title">${t.nombre}</p>
                  <p class="tour-price">
                    <fmt:formatNumber value="${t.precio}" type="currency"
                                      currencySymbol="$" maxFractionDigits="0"/>
                    <span class="fs-details"> / persona</span>
                  </p>
                  <p class="fs-details">${t.duracionDias} días &bull; Máx. ${t.capacidadMaxima} pax</p>
                </div>
              </div>
            </a>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>

  </div>
</section>

<%-- ══ CONTACTO ════════════════════════════════════════════════════════════ --%>
<%@ include file="/WEB-INF/views/layout/contact-section.jsp" %>

<script>
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
