<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle"   value="Clientes" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">

    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:var(--size-150)">
      <div>
        <h1 class="fs-primary-heading">Clientes</h1>
        <p class="fs-details">Total: ${totalClientes} registrados</p>
      </div>
      <a href="${pageContext.request.contextPath}/clientes?accion=nuevo"
         style="padding:12px var(--size-150);background:var(--clr-verde);
                color:var(--clr-blanco);border-radius:100px;font-weight:700">
        + Registrar cliente
      </a>
    </div>

    <%-- Tabla --%>
    <div style="overflow-x:auto;border:1.5px solid var(--clr-negro);border-radius:var(--size-100)">
      <table style="width:100%;border-collapse:collapse;font-size:var(--fs-200)">
        <thead>
          <tr style="background:var(--clr-gris-900);color:var(--clr-blanco)">
            <th style="padding:var(--size-100);text-align:left">ID</th>
            <th style="padding:var(--size-100);text-align:left">Nombre</th>
            <th style="padding:var(--size-100);text-align:left">Correo</th>
            <th style="padding:var(--size-100);text-align:left">Teléfono</th>
            <th style="padding:var(--size-100);text-align:left">Documento</th>
            <th style="padding:var(--size-100);text-align:center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty clientes}">
              <tr>
                <td colspan="6"
                    style="padding:var(--size-200);text-align:center;color:var(--clr-gris-400)">
                  No hay clientes registrados.
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="c" items="${clientes}" varStatus="st">
                <tr style="border-top:1px solid var(--clr-gris-200);
                           background:${st.index % 2 == 0 ? 'var(--clr-blanco)' : '#f9f9f9'}">
                  <td style="padding:var(--size-100)">
                    <span class="tag bgr-blue">${c.idUsuario}</span>
                  </td>
                  <td style="padding:var(--size-100);font-weight:700">
                    ${c.nombre} ${c.apellido}
                    <br><span class="fs-details">${c.fechaNacimiento}</span>
                  </td>
                  <td style="padding:var(--size-100)">${c.correo}</td>
                  <td style="padding:var(--size-100)">${c.telefono}</td>
                  <td style="padding:var(--size-100)">
                    <span class="tag bgr-yellow">${c.tipoDocumento}</span>
                    ${c.numeroDocumento}
                  </td>
                  <td style="padding:var(--size-100);text-align:center">
                    <div style="display:flex;gap:var(--size-50);justify-content:center">
                      <a href="${pageContext.request.contextPath}/clientes?accion=editar&id=${c.idUsuario}"
                         style="padding:4px 12px;border:1.5px solid var(--clr-negro);
                                border-radius:100px;font-size:var(--fs-100);font-weight:700">
                        Editar
                      </a>
                      <a href="${pageContext.request.contextPath}/reservas?idCliente=${c.idUsuario}"
                         style="padding:4px 12px;background:var(--clr-verde);color:var(--clr-blanco);
                                border-radius:100px;font-size:var(--fs-100);font-weight:700">
                        Reservas
                      </a>
                      <a href="${pageContext.request.contextPath}/clientes?accion=eliminar&id=${c.idUsuario}"
                         style="padding:4px 12px;background:var(--clr-rojo);color:var(--clr-negro);
                                border-radius:100px;font-size:var(--fs-100);font-weight:700"
                         onclick="return confirm('¿Eliminar a ${c.nombre} ${c.apellido}?')">
                        Eliminar
                      </a>
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
