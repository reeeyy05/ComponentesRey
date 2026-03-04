package es.componentesrey.controllers;

import es.componentesrey.DAO.UsuarioDAO;
import es.componentesrey.beans.Usuario;
import es.componentesrey.utils.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author arey1
 */
@WebServlet(name = "LogoutController", urlPatterns = {"/LogoutController"})
public class LogoutController extends HttpServlet {

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
        Utilidades.borrarCookieCarrito(response);

        HttpSession sesionActual = request.getSession(false);

        if (sesionActual != null) {
            Usuario u = (Usuario) sesionActual.getAttribute("usuario");
            if (u != null) {
                UsuarioDAO dao = new UsuarioDAO();
                dao.actualizarUltimoAcceso(u.getIdUsuario());
            }

            // Luego destruimos la sesión
            sesionActual.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/inicio");
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
        doGet(request, response);
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
