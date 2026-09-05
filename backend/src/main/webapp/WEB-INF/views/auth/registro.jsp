<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle"   value="Registro" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">
    <div class="contact-form max-width" style="max-width:600px;margin:auto">

      <div class="title-header">
        <svg width="180" height="50" viewBox="0 0 204 38" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15.104 31.448C12.3307 31.448 9.89867 30.936 7.808 29.912C5.71733 28.8667 4.08533 27.4053 2.912 25.528C1.76 23.6507 1.184 21.464 1.184 18.968C1.184 16.4507 1.76 14.264 2.912 12.408C4.08533 10.552 5.73867 9.112 7.872 8.088C10.0053 7.064 12.5333 6.552 15.456 6.552C16.16 6.552 16.7467 6.584 17.216 6.648C17.6853 6.69066 18.1227 6.744 18.528 6.808C18.9333 6.872 19.392 6.936 19.904 7C20.4373 7.04267 21.0987 7.064 21.888 7.064L22.848 13.464H22.528C22.2933 12.2053 21.824 11.096 21.12 10.136C20.4373 9.15467 19.5733 8.38667 18.528 7.832C17.504 7.256 16.3627 6.968 15.104 6.968C13.8667 6.968 12.7253 7.26667 11.68 7.864C10.6347 8.46133 9.73867 9.29333 8.992 10.36C8.24533 11.448 7.65867 12.7493 7.232 14.264C6.82667 15.7573 6.624 17.4 6.624 19.192C6.624 21.56 6.98667 23.6293 7.712 25.4C8.43733 27.1493 9.46133 28.5147 10.784 29.496C12.1067 30.456 13.664 30.936 15.456 30.936C17.504 30.936 19.1893 30.328 20.512 29.112C21.856 27.8747 22.688 26.1573 23.008 23.96H23.328L22.4 30.872C21.3973 30.872 20.512 30.9147 19.744 31C18.976 31.1067 18.2293 31.2027 17.504 31.288C16.8 31.3947 16 31.448 15.104 31.448Z" fill="black"/>
        </svg>
      </div>

      <c:if test="${not empty errores}">
        <div class="alert-global alert-error padding-block-100"
             style="margin-bottom:var(--size-100)">
          <ul style="margin-left:var(--size-100)">
            <c:forEach var="e" items="${errores}"><li>${e}</li></c:forEach>
          </ul>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/clientes?accion=guardar"
            method="post" id="formRegistro">

        <div class="form-two-col">

          <div class="field">
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" name="nombre"
                   placeholder="Jane" required maxlength="80"
                   value="${cliente.nombre}"/>
            <span class="error">El nombre es obligatorio.</span>
          </div>

          <div class="field">
            <label for="apellido">Apellido</label>
            <input type="text" id="apellido" name="apellido"
                   placeholder="Doe" required maxlength="80"
                   value="${cliente.apellido}"/>
            <span class="error">El apellido es obligatorio.</span>
          </div>

          <div class="field form-full-col">
            <label for="correo">Correo</label>
            <input type="email" id="correo" name="correo"
                   placeholder="example@email.com" required maxlength="120"
                   value="${cliente.correo}"/>
            <span class="error">Ingresa un correo válido.</span>
          </div>

          <div class="field">
            <label for="celular">Celular</label>
            <div class="phone-wrapper">
              <select name="indicativo">
                <option value="" disabled selected hidden>+##</option>
                <option value="+57" selected>+57</option>
                <option value="+1">+01</option>
                <option value="+34">+34</option>
                <option value="+52">+52</option>
              </select>
              <input type="text" id="celular" name="telefono"
                     placeholder="### #######" maxlength="10"
                     value="${cliente.telefono}"/>
            </div>
            <span class="error">Ingresa un número válido.</span>
          </div>

          <div class="field">
            <label for="tipoDoc">Tipo de documento</label>
            <select id="tipoDoc" name="tipoDocumento" required>
              <option value="">Seleccionar…</option>
              <option value="CC"        ${cliente.tipoDocumento=='CC'?'selected':''}>CC</option>
              <option value="CE"        ${cliente.tipoDocumento=='CE'?'selected':''}>CE</option>
              <option value="PASAPORTE" ${cliente.tipoDocumento=='PASAPORTE'?'selected':''}>Pasaporte</option>
            </select>
          </div>

          <div class="field">
            <label for="numDoc">Número de documento</label>
            <input type="text" id="numDoc" name="numeroDocumento"
                   placeholder="1023456789" required maxlength="30"
                   value="${cliente.numeroDocumento}"/>
            <span class="error">El documento es obligatorio.</span>
          </div>

          <div class="field">
            <label for="fechaNac">Fecha de nacimiento</label>
            <input type="date" id="fechaNac" name="fechaNacimiento"
                   value="${cliente.fechaNacimiento}" required/>
          </div>

          <div class="field">
            <label for="nacionalidad">Nacionalidad</label>
            <input type="text" id="nacionalidad" name="nacionalidad"
                   placeholder="Colombiana" maxlength="60"
                   value="${not empty cliente.nacionalidad ? cliente.nacionalidad : 'Colombiana'}"/>
          </div>

          <div class="field">
            <label for="pwd">Contraseña</label>
            <input type="password" id="pwd" name="contrasena"
                   placeholder="Contraseña" required/>
            <span class="error">La contraseña es obligatoria.</span>
          </div>

          <div class="field">
            <label for="pwd2">Repetir Contraseña</label>
            <input type="password" id="pwd2" name="contrasena2"
                   placeholder="Contraseña"/>
            <span class="error" id="err-pwd2">Las contraseñas no coinciden.</span>
          </div>

        </div><!-- /form-two-col -->

        <div class="field">
          <div class="checkbox-wrapper">
            <input type="checkbox" id="politica" name="politica" required/>
            <label for="politica">
              Acepto la política de tratamiento de datos personales
            </label>
          </div>
          <span class="error">Debes aceptar la política.</span>
        </div>

        <button type="submit" class="btn-submit">Crear cuenta</button>
        <div id="ingresar">
          <a href="${pageContext.request.contextPath}/auth/ingreso"
             class="fs-details">¿Ya tienes cuenta? Inicia sesión</a>
        </div>
      </form>
    </div>
  </div>
</section>

<script>
  document.getElementById('formRegistro').addEventListener('submit', function(e) {
    const p1 = document.getElementById('pwd').value;
    const p2 = document.getElementById('pwd2').value;
    if (p1 !== p2) {
      e.preventDefault();
      document.getElementById('err-pwd2').style.display = 'block';
    }
  });
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
