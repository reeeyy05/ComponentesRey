package es.componentesrey.DAO;

import es.componentesrey.beans.Usuario;

/**
 * Interfaz que define las operaciones permitidas sobre la tabla de usuarios,
 * contemplando el registro, inicio de sesión y gestión del perfil.
 *
 * @author arey1
 */
public interface IUsuarioDAO {

    /**
     * Inserta un nuevo usuario en el sistema.
     *
     * * @param usuario Objeto Usuario cargado con los datos del formulario de
     * registro.
     * @return true si la inserción fue correcta, false de lo contrario.
     */
    public boolean registrarUsuario(Usuario usuario);

    /**
     * Verifica en la base de datos si ya existe un correo electrónico
     * registrado.
     *
     * * @param email Correo a comprobar.
     * @return true si el correo ya existe, false si está libre.
     */
    public boolean comprobarEmail(String email);

    /**
     * Obtiene los datos esenciales de un usuario a partir de su correo
     * electrónico.
     *
     * * @param correoElectronico Correo identificativo del cliente.
     * @return Objeto Usuario con sus datos mapeados, o null si no se encuentra.
     */
    public Usuario getUsuarioPorEmail(String correoElectronico);

    /**
     * Actualiza únicamente el nombre del fichero de la imagen de perfil del
     * usuario.
     *
     * * @param idDelUsuario Identificador del usuario a actualizar.
     * @param nombreDelAvatar Nombre o ruta del nuevo archivo de imagen.
     * @return true si la actualización en la BD tuvo éxito.
     */
    public boolean actualizarAvatarUsuario(short idDelUsuario, String nombreDelAvatar);

    /**
     * Verifica las credenciales introducidas para el acceso a la cuenta y
     * recupera todos los datos del usuario en un objeto.
     *
     * * @param email Correo de inicio de sesión.
     * @param passwordMd5 Contraseña ya cifrada con hash MD5.
     * @return Objeto Usuario con todos sus atributos rellenos si el login es
     * correcto, null si no coincide.
     */
    public Usuario hacerLogin(String email, String passwordMd5);

    /**
     * Modifica la fecha de último acceso del usuario en la base de datos a la
     * fecha y hora actuales.
     *
     * * @param idUsuario Identificador del cliente.
     */
    public void actualizarUltimoAcceso(short idUsuario);

    /**
     * Actualiza los datos modificables del perfil del usuario (dirección,
     * nombre, teléfono...), exceptuando la seguridad y correos.
     *
     * * @param usuario Objeto Usuario modificado.
     * @return true si los cambios se guardaron correctamente en BD.
     */
    public boolean actualizarDatosPersonales(Usuario usuario);

    /**
     * Sustituye la contraseña actual del cliente por una nueva (cifrada
     * previamente).
     *
     * * @param idUsuario Identificador del usuario.
     * @param nuevaPassword Nueva contraseña en formato hash.
     * @return true si el cambio se guardó de forma correcta.
     */
    public boolean cambiarPassword(short idUsuario, String nuevaPassword);

    /**
     * Cierra la conexión y la devuelve al sistema de pools del servidor.
     */
    public void closeConnection();
}
