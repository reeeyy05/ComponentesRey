<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="request"/>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Componentes Rey</title>
        <link rel="stylesheet" href="${contexto}/CSS/estilos.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    </head>
    
    <body>
        <jsp:include page="INC/cabecera.jsp" />
        <div class="contenedor-principal">

            <aside class="barra-lateral">
                <h3>Precio</h3>
                <div class="caja-filtro-precio">
                    <form action="${contexto}/inicio" method="post">
                        <input type="hidden" name="accion" value="filtrarPrecio">
                        <input type="range" name="filtroPrecio" 
                               min="${applicationScope.precioMin}" 
                               max="${applicationScope.precioMax}" 
                               value="${precioSeleccionado != null ? precioSeleccionado : applicationScope.precioMax}"
                               class="slider-rango" 
                               onchange="this.form.submit()" 
                               title="Desliza para ajustar el precio máximo">

                        <div class="etiquetas-rango-precio">
                            <span>
                                <fmt:setLocale value="es_ES"/>
                                <fmt:formatNumber value="${applicationScope.precioMin}" type="currency" currencySymbol="€"/>
                            </span>
                            <span>
                                <fmt:formatNumber value="${applicationScope.precioMax}" type="currency" currencySymbol="€"/>
                            </span>
                        </div>
                    </form>
                </div>

                <h3>Marcas</h3>
                <ul class="lista-categorias"> 
                    <li>
                        <form action="${contexto}/inicio" method="post">
                            <button type="submit" class="btn-link-filtro">Ver Todas</button>
                        </form>
                    </li>
                    <c:forEach items="${applicationScope.marcas}" var="marca">
                        <li>
                            <form action="${contexto}/inicio" method="post">
                                <input type="hidden" name="nombreMarca" value="${marca}">
                                <input type="hidden" name="accion" value="filtrarMarca">
                                <button type="submit" class="btn-link-filtro">${marca}</button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>

                <h3 class="titulo-espaciado">Categorías</h3>
                <ul class="lista-categorias"> 
                    <li>
                        <form action="${contexto}/inicio" method="post">
                            <button type="submit" class="btn-link-filtro">Ver Todas</button>
                        </form>
                    </li>
                    <c:forEach items="${applicationScope.categorias}" var="cat">
                        <li>
                            <form action="${contexto}/inicio" method="post">
                                <input type="hidden" name="idCategoria" value="${cat.idCategoria}">
                                <input type="hidden" name="accion" value="filtrarCategoria">
                                <button type="submit" class="btn-link-filtro">${cat.nombre}</button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>
            </aside>

            <main class="contenedor-tienda">

                <c:if test="${empty productos}">
                    <div class="tarjeta-vacia">
                        <span class="icono-search-vacio">search_off</span>
                        <h2 class="titulo-gris">No hay productos</h2>
                        <p>No existen artículos en esta categoría actualmente.</p>
                        <form action="${contexto}/inicio" method="post">
                            <button type="submit" class="boton-oscuro">Ver todos los productos</button>
                        </form>
                    </div>
                </c:if>

                <div class="rejilla-productos">
                    <c:forEach items="${productos}" var="producto">
                        <c:set var="urlImagen" value="" />
                        <c:choose>
                            <c:when test="${producto.imagen == 'default.jpg'}">
                                <c:set var="urlImagen" value="${contexto}/imagenes/productos/default.jpg" />
                            </c:when>
                            <c:when test="${producto.imagen.startsWith('http')}">
                                <c:set var="urlImagen" value="${producto.imagen}" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="urlImagen" value="${contexto}/imagenes/productos/${producto.imagen}.jpg" />
                            </c:otherwise>
                        </c:choose>

                        <div class="tarjeta-producto" 
                             onclick="abrirModalProducto(this)"
                             data-id="${producto.idProducto}"
                             data-nombre="<c:out value='${producto.nombre}' escapeXml='true'/>"
                             data-desc="<c:out value='${producto.descripcion}' escapeXml='true'/>"
                             data-precio="${producto.precio}"
                             data-img="${urlImagen}"
                             title="Ver detalles del producto">

                            <div class="caja-imagen-modal">
                                <img src="${urlImagen}" alt="Producto" onerror="this.src='${contexto}/imagenes/productos/default.jpg';">
                            </div>

                            <div class="cuerpo-tarjeta">
                                <div class="nombre-producto" title="${producto.nombre}">${producto.nombre}</div>
                                <div class="precio-producto">
                                    <fmt:setLocale value="es_ES"/>
                                    <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€"/>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </main>
        </div>

        <div id="modal-producto-id" class="fondo-modal-producto">
            <div class="caja-modal-producto">
                <div class="cabecera-modal-producto">
                    <h3 class="titulo-modal-producto" id="modal-prod-nombre"></h3>
                    <button type="button" class="btn-cerrar-producto" onclick="cerrarModalProducto()">&times;</button>
                </div>
                <div class="cuerpo-modal-producto">
                    <div class="caja-img-producto">
                        <img id="modal-prod-img" src="" alt="Producto" class="img-modal-producto">
                    </div>
                    <div class="info-texto-producto">
                        <p class="desc-modal-producto" id="modal-prod-desc"></p>
                        <div class="precio-modal-producto" id="modal-prod-precio"></div>

                        <div class="form-modal-comprar">
                            <input type="hidden" id="modal-prod-id" value="">
                            <button type="button" class="boton-comprar-modal" onclick="anadirAlCarritoAjax(this)">
                                Añadir a la cesta <span class="icono-btn-carrito">add_shopping_cart</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="INC/footer.jsp" />

        <script>
            const formateadorPrecio = new Intl.NumberFormat('es-ES', {style: 'currency', currency: 'EUR'});

            function abrirModalProducto(elemento) {
                let id = elemento.getAttribute('data-id');
                let nombre = elemento.getAttribute('data-nombre');
                let desc = elemento.getAttribute('data-desc');
                let precio = parseFloat(elemento.getAttribute('data-precio'));
                let imgUrl = elemento.getAttribute('data-img');

                document.getElementById('modal-prod-id').value = id;
                document.getElementById('modal-prod-nombre').innerText = nombre;

                if (!desc || desc.trim() === "" || desc === "null") {
                    document.getElementById('modal-prod-desc').innerText = "Este producto no tiene una descripción detallada en este momento.";
                } else {
                    document.getElementById('modal-prod-desc').innerText = desc;
                }

                document.getElementById('modal-prod-precio').innerText = formateadorPrecio.format(precio);
                document.getElementById('modal-prod-img').src = imgUrl;

                document.getElementById('modal-producto-id').style.display = 'flex';
            }

            function cerrarModalProducto() {
                document.getElementById('modal-producto-id').style.display = 'none';
            }

            async function anadirAlCarritoAjax(botonElemento) {
                let idProd = document.getElementById('modal-prod-id').value;
                const parametrosAjax = new URLSearchParams();
                parametrosAjax.append("accion", "anadir");
                parametrosAjax.append("id", idProd);

                try {
                    let respuestaFetch = await fetch('${contexto}/CarritoAjaxController', {
                        method: "POST",
                        credentials: "same-origin",
                        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                        body: parametrosAjax.toString()
                    });

                    let datosJson = await respuestaFetch.json();

                    if (!datosJson.error) {
                        let contadorCabecera = document.querySelector(".boton-carrito-header span:last-child");
                        if (contadorCabecera) {
                            contadorCabecera.innerText = "[" + datosJson.cantidadTotal + "]";
                        }

                        let textoOriginal = botonElemento.innerHTML;
                        botonElemento.innerHTML = '¡Añadido! <span class="icono-btn-carrito">check_circle</span>';
                        botonElemento.style.backgroundColor = "#00C950";

                        setTimeout(() => {
                            botonElemento.innerHTML = textoOriginal;
                            botonElemento.style.backgroundColor = "";
                        }, 1500);
                    }
                } catch (error) {
                    console.error("Error crítico de conexión AJAX:", error);
                }
            }
        </script>
    </body>
</html>