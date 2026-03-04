package es.componentesrey.DAO;

import es.componentesrey.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementación de la interfaz IUsuarioDAO. Administra el acceso a datos para
 * todo el apartado de clientes/usuarios interactuando con la tabla 'usuarios'.
 *
 * @author arey1
 */
public class UsuarioDAO implements IUsuarioDAO {

    /**
     * Realiza un INSERT SQL para almacenar un nuevo usuario.
     *
     * * @param usuario Objeto mapeado que contiene todos los campos
     * necesarios.
     * @return true en caso de éxito en la inserción.
     */
    @Override
    public boolean registrarUsuario(Usuario usuario) {
        boolean registrado = false;
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "INSERT INTO usuarios (email, password, nombre, apellidos, nif, telefono, "
                + "direccion, codigo_postal, localidad, provincia, avatar) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, usuario.getEmail());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getNombre());
            pstmt.setString(4, usuario.getApellidos());
            pstmt.setString(5, usuario.getNif());
            pstmt.setString(6, usuario.getTelefono());
            pstmt.setString(7, usuario.getDireccion());
            pstmt.setString(8, usuario.getCodigoPostal());
            pstmt.setString(9, usuario.getLocalidad());
            pstmt.setString(10, usuario.getProvincia());
            pstmt.setString(11, usuario.getAvatar());

            // Si executeUpdate es mayor a 0, significa que se insertó la fila correctamente
            if (pstmt.executeUpdate() > 0) {
                registrado = true;
            }
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("Error al registrar usuario: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return registrado;
    }

    /**
     * Consulta si la dirección de correo proporcionada está siendo utilizada
     * por otro usuario.
     *
     * * @param email Email a verificar en la base de datos.
     * @return true si la consulta devuelve algún resultado (el correo existe).
     */
    @Override
    public boolean comprobarEmail(String email) {
        boolean existe = false;
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "SELECT email FROM usuarios WHERE email = ?";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            // Si el ResultSet tiene algún registro, el email ya está ocupado
            if (rs.next()) {
                existe = true;
            }

            rs.close();
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("Error al comprobar email: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return existe;
    }

    /**
     * Construye un objeto Usuario parcial (solo datos básicos) a través de la
     * búsqueda por correo electrónico, útil para identificaciones rápidas.
     *
     * * @param correoElectronico Email registrado del usuario.
     * @return Objeto Usuario mapeado o null en caso de no hallarse
     * coincidencia.
     */
    @Override
    public Usuario getUsuarioPorEmail(String correoElectronico) {
        Usuario usuarioEncontrado = null;
        Connection conexionBaseDatos = ConnectionFactory.getConnection();
        String consultaSQL = "SELECT * FROM usuarios WHERE email = ?";

        try {
            PreparedStatement sentenciaPreparada = conexionBaseDatos.prepareStatement(consultaSQL);
            sentenciaPreparada.setString(1, correoElectronico);
            ResultSet resultadosConsulta = sentenciaPreparada.executeQuery();

            if (resultadosConsulta.next()) {
                usuarioEncontrado = new Usuario();
                usuarioEncontrado.setIdUsuario(resultadosConsulta.getShort("idusuario"));
                usuarioEncontrado.setEmail(resultadosConsulta.getString("email"));
                usuarioEncontrado.setNombre(resultadosConsulta.getString("nombre"));
                usuarioEncontrado.setApellidos(resultadosConsulta.getString("apellidos"));
                usuarioEncontrado.setAvatar(resultadosConsulta.getString("avatar"));
            }
            resultadosConsulta.close();
            sentenciaPreparada.close();
        } catch (SQLException excepcionSQL) {
            excepcionSQL.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return usuarioEncontrado;
    }

    /**
     * Actualiza el registro de avatar del usuario con un UPDATE tras la subida
     * exitosa de la imagen al servidor.
     *
     * * @param idDelUsuario Identificador del usuario propietario de la
     * imagen.
     * @param nombreDelAvatar Nombre almacenado del fichero.
     * @return true si la modificación tuvo impacto en BD.
     */
    @Override
    public boolean actualizarAvatarUsuario(short idDelUsuario, String nombreDelAvatar) {
        boolean actualizacionCorrecta = false;
        Connection conexionBaseDatos = ConnectionFactory.getConnection();
        String consultaSQL = "UPDATE usuarios SET avatar = ? WHERE idusuario = ?";

        try {
            PreparedStatement sentenciaPreparada = conexionBaseDatos.prepareStatement(consultaSQL);
            sentenciaPreparada.setString(1, nombreDelAvatar);
            sentenciaPreparada.setShort(2, idDelUsuario);

            if (sentenciaPreparada.executeUpdate() > 0) {
                actualizacionCorrecta = true;
            }
            sentenciaPreparada.close();
        } catch (SQLException excepcionSQL) {
            excepcionSQL.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return actualizacionCorrecta;
    }

    /**
     * Compara el usuario y contraseña hash proporcionados con los de la base de
     * datos para conceder acceso. Si tiene éxito, extrae todo el Bean Usuario
     * para guardarlo en Sesión.
     *
     * * @param email Correo electrónico.
     * @param password Password procesada.
     * @return Objeto Usuario completo (con su avatar por defecto manejado si es
     * nulo) o null.
     */
    @Override
    public Usuario hacerLogin(String email, String password) {
        Usuario usuario = null;
        Connection conexion = ConnectionFactory.getConnection();

        // Cuidado: asegúrate de que en la consulta pides el 'avatar' (o usa SELECT *)
        String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getShort("idusuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setNif(rs.getString("nif"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setCodigoPostal(rs.getString("codigo_postal"));
                usuario.setLocalidad(rs.getString("localidad"));
                usuario.setProvincia(rs.getString("provincia"));
                usuario.setUltimoAcceso(rs.getDate("ultimo_acceso"));

                String avatarBD = rs.getString("avatar");
                if (avatarBD == null || avatarBD.trim().isEmpty()) {
                    usuario.setAvatar("perfil_default.jpg"); // Si está vacío, ponemos el de por defecto
                } else {
                    usuario.setAvatar(avatarBD); // Si tiene foto, la guardamos
                }
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return usuario;
    }

    /**
     * Cambia el valor 'ultimo_acceso' de la tabla 'usuarios' a la fecha del
     * servidor SQL utilizando la función NOW().
     *
     * * @param idUsuario Identificador del usuario que ha logueado.
     */
    @Override
    public void actualizarUltimoAcceso(short idUsuario) {
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "UPDATE usuarios SET ultimo_acceso = NOW() WHERE idusuario = ?";
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setShort(1, idUsuario);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
    }

    /**
     * Aplica la modificación de los datos básicos y de envío que el usuario
     * editó desde su página de perfil.
     *
     * * @param usuario Bean conteniendo los datos refrescados a aplicar al
     * update.
     * @return true si la operación altera satisfactoriamente una fila.
     */
    @Override
    public boolean actualizarDatosPersonales(Usuario usuario) {
        boolean actualizado = false;
        Connection conexion = ConnectionFactory.getConnection();

        // Actualizamos todo menos el NIF, el Email, el Avatar y el Password (como pide el PDF)
        String sql = "UPDATE usuarios SET nombre=?, apellidos=?, telefono=?, direccion=?, codigo_postal=?, localidad=?, provincia=? WHERE idusuario=?";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellidos());
            pstmt.setString(3, usuario.getTelefono());
            pstmt.setString(4, usuario.getDireccion());
            pstmt.setString(5, usuario.getCodigoPostal());
            pstmt.setString(6, usuario.getLocalidad());
            pstmt.setString(7, usuario.getProvincia());
            pstmt.setShort(8, usuario.getIdUsuario());

            if (pstmt.executeUpdate() > 0) {
                actualizado = true;
            }
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return actualizado;
    }

    /**
     * Ejecuta una sentencia UPDATE dirigida a modificar el campo 'password'.
     *
     * * @param idUsuario El usuario dueño de la cuenta.
     * @param nuevaPassword La nueva clave cifrada (ya no puede verse el texto
     * en plano).
     * @return true si se efectúa el cambio.
     */
    @Override
    public boolean cambiarPassword(short idUsuario, String nuevaPassword) {
        boolean cambiado = false;
        Connection conexion = ConnectionFactory.getConnection();

        String sql = "UPDATE usuarios SET password=? WHERE idusuario=?";
        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, nuevaPassword);
            pstmt.setShort(2, idUsuario);

            if (pstmt.executeUpdate() > 0) {
                cambiado = true;
            }
            pstmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return cambiado;
    }

    /**
     * Libera de forma segura la conexión invocando el Factory.
     */
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }
}
