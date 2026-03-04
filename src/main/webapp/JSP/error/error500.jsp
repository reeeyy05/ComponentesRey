<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Error del servidor</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilos.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    </head>
    <body class="cuerpo-pagina">
        <jsp:include page="/INC/cabecera.jsp" />

        <main class="contenedor-principal-bloque">
            <div class="tarjeta-error">
                <img src="${contexto}/imagenes/error/error500.png" alt="Error 500" style="max-width: 400px; width: 100%; height: auto; margin-bottom: 20px;">

                <h2>¡Ups! Algo se ha roto en el servidor.</h2>
                <p>Nuestros técnicos informáticos (o nosotros mismos) ya estamos trabajando para solucionarlo.</p>

                <c:if test="${not empty requestScope.error}">
                    <div class="mensaje-error-servidor" style="text-align: left; font-family: monospace;">
                        <strong>Detalle técnico:</strong> ${requestScope.error}
                    </div>
                </c:if>

                <a href="${contexto}/inicio" class="boton-tramitar" style="text-decoration: none; display: inline-block; padding: 12px 25px; font-size: 1rem; text-transform: none; margin-top: 10px;">Volver a la página principal</a>
            </div>
        </main>

        <jsp:include page="/INC/footer.jsp" />
    </body>
</html>