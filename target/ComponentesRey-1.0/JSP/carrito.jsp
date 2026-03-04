<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mi Cesta</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilos.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
        <script>const CONTEXTO = '${contexto}';</script>
        <script src="${contexto}/JS/carrito.js"></script>
    </head>
    <body class="cuerpo-pagina">

        <jsp:include page="../INC/cabecera.jsp" />

        <div class="contenedor-principal-bloque"> 

            <h1 class="titulo-cesta">
                Cesta de la compra
                <span id="header-cantidad" class="subtexto-cesta">
                    (${sessionScope.cantidadTotal != null ? sessionScope.cantidadTotal : 0} productos)
                </span>
            </h1>

            <c:if test="${empty sessionScope.carrito or sessionScope.carrito.size() == 0}">
                <div class="tarjeta-vacia">
                    <span class="icono-cesta-vacia">shopping_cart_off</span>
                    <h2 class="titulo-gris">Tu cesta está vacía</h2>
                    <p class="texto-pedidos-vacio">¡No dejes escapar nuestras ofertas!</p>
                    <form action="${contexto}/inicio" method="get" class="form-tramitar">
                        <button type="submit" class="boton-oscuro">Ver Productos</button>
                    </form>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.carrito and sessionScope.carrito.size() > 0}">

                <div class="contenedor-tabla-carrito">
                    <table class="tabla-carrito">
                        <thead>
                            <tr>
                                <th class="th-izquierda">Artículo</th>
                                <th class="th-carrito-central">Precio</th>
                                <th class="th-carrito-central">Unidades</th> 
                                <th class="th-carrito-central">Total</th>
                                <th class="th-carrito-central">Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${sessionScope.carrito}" var="linea">
                                <tr id="fila-${linea.producto.idProducto}">
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

                                    <td class="td-carrito-central">
                                        <div class="control-cantidad">
                                            <button type="button" class="btn-qty-restar" data-id="${linea.producto.idProducto}">-</button>
                                            <span class="numero-qty" id="qty-${linea.producto.idProducto}">${linea.cantidad}</span>
                                            <button type="button" class="btn-qty-sumar" data-id="${linea.producto.idProducto}">+</button>
                                        </div>
                                    </td>

                                    <td class="td-carrito-central">
                                        <span class="precio-total-linea" id="linea-total-${linea.producto.idProducto}">
                                            <fmt:formatNumber value="${linea.importeTotal}" type="currency" currencySymbol="€"/>
                                        </span>
                                    </td>

                                    <td class="td-carrito-central">
                                        <button type="button" class="btn-eliminar" data-id="${linea.producto.idProducto}" title="Eliminar artículo">
                                            <span class="icono-eliminar-linea">delete</span>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="zona-resumen">
                    <div class="fila-total">
                        <span class="etiqueta-resumen">Subtotal:</span>
                        <span class="valor-resumen" id="txt-subtotal">
                            <fmt:formatNumber value="${sessionScope.subtotalCarrito}" type="currency" currencySymbol="€"/>
                        </span>
                    </div>

                    <div class="fila-total">
                        <span class="etiqueta-resumen">IVA (21%):</span>
                        <span class="valor-resumen" id="txt-iva">
                            <fmt:formatNumber value="${sessionScope.ivaCarrito}" type="currency" currencySymbol="€"/>
                        </span>
                    </div>

                    <div class="fila-total-final">
                        <span class="etiqueta-final">Total Pedido:</span>
                        <span class="precio-final" id="txt-total">
                            <fmt:formatNumber value="${sessionScope.totalCarrito}" type="currency" currencySymbol="€"/>
                        </span>
                    </div>

                    <div class="acciones-carrito">
                        <a href="${contexto}/inicio" class="enlace-volver">Seguir comprando</a>

                        <c:choose>
                            <c:when test="${empty sessionScope.usuario}">
                                <button type="button" class="boton-tramitar" onclick="window.location.href = '${contexto}/RegistroController'">Realizar Pedido</button>
                            </c:when>
                            <c:otherwise>
                                <form action="${contexto}/ProcesarPedidoController" method="post" class="form-tramitar">
                                    <button type="submit" class="boton-tramitar">Realizar Pedido</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </c:if>
        </div>

        <jsp:include page="../INC/footer.jsp" />
    </body>
</html>