document.addEventListener("DOMContentLoaded", function () {
    const formPerfil = document.getElementById("formPerfil");
    const formPassword = document.getElementById("formPassword");

    // Función para mostrar/ocultar los errores visuales
    function gestionarEstadoError(idDelCampo, hayError, mensajeDeError) {
        const campoInput = document.getElementById(idDelCampo);
        const spanMensaje = document.getElementById("err-" + idDelCampo);

        if (!campoInput || !spanMensaje) return; // Evita errores si no encuentra el campo

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

    // EL RESTO DE CAMPOS DE TEXTO OBLIGATORIOS (Datos Personales)
    const arrayCamposRequeridos = ["nombre", "apellidos", "direccion", "localidad", "provincia"];
    arrayCamposRequeridos.forEach(function (idDelCampo) {
        let campo = document.getElementById(idDelCampo);
        if (campo) {
            campo.addEventListener("blur", function () {
                if (this.value.trim() === "")
                    gestionarEstadoError(idDelCampo, true, "Este campo es obligatorio.");
                else
                    gestionarEstadoError(idDelCampo, false, "");
            });
        }
    });

    // CÓDIGO POSTAL Y TELÉFONO
    const campoCP = document.getElementById("codigo_postal");
    if (campoCP) {
        campoCP.addEventListener("blur", function () {
            const cpIntroducido = this.value.trim();
            const formatoCorrectoCP = /^\d{5}$/;

            if (cpIntroducido === "")
                gestionarEstadoError("codigo_postal", true, "El código postal es obligatorio.");
            else if (!formatoCorrectoCP.test(cpIntroducido))
                gestionarEstadoError("codigo_postal", true, "Debe tener exactamente 5 números.");
            else
                gestionarEstadoError("codigo_postal", false, "");
        });
    }

    const campoTlf = document.getElementById("telefono");
    if (campoTlf) {
        campoTlf.addEventListener("blur", function () {
            const telefonoIntroducido = this.value.trim();
            const formatoCorrectoTelefono = /^[679]\d{8}$/;

            if (telefonoIntroducido !== "" && !formatoCorrectoTelefono.test(telefonoIntroducido)) {
                gestionarEstadoError("telefono", true, "Debe tener 9 números y empezar por 6, 7 o 9.");
            } else {
                gestionarEstadoError("telefono", false, "");
            }
        });
    }

    // VALIDAR PESO DEL AVATAR
    const campoAvatar = document.getElementById("nuevo_avatar");
    if (campoAvatar) {
        campoAvatar.addEventListener("change", function () {
            const tamanoMaximoPermitido = 102400; // 100 KB
            if (this.files.length > 0 && this.files[0].size > tamanoMaximoPermitido) {
                gestionarEstadoError("nuevo_avatar", true, "La imagen sobrepasa los 100KB permitidos.");
            } else {
                gestionarEstadoError("nuevo_avatar", false, "");
            }
        });
    }

    // VALIDACIÓN DEL FORMULARIO DE CAMBIO DE CONTRASEÑA
    const passActual = document.getElementById("pass_actual");
    const passNueva = document.getElementById("pass_nueva");
    const passRepetir = document.getElementById("pass_repetir");

    if (passActual) {
        passActual.addEventListener("blur", function () {
            if (this.value.trim() === "") gestionarEstadoError("pass_actual", true, "Requerido.");
            else gestionarEstadoError("pass_actual", false, "");
        });
    }

    if (passNueva) {
        passNueva.addEventListener("blur", function () {
            const val = this.value.trim();
            if (val === "") gestionarEstadoError("pass_nueva", true, "Debes introducir una nueva contraseña.");
            else if (val.length < 6) gestionarEstadoError("pass_nueva", true, "Mínimo 6 caracteres.");
            else gestionarEstadoError("pass_nueva", false, "");

            if (passRepetir && passRepetir.value !== "") passRepetir.dispatchEvent(new Event('blur'));
        });
    }

    if (passRepetir) {
        passRepetir.addEventListener("blur", function () {
            const valNueva = passNueva.value.trim();
            const valRep = this.value.trim();
            if (valRep === "") gestionarEstadoError("pass_repetir", true, "Debes repetir la contraseña.");
            else if (valRep !== valNueva) gestionarEstadoError("pass_repetir", true, "Las contraseñas no coinciden.");
            else gestionarEstadoError("pass_repetir", false, "");
        });
    }

    // SUBMIT: DATOS PERSONALES
    if (formPerfil) {
        formPerfil.addEventListener("submit", function (eventoSubmit) {
            const inputsTexto = formPerfil.querySelectorAll("input[type=text]:not([readonly])");
            inputsTexto.forEach(input => input.dispatchEvent(new Event("blur")));
            if (campoAvatar) campoAvatar.dispatchEvent(new Event("change"));

            const errores = formPerfil.querySelectorAll(".input-error");
            if (errores.length > 0) {
                eventoSubmit.preventDefault();
                errores[0].focus();
            }
        });
    }

    // SUBMIT: CAMBIO DE CONTRASEÑA
    if (formPassword) {
        formPassword.addEventListener("submit", function (eventoSubmit) {
            const inputsPass = formPassword.querySelectorAll("input[type=password]");
            inputsPass.forEach(input => input.dispatchEvent(new Event("blur")));

            const errores = formPassword.querySelectorAll(".input-error");
            if (errores.length > 0) {
                eventoSubmit.preventDefault();
                errores[0].focus();
            }
        });
    }
});