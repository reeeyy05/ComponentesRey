package es.componentesrey.DAO;

import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.Pedido;
import es.componentesrey.beans.Usuario;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos relacionadas con la
 * gestión de carritos de compra y pedidos consolidados.
 *
 * @author arey1
 */
public interface IPedidoDAO {

    /**
     * Guarda el estado actual del carrito de un usuario en la base de datos,
     * eliminando el carrito anterior si existiese (estado 'c').
     *
     * * @param usuario El usuario propietario del carrito.
     * @param carrito Lista de líneas de carrito que se van a guardar.
     * @param importeTotal Importe subtotal del carrito.
     * @param ivaTotal Cantidad de IVA aplicable al carrito.
     */
    public void guardarCarritoBD(Usuario usuario, List<LineaCarrito> carrito, double importeTotal, double ivaTotal);

    /**
     * Recupera el carrito guardado (estado 'c') de un usuario desde la base de
     * datos.
     *
     * * @param idUsuario El identificador del usuario.
     * @return Lista de líneas de carrito previamente guardadas.
     */
    public List<LineaCarrito> getCarritoBD(short idUsuario);

    /**
     * Formaliza la compra de un carrito, convirtiendo su estado a 'f'
     * (Finalizado) e insertando o actualizando sus líneas asociadas.
     *
     * * @param usuario Usuario que tramita el pedido.
     * @param carrito Lista de artículos a comprar.
     * @param importeTotal Importe total sin impuestos del pedido.
     * @param ivaTotal Impuestos totales aplicables al pedido.
     * @return true si la transacción a BD fue exitosa, false en caso contrario.
     */
    public boolean tramitarPedido(Usuario usuario, List<LineaCarrito> carrito, double importeTotal, double ivaTotal);

    /**
     * Obtiene el historial de pedidos finalizados (estado 'f') de un cliente.
     *
     * * @param idUsuario Identificador del cliente.
     * @return Lista de objetos Pedido, incluyendo las líneas de pedido
     * anidadas.
     */
    public List<Pedido> getPedidosPorUsuario(short idUsuario);

    /**
     * Cierra la conexión a la base de datos.
     */
    public void closeConnection();

}
