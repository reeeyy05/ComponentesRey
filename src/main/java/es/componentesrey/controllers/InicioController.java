package es.componentesrey.controllers;

import es.componentesrey.DAO.CategoriaDAO;
import es.componentesrey.DAO.ICategoriaDAO;
import es.componentesrey.DAO.IProductoDAO;
import es.componentesrey.DAO.ProductoDAO;
import es.componentesrey.beans.Categoria;
import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.Producto;
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
@WebServlet(name = "InicioController", urlPatterns = {"/inicio"})
public class InicioController extends HttpServlet {

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

        if (sesion.getAttribute("usuario") == null && sesion.getAttribute("carrito") == null) {
            List<LineaCarrito> carritoRecuperado = Utilidades.leerCarritoDeCookie(request);
            if (carritoRecuperado != null && !carritoRecuperado.isEmpty()) {
                sesion.setAttribute("carrito", carritoRecuperado);

                int cantidadTotal = 0;
                double subtotal = 0;
                for (LineaCarrito linea : carritoRecuperado) {
                    cantidadTotal += linea.getCantidad();
                    subtotal += (linea.getProducto().getPrecio() * linea.getCantidad());
                }
                double iva = subtotal * 0.21;
                sesion.setAttribute("cantidadTotal", cantidadTotal);
                sesion.setAttribute("subtotalCarrito", subtotal);
                sesion.setAttribute("ivaCarrito", iva);
                sesion.setAttribute("totalCarrito", subtotal + iva);
            }
        }

        IProductoDAO pDao = new ProductoDAO();
        List<Producto> productos = pDao.getProductos();
        request.setAttribute("productos", productos);

        if (getServletContext().getAttribute("categorias") == null) {
            ICategoriaDAO cDao = new CategoriaDAO();
            List<Categoria> categorias = cDao.getCategorias();
            getServletContext().setAttribute("categorias", categorias);
        }

        if (getServletContext().getAttribute("marcas") == null) {
            List<String> marcas = pDao.getMarcas();
            getServletContext().setAttribute("marcas", marcas);
        }

        if (getServletContext().getAttribute("precioMax") == null) {
            getServletContext().setAttribute("precioMax", pDao.getPrecioMax());
            getServletContext().setAttribute("precioMin", pDao.getPrecioMin());
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
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
        IProductoDAO pDao = new ProductoDAO();
        List<Producto> productos = null;

        if ("buscar".equals(accion)) {
            String busqueda = request.getParameter("busqueda");
            productos = pDao.getProductosBusqueda(busqueda);
            
        } else if ("filtrarMarca".equals(accion)) {
            String marca = request.getParameter("nombreMarca");
            List<Producto> todos = pDao.getProductos();
            productos = new java.util.ArrayList<>();
            for (Producto p : todos) {
                if (marca.equals(p.getMarca())) {
                    productos.add(p);
                }
            }
        } else if ("filtrarCategoria".equals(accion)) {
            byte idCategoria = Byte.parseByte(request.getParameter("idCategoria"));
            List<Producto> todos = pDao.getProductos();
            productos = new java.util.ArrayList<>();
            for (Producto p : todos) {
                if (p.getCategoria().getIdCategoria() == idCategoria) {
                    productos.add(p);
                }
            }
        } else if ("filtrarPrecio".equals(accion)) {
            double precioSeleccionado = Double.parseDouble(request.getParameter("filtroPrecio"));
            List<Producto> todos = pDao.getProductos();
            productos = new java.util.ArrayList<>();
            for (Producto p : todos) {
                if (p.getPrecio() <= precioSeleccionado) {
                    productos.add(p);
                }
            }
            request.setAttribute("precioSeleccionado", precioSeleccionado);
        } else {
            productos = pDao.getProductos();
        }

        request.setAttribute("productos", productos);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
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
