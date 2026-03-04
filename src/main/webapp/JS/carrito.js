document.addEventListener("DOMContentLoaded", () => {

    const formateadorEuros = new Intl.NumberFormat('es-ES', {style: 'currency', currency: 'EUR'});

    const botonesAccion = document.querySelectorAll(".btn-qty-sumar, .btn-qty-restar, .btn-eliminar");

    botonesAccion.forEach(boton => {
        boton.addEventListener("click", async function (e) {
            e.preventDefault();

            // LA CLAVE: Captura el botón contenedor y nunca el icono interior
            const botonApretado = e.currentTarget;

            const idProd = botonApretado.getAttribute("data-id");
            let accionSeleccionada = "";

            if (botonApretado.classList.contains("btn-qty-sumar")) {
                accionSeleccionada = "sumar";
            } else if (botonApretado.classList.contains("btn-qty-restar")) {
                accionSeleccionada = "restar";
            } else if (botonApretado.classList.contains("btn-eliminar")) {
                accionSeleccionada = "eliminar";
            }

            if (!accionSeleccionada || !idProd) {
                console.error("Error JS: Faltan parámetros para AJAX.");
                return;
            }

            const parametrosAjax = new URLSearchParams();
            parametrosAjax.append("accion", accionSeleccionada);
            parametrosAjax.append("id", idProd);

            try {
                let respuestaFetch = await fetch(CONTEXTO + "/CarritoAjaxController", {
                    method: "POST",
                    credentials: "same-origin", // PERMITE GUARDAR/LEER LA COOKIE
                    headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                    body: parametrosAjax.toString()
                });

                let datosJson = await respuestaFetch.json();

                if (!datosJson.error) {
                    if (datosJson.itemEliminado) {
                        let fila = document.getElementById("fila-" + idProd);
                        if (fila)
                            fila.remove();

                        if (datosJson.cantidadTotal === 0) {
                            window.location.href = CONTEXTO + "/CarritoController";
                            return;
                        }
                    } else {
                        let qtyEl = document.getElementById("qty-" + idProd);
                        let totalEl = document.getElementById("linea-total-" + idProd);
                        if (qtyEl)
                            qtyEl.innerText = datosJson.itemCantidad;
                        if (totalEl)
                            totalEl.innerText = formateadorEuros.format(datosJson.itemImporteTotal);
                    }

                    let subtotalEl = document.getElementById("txt-subtotal");
                    let ivaEl = document.getElementById("txt-iva");
                    let totalPedidoEl = document.getElementById("txt-total");

                    if (subtotalEl)
                        subtotalEl.innerText = formateadorEuros.format(datosJson.subtotal);
                    if (ivaEl)
                        ivaEl.innerText = formateadorEuros.format(datosJson.iva);
                    if (totalPedidoEl)
                        totalPedidoEl.innerText = formateadorEuros.format(datosJson.total);

                    let tituloCantidad = document.getElementById("header-cantidad");
                    if (tituloCantidad) {
                        tituloCantidad.innerText = "(" + datosJson.cantidadTotal + " productos)";
                    }

                    let contadorCabecera = document.querySelector(".texto-cantidad-header");
                    if (contadorCabecera) {
                        contadorCabecera.innerText = "[" + datosJson.cantidadTotal + "]";
                    }
                } else {
                    console.error("El servidor devolvió un error JSON:", datosJson.mensaje);
                }
            } catch (error) {
                console.error("Error crítico de conexión AJAX:", error);
            }
        });
    });
});