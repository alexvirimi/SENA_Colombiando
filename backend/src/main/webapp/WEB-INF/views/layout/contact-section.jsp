<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Sección de contacto reutilizable — incluida en index, servicios, acerca-de --%>
<section class="padding-block-250">
  <div class="container">
    <div class="contact-form max-width">
      <div class="title-header">
        <p>¿Quieres saber más?</p>
        <h2 class="fs-secondary-heading">Contáctanos</h2>
      </div>
      <form action="${pageContext.request.contextPath}/contacto" method="post">
        <div class="field">
          <label for="contacto-nombre">Nombre Completo</label>
          <input type="text" id="contacto-nombre" name="nombre"
                 placeholder="Jane Doe" required />
          <span class="error" id="err-nombre">El nombre es obligatorio.</span>
        </div>
        <div class="field">
          <label for="contacto-correo">Correo</label>
          <input type="email" id="contacto-correo" name="correo"
                 placeholder="example@email.com" required />
          <span class="error" id="err-correo">Ingresa un correo válido.</span>
        </div>
        <div class="field">
          <label for="contacto-celular">Celular</label>
          <div class="phone-wrapper">
            <select name="indicativo">
              <option value="" disabled selected hidden>+##</option>
              <option value="+57">+57</option>
              <option value="+1">+01</option>
              <option value="+34">+34</option>
              <option value="+52">+52</option>
            </select>
            <input type="text" id="contacto-celular" name="celular"
                   placeholder="### #######" />
          </div>
        </div>
        <div class="field">
          <label for="contacto-mensaje">Mensaje</label>
          <textarea id="contacto-mensaje" name="mensaje"
                    placeholder="Mensaje"></textarea>
        </div>
        <div class="field">
          <div class="checkbox-wrapper">
            <input type="checkbox" id="contacto-politica" name="politica" required />
            <label for="contacto-politica">
              Acepto la política de tratamiento de datos personales
            </label>
          </div>
          <span class="error" id="err-politica">Debes aceptar la política.</span>
        </div>
        <button type="submit" class="btn-submit">Enviar Mensaje</button>
      </form>
    </div>
  </div>
</section>
