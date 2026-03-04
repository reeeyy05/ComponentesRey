package es.componentesrey.beans;

import java.io.Serializable;

/**
 * Contiene toda la información de un artículo de la tienda.
 * Implementa Serializable para poder guardarse en la sesión HTTP.
 *
 * @author arey1
 */
public class Producto implements Serializable {

    private short idProducto;
    private Categoria categoria;
    private String nombre;
    private String descripcion;
    private double precio;
    private String marca;
    private String imagen;

    /**
     * Constructor por defecto vacío. Necesario para inicializar el Bean.
     */
    public Producto() {
    }

    /**
     * Obtiene el identificador único del producto.
     *
     * @return id del producto.
     */
    public short getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el identificador único del producto.
     *
     * @param idProducto Identificador a asignar.
     */
    public void setIdProducto(short idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene la categoría a la que pertenece el producto.
     *
     * @return Objeto Categoria asociado.
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del producto.
     *
     * @param categoria Objeto Categoria a asignar.
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return Nombre del artículo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción detallada del producto.
     *
     * @return Texto con la descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     *
     * @param descripcion Texto descriptivo a asignar.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio actual del producto.
     *
     * @return Precio en formato decimal.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio Valor monetario a asignar.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la marca del producto.
     *
     * @return Nombre de la marca.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del fabricante del producto.
     *
     * @param marca Nombre de la marca.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene el nombre del archivo de la imagen del producto.
     *
     * @return Nombre de la imagen (ej: "default.jpg").
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece el nombre del archivo de la imagen.
     *
     * @param imagen Nombre del archivo.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
