package es.componentesrey.DAO;

import es.componentesrey.beans.Producto;
import java.util.List;

/**
 * Interfaz que define los métodos de acceso a datos para manipular e iterar
 * sobre el catálogo de productos y sus atributos (marcas, precios).
 *
 * @author arey1
 */
public interface IProductoDAO {

    /**
     * Obtiene todos los productos disponibles en la base de datos junto con su
     * categoría.
     *
     * * @return Lista de objetos Producto.
     */
    public List<Producto> getProductos();

    /**
     * Obtiene una lista de productos que coincidan parcialmente con un texto de
     * búsqueda.
     *
     * * @param busqueda Texto a buscar en el nombre del producto.
     * @return Lista filtrada de productos.
     */
    public List<Producto> getProductosBusqueda(String busqueda);

    /**
     * Recupera un único producto a partir de su identificador numérico.
     *
     * * @param idProducto ID primario del producto buscado.
     * @return Objeto Producto si existe, o null si no se encuentra.
     */
    public Producto getProductoPorId(int idProducto);

    /**
     * Extrae una lista de las distintas marcas registradas en el catálogo de
     * productos.
     *
     * * @return Lista de nombres de marcas ordenadas alfabéticamente.
     */
    public List<String> getMarcas();

    /**
     * Obtiene el precio más alto dentro de todo el catálogo.
     *
     * * @return Valor numérico del producto más caro.
     */
    public double getPrecioMax();

    /**
     * Obtiene el precio más bajo dentro de todo el catálogo.
     *
     * * @return Valor numérico del producto más barato.
     */
    public double getPrecioMin();

    /**
     * Cierra y devuelve la conexión utilizada a la base de datos.
     */
    public void closeConnection();
}
