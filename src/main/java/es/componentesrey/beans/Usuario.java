package es.componentesrey.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Contiene todos los atributos personales y de acceso del cliente. Implementa
 * Serializable para poder ser almacenada en la sesión HTTP.
 *
 * @author arey1
 */
public class Usuario implements Serializable {

    private short idUsuario;
    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private String nif;
    private String telefono;
    private String direccion;
    private String codigoPostal;
    private String localidad;
    private String provincia;
    private Date ultimoAcceso;
    private String avatar;

    /**
     * Constructor por defecto vacío
     */
    public Usuario() {
    }

//    GETTERS Y SETTERS
    /**
     * Obtiene el identificador único del usuario.
     *
     * @return idUsuario de tipo short.
     */
    public short getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param idUsuario El identificador a asignar.
     */
    public void setIdUsuario(short idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el correo electrónico del usuario (login).
     *
     * @return email del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email Correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña cifrada del usuario.
     *
     * @return contraseña en formato String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña cifrada del usuario.
     *
     * @param password Contraseña ya cifrada (MD5).
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre de pila del usuario.
     *
     * @return nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     *
     * @return apellidos del usuario.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     *
     * @param apellidos Apellidos a asignar.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el NIF/DNI del usuario.
     *
     * @return DNI con letra.
     */
    public String getNif() {
        return nif;
    }

    /**
     * Establece el NIF/DNI del usuario.
     *
     * @param nif NIF a asignar.
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Obtiene el teléfono de contacto.
     *
     * @return teléfono en formato String.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono de contacto.
     *
     * @param telefono Teléfono a asignar.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección física del usuario para envíos.
     *
     * @return dirección de calle, número, etc.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección física del usuario.
     *
     * @param direccion Dirección completa.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el código postal del municipio.
     *
     * @return código postal de 5 dígitos.
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Establece el código postal del usuario.
     *
     * @param codigoPostal Código postal a asignar.
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene la localidad o municipio del usuario.
     *
     * @return nombre de la localidad.
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Establece la localidad del usuario.
     *
     * @param localidad Nombre de la localidad.
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Obtiene la provincia de residencia.
     *
     * @return nombre de la provincia.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Establece la provincia del usuario.
     *
     * @param provincia Nombre de la provincia.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Obtiene la fecha y hora de la última vez que el usuario hizo login.
     *
     * @return Objeto Date con el último acceso.
     */
    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    /**
     * Establece la fecha del último acceso.
     *
     * @param ultimoAcceso Fecha a registrar.
     */
    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * Obtiene el nombre del archivo de imagen (avatar) asociado al perfil.
     *
     * @return Nombre de la imagen, o null si no tiene.
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Establece el nombre del archivo del avatar del perfil.
     *
     * @param avatar Nombre del archivo a asociar.
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
