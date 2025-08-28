package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class ConexionMYSQL {

    private static String HOST;
    private static String PUERTO;
    private static String BASE_DE_DATOS;
    private static String USUARIO;
    private static String CONTRASENA;
    private static String URL;

    static {
        // Cargar las propiedades desde el archivo
        try (InputStream input = ConexionMYSQL.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(input);

            HOST = props.getProperty("db.host");
            PUERTO = props.getProperty("db.puerto");
            BASE_DE_DATOS = props.getProperty("db.base");
            USUARIO = props.getProperty("db.usuario");
            CONTRASENA = props.getProperty("db.contrasena");

            // si no hay db.url en el archivo, la construimos
            String urlProp = props.getProperty("db.url");
            if (urlProp != null && !urlProp.isBlank()) {
                URL = urlProp;
            } else {
                URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS + "?serverTimezone=America/Lima";
            }

        } catch (IOException e) {
            throw new ExceptionInInitializerError("No se pudo cargar db.properties: " + e.getMessage());
        }
    }

    // Método de conexión
    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: No se pudo cargar el JDBC", e);
        }
    }
}
