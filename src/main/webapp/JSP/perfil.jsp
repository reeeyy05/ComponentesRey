<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mi Perfil - Componentes Rey</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilos.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    </head>
    <body class="cuerpo-pagina">

        <jsp:include page="../INC/cabecera.jsp" />

        <main class="contenedor-principal-bloque">

            <div class="contenedor-registro-perfil">
                <h1 class="titulo-seccion-perfil">Mi Perfil</h1>

                <c:if test="${not empty mensajeExitoDatos}">
                    <div class="perfil-mensaje-exito">${mensajeExitoDatos}</div>
                </c:if>
                <c:if test="${not empty mensajeErrorDatos}">
                    <div class="error-perfil-centrado">${mensajeErrorDatos}</div>
                </c:if>

                <form action="${contexto}/PerfilController" method="post" enctype="multipart/form-data" class="formulario-grid">
                    <input type="hidden" name="accion" value="actualizar_datos">

                    <div class="grupo-form-avatar-perfil">
                        <div class="caja-imagen-perfil">
                            <c:choose>
                                <c:when test="${empty sessionScope.usuario.avatar || sessionScope.usuario.avatar == 'perfil_default.jpg'}">
                                    <c:set var="rutaAvatarPerfil" value="${contexto}/imagenes/perfil_default.jpg"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="rutaAvatarPerfil" value="${contexto}/imagenes/avatares/${sessionScope.usuario.avatar}"/>
                                </c:otherwise>
                            </c:choose>
                            <img id="previa" src="${rutaAvatarPerfil}" alt="Mi Perfil" class="img-perfil-grande" onerror="this.src='${contexto}/imagenes/perfil_default.jpg';">
                        </div>

                        <div class="caja-subida-avatar">
                            <label for="nuevo_avatar" class="label-form-general">Cambiar Foto de Perfil:</label>
                            <input type="file" id="nuevo_avatar" name="nuevo_avatar" accept="image/*" class="input-form-general" onchange="readURL(this)">
                            <span class="nota-archivo">Formatos soportados: JPG o PNG. (Opcional)</span>
                        </div>
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="email" class="label-form-general">Correo Electrónico:</label>
                        <input type="email" id="email" value="${sessionScope.usuario.email}" class="input-form-general" readonly>
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="nif" class="label-form-general">NIF / DNI:</label>
                        <input type="text" id="nif" value="${sessionScope.usuario.nif}" class="input-form-general" readonly>
                    </div>

                    <div class="grupo-form-ancho-completo">
                        <label for="ultimo_acceso" class="label-form-general">Último Acceso Registrado:</label>
                        <fmt:formatDate value="${sessionScope.usuario.ultimoAcceso}" pattern="dd/MM/yyyy - HH:mm:ss" var="fechaAccesoFormateada" />
                        <input type="text" id="ultimo_acceso" value="${not empty sessionScope.usuario.ultimoAcceso ? fechaAccesoFormateada : 'Recién registrado'}" class="input-form-general" readonly>
                    </div>
                    <div class="grupo-form-mitad">
                        <label for="nombre" class="label-form-general">Nombre:</label>
                        <input type="text" id="nombre" name="nombre" value="${sessionScope.usuario.nombre}" class="input-form-general">
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="apellidos" class="label-form-general">Apellidos:</label>
                        <input type="text" id="apellidos" name="apellidos" value="${sessionScope.usuario.apellidos}" class="input-form-general">
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="telefono" class="label-form-general">Teléfono:</label>
                        <input type="text" id="telefono" name="telefono" value="${sessionScope.usuario.telefono}" class="input-form-general">
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="codigo_postal" class="label-form-general">Código Postal:</label>
                        <input type="text" id="codigo_postal" name="codigoPostal" value="${sessionScope.usuario.codigoPostal}" class="input-form-general" maxlength="5">
                    </div>

                    <div class="grupo-form-ancho-completo">
                        <label for="direccion" class="label-form-general">Dirección:</label>
                        <input type="text" id="direccion" name="direccion" value="${sessionScope.usuario.direccion}" class="input-form-general">
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="localidad" class="label-form-general">Localidad:</label>
                        <input type="text" id="localidad" name="localidad" value="${sessionScope.usuario.localidad}" class="input-form-general">
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="provincia" class="label-form-general">Provincia:</label>
                        <input type="text" id="provincia" name="provincia" value="${sessionScope.usuario.provincia}" class="input-form-general">
                    </div>

                    <button type="submit" class="btn-perfil-enviar">Guardar Cambios</button>
                </form>
            </div>

            <div class="contenedor-registro-pass">
                <h2 class="titulo-seccion-perfil">Cambiar Contraseña</h2>

                <c:if test="${not empty mensajeExitoPass}">
                    <div class="perfil-mensaje-exito">${mensajeExitoPass}</div>
                </c:if>
                <c:if test="${not empty mensajeErrorPass}">
                    <div class="error-perfil-centrado">${mensajeErrorPass}</div>
                </c:if>

                <form action="${contexto}/PerfilController" method="post" class="formulario-grid">
                    <input type="hidden" name="accion" value="cambiar_password">

                    <div class="grupo-form-ancho-completo">
                        <label for="pass_actual" class="label-form-general">Contraseña Actual:</label>
                        <input type="password" id="pass_actual" name="pass_actual" class="input-form-general">
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="pass_nueva" class="label-form-general">Nueva Contraseña:</label>
                        <input type="password" id="pass_nueva" name="pass_nueva" class="input-form-general">
                    </div>

                    <div class="grupo-form-mitad">
                        <label for="pass_repetir" class="label-form-general">Repetir Contraseña:</label>
                        <input type="password" id="pass_repetir" name="pass_repetir" class="input-form-general">
                    </div>

                    <button type="submit" class="btn-perfil-enviar" style="background-color: #333;">Actualizar Contraseña</button>
                </form>
            </div>

            <div class="caja-perfil-pedidos">
                <h2 class="titulo-seccion-perfil">Historial de Pedidos</h2>

                <c:choose>
                    <c:when test="${empty misPedidos}">
                        <div class="caja-pedidos-vacia">
                            <span class="icono-pedidos-vacio">inventory_2</span>
                            <p class="texto-pedidos-vacio">Aún no has realizado ningún pedido en nuestra tienda.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="rejilla-tarjetas-pedidos">
                            <c:forEach items="${misPedidos}" var="pedido">

                                <div class="tarjeta-mini-pedido" title="Haz clic para ver el desglose" onclick="abrirModalPedido(${pedido.idPedido})">
                                    <span class="icono-caja-pedido">local_shipping</span>
                                    <div class="textos-tarjeta-pedido">
                                        <span class="titulo-mini-pedido">Pedido #${pedido.idPedido}</span>
                                        <span class="fecha-mini-pedido"><fmt:formatDate value="${pedido.fecha}" pattern="dd/MM/yyyy"/></span>
                                        <span class="importe-mini-pedido"><fmt:formatNumber value="${pedido.importe + pedido.iva}" type="currency" currencySymbol="€"/></span>
                                        <span style="font-size:0.8rem; margin-top:3px; color: ${pedido.estado == 'f' ? 'green' : 'orange'}; font-weight: bold;">
                                            ${pedido.estado == 'f' ? 'Completado' : 'Pendiente'}
                                        </span>
                                    </div>
                                </div>

                                <div id="html-pedido-${pedido.idPedido}" style="display:none;">
                                    <div class="contenedor-tabla-carrito">
                                        <table class="tabla-carrito">
                                            <thead>
                                                <tr>
                                                    <th class="th-izquierda">Artículo</th>
                                                    <th class="th-carrito-central">Precio Unit.</th>
                                                    <th class="th-carrito-central">Unidades</th> 
                                                    <th class="th-carrito-central">Total Línea</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${pedido.lineas}" var="linea">
                                                    <tr>
                                                        <td class="td-izquierda">
                                                            <div class="info-producto-flex">
                                                                <div class="caja-carrito-mini">
                                                                    <c:choose>
                                                                        <c:when test="${linea.producto.imagen == 'default.jpg'}">
                                                                            <img src="${contexto}/imagenes/productos/default.jpg" alt="Sin imagen" class="img-carrito-mini">
                                                                        </c:when>
                                                                        <c:when test="${linea.producto.imagen.startsWith('http')}">
                                                                            <img src="${linea.producto.imagen}" alt="Imagen producto" class="img-carrito-mini">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img src="${contexto}/imagenes/productos/${linea.producto.imagen}.jpg" onerror="this.src='${contexto}/imagenes/productos/default.jpg';" class="img-carrito-mini">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </div>
                                                                <div class="caja-info-articulo">
                                                                    <div class="dato-nombre">${linea.producto.nombre}</div>
                                                                    <div class="dato-id">Ref: #${linea.producto.idProducto}</div>
                                                                </div>
                                                            </div>
                                                        </td>

                                                        <td class="td-carrito-central">
                                                            <fmt:setLocale value="es_ES"/>
                                                            <fmt:formatNumber value="${linea.producto.precio}" type="currency" currencySymbol="€"/>
                                                        </td>

                                                        <td class="td-carrito-central" style="font-weight: bold;">
                                                            x${linea.cantidad}
                                                        </td>

                                                        <td class="td-carrito-central">
                                                            <span class="precio-total-linea">
                                                                <fmt:formatNumber value="${linea.producto.precio * linea.cantidad}" type="currency" currencySymbol="€"/>
                                                            </span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div class="zona-resumen" style="margin-top: 20px; background-color: #f8f9fa;">
                                        <div class="fila-total">
                                            <span class="etiqueta-resumen">Subtotal:</span>
                                            <span class="valor-resumen">
                                                <fmt:formatNumber value="${pedido.importe}" type="currency" currencySymbol="€"/>
                                            </span>
                                        </div>
                                        <div class="fila-total">
                                            <span class="etiqueta-resumen">IVA (21%):</span>
                                            <span class="valor-resumen">
                                                <fmt:formatNumber value="${pedido.iva}" type="currency" currencySymbol="€"/>
                                            </span>
                                        </div>
                                        <div class="fila-total-final" style="border-top: 1px solid #dee2e6; margin-top:10px; padding-top: 10px;">
                                            <span class="etiqueta-final">Total Pagado:</span>
                                            <span class="precio-final">
                                                <fmt:formatNumber value="${pedido.importe + pedido.iva}" type="currency" currencySymbol="€"/>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </main>

        <div id="modal-pedido-id" class="fondo-modal-producto" style="display:none;">
            <div class="caja-modal-pedido">
                <div class="cabecera-modal-producto">
                    <h3 class="titulo-modal-producto" id="modal-pedido-titulo">Detalle del Pedido</h3>
                    <button type="button" class="btn-cerrar-producto" onclick="cerrarModalPedido()">&times;</button>
                </div>
                <div class="cuerpo-modal-pedido" id="modal-pedido-contenido">
                </div>
            </div>
        </div>

        <jsp:include page="../INC/footer.jsp" />

        <script>
            // === LÓGICA DE PREVISUALIZACIÓN DE IMAGEN ADAPTADA DEL PROFESOR ===
            function readURL(input) {
                if (input.files && input.files[0]) {
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        document.getElementById('previa').src = e.target.result;
                    };
                    reader.readAsDataURL(input.files[0]);
                }
            }

            function abrirModalPedido(idPedido) {
                let titulo = document.getElementById('modal-pedido-titulo');
                let contenido = document.getElementById('modal-pedido-contenido');
                let htmlOculto = document.getElementById('html-pedido-' + idPedido);

                if (htmlOculto) {
                    titulo.innerText = "Detalle del Pedido #" + idPedido;
                    contenido.innerHTML = htmlOculto.innerHTML;
                    document.getElementById('modal-pedido-id').style.display = 'flex';
                }
            }

            function cerrarModalPedido() {
                document.getElementById('modal-pedido-id').style.display = 'none';
            }
        </script>
    </body>
</html>