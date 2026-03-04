package es.componentesrey.beans;

import java.io.Serializable;

/**
 * Clase que representa un elemento dentro de la cesta de la compra temporal.
 * Almacena el producto y la cantidad deseada antes de formalizar el pedido.
 *
 * @author arey1
 */
public class LineaCarrito implements Serializable {

    private Producto producto;
    private int cantidad;

    /**
     * Constructor por defecto vacío. Necesario para inicializar el Bean.
     */
    public LineaCarrito() {
    }

    public LineaCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

//    METODOS
    /**
     * Método auxiliar que calcula el importe total de esta línea.
     *
     * @return Precio del producto multiplicado por la cantidad.
     */
    public double getImporteTotal() {
        return producto.getPrecio() * cantidad;
    }

//    GETTERS Y SETTERS
    /**
     * Obtiene el producto asociado a esta línea del carrito.
     *
     * @return Objeto Producto.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el producto para esta línea.
     *
     * @param producto Objeto Producto.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad seleccionada de este producto.
     *
     * @return Cantidad en formato short.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de este producto en el carrito.
     *
     * @param cantidad Número de unidades.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
