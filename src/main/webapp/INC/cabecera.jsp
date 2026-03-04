<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<header class="cabecera-principal">
    <div class="contenedor-cabecera">

        <div class="caja-logo">
            <a href="${contexto}/inicio">
                <img src="${contexto}/imagenes/logo.png" alt="Componentes Rey" class="imagen-logo">
            </a>
        </div>

        <div class="zona-buscador">
            <form action="${contexto}/inicio" method="post" class="form-buscador-cabecera">
                <input type="hidden" name="accion" value="buscar">
                <input type="text" name="busqueda" placeholder="Buscar productos..." class="input-buscador-cabecera">
                <button type="submit" class="btn-buscador-cabecera" title="Buscar">
                    <span class="icono-lupa-cabecera">search</span>
                </button>
            </form>
        </div>

        <div class="zona-usuario">
            <c:choose>
                <c:when test="${empty sessionScope.usuario}">
                    <a href="${contexto}/LoginController" class="enlace-texto-cabecera" title="Acceder a tu cuenta">Acceder</a>
                    <a href="${contexto}/RegistroController" class="enlace-texto-cabecera" title="Crear nueva cuenta">Registrarse</a>
                </c:when>

                <c:otherwise>
                    <a href="${contexto}/PerfilController" class="enlace-perfil-cabecera" title="Mi Perfil">
                        <c:choose>
                            <c:when test="${empty sessionScope.usuario.avatar || sessionScope.usuario.avatar == 'perfil_default.jpg'}">
                                <c:set var="rutaAvatarHeader" value="${contexto}/imagenes/perfil_default.jpg"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="rutaAvatarHeader" value="${contexto}/imagenes/avatares/${sessionScope.usuario.avatar}"/>
                            </c:otherwise>
                        </c:choose>

                        <img src="${rutaAvatarHeader}" alt="Usuario" class="img-avatar-cabecera" onerror="this.src='${contexto}/imagenes/perfil_default.jpg';">
                        <span>${sessionScope.usuario.nombre}</span>
                    </a>
                    <a href="${contexto}/LogoutController" class="enlace-texto-cabecera" title="Cerrar Sesión" style="color: #ffcccc;">Salir</a>
                </c:otherwise>
            </c:choose>

            <a href="${contexto}/CarritoController" class="boton-carrito-header" title="Ver Cesta">
                <span class="icono-btn-header">shopping_cart</span>
                <span>[${sessionScope.cantidadTotal != null ? sessionScope.cantidadTotal : 0}]</span>
            </a>
        </div>

    </div>
</header>