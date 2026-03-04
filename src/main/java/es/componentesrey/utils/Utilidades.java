package es.componentesrey.utils;

import es.componentesrey.DAO.ProductoDAO;
import es.componentesrey.beans.LineaCarrito;
import es.componentesrey.beans.Producto;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author arey1
 */
public class Utilidades {

    public static char calcularLetraDNI(String dni) {
        int numero = Integer.parseInt(dni.trim());
        String caracteres = "TRWAGMYFPDXBNJZSQVHLCKE";
        return caracteres.charAt(numero % 23);
    }

    // CIFRADO DE CONTRASEÑAS (MD5
    public static String cifrarMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // EXTRACCIÓN DE NOMBRES DE ARCHIVO MULTIPART (Para fotos de Perfil)
    /**
     * Extrae el nombre original de un archivo subido a través de un formulario
     * multipart.
     *
     * @param part Objeto Part que contiene el archivo.
     * @return String con el nombre exacto del archivo subido.
     */
    public static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    // SISTEMA DE COOKIES PARA EL CARRITO ANÓNIMO
    public static void guardarCarritoEnCookie(HttpServletResponse response, List<LineaCarrito> carrito) {
        StringBuilder sb = new StringBuilder();
        if (carrito != null && !carrito.isEmpty()) {
            for (LineaCarrito lc : carrito) {
                sb.append(lc.getProducto().getIdProducto()).append("-").append(lc.getCantidad()).append("_");
            }
        }
        Cookie cookie = new Cookie("carritoAnonimo", sb.toString());
        cookie.setMaxAge(2 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static List<LineaCarrito> leerCarritoDeCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("carritoAnonimo".equals(c.getName())) {
                    String valor = c.getValue();
                    if (valor != null && !valor.isEmpty()) {
                        List<LineaCarrito> carrito = new ArrayList<>();
                        ProductoDAO pdao = new ProductoDAO();
                        String[] items = valor.split("_");
                        for (String item : items) {
                            if (!item.isEmpty()) {
                                String[] partes = item.split("-");
                                if (partes.length == 2) {
                                    try {
                                        int id = Integer.parseInt(partes[0]);
                                        short qty = Short.parseShort(partes[1]);
                                        Producto p = pdao.getProductoPorId(id);
                                        if (p != null) {
                                            LineaCarrito lc = new LineaCarrito();
                                            lc.setProducto(p);
                                            lc.setCantidad(qty);
                                            carrito.add(lc);
                                        }
                                    } catch (NumberFormatException e) {
                                    }
                                }
                            }
                        }
                        return carrito;
                    }
                }
            }
        }
        return null;
    }

    public static void borrarCookieCarrito(HttpServletResponse response) {
        Cookie cookie = new Cookie("carritoAnonimo", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
