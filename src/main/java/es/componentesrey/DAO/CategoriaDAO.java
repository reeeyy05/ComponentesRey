package es.componentesrey.DAO;

import es.componentesrey.beans.Categoria;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz ICategoriaDAO. Se encarga de realizar las
 * consultas SQL reales a la tabla 'categorias' de la base de datos.
 *
 * @author arey1
 */
public class CategoriaDAO implements ICategoriaDAO {

    /**
     * Consulta la base de datos para extraer todas las categorías y sus
     * imágenes.
     *
     * * @return Lista de objetos Categoria mapeados desde el ResultSet.
     */
    @Override
    public List<Categoria> getCategorias() {
        List<Categoria> lista = new ArrayList<>();
        Connection conexion = ConnectionFactory.getConnection();
        String sql = "SELECT idcategoria, nombre, imagen FROM categorias ORDER BY nombre ASC";
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getByte("idcategoria"));
                c.setNombre(rs.getString("nombre"));
                c.setImagen(rs.getString("imagen"));
                lista.add(c);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return lista;
    }

    /**
     * Llama al método estático de ConnectionFactory para cerrar la conexión
     * usada.
     */
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

}
