package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class UpdateTablaCat {

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

                // 1) Borrar datos de prueba (tablas hijas)
                try (Statement st = cn.createStatement()) {
                    int delIng = st.executeUpdate("DELETE FROM ingresos");
                    int delEgr = st.executeUpdate("DELETE FROM egresos");
                    System.out.println("🗑️ ingresos eliminados: " + delIng);
                    System.out.println("🗑️ egresos eliminados: " + delEgr);
                }

                // 2) Quitar índice único uq_cat_user si existe
                if (indexExists(cn, "categorias", "uq_cat_user")) {
                    try (Statement st = cn.createStatement()) {
                        st.executeUpdate("ALTER TABLE categorias DROP INDEX uq_cat_user");
                        System.out.println("🔧 Eliminado índice único uq_cat_user");
                    }
                } else {
                    System.out.println("ℹ️ Índice uq_cat_user no existe (ok)");
                }

                // 3) Quitar CHECK chk_userid si existe (MySQL 8.0.16+)
                if (checkExists(cn, "categorias", "chk_userid")) {
                    try (Statement st = cn.createStatement()) {
                        st.executeUpdate("ALTER TABLE categorias DROP CHECK chk_userid");
                        System.out.println("🔧 Eliminado CHECK chk_userid");
                    }
                } else {
                    System.out.println("ℹ️ CHECK chk_userid no existe (ok)");
                }

                // 4) Quitar columna user_id si existe
                if (columnExists(cn, "categorias", "user_id")) {
                    try (Statement st = cn.createStatement()) {
                        st.executeUpdate("ALTER TABLE categorias DROP COLUMN user_id");
                        System.out.println("🧱 Eliminada columna user_id de categorias");
                    }
                } else {
                    System.out.println("ℹ️ Columna user_id ya no existe (ok)");
                }

                System.out.println("✅ Esquema simplificado: categorias ya no tiene user_id.");
                System.out.println("   (ingresos/egresos fueron vaciadas como datos de prueba)");

                // ——— OPCIONAL (comentado): endurecer unicidad por tipo+nombre normalizado ———
                // Habilita si deseas prevenir “casi duplicados” a futuro.
                /*
                try (Statement st = cn.createStatement()) {
                    // Agregar columna generada con nombre normalizado (minúsculas/espacios)
                    if (!columnExists(cn, "categorias", "nombre_norm")) {
                        st.executeUpdate(
                            "ALTER TABLE categorias " +
                            "ADD COLUMN nombre_norm VARCHAR(120) " +
                            "GENERATED ALWAYS AS (LOWER(REPLACE(REPLACE(REPLACE(TRIM(nombre), '-', ' '), '_', ' '), '  ', ' '))) " +
                            "STORED COLLATE utf8mb4_0900_ai_ci"
                        );
                        System.out.println("🧩 Agregada columna generada nombre_norm");
                    }
                    // Índice único por tipo + nombre_norm
                    if (!indexExists(cn, "categorias", "uq_categoria_tipo_norm")) {
                        st.executeUpdate(
                            "CREATE UNIQUE INDEX uq_categoria_tipo_norm ON categorias (tipo, nombre_norm)"
                        );
                        System.out.println("🛡️ Creado índice único uq_categoria_tipo_norm");
                    }
                }
                */

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Helpers para verificar existencia en information_schema
        private static boolean indexExists(Connection cn, String table, String index) throws SQLException {
            String sql = """
                SELECT 1
                FROM information_schema.statistics
                WHERE table_schema = DATABASE()
                  AND table_name = ?
                  AND index_name = ?
                LIMIT 1
            """;
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, table);
                ps.setString(2, index);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }

        private static boolean columnExists(Connection cn, String table, String column) throws SQLException {
            String sql = """
                SELECT 1
                FROM information_schema.columns
                WHERE table_schema = DATABASE()
                  AND table_name = ?
                  AND column_name = ?
                LIMIT 1
            """;
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, table);
                ps.setString(2, column);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }

        private static boolean checkExists(Connection cn, String table, String checkName) throws SQLException {
            String sql = """
                SELECT 1
                FROM information_schema.table_constraints
                WHERE table_schema = DATABASE()
                  AND table_name = ?
                  AND constraint_type = 'CHECK'
                  AND constraint_name = ?
                LIMIT 1
            """;
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, table);
                ps.setString(2, checkName);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }