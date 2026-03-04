package es.componentesrey.controllers;

import es.componentesrey.DAO.IProductoDAO;
import es.componentesrey.DAO.PedidoDAO;
import es.componentesrey.DAO.ProductoDAO;
import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.Producto;
import es.componentesrey.beans.Usuario;
import es.componentesrey.utils.Utilidades;
import java.io.IOException;
import java.util.ArrayList;
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
@WebServlet(name = "CarritoController", urlPatterns = {"/CarritoController"})
public class CarritoController extends HttpServlet {

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
        List<LineaCarrito> carrito = (List<LineaCarrito>) sesion.getAttribute("carrito");

        if (carrito == null && sesion.getAttribute("usuario") == null) {
            carrito = Utilidades.leerCarritoDeCookie(request);
            if (carrito != null) {
                sesion.setAttribute("carrito", carrito);
            }
        }

        if (carrito == null) {
            carrito = new ArrayList<>();
            sesion.setAttribute("carrito", carrito);
        }

        int cantidadTotal = 0;
        double subtotal = 0;
        for (LineaCarrito linea : carrito) {
            cantidadTotal += linea.getCantidad();
            subtotal += (linea.getProducto().getPrecio() * linea.getCantidad());
        }
        double iva = subtotal * 0.21;
        double total = subtotal + iva;

        sesion.setAttribute("cantidadTotal", cantidadTotal);
        sesion.setAttribute("subtotalCarrito", subtotal);
        sesion.setAttribute("ivaCarrito", iva);
        sesion.setAttribute("totalCarrito", total);

        request.getRequestDispatcher("/JSP/carrito.jsp").forward(request, response);
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
        String accion = request.getParameter("accion");
        HttpSession sesion = request.getSession();
        List<LineaCarrito> carrito = (List<LineaCarrito>) sesion.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        if ("anadir".equals(accion)) {
            int idProducto = Integer.parseInt(request.getParameter("id"));
            boolean existe = false;

            for (LineaCarrito linea : carrito) {
                if (linea.getProducto().getIdProducto() == idProducto) {
                    linea.setCantidad((short) (linea.getCantidad() + 1));
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                IProductoDAO dao = new ProductoDAO();
                Producto p = dao.getProductoPorId(idProducto);
                if (p != null) {
                    LineaCarrito nuevaLinea = new LineaCarrito();
                    nuevaLinea.setProducto(p);
                    nuevaLinea.setCantidad((short) 1);
                    carrito.add(nuevaLinea);
                }
            }
            sesion.setAttribute("carrito", carrito);

            int cantidadTotal = 0;
            double subtotal = 0;
            for (LineaCarrito linea : carrito) {
                cantidadTotal += linea.getCantidad();
                subtotal += (linea.getProducto().getPrecio() * linea.getCantidad());
            }
            double iva = subtotal * 0.21;
            double total = subtotal + iva;

            sesion.setAttribute("cantidadTotal", cantidadTotal);
            sesion.setAttribute("subtotalCarrito", subtotal);
            sesion.setAttribute("ivaCarrito", iva);
            sesion.setAttribute("totalCarrito", total);

            Usuario usuario = (Usuario) sesion.getAttribute("usuario");
            if (usuario != null) {
                PedidoDAO pdao = new PedidoDAO();
                pdao.guardarCarritoBD(usuario, carrito, total, iva);
            } else {
                Utilidades.guardarCarritoEnCookie(response, carrito);
            }
        }
        response.sendRedirect(request.getContextPath() + "/CarritoController");
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
