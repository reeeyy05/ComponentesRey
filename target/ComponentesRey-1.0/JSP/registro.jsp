<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registro de Usuario</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilos.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
        <script>const CONTEXTO = '${contexto}';</script>
        <script src="${contexto}/JS/registro.js"></script>
    </head>
    <body>
        <jsp:include page="/INC/cabecera.jsp" />

        <main class="contenedor-registro">
            <h2 class="titulo-seccion-login" style="border:none; margin-bottom:10px;">Crear una Cuenta</h2>

            <c:if test="${not empty requestScope.error}">
                <div class="mensaje-error-servidor">
                    <strong>Error:</strong> ${requestScope.error}
                </div>
            </c:if>

            <form action="${contexto}/RegistroController" method="POST" enctype="multipart/form-data" class="formulario-grid" id="formRegistro">

                <div class="grupo-form">
                    <label for="email">Correo Electrónico *</label>
                    <input type="text" id="email" name="email" maxlength="50" placeholder="ejemplo@correo.com">
                    <span class="mensaje-error" id="err-email"></span>
                </div>

                <div class="grupo-form">
                    <label for="password">Contraseña *</label>
                    <input type="password" id="password" name="password" maxlength="100">
                    <span class="mensaje-error" id="err-password"></span>
                </div>

                <div class="grupo-form">
                    <label for="passwordRep">Repetir Contraseña *</label>
                    <input type="password" id="passwordRep" name="passwordRep" maxlength="100">
                    <span class="mensaje-error" id="err-passwordRep"></span>
                </div>

                <div class="grupo-form">
                    <label for="nif">NIF (solo 8 números) *</label>
                    <div class="fila-nif">
                        <input type="text" id="nif" name="nif" maxlength="8" placeholder="12345678">
                        <input type="text" id="letraNif" name="letraNif" class="input-letra-nif" readonly>
                    </div>
                    <span class="mensaje-error" id="err-nif"></span>
                </div>

                <div class="grupo-form">
                    <label for="nombre">Nombre *</label>
                    <input type="text" id="nombre" name="nombre" maxlength="20">
                    <span class="mensaje-error" id="err-nombre"></span>
                </div>

                <div class="grupo-form">
                    <label for="apellidos">Apellidos *</label>
                    <input type="text" id="apellidos" name="apellidos" maxlength="30">
                    <span class="mensaje-error" id="err-apellidos"></span>
                </div>

                <div class="grupo-form">
                    <label for="telefono">Teléfono</label>
                    <input type="text" id="telefono" name="telefono" maxlength="9">
                    <span class="mensaje-error" id="err-telefono"></span>
                </div>

                <div class="grupo-form">
                    <label for="codigoPostal">Código Postal *</label>
                    <input type="text" id="codigoPostal" name="codigoPostal" maxlength="5">
                    <span class="mensaje-error" id="err-codigoPostal"></span>
                </div>

                <div class="grupo-form">
                    <label for="direccion">Dirección *</label>
                    <input type="text" id="direccion" name="direccion" maxlength="40">
                    <span class="mensaje-error" id="err-direccion"></span>
                </div>

                <div class="grupo-form">
                    <label for="localidad">Localidad *</label>
                    <input type="text" id="localidad" name="localidad" maxlength="40">
                    <span class="mensaje-error" id="err-localidad"></span>
                </div>

                <div class="grupo-form">
                    <label for="provincia">Provincia *</label>
                    <input type="text" id="provincia" name="provincia" maxlength="30">
                    <span class="mensaje-error" id="err-provincia"></span>
                </div>

                <div class="grupo-form-ancho-completo" style="text-align: center;">
                    <label>Previsualización del Avatar</label><br>
                    <img id="previa" src="${contexto}/imagenes/perfil_default.jpg" alt="Previsualización Avatar" style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover; border: 3px solid var(--boton-primario); margin-bottom: 15px;">
                </div>

                <div class="grupo-form-ancho-completo">
                    <label for="avatar">Foto de Perfil (Solo JPG/PNG)</label>
                    <input type="file" id="avatar" name="avatar" accept="image/png, image/jpeg">
                    <span class="mensaje-error" id="err-avatar"></span>
                </div>

                <button type="submit" class="btn-registro">Completar Registro</button>
            </form>
        </main>

        <jsp:include page="/INC/footer.jsp" />
    </body>
</html>