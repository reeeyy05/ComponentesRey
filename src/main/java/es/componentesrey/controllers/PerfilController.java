package es.componentesrey.controllers;

import es.componentesrey.DAO.PedidoDAO;
import es.componentesrey.DAO.UsuarioDAO;
import es.componentesrey.beans.Usuario;
import es.componentesrey.utils.Utilidades;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author arey1
 */
@WebServlet(name = "PerfilController", urlPatterns = {"/PerfilController"})
public class PerfilController extends HttpServlet {

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
        HttpSession sesion = request.getSession();
        Usuario u = (Usuario) sesion.getAttribute("usuario");

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/LoginController");
            return;
        }

        // Cargar los pedidos del usuario para mostrarlos en el perfil
        PedidoDAO pdao = new PedidoDAO();
        request.setAttribute("misPedidos", pdao.getPedidosPorUsuario(u.getIdUsuario()));

        request.getRequestDispatcher("/JSP/perfil.jsp").forward(request, response);
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
        HttpSession sesion = request.getSession();
        Usuario usuarioLogueado = (Usuario) sesion.getAttribute("usuario");

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/LoginController");
            return;
        }

        String accion = request.getParameter("accion");
        UsuarioDAO dao = new UsuarioDAO();
        PedidoDAO pdao = new PedidoDAO();

        if ("actualizar_datos".equals(accion)) {

            // 1. Cargamos los campos de texto con BeanUtils
            try {
                BeanUtils.populate(usuarioLogueado, request.getParameterMap());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                request.setAttribute("mensajeErrorDatos", "Error al procesar el formulario.");
                request.setAttribute("misPedidos", pdao.getPedidosPorUsuario(usuarioLogueado.getIdUsuario()));
                request.getRequestDispatcher("/JSP/perfil.jsp").forward(request, response);
                return;
            }

            // 2. Gestionamos la foto si el usuario ha subido una
            Part filePart = request.getPart("nuevo_avatar");
            if (filePart != null && filePart.getSize() > 0) {

                String fileName = Utilidades.extractFileName(filePart);

                if (fileName != null && !fileName.isEmpty()) {

                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i);
                    }

                    String nuevoNombre = "avatar_" + usuarioLogueado.getIdUsuario() + "_" + System.currentTimeMillis() + extension;
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "imagenes" + File.separator + "avatares";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                    filePart.write(uploadPath + File.separator + nuevoNombre);

                    // Borramos la imagen antigua para no saturar el servidor
                    if (usuarioLogueado.getAvatar() != null && !usuarioLogueado.getAvatar().equals("perfil_default.jpg")) {
                        File oldFile = new File(uploadPath + File.separator + usuarioLogueado.getAvatar());
                        if (oldFile.exists()) {
                            oldFile.delete();
                        }
                    }

                    // Se lo asignamos al objeto Java
                    usuarioLogueado.setAvatar(nuevoNombre);

                    // ¡CLAVE! Ordenamos a la Base de Datos que cambie explícitamente el avatar
                    dao.actualizarAvatarUsuario(usuarioLogueado.getIdUsuario(), nuevoNombre);
                }
            }

            // 3. Usamos el método correcto de tu DAO para el resto de textos
            boolean exito = dao.actualizarDatosPersonales(usuarioLogueado);
            if (exito) {
                sesion.setAttribute("usuario", usuarioLogueado);
                request.setAttribute("mensajeExitoDatos", "¡Datos actualizados correctamente!");
            } else {
                request.setAttribute("mensajeErrorDatos", "Ocurrió un error al actualizar los datos.");
            }

        } else if ("cambiar_password".equals(accion)) {
            String passActual = request.getParameter("pass_actual");
            String passNueva = request.getParameter("pass_nueva");
            String passRepetir = request.getParameter("pass_repetir");

            String passActualMD5 = Utilidades.cifrarMD5(passActual);

            if (!passActualMD5.equals(usuarioLogueado.getPassword())) {
                request.setAttribute("mensajeErrorPass", "La contraseña actual es incorrecta.");
            } else if (!passNueva.equals(passRepetir)) {
                request.setAttribute("mensajeErrorPass", "Las nuevas contraseñas no coinciden.");
            } else {
                String nuevaPassMD5 = Utilidades.cifrarMD5(passNueva);
                boolean exito = dao.cambiarPassword(usuarioLogueado.getIdUsuario(), nuevaPassMD5);
                if (exito) {
                    usuarioLogueado.setPassword(nuevaPassMD5);
                    sesion.setAttribute("usuario", usuarioLogueado);
                    request.setAttribute("mensajeExitoPass", "¡Contraseña cambiada con éxito!");
                } else {
                    request.setAttribute("mensajeErrorPass", "Error en la base de datos.");
                }
            }
        }

        request.setAttribute("misPedidos", pdao.getPedidosPorUsuario(usuarioLogueado.getIdUsuario()));
        request.getRequestDispatcher("/JSP/perfil.jsp").forward(request, response);
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
