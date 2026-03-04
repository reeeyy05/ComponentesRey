package es.componentesrey.beans;

import java.io.Serializable;

/**
 * Clase DTO que representa una Categoría de la tienda. Sirve para clasificar
 * los distintos productos.
 *
 * @author arey1
 */
public class Categoria implements Serializable {

    private byte idCategoria;
    private String nombre;
    private String imagen;

    /**
     * Constructor por defecto.
     */
    public Categoria() {
    }

    /**
     * Obtiene el identificador de la categoría.
     *
     * @return Id de la categoría (tipo byte).
     */
    public byte getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categoría.
     *
     * @param idCategoria Id a asignar.
     */
    public void setIdCategoria(byte idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return Nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la imagen de la categoría.
     *
     * @return Imagen de la categoría.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen de la categoría.
     *
     * @param imagen Imagen a asignar.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
