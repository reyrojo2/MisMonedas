package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class CrearTablaCat {

	private static final String HOST = "mismonedas.cps2o2a0qthk.us-east-2.rds.amazonaws.com";
    private static final String PUERTO = "3306";
    private static final String BASE_DE_DATOS = "mismonedas";
    private static final String USUARIO = "admin";
    private static final String CONTRASENA = "LP1Grupo42025";

        private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS
            + "?useSSL=false&serverTimezone=UTC";

        public static void main(String[] args) {
            try (Connection cn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
                System.out.println("✅ Conectado a la base de datos");

                String sql = "SELECT id, nombre, tipo, created_at FROM categorias ORDER BY tipo, nombre";
                try (Statement st = cn.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nombre = rs.getString("nombre");
                        String tipo = rs.getString("tipo");
                        Timestamp created = rs.getTimestamp("created_at");

                        System.out.printf("ID: %d | Nombre: %s | Tipo: %s | Creado: %s%n",
                                id, nombre, tipo, created);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }