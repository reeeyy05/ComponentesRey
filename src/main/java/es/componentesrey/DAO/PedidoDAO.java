package es.componentesrey.DAO;

import es.componentesrey.beans.Categoria;
import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.LineaPedido;
import es.componentesrey.beans.Pedido;
import es.componentesrey.beans.Producto;
import es.componentesrey.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz IPedidoDAO. Gestiona las operaciones CRUD
 * transaccionales de las tablas 'pedidos' y 'lineaspedidos'.
 *
 * @author arey1
 */
public class PedidoDAO implements IPedidoDAO {

    /**
     * Almacena de forma persistente el carrito de la compra como un pedido en
     * estado pendiente ('c'). Utiliza transacciones (commit/rollback) para
     * garantizar la integridad de las cabeceras y líneas.
     *
     * * @param usuario Usuario propietario del carrito.
     * @param carrito Lista de elementos a guardar.
     * @param importeTotal Importe de los artículos.
     * @param ivaTotal Importe del IVA.
     */
    @Override
    public void guardarCarritoBD(Usuario usuario, List<LineaCarrito> carrito, double importeTotal, double ivaTotal) {
        Connection conexion = ConnectionFactory.getConnection();

        try {
            conexion.setAutoCommit(false);
            short idPedido = -1;

            String sqlBuscar = "SELECT idpedido FROM pedidos WHERE idusuario = ? AND estado = 'c'";
            PreparedStatement pstmtBuscar = conexion.prepareStatement(sqlBuscar);
            pstmtBuscar.setShort(1, usuario.getIdUsuario());
            ResultSet rs = pstmtBuscar.executeQuery();

            if (rs.next()) {
                idPedido = rs.getShort("idpedido");

                String sqlBorrar = "DELETE FROM lineaspedidos WHERE idpedido = ?";
                PreparedStatement pstmtBorrar = conexion.prepareStatement(sqlBorrar);
                pstmtBorrar.setShort(1, idPedido);
                pstmtBorrar.executeUpdate();
                pstmtBorrar.close();

                String sqlUpdate = "UPDATE pedidos SET importe = ?, iva = ?, fecha = NOW() WHERE idpedido = ?";
                PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate);
                pstmtUpdate.setDouble(1, importeTotal);
                pstmtUpdate.setDouble(2, ivaTotal);
                pstmtUpdate.setShort(3, idPedido);
                pstmtUpdate.executeUpdate();
                pstmtUpdate.close();

            } else {
                String sqlInsert = "INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) VALUES (NOW(), 'c', ?, ?, ?)";
                PreparedStatement pstmtInsert = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                pstmtInsert.setShort(1, usuario.getIdUsuario());
                pstmtInsert.setDouble(2, importeTotal);
                pstmtInsert.setDouble(3, ivaTotal);
                pstmtInsert.executeUpdate();

                ResultSet rsKeys = pstmtInsert.getGeneratedKeys();
                if (rsKeys.next()) {
                    idPedido = rsKeys.getShort(1);
                }
                rsKeys.close();
                pstmtInsert.close();
            }
            rs.close();
            pstmtBuscar.close();

            if (idPedido != -1 && carrito != null && !carrito.isEmpty()) {
                String sqlLinea = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)";
                PreparedStatement pstmtLinea = conexion.prepareStatement(sqlLinea);

                for (LineaCarrito linea : carrito) {
                    pstmtLinea.setShort(1, idPedido);
                    pstmtLinea.setShort(2, (short) linea.getProducto().getIdProducto());
                    pstmtLinea.setShort(3, (short) linea.getCantidad());
                    pstmtLinea.executeUpdate();
                }
                pstmtLinea.close();
            }

            conexion.commit();

        } catch (SQLException ex) {
            System.err.println("Error guardando el pedido en BD: " + ex.getMessage());
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (conexion != null) {
                    conexion.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.closeConnection();
        }
    }

    /**
     * Rescata el carrito no finalizado de un cliente desde la base de datos
     * cruzando datos de pedidos, líneas de pedidos, productos y categorías.
     *
     * * @param idUsuario El id del cliente.
     * @return Lista de líneas de carrito construida a partir de la BD.
     */
    @Override
    public List<LineaCarrito> getCarritoBD(short idUsuario) {
        List<LineaCarrito> carrito = new ArrayList<>();
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "SELECT lp.cantidad, p.idproducto, p.nombre, p.descripcion, p.precio, p.marca, p.imagen, c.idcategoria, c.nombre AS nombre_cat "
                + "FROM pedidos ped "
                + "INNER JOIN lineaspedidos lp ON ped.idpedido = lp.idpedido "
                + "INNER JOIN productos p ON lp.idproducto = p.idproducto "
                + "INNER JOIN categorias c ON p.idcategoria = c.idcategoria "
                + "WHERE ped.idusuario = ? AND ped.estado = 'c'";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setShort(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Producto prod = new Producto();
                prod.setIdProducto(rs.getShort("idproducto"));
                prod.setNombre(rs.getString("nombre"));
                prod.setDescripcion(rs.getString("descripcion"));
                prod.setPrecio(rs.getDouble("precio"));
                prod.setMarca(rs.getString("marca"));
                prod.setImagen(rs.getString("imagen"));

                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getByte("idcategoria"));
                cat.setNombre(rs.getString("nombre_cat"));
                prod.setCategoria(cat);

                LineaCarrito lc = new LineaCarrito();
                lc.setProducto(prod);
                lc.setCantidad(rs.getShort("cantidad"));

                carrito.add(lc);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return carrito;
    }

    /**
     * Recupera todos los pedidos que hayan sido finalizados (estado 'f') por un
     * cliente, ordenados del más reciente al más antiguo, incluyendo sus
     * artículos.
     *
     * * @param idUsuario Id del cliente consultado.
     * @return Lista de pedidos históricos.
     */
    @Override
    public List<Pedido> getPedidosPorUsuario(short idUsuario) {
        List<Pedido> listaPedidos = new ArrayList<>();
        Connection conexion = ConnectionFactory.getConnection();

        // Importante: filtramos por 'f' (finalizado)
        String sql = "SELECT idpedido, fecha, estado, importe, iva FROM pedidos WHERE idusuario = ? AND estado = 'f' ORDER BY fecha DESC";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setShort(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getShort("idpedido"));
                pedido.setFecha(rs.getDate("fecha"));
                pedido.setEstado(rs.getString("estado"));
                pedido.setIdUsuario(idUsuario);
                pedido.setImporte(rs.getDouble("importe"));
                pedido.setIva(rs.getDouble("iva"));

                pedido.setLineas(getLineasDeUnPedido(pedido.getIdPedido(), conexion));

                listaPedidos.add(pedido);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return listaPedidos;
    }

    /**
     * Método auxiliar privado que obtiene el desglose de líneas de un pedido
     * específico. Reutiliza la conexión activa del método llamante para evitar
     * abrir múltiples conexiones.
     *
     * * @param idPedido El id del pedido principal.
     * @param conexion La conexión SQL actual.
     * @return Lista de líneas asociadas al pedido.
     */
    private List<LineaPedido> getLineasDeUnPedido(short idPedido, Connection conexion) {
        List<LineaPedido> lineas = new ArrayList<>();

        String sql = "SELECT lp.idlinea, lp.idproducto, lp.cantidad, p.nombre, p.precio, p.imagen "
                + "FROM lineaspedidos lp "
                + "INNER JOIN productos p ON lp.idproducto = p.idproducto "
                + "WHERE lp.idpedido = ?";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setShort(1, idPedido);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LineaPedido lp = new LineaPedido();
                lp.setIdLinea(rs.getShort("idlinea"));
                lp.setIdPedido(idPedido);
                lp.setIdProducto(rs.getShort("idproducto"));
                lp.setCantidad(rs.getShort("cantidad"));

                Producto prod = new Producto();
                prod.setIdProducto(rs.getShort("idproducto"));
                prod.setNombre(rs.getString("nombre"));
                prod.setPrecio(rs.getDouble("precio"));
                prod.setImagen(rs.getString("imagen"));
                lp.setProducto(prod);

                lineas.add(lp);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lineas;
    }

    /**
     * Realiza la compra formal del carrito cambiando su estado a 'f' y
     * actualizando precios, fechas e impuestos mediante una transacción atómica
     * SQL.
     *
     * * @param usuario Usuario que consolida la compra.
     * @param carrito Artículos a adquirir.
     * @param importeTotal Precio total sin IVA.
     * @param ivaTotal Cantidad de impuestos.
     * @return true si el pedido se tramitó sin errores SQL, false en caso
     * contrario.
     */
    @Override
    public boolean tramitarPedido(Usuario usuario, List<LineaCarrito> carrito, double importeTotal, double ivaTotal) {
        boolean exito = false;
        Connection conexion = ConnectionFactory.getConnection();

        try {
            conexion.setAutoCommit(false);
            short idPedido = -1;

            // Comprobamos si hay un carrito guardado
            String sqlBuscar = "SELECT idpedido FROM pedidos WHERE idusuario = ? AND estado = 'c'";
            PreparedStatement pstmtBuscar = conexion.prepareStatement(sqlBuscar);
            pstmtBuscar.setShort(1, usuario.getIdUsuario());
            ResultSet rs = pstmtBuscar.executeQuery();

            if (rs.next()) {
                idPedido = rs.getShort("idpedido");

                // Borramos las líneas antiguas
                String sqlBorrar = "DELETE FROM lineaspedidos WHERE idpedido = ?";
                PreparedStatement pstmtBorrar = conexion.prepareStatement(sqlBorrar);
                pstmtBorrar.setShort(1, idPedido);
                pstmtBorrar.executeUpdate();
                pstmtBorrar.close();

                // IMPORTANTE: Le cambiamos el estado a 'f' (Finalizado)
                String sqlUpdate = "UPDATE pedidos SET importe = ?, iva = ?, fecha = NOW(), estado = 'f' WHERE idpedido = ?";
                PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate);
                pstmtUpdate.setDouble(1, importeTotal);
                pstmtUpdate.setDouble(2, ivaTotal);
                pstmtUpdate.setShort(3, idPedido);
                pstmtUpdate.executeUpdate();
                pstmtUpdate.close();

            } else {
                // Si por lo que sea no tenía carrito guardado, lo insertamos directamente como 'f'
                String sqlInsert = "INSERT INTO pedidos (fecha, estado, idusuario, importe, iva) VALUES (NOW(), 'f', ?, ?, ?)";
                PreparedStatement pstmtInsert = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                pstmtInsert.setShort(1, usuario.getIdUsuario());
                pstmtInsert.setDouble(2, importeTotal);
                pstmtInsert.setDouble(3, ivaTotal);
                pstmtInsert.executeUpdate();

                ResultSet rsKeys = pstmtInsert.getGeneratedKeys();
                if (rsKeys.next()) {
                    idPedido = rsKeys.getShort(1);
                }
                rsKeys.close();
                pstmtInsert.close();
            }
            rs.close();
            pstmtBuscar.close();

            // Insertamos las líneas del carrito al pedido finalizado
            if (idPedido != -1 && carrito != null && !carrito.isEmpty()) {
                String sqlLinea = "INSERT INTO lineaspedidos (idpedido, idproducto, cantidad) VALUES (?, ?, ?)";
                PreparedStatement pstmtLinea = conexion.prepareStatement(sqlLinea);

                for (LineaCarrito linea : carrito) {
                    pstmtLinea.setShort(1, idPedido);
                    pstmtLinea.setShort(2, (short) linea.getProducto().getIdProducto());
                    pstmtLinea.setShort(3, (short) linea.getCantidad());
                    pstmtLinea.executeUpdate();
                }
                pstmtLinea.close();
            }

            conexion.commit();
            exito = true;

        } catch (SQLException ex) {
            System.err.println("Error al tramitar el pedido: " + ex.getMessage());
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (conexion != null) {
                    conexion.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.closeConnection();
        }
        return exito;
    }

    /**
     * Llama al método estático de ConnectionFactory para liberar la conexión en
     * el pool.
     */
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
