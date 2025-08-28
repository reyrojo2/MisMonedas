package model;

import java.sql.*;

public class DedupCategorias {
	private static final String HOST = "mismonedas.cps2o2a0qthk.us-east-2.rds.amazonaws.com";
    private static final String PUERTO = "3306";
    private static final String BASE_DE_DATOS = "mismonedas";
    private static final String USER = "admin";
    private static final String PASS = "LP1Grupo42025";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS + "?useSSL=false&serverTimezone=America/Lima";


    public static void main(String[] args) throws Exception {
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS)) {
          cn.setAutoCommit(false);
          try (PreparedStatement ps = cn.prepareStatement(
              "INSERT INTO categorias (nombre, tipo) VALUES (?, 'egreso') " +
              "ON DUPLICATE KEY UPDATE id = id")) {

            ps.setString(1, "Diezmos");  ps.executeUpdate();
            ps.setString(1, "Ofrendas"); ps.executeUpdate();

            cn.commit();
            System.out.println("✅ Categorías Diezmos y Ofrendas listas en 'egreso'.");
          } catch (SQLException e) { cn.rollback(); throw e; }
        }
      }
    }