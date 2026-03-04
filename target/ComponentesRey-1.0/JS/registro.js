document.addEventListener("DOMContentLoaded", function () {
    const formularioRegistro = document.getElementById("formRegistro");

    // Función para mostrar (rojo) u ocultar (verde) los errores visuales
    function gestionarEstadoError(idDelCampo, hayError, mensajeDeError) {
        const campoInput = document.getElementById(idDelCampo);
        const spanMensaje = document.getElementById("err-" + idDelCampo);

        if (hayError) {
            campoInput.classList.add("input-error");
            campoInput.classList.remove("input-exito");
            spanMensaje.innerText = mensajeDeError;
            spanMensaje.style.display = "block";
        } else {
            campoInput.classList.remove("input-error");
            if (campoInput.value.trim() !== "")
                campoInput.classList.add("input-exito");
            spanMensaje.style.display = "none";
        }
    }

    // COMPROBAR EMAIL
    document.getElementById("email").addEventListener("blur", async function () {
        const emailIntroducido = this.value.trim();
        const formatoCorrectoEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (emailIntroducido === "") {
            gestionarEstadoError("email", true, "El email es obligatorio.");
        } else if (!formatoCorrectoEmail.test(emailIntroducido)) {
            gestionarEstadoError("email", true, "Formato de email incorrecto.");
        } else {

            // Preparamos los datos asíncronos
            const parametrosAjax = new URLSearchParams();
            parametrosAjax.append("accion", "comprobarEmail");
            parametrosAjax.append("email", emailIntroducido);

            // Realizamos la petición silenciosa al servidor
            let respuestaFetch = await fetch(CONTEXTO + "/RegistroAjaxController", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
                },
                body: parametrosAjax.toString()
            });

            // Convertimos la respuesta cruda en un objeto JSON manejable
            let datosJson = await respuestaFetch.json();

            if (datosJson.tipo === "warning") {
                gestionarEstadoError("email", true, datosJson.mensaje); // Correo ya ocupado en BBDD
            } else {
                gestionarEstadoError("email", false, ""); // Correo libre
            }
        }
    });

    // VALIDAR LAS 2 CONTRASEÑAS
    document.getElementById("password").addEventListener("blur", function () {
        const contrasenaPrincipal = this.value.trim();

        if (contrasenaPrincipal === "")
            gestionarEstadoError("password", true, "La contraseña es obligatoria.");
        else if (contrasenaPrincipal.length < 6)
            gestionarEstadoError("password", true, "Mínimo 6 caracteres.");
        else
            gestionarEstadoError("password", false, "");

        // Si ya hay algo en "repetir contraseña", disparamos su evento para que se reevalúe
        const campoRepetir = document.getElementById("passwordRep");
        if (campoRepetir.value !== "")
            campoRepetir.dispatchEvent(new Event('blur'));
    });

    document.getElementById("passwordRep").addEventListener("blur", function () {
        const contrasenaPrincipal = document.getElementById("password").value;
        const contrasenaRepetida = this.value.trim();

        if (contrasenaRepetida === "")
            gestionarEstadoError("passwordRep", true, "Debes repetir la contraseña.");
        else if (contrasenaRepetida !== contrasenaPrincipal)
            gestionarEstadoError("passwordRep", true, "Las contraseñas no coinciden.");
        else
            gestionarEstadoError("passwordRep", false, "");
    });

    // NIF
    const campoNifNumeros = document.getElementById("nif");
    const campoNifLetra = document.getElementById("letraNif");

    campoNifNumeros.addEventListener("input", async function () {
        this.value = this.value.replace(/[^0-9]/g, ''); // Evitamos que el usuario teclee letras visualmente
        let numerosNifIntroducidos = this.value;

        // Si ya hay 8 números, disparamos el AJAX instantáneamente
        if (numerosNifIntroducidos.length === 8) {

            const parametrosAjax = new URLSearchParams();
            parametrosAjax.append("accion", "calcularNif");
            parametrosAjax.append("nif", numerosNifIntroducidos);

            let respuestaFetch = await fetch(CONTEXTO + "/RegistroAjaxController", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
                },
                body: parametrosAjax.toString()
            });

            let datosJson = await respuestaFetch.json();

            if (datosJson.tipo === 'warning') {
                gestionarEstadoError("nif", true, datosJson.letra); // Muestra el error
                campoNifLetra.value = "";
                campoNifLetra.classList.remove("input-exito");
            } else {
                campoNifLetra.value = datosJson.letra; // Pone la letra calculada por Java
                gestionarEstadoError("nif", false, "");
                campoNifLetra.classList.add("input-exito");
            }

        } else {
            // Mientras no haya 8 números, mantenemos la letra vacía
            campoNifLetra.value = "";
            campoNifLetra.classList.remove("input-exito");
        }
    });

    // Validamos al salir del campo (blur) por si lo deja a medias o vacío
    campoNifNumeros.addEventListener("blur", function () {
        let longitud = this.value.length;
        if (longitud > 0 && longitud < 8) {
            gestionarEstadoError("nif", true, "Debe tener exactamente 8 números.");
        } else if (longitud === 0) {
            gestionarEstadoError("nif", true, "El NIF es obligatorio.");
        }
    });

    // EL RESTO DE CAMPOS DE TEXTO OBLIGATORIOS
    const arrayCamposRequeridos = ["nombre", "apellidos", "direccion", "localidad", "provincia"];
    arrayCamposRequeridos.forEach(function (idDelCampo) {
        document.getElementById(idDelCampo).addEventListener("blur", function () {
            if (this.value.trim() === "")
                gestionarEstadoError(idDelCampo, true, "Este campo es obligatorio.");
            else
                gestionarEstadoError(idDelCampo, false, "");
        });
    });

    // CÓDIGO POSTAL Y TELÉFONO
    document.getElementById("codigoPostal").addEventListener("blur", function () {
        const cpIntroducido = this.value.trim();
        const formatoCorrectoCP = /^\d{5}$/; // Expresión regular: Exactamente 5 dígitos

        if (cpIntroducido === "")
            gestionarEstadoError("codigoPostal", true, "El código postal es obligatorio.");
        else if (!formatoCorrectoCP.test(cpIntroducido))
            gestionarEstadoError("codigoPostal", true, "Debe tener exactamente 5 números.");
        else
            gestionarEstadoError("codigoPostal", false, "");
    });

    document.getElementById("telefono").addEventListener("blur", function () {
        const telefonoIntroducido = this.value.trim();
        const formatoCorrectoTelefono = /^[679]\d{8}$/;

        if (telefonoIntroducido !== "" && !formatoCorrectoTelefono.test(telefonoIntroducido)) {
            gestionarEstadoError("telefono", true, "Debe tener 9 números y empezar por 6, 7 o 9.");
        } else {
            gestionarEstadoError("telefono", false, "");
        }
    });

    // === PREVISUALIZACIÓN DEL AVATAR (Lógica del profesor adaptada a Vanilla JS) ===
    const campoAvatar = document.getElementById("avatar");
    if (campoAvatar) {
        campoAvatar.addEventListener("change", function () {
            if (this.files && this.files[0]) {
                let reader = new FileReader();
                reader.onload = function (e) {
                    const imagenPrevia = document.getElementById("previa");
                    if (imagenPrevia) {
                        imagenPrevia.src = e.target.result;
                    }
                };
                reader.readAsDataURL(this.files[0]);
            }
        });
    }

    // VALIDACIÓN GENERAL ANTES DE ENVIAR (SUBMIT)
    formularioRegistro.addEventListener("submit", function (eventoSubmit) {

        // Disparamos el evento blur artificialmente en todos los inputs para forzar que salgan los mensajes de error
        const todosLosInputsTexto = formularioRegistro.querySelectorAll("input[type=text], input[type=password]");
        todosLosInputsTexto.forEach(inputIndividual => inputIndividual.dispatchEvent(new Event("blur")));

        // Validamos físicamente el peso del archivo de imagen
        const tamanoMaximoPermitido = 102400; // 100 KB

        if (campoAvatar.files.length > 0 && campoAvatar.files[0].size > tamanoMaximoPermitido) {
            gestionarEstadoError("avatar", true, "La imagen sobrepasa los 100KB.");
        } else {
            gestionarEstadoError("avatar", false, "");
        }

        // Si existe algún campo en la página que contenga la clase .input-error, detenemos el envío
        const camposConErrores = formularioRegistro.querySelectorAll(".input-error");
        if (camposConErrores.length > 0) {
            eventoSubmit.preventDefault(); // Corta en seco el envío hacia el Servlet de Java
            camposConErrores[0].focus(); // Hace focus visualmente en el primer error que encuentra
        }
    });
});