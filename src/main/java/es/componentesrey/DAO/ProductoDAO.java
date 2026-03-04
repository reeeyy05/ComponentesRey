package es.componentesrey.DAO;

import es.componentesrey.beans.Producto;
import es.componentesrey.beans.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz IProductoDAO. Controla el acceso a la tabla
 * 'productos' realizando mapeos y uniones con la tabla 'categorias'.
 *
 * @author arey1
 */
public class ProductoDAO implements IProductoDAO {

    /**
     * Carga de la base de datos todo el catálogo de productos incluyendo la
     * información de sus categorías asociadas a través de un INNER JOIN.
     *
     * * @return Lista con todos los productos de la tienda.
     */
    @Override
    public List<Producto> getProductos() {
        List<Producto> lista = new ArrayList<>();
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "SELECT p.idproducto, p.nombre, p.descripcion, p.precio, p.marca, p.imagen, c.idcategoria, c.nombre AS nombre_cat "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.idcategoria = c.idcategoria";

        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getShort("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion")); // RECUPERAMOS LA DESCRIPCIÓN
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getString("imagen"));
                p.setMarca(rs.getString("marca"));

                Categoria c = new Categoria();
                c.setIdCategoria(rs.getByte("idcategoria"));
                c.setNombre(rs.getString("nombre_cat"));
                p.setCategoria(c);

                lista.add(p);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return lista;
    }

    /**
     * Busca en la base de datos aquellos productos cuyo nombre contenga la
     * cadena proporcionada.
     *
     * * @param busqueda Cadena de texto a buscar.
     * @return Lista de productos que hacen 'match' con la búsqueda.
     */
    @Override
    public List<Producto> getProductosBusqueda(String busqueda) {
        List<Producto> lista = new ArrayList<>();
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "SELECT p.idproducto, p.nombre, p.descripcion, p.precio, p.marca, p.imagen, c.idcategoria, c.nombre AS nombre_cat "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.idcategoria = c.idcategoria "
                + "WHERE p.nombre LIKE ?";

        try {
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setString(1, "%" + busqueda + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getShort("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion")); // RECUPERAMOS LA DESCRIPCIÓN
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getString("imagen"));
                p.setMarca(rs.getString("marca"));

                Categoria c = new Categoria();
                c.setIdCategoria(rs.getByte("idcategoria"));
                c.setNombre(rs.getString("nombre_cat"));
                p.setCategoria(c);

                lista.add(p);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return lista;
    }

    /**
     * Obtiene el detalle exhaustivo de un único producto empleando su
     * identificador.
     *
     * * @param idProducto ID único a buscar en la tabla 'productos'.
     * @return El objeto Producto mapeado, o null si la consulta no devuelve
     * resultados.
     */
    @Override
    public Producto getProductoPorId(int idProducto) {
        Producto p = null;
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "SELECT p.idproducto, p.nombre, p.descripcion, p.precio, p.marca, p.imagen, "
                + "c.idcategoria, c.nombre AS nombre_cat "
                + "FROM productos p "
                + "INNER JOIN categorias c ON p.idcategoria = c.idcategoria "
                + "WHERE p.idproducto = ?";

        try {
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Producto();
                p.setIdProducto(rs.getShort("idproducto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getDouble("precio"));
                p.setImagen(rs.getString("imagen"));
                p.setDescripcion(rs.getString("descripcion")); // RECUPERAMOS LA DESCRIPCIÓN
                p.setMarca(rs.getString("marca"));

                Categoria c = new Categoria();
                c.setIdCategoria(rs.getByte("idcategoria"));
                c.setNombre(rs.getString("nombre_cat"));
                p.setCategoria(c);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return p;
    }

    /**
     * Realiza un filtrado de marcas (excluyendo vacías o nulas) para obtener
     * los distintos fabricantes sin duplicados presentes en el catálogo actual.
     *
     * * @return Lista de Strings con los nombres de las marcas registradas.
     */
    @Override
    public List<String> getMarcas() {
        List<String> marcas = new ArrayList<>();
        Connection conexion = ConnectionFactory.getConnection();
        String sql = "SELECT DISTINCT marca FROM productos WHERE marca IS NOT NULL AND marca != '' ORDER BY marca ASC";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                marcas.add(rs.getString("marca"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return marcas;
    }

    /**
     * Utiliza una función de agregación SQL (MAX) para obtener el precio del
     * artículo más caro del catálogo.
     *
     * * @return Precio máximo encontrado.
     */
    @Override
    public double getPrecioMax() {
        double precio = 0;
        Connection conexion = ConnectionFactory.getConnection();
        String sql = "SELECT MAX(precio) AS maximo FROM productos";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                precio = rs.getDouble("maximo");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return precio;
    }

    /**
     * Utiliza una función de agregación SQL (MIN) para obtener el precio del
     * artículo más barato del catálogo.
     *
     * * @return Precio mínimo encontrado.
     */
    @Override
    public double getPrecioMin() {
        double precio = 0;
        Connection conexion = ConnectionFactory.getConnection();
        String sql = "SELECT MIN(precio) AS minimo FROM productos";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                precio = rs.getDouble("minimo");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return precio;
    }

    /**
     * Libera el recurso de la conexión de vuelta al pool.
     */
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
