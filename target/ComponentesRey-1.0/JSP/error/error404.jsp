<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Página no encontrada</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilos.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    </head>
    <body class="cuerpo-pagina">
        <jsp:include page="/INC/cabecera.jsp" />

        <main class="contenedor-principal-bloque">
            <div class="tarjeta-error">
                <img src="${contexto}/imagenes/error/error404.png" alt="Error 404" style="max-width: 400px; width: 100%; height: auto; margin-bottom: 20px;">

                <h2>¡Vaya! No encontramos lo que buscas.</h2>
                <p>La página a la que intentas acceder no existe o ha sido movida de sitio.</p>

                <a href="${contexto}/inicio" class="boton-tramitar" style="text-decoration: none; display: inline-block; padding: 12px 25px; font-size: 1rem; text-transform: none;">Volver a la tienda</a>
            </div>
        </main>

        <jsp:include page="/INC/footer.jsp" />
    </body>
</html>