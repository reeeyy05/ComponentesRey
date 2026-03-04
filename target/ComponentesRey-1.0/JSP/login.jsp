<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Acceder</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilos.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    </head>
    <body class="cuerpo-pagina">

        <jsp:include page="../INC/cabecera.jsp" />

        <div class="contenedor-login-principal">

            <div class="caja-login-main"> 
                <h2 class="titulo-seccion-login">Iniciar Sesión</h2>

                <c:if test="${not empty error}">
                    <div class="error-login-centrado">
                        ${error}
                    </div>
                </c:if>

                <form action="${contexto}/LoginController" method="post" class="formulario-grid">
                    <div class="grupo-form-completo">
                        <label for="email" class="label-form-general">Correo Electrónico:</label>
                        <input type="email" id="email" name="email" placeholder="ejemplo@correo.com" class="input-form-general">
                    </div>

                    <div class="grupo-form-password">
                        <label for="password" class="label-form-general">Contraseña:</label>
                        <input type="password" id="password" name="password" placeholder="Contraseña" class="input-form-general">
                    </div>

                    <button type="submit" class="btn-login-enviar">Entrar a mi cuenta</button>
                </form>

                <div class="caja-registro-alternativo">
                    <p class="texto-gris">¿Todavía no tienes cuenta?<br>
                        <a href="${contexto}/RegistroController" class="enlace-destacado">Regístrate aquí</a>
                    </p>
                </div>

            </div>
        </div>

        <jsp:include page="../INC/footer.jsp" />

    </body>
</html>