package es.componentesrey.controllers;

import es.componentesrey.DAO.PedidoDAO;
import es.componentesrey.DAO.UsuarioDAO;
import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.Usuario;
import es.componentesrey.utils.Utilidades;
import java.io.IOException;
import java.util.Date;
import java.util.List;
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
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

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
        request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
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
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.hacerLogin(email, Utilidades.cifrarMD5(password));

        if (usuario != null) {
            HttpSession sesion = request.getSession();

            dao.actualizarUltimoAcceso(usuario.getIdUsuario());
            usuario.setUltimoAcceso(new Date());

            sesion.setAttribute("usuario", usuario);

            List<LineaCarrito> carritoSesion = (List<LineaCarrito>) sesion.getAttribute("carrito");
            PedidoDAO pdao = new PedidoDAO();

            if (carritoSesion != null && !carritoSesion.isEmpty()) {
                int cantidadTotal = 0;
                double subtotal = 0;
                for (LineaCarrito linea : carritoSesion) {
                    cantidadTotal += linea.getCantidad();
                    subtotal += (linea.getProducto().getPrecio() * linea.getCantidad());
                }
                double iva = subtotal * 0.21;
                double total = subtotal + iva;

                sesion.setAttribute("cantidadTotal", cantidadTotal);
                sesion.setAttribute("subtotalCarrito", subtotal);
                sesion.setAttribute("ivaCarrito", iva);
                sesion.setAttribute("totalCarrito", total);

                pdao.guardarCarritoBD(usuario, carritoSesion, total, iva);
                Utilidades.borrarCookieCarrito(response);
            } else {
                List<LineaCarrito> carritoGuardado = pdao.getCarritoBD(usuario.getIdUsuario());

                if (carritoGuardado != null && !carritoGuardado.isEmpty()) {
                    sesion.setAttribute("carrito", carritoGuardado);

                    int cantidadTotal = 0;
                    double subtotal = 0;
                    for (LineaCarrito linea : carritoGuardado) {
                        cantidadTotal += linea.getCantidad();
                        subtotal += (linea.getProducto().getPrecio() * linea.getCantidad());
                    }
                    double iva = subtotal * 0.21;
                    double total = subtotal + iva;

                    sesion.setAttribute("cantidadTotal", cantidadTotal);
                    sesion.setAttribute("subtotalCarrito", subtotal);
                    sesion.setAttribute("ivaCarrito", iva);
                    sesion.setAttribute("totalCarrito", total);
                } else {
                    sesion.setAttribute("cantidadTotal", 0);
                    sesion.removeAttribute("subtotalCarrito");
                    sesion.removeAttribute("ivaCarrito");
                    sesion.removeAttribute("totalCarrito");
                }
            }

            response.sendRedirect(request.getContextPath() + "/inicio");
        } else {
            request.setAttribute("error", "Email o contraseña incorrectos");
            request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
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
