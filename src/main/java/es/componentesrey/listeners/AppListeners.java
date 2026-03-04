package es.componentesrey.listeners;

import es.componentesrey.DAO.CategoriaDAO;
import es.componentesrey.DAO.ProductoDAO;
import es.componentesrey.beans.Categoria;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContext;

/**
 *
 * @author arey1
 */
@WebListener
public class AppListeners implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 1. Obtenemos el contexto global (memoria de la aplicación)
        ServletContext application = sce.getServletContext();

        // 2. Cargar CATEGORÍAS
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<Categoria> listaCategorias = categoriaDAO.getCategorias();
        application.setAttribute("categorias", listaCategorias);

        // 3. Cargar MARCAS, PRECIO MAX y PRECIO MIN
        ProductoDAO productoDAO = new ProductoDAO();

        List<String> listaMarcas = productoDAO.getMarcas();
        application.setAttribute("marcas", listaMarcas);

        double precioMin = productoDAO.getPrecioMin();
        application.setAttribute("precioMin", precioMin);

        double precioMax = productoDAO.getPrecioMax();
        application.setAttribute("precioMax", precioMax);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }

}
