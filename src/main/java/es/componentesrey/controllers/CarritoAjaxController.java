package es.componentesrey.controllers;

import es.componentesrey.DAO.IProductoDAO;
import es.componentesrey.DAO.PedidoDAO;
import es.componentesrey.DAO.ProductoDAO;
import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.Producto;
import es.componentesrey.beans.Usuario;
import es.componentesrey.utils.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "CarritoAjaxController", urlPatterns = {"/CarritoAjaxController"})
public class CarritoAjaxController extends HttpServlet {

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
        String idParam = request.getParameter("id");

        if (accion == null || idParam == null || idParam.isEmpty()) {
            out.print("{\"error\": true, \"mensaje\": \"Faltan parametros\"}");
            out.flush();
            return;
        }

        int idProducto;
        try {
            idProducto = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            out.print("{\"error\": true, \"mensaje\": \"ID no valido\"}");
            out.flush();
            return;
        }

        HttpSession sesion = request.getSession();
        List<LineaCarrito> carrito = (List<LineaCarrito>) sesion.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
            sesion.setAttribute("carrito", carrito);
        }

        boolean itemEliminado = false;
        int itemCantidad = 0;
        double itemImporteTotal = 0;

        if ("anadir".equals(accion)) {
            boolean existe = false;
            for (LineaCarrito linea : carrito) {
                if (linea.getProducto().getIdProducto() == idProducto) {
                    linea.setCantidad((short) (linea.getCantidad() + 1));
                    itemCantidad = linea.getCantidad();
                    itemImporteTotal = linea.getProducto().getPrecio() * itemCantidad;
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
                    itemCantidad = 1;
                    itemImporteTotal = p.getPrecio();
                }
            }
        } else {
            for (int i = 0; i < carrito.size(); i++) {
                LineaCarrito linea = carrito.get(i);
                if (linea.getProducto().getIdProducto() == idProducto) {
                    if ("sumar".equals(accion)) {
                        linea.setCantidad((short) (linea.getCantidad() + 1));
                        itemCantidad = linea.getCantidad();
                        itemImporteTotal = linea.getProducto().getPrecio() * itemCantidad;
                    } else if ("restar".equals(accion)) {
                        if (linea.getCantidad() > 1) {
                            linea.setCantidad((short) (linea.getCantidad() - 1));
                            itemCantidad = linea.getCantidad();
                            itemImporteTotal = linea.getProducto().getPrecio() * itemCantidad;
                        } else {
                            carrito.remove(i);
                            itemEliminado = true;
                        }
                    } else if ("eliminar".equals(accion)) {
                        carrito.remove(i);
                        itemEliminado = true;
                    }
                    break;
                }
            }
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

        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        if (usuario != null) {
            PedidoDAO pdao = new PedidoDAO();
            pdao.guardarCarritoBD(usuario, carrito, total, iva);
        } else {
            Utilidades.guardarCarritoEnCookie(response, carrito);
        }

        String json = "{"
                + "\"error\": false,"
                + "\"itemEliminado\": " + itemEliminado + ","
                + "\"itemCantidad\": " + itemCantidad + ","
                + "\"itemImporteTotal\": " + itemImporteTotal + ","
                + "\"subtotal\": " + subtotal + ","
                + "\"iva\": " + iva + ","
                + "\"total\": " + total + ","
                + "\"cantidadTotal\": " + cantidadTotal
                + "}";

        out.print(json);
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
