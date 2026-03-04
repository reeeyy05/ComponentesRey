package es.componentesrey.controllers;

import es.componentesrey.DAO.UsuarioDAO;
import es.componentesrey.beans.Usuario;
import es.componentesrey.utils.Utilidades;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Controlador (Servlet) encargado de gestionar el proceso de registro de nuevos
 * usuarios en la plataforma. Recibe peticiones GET para mostrar el formulario y
 * POST para procesarlo.
 *
 * @author arey1
 */
@WebServlet(name = "RegistroController", urlPatterns = {"/RegistroController"})
@MultipartConfig
public class RegistroController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario nuevoUsuario = new Usuario();

        try {
            BeanUtils.populate(nuevoUsuario, request.getParameterMap());

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar los datos del formulario.");
            request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
            return;
        }

        // Recuperamos a mano los que requieren validación especial (contraseñas y NIF separado)
        String password = request.getParameter("password");
        String confirmarPass = request.getParameter("passwordRep");
        String nifNumero = request.getParameter("nif");
        String nifLetra = request.getParameter("letraNif");

        // Validar que no lleguen nulos para evitar el Error 500
        if (password == null || confirmarPass == null) {
            request.setAttribute("error", "Las contraseñas no pueden estar vacías.");
            request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
            return;
        }

        // Validar contraseñas
        if (!password.equals(confirmarPass)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
            return;
        }

        // Encriptar la contraseña y unificar NIF
        nuevoUsuario.setPassword(Utilidades.cifrarMD5(password));
        nuevoUsuario.setNif(nifNumero + nifLetra.toUpperCase());

        // Ponemos avatar por defecto
        nuevoUsuario.setAvatar("perfil_default.jpg");

        // Guardado usando el patrón DAO
        UsuarioDAO dao = new UsuarioDAO();
        boolean exito = dao.registrarUsuario(nuevoUsuario);

        if (exito) {
            Usuario usuarioLogueado = dao.hacerLogin(nuevoUsuario.getEmail(), nuevoUsuario.getPassword());

            if (usuarioLogueado != null) {
                // 1. Actualizamos su fecha de último acceso
                dao.actualizarUltimoAcceso(usuarioLogueado.getIdUsuario());

                // 2. Lo guardamos en la sesión
                HttpSession sesion = request.getSession();
                sesion.setAttribute("usuario", usuarioLogueado);

                // 3. Redirigimos a la página de inicio (tienda)
                response.sendRedirect(request.getContextPath() + "/inicio");
            } else {
                // Fallback de seguridad: si por algo fallase el auto-login, lo mandamos al login manual
                request.setAttribute("mensajeRegistro", "¡Registro completado! Por favor, inicia sesión.");
                request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
            }
        } else {
            // Si falla (Ej: correo duplicado detectado por Base de Datos)
            request.setAttribute("error", "Hubo un error en el registro. Es posible que el email ya exista.");
            request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
