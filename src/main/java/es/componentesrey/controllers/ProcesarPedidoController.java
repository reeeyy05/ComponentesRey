package es.componentesrey.controllers;

import es.componentesrey.DAO.PedidoDAO;
import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.Usuario;
import java.io.IOException;
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
@WebServlet(name = "ProcesarPedidoController", urlPatterns = {"/ProcesarPedidoController"})
public class ProcesarPedidoController extends HttpServlet {

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
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        // 1. Si no está logueado por algún motivo, lo mandamos al login
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/LoginController");
            return;
        }

        List<LineaCarrito> carrito = (List<LineaCarrito>) sesion.getAttribute("carrito");
        Double totalCarrito = (Double) sesion.getAttribute("totalCarrito");
        Double ivaCarrito = (Double) sesion.getAttribute("ivaCarrito");

        // 2. Si el carrito está vacío o nulo, lo mandamos al inicio
        if (carrito == null || carrito.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }

        // 3. Tramitamos el pedido en la Base de Datos
        PedidoDAO pedidoDAO = new PedidoDAO();
        boolean exito = pedidoDAO.tramitarPedido(usuario, carrito, totalCarrito, ivaCarrito);

        if (exito) {
            // 4. Si se ha guardado bien, vaciamos el carrito de la sesión
            sesion.removeAttribute("carrito");
            sesion.removeAttribute("cantidadTotal");
            sesion.removeAttribute("subtotalCarrito");
            sesion.removeAttribute("ivaCarrito");
            sesion.removeAttribute("totalCarrito");

            // 5. Redirigimos al perfil y le mostramos un mensaje de éxito. 
            // Así verá inmediatamente la tarjeta de su nuevo pedido.
            sesion.setAttribute("mensajeExitoDatos", "¡Tu pedido se ha realizado con éxito!");
            response.sendRedirect(request.getContextPath() + "/PerfilController");
        } else {
            // Si hubo un fallo en la BD, lo devolvemos al carrito
            response.sendRedirect(request.getContextPath() + "/CarritoController?error=1");
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
