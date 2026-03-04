package es.componentesrey.beans;

import java.io.Serializable;

/**
 * Clase DTO que representa el desglose (una línea) de un pedido ya finalizado y
 * consolidado en la Base de Datos. A diferencia de LineaCarrito, esta clase sí
 * tiene un identificador de Pedido al que pertenece.
 *
 * @author arey1
 */
public class LineaPedido implements Serializable {

    private short idLinea;
    private short idPedido;
    private short idProducto;
    private short cantidad;
    private Producto producto;

    /**
     * Constructor por defecto.
     */
    public LineaPedido() {
    }

//    GETTERS Y SETTERS
    /**
     * Obtiene el identificador único de la línea de pedido.
     *
     * * @return Id de la línea.
     */
    public short getIdLinea() {
        return idLinea;
    }

    /**
     * Establece el identificador de la línea de pedido.
     *
     * * @param idLinea Id a asignar a la línea.
     */
    public void setIdLinea(short idLinea) {
        this.idLinea = idLinea;
    }

    /**
     * Obtiene el identificador del pedido padre al que pertenece esta línea.
     *
     * @return Id del pedido.
     */
    public short getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el identificador del pedido padre.
     *
     * @param idPedido Id de cabecera a asignar.
     */
    public void setIdPedido(short idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene el identificador del producto de la línea.
     *
     * * @return Id del producto.
     */
    public short getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador del producto de la línea.
     *
     * * @param idProducto Id del producto a asignar.
     */
    public void setIdProducto(short idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene la cantidad del producto comprado en esta línea.
     *
     * * @return Cantidad de unidades.
     */
    public short getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad del producto comprado.
     *
     * * @param cantidad Número de unidades a asignar.
     */
    public void setCantidad(short cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el objeto Producto detallado asociado a esta línea.
     *
     * * @return Objeto de tipo Producto.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el objeto Producto detallado para esta línea.
     *
     * * @param producto Objeto Producto a asignar.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
