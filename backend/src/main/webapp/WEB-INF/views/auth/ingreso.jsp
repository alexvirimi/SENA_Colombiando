<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle"   value="Ingresar" />
<c:set var="currentPage" value="" />
<c:set var="extraCss"    value="${['contact-form.css']}" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<section class="padding-block-250">
  <div class="container">
    <div class="contact-form max-width" style="max-width:480px;margin:auto">

      <%-- Logo full --%>
      <div class="title-header">
        <svg width="180" height="50" viewBox="0 0 204 57" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M15.104 31.448C12.3307 31.448 9.89867 30.936 7.808 29.912C5.71733 28.8667 4.08533 27.4053 2.912 25.528C1.76 23.6507 1.184 21.464 1.184 18.968C1.184 16.4507 1.76 14.264 2.912 12.408C4.08533 10.552 5.73867 9.112 7.872 8.088C10.0053 7.064 12.5333 6.552 15.456 6.552C16.16 6.552 16.7467 6.584 17.216 6.648C17.6853 6.69066 18.1227 6.744 18.528 6.808C18.9333 6.872 19.392 6.936 19.904 7C20.4373 7.04267 21.0987 7.064 21.888 7.064L22.848 13.464H22.528C22.2933 12.2053 21.824 11.096 21.12 10.136C20.4373 9.15467 19.5733 8.38667 18.528 7.832C17.504 7.256 16.3627 6.968 15.104 6.968C13.8667 6.968 12.7253 7.26667 11.68 7.864C10.6347 8.46133 9.73867 9.29333 8.992 10.36C8.24533 11.448 7.65867 12.7493 7.232 14.264C6.82667 15.7573 6.624 17.4 6.624 19.192C6.624 21.56 6.98667 23.6293 7.712 25.4C8.43733 27.1493 9.46133 28.5147 10.784 29.496C12.1067 30.456 13.664 30.936 15.456 30.936C17.504 30.936 19.1893 30.328 20.512 29.112C21.856 27.8747 22.688 26.1573 23.008 23.96H23.328L22.4 30.872C21.3973 30.872 20.512 30.9147 19.744 31C18.976 31.1067 18.2293 31.2027 17.504 31.288C16.8 31.3947 16 31.448 15.104 31.448Z" fill="black"/>
          <path d="M19 54V53.84C19.3947 53.84 19.7093 53.7333 19.944 53.52C20.1787 53.3067 20.296 53.0187 20.296 52.656V43.36C20.296 42.9973 20.1733 42.7093 19.928 42.496C19.6933 42.272 19.3893 42.16 19.016 42.16V42H24.168C25.672 42 26.984 42.2293 28.104 42.688C29.224 43.136 30.0933 43.808 30.712 44.704C31.3307 45.6 31.64 46.704 31.64 48.016C31.64 49.328 31.3253 50.4267 30.696 51.312C30.0773 52.1973 29.208 52.8693 28.088 53.328C26.968 53.776 25.6613 54 24.168 54H19Z" fill="black"/>
        </svg>
      </div>

      <c:if test="${not empty param.error}">
        <div class="alert-global alert-error padding-block-100"
             style="margin-bottom:var(--size-100)">
          ${param.error}
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/auth/login" method="post">
        <div class="field">
          <label for="correo">Correo</label>
          <input type="email" id="correo" name="correo"
                 placeholder="example@email.com" required
                 value="${param.correo}"/>
          <span class="error">Ingresa un correo válido.</span>
        </div>
        <div class="field">
          <label for="pwd">Contraseña</label>
          <input type="password" id="pwd" name="contrasena"
                 placeholder="Contraseña" required/>
          <span class="error">La contraseña es obligatoria.</span>
          <a href="${pageContext.request.contextPath}/auth/recuperar"
             class="fs-details">Olvidé la Contraseña</a>
        </div>
        <div class="field" id="ingresar">
          <button type="submit" class="btn-submit">Ingresar</button>
          <a href="${pageContext.request.contextPath}/auth/registro"
             class="fs-details">¿Aún no tienes cuenta? Regístrate</a>
        </div>
      </form>

    </div>
  </div>
</section>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
