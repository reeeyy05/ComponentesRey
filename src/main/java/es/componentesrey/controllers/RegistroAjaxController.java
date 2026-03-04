package es.componentesrey.controllers;

import es.componentesrey.DAO.UsuarioDAO;
import es.componentesrey.utils.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author arey1
 */
@WebServlet(name = "RegistroAjaxController", urlPatterns = {"/RegistroAjaxController"})
public class RegistroAjaxController extends HttpServlet {

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String accion = request.getParameter("accion");
        JSONObject objeto = new JSONObject();

        if (accion != null) {
            switch (accion) {
                case "comprobarEmail":
                    String email = request.getParameter("email");
                    UsuarioDAO dao = new UsuarioDAO();
                    if (dao.comprobarEmail(email)) {
                        objeto.put("tipo", "warning");
                        objeto.put("mensaje", "El email ya está registrado.");
                    } else {
                        objeto.put("tipo", "success");
                        objeto.put("mensaje", "Email disponible.");
                    }
                    break;

                case "calcularNif":
                    try {
                    String nifStr = request.getParameter("nif");
                    int numero = Integer.parseInt(nifStr); // Solo se hace para comprobar que no lleve letras

                    if (numero != 0 && nifStr.length() == 8) {
                        String letraCalculada = String.valueOf(Utilidades.calcularLetraDNI(nifStr));
                        objeto.put("tipo", "success");
                        objeto.put("letra", letraCalculada);
                    } else {
                        objeto.put("tipo", "warning");
                        objeto.put("letra", "El formato del NIF es incorrecto.");
                    }
                } catch (NumberFormatException e) {
                    objeto.put("tipo", "warning");
                    objeto.put("letra", "El DNI tiene que ser un número.");
                }
                break;
            }
        }

        out.print(objeto.toString());
        out.flush();
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
