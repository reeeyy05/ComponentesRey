package es.componentesrey.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Clase DTO que representa un Pedido finalizado por un usuario. Contiene los
 * datos de facturación, estado y la lista de líneas de pedido.
 *
 * @author arey1
 */
public class Pedido implements Serializable {

    private short idPedido;
    private Date fecha;
    private String estado;
    private short idUsuario;
    private double importe;
    private double iva;
    private List<LineaPedido> lineas;

    /**
     * Constructor por defecto vacío
     */
    public Pedido() {
    }

    /**
     * Obtiene el identificador del pedido.
     *
     * @return id del pedido.
     */
    public short getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el identificador del pedido.
     *
     * @param idPedido Id a asignar.
     */
    public void setIdPedido(short idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene la fecha en la que se realizó el pedido.
     *
     * @return Fecha del pedido.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de realización del pedido.
     *
     * @param fecha Fecha a asignar.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el estado actual del pedido.
     *
     * @return 'f' para finalizado, 'p' para pendiente, etc.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del pedido.
     *
     * @param estado Carácter que representa el estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el id del usuario
     *
     * @return id del usuario
     */
    public short getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el id del usuario
     *
     * @param idUsuario
     */
    public void setIdUsuario(short idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el importe total del pedido (sin IVA).
     *
     * @return Subtotal del pedido.
     */
    public double getImporte() {
        return importe;
    }

    /**
     * Establece el importe total del pedido (sin IVA).
     *
     * @param importe Subtotal a asignar.
     */
    public void setImporte(double importe) {
        this.importe = importe;
    }

    /**
     * Obtiene el valor total del IVA aplicado al pedido.
     *
     * @return Cantidad de IVA.
     */
    public double getIva() {
        return iva;
    }

    /**
     * Establece el valor total del IVA del pedido.
     *
     * @param iva Cantidad de IVA a asignar.
     */
    public void setIva(double iva) {
        this.iva = iva;
    }

    /**
     * Obtiene la lista de artículos (líneas) que componen este pedido.
     *
     * @return Lista de objetos LineaPedido.
     */
    public List<LineaPedido> getLineas() {
        return lineas;
    }

    /**
     * Establece la lista de artículos del pedido.
     *
     * @param lineas Lista de líneas a asignar.
     */
    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas;
    }
}
