package es.componentesrey.DAO;

import java.util.List;
import es.componentesrey.beans.Categoria;

/**
 * Interfaz que define las operaciones de acceso a datos permitidas para la
 * entidad Categoria.
 *
 * @author arey1
 */
public interface ICategoriaDAO {

    /**
     * Obtiene una lista con todas las categorías registradas en la base de
     * datos.
     *
     * * @return Lista de objetos Categoria ordenados alfabéticamente por su
     * nombre.
     */
    public List<Categoria> getCategorias();

    /**
     * Cierra la conexión a la base de datos devolviéndola al pool de conexiones.
     */
    public void closeConnection();
}
