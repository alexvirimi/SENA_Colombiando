<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle"   value="${titulo}" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">

    <div class="padding-block-100">
      <a href="${pageContext.request.contextPath}/clientes"
         style="color:var(--clr-verde);font-size:var(--fs-200);font-weight:700">
        ← Volver a clientes
      </a>
    </div>

    <div class="contact-form" style="max-width:800px;margin:auto">
      <div class="title-header">
        <h2 class="fs-secondary-heading">${titulo}</h2>
      </div>

      <c:if test="${not empty errores}">
        <div class="alert-global alert-error padding-block-100"
             style="margin-bottom:var(--size-150)">
          <ul style="margin-left:var(--size-100)">
            <c:forEach var="e" items="${errores}"><li>${e}</li></c:forEach>
          </ul>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/clientes" method="post">
        <input type="hidden" name="accion"
               value="${modoEdicion ? 'actualizar' : 'guardar'}">
        <c:if test="${modoEdicion}">
          <input type="hidden" name="idUsuario" value="${cliente.idUsuario}">
        </c:if>

        <div class="form-two-col">

          <div class="field">
            <label for="nombre">Nombre *</label>
            <input type="text" id="nombre" name="nombre"
                   value="${cliente.nombre}" required maxlength="80"
                   placeholder="Jane">
            <span class="error">El nombre es obligatorio.</span>
          </div>

          <div class="field">
            <label for="apellido">Apellido *</label>
            <input type="text" id="apellido" name="apellido"
                   value="${cliente.apellido}" required maxlength="80"
                   placeholder="Doe">
            <span class="error">El apellido es obligatorio.</span>
          </div>

          <div class="field form-full-col">
            <label for="correo">Correo *</label>
            <input type="email" id="correo" name="correo"
                   value="${cliente.correo}" required maxlength="120"
                   placeholder="example@email.com">
            <span class="error">Correo inválido.</span>
          </div>

          <div class="field">
            <label for="telefono">Teléfono *</label>
            <div class="phone-wrapper">
              <select><option>+57</option></select>
              <input type="text" id="telefono" name="telefono"
                     value="${cliente.telefono}" maxlength="10"
                     placeholder="3001234567">
            </div>
            <span class="error">10 dígitos requeridos.</span>
          </div>

          <div class="field">
            <label for="fechaNac">Fecha de nacimiento *</label>
            <input type="date" id="fechaNac" name="fechaNacimiento"
                   value="${cliente.fechaNacimiento}" required>
          </div>

          <div class="field">
            <label for="tipoDoc">Tipo de documento *</label>
            <select id="tipoDoc" name="tipoDocumento" required>
              <option value="">Seleccionar…</option>
              <option value="CC"        ${cliente.tipoDocumento=='CC'        ?'selected':''}>CC</option>
              <option value="CE"        ${cliente.tipoDocumento=='CE'        ?'selected':''}>CE</option>
              <option value="PASAPORTE" ${cliente.tipoDocumento=='PASAPORTE' ?'selected':''}>Pasaporte</option>
              <option value="NIT"       ${cliente.tipoDocumento=='NIT'       ?'selected':''}>NIT</option>
            </select>
          </div>

          <div class="field">
            <label for="numDoc">Número de documento *</label>
            <input type="text" id="numDoc" name="numeroDocumento"
                   value="${cliente.numeroDocumento}" required maxlength="30"
                   placeholder="1023456789">
            <span class="error">El documento es obligatorio.</span>
          </div>

          <div class="field">
            <label for="nacionalidad">Nacionalidad</label>
            <input type="text" id="nacionalidad" name="nacionalidad"
                   value="${not empty cliente.nacionalidad ? cliente.nacionalidad : 'Colombiana'}"
                   maxlength="60">
          </div>

          <div class="field">
            <label for="contrasena">
              Contraseña
              <c:if test="${!modoEdicion}">*</c:if>
            </label>
            <input type="password" id="contrasena" name="contrasena"
                   ${!modoEdicion ? 'required' : ''}
                   placeholder="${modoEdicion ? 'Dejar vacío para mantener' : 'Contraseña'}">
            <span class="error">La contraseña es obligatoria.</span>
          </div>

        </div>

        <button type="submit" class="btn-submit">
          ${modoEdicion ? 'Guardar cambios' : 'Registrar cliente'}
        </button>
      </form>
    </div>
  </div>
</section>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
