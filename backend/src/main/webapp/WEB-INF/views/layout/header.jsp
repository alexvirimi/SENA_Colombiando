<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Colombiando${not empty pageTitle ? ' — '.concat(pageTitle) : ''}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/reset.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/base.css" />
  <c:if test="${not empty extraCss}">
    <c:forEach var="css" items="${extraCss}">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/${css}" />
    </c:forEach>
  </c:if>
</head>
<body>

<header class="primary-header padding-block-150">
  <div class="container">
    <nav aria-label="principal" class="primary-nav">

      <!-- Logo -->
      <div>
        <a href="${pageContext.request.contextPath}/index">
          <%-- Logo SVG inline para evitar path problems --%>
          <svg width="160" height="30" viewBox="0 0 204 38" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M15.104 31.448C12.3307 31.448 9.89867 30.936 7.808 29.912C5.71733 28.8667 4.08533 27.4053 2.912 25.528C1.76 23.6507 1.184 21.464 1.184 18.968C1.184 16.4507 1.76 14.264 2.912 12.408C4.08533 10.552 5.73867 9.112 7.872 8.088C10.0053 7.064 12.5333 6.552 15.456 6.552C16.16 6.552 16.7467 6.584 17.216 6.648C17.6853 6.69066 18.1227 6.744 18.528 6.808C18.9333 6.872 19.392 6.936 19.904 7C20.4373 7.04267 21.0987 7.064 21.888 7.064L22.848 13.464H22.528C22.2933 12.2053 21.824 11.096 21.12 10.136C20.4373 9.15467 19.5733 8.38667 18.528 7.832C17.504 7.256 16.3627 6.968 15.104 6.968C13.8667 6.968 12.7253 7.26667 11.68 7.864C10.6347 8.46133 9.73867 9.29333 8.992 10.36C8.24533 11.448 7.65867 12.7493 7.232 14.264C6.82667 15.7573 6.624 17.4 6.624 19.192C6.624 21.56 6.98667 23.6293 7.712 25.4C8.43733 27.1493 9.46133 28.5147 10.784 29.496C12.1067 30.456 13.664 30.936 15.456 30.936C17.504 30.936 19.1893 30.328 20.512 29.112C21.856 27.8747 22.688 26.1573 23.008 23.96H23.328L22.4 30.872C21.3973 30.872 20.512 30.9147 19.744 31C18.976 31.1067 18.2293 31.2027 17.504 31.288C16.8 31.3947 16 31.448 15.104 31.448ZM33.2928 31.448C31.5434 31.448 30.0074 31.096 28.6848 30.392C27.3834 29.688 26.3701 28.696 25.6448 27.416C24.9194 26.136 24.5568 24.6213 24.5568 22.872C24.5568 21.1227 24.9194 19.608 25.6448 18.328C26.3701 17.048 27.3834 16.056 28.6848 15.352C30.0074 14.648 31.5434 14.296 33.2928 14.296C35.0421 14.296 36.5674 14.648 37.8688 15.352C39.1701 16.056 40.1834 17.048 40.9088 18.328C41.6341 19.608 41.9968 21.1227 41.9968 22.872C41.9968 24.6213 41.6341 26.136 40.9088 27.416C40.1834 28.696 39.1701 29.688 37.8688 30.392C36.5674 31.096 35.0421 31.448 33.2928 31.448ZM42.25 31V30.68C43.018 30.68 43.6367 30.456 44.106 30.008C44.5967 29.5387 44.842 28.9093 44.842 28.12V9.88C44.842 9.112 44.5967 8.49333 44.106 8.024C43.6367 7.55467 43.018 7.32 42.25 7.32V7H45.514C46.602 7 47.434 6.88266 48.01 6.648C48.5433 6.41333 48.9167 6.12533 49.13 5.784H49.45V28.12C49.45 28.9093 49.6847 29.5387 50.154 30.008C50.6447 30.456 51.274 30.68 52.042 30.68V31H42.25ZM194.043 31.448C192.293 31.448 190.757 31.096 189.435 30.392C188.133 29.688 187.12 28.696 186.395 27.416C185.669 26.136 185.307 24.6213 185.307 22.872C185.307 21.1227 185.669 19.608 186.395 18.328C187.12 17.048 188.133 16.056 189.435 15.352C190.757 14.648 192.293 14.296 194.043 14.296C195.792 14.296 197.317 14.648 198.619 15.352C199.92 16.056 200.933 17.048 201.659 18.328C202.384 19.608 202.747 21.1227 202.747 22.872C202.747 24.6213 202.384 26.136 201.659 27.416C200.933 28.696 199.92 29.688 198.619 30.392C197.317 31.096 195.792 31.448 194.043 31.448Z" fill="black"/>
          </svg>
        </a>
      </div>

      <!-- Navegación central -->
      <div class="nav-wrapper">
        <ul role="list" class="nav-list">
          <li>
            <a href="${pageContext.request.contextPath}/index"
               class="nav-list-item ${currentPage == 'inicio' ? 'active' : ''}">
              Inicio
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/tours"
               class="nav-list-item ${currentPage == 'servicios' ? 'active' : ''}">
              Servicios
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/acerca"
               class="nav-list-item ${currentPage == 'acerca' ? 'active' : ''}">
              Acerca de
            </a>
          </li>
        </ul>
      </div>

      <!-- Perfil / avatar -->
      <div>
        <a href="${pageContext.request.contextPath}/clientes"
           class="profile-div" title="Gestión de clientes">
          👤
        </a>
      </div>

    </nav>
  </div>
</header>

<%-- Mensaje de alerta global (viene de redirect con ?mensaje=...&tipo=...) --%>
<c:if test="${not empty param.mensaje}">
  <div class="container">
    <div class="alert-global alert-${not empty param.tipo ? param.tipo : 'info'}
                padding-block-100">
      ${param.mensaje}
    </div>
  </div>
</c:if>
