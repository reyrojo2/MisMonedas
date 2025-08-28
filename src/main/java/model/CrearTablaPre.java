package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class CrearTablaPre {

	private static final String HOST = "mismonedas.cps2o2a0qthk.us-east-2.rds.amazonaws.com";
    private static final String PUERTO = "3306";
    private static final String BASE_DE_DATOS = "mismonedas";
    private static final String USUARIO = "admin";
    private static final String CONTRASENA = "LP1Grupo42025";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS + "?useSSL=false&serverTimezone=America/Lima";

    public static void main(String[] args) {
        try (Connection cn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            System.out.println("✅ Conectado a BD: " + BASE_DE_DATOS);

            // 0) Crear tabla si no existe (por si acaso)
            String createIfNotExists = """
                CREATE TABLE IF NOT EXISTS categorias (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  nombre VARCHAR(100) NOT NULL,
                  tipo ENUM('ingreso','egreso') NOT NULL,
                  user_id INT NOT NULL,
                  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                  UNIQUE KEY uq_cat_user (user_id, nombre, tipo)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            """;
            try (Statement st = cn.createStatement()) {
                st.execute(createIfNotExists);
                System.out.println("🧱 Tabla 'categorias' OK (si no existía).");
            }

            // 1) Si existe una FK sobre user_id hacia users(id), eliminarla
            String fkName = findUserIdForeignKey(cn, BASE_DE_DATOS, "categorias");
            if (fkName != null) {
                try (Statement st = cn.createStatement()) {
                    st.execute("ALTER TABLE categorias DROP FOREIGN KEY `" + fkName + "`");
                    System.out.println("🔓 FK '" + fkName + "' eliminada de categorias.user_id");
                }
            } else {
                System.out.println("ℹ️ No se encontró FK en categorias.user_id");
            }

            // 2) Asegurar tipo de columna (permite 0), sin FK
            try (Statement st = cn.createStatement()) {
                st.execute("ALTER TABLE categorias MODIFY user_id INT NOT NULL");
                System.out.println("🔧 Columna user_id -> INT NOT NULL (OK).");
            }

            // 3) Asegurar CHECK (user_id >= 0)
            dropCheckIfExists(cn, "categorias", "chk_userid");
            try (Statement st = cn.createStatement()) {
                st.execute("ALTER TABLE categorias ADD CONSTRAINT chk_userid CHECK (user_id >= 0)");
                System.out.println("✅ CHECK chk_userid (user_id >= 0) añadido.");
            } catch (SQLException ex) {
                // MySQL < 8.0.16 puede ignorar CHECK; lo toleramos
                System.out.println("⚠️ No se pudo agregar CHECK (posible versión MySQL), se continúa: " + ex.getMessage());
            }

            // 4) Asegurar UNIQUE KEY (user_id, nombre, tipo)
            if (!indexExists(cn, BASE_DE_DATOS, "categorias", "uq_cat_user")) {
                try (Statement st = cn.createStatement()) {
                    st.execute("ALTER TABLE categorias ADD UNIQUE KEY uq_cat_user (user_id, nombre, tipo)");
                    System.out.println("✅ UNIQUE uq_cat_user creado.");
                }
            } else {
                System.out.println("ℹ️ UNIQUE uq_cat_user ya existe.");
            }

            // 5) Insertar categorías por defecto (user_id=0)
            List<String> egresos = Arrays.asList(
                "Alimentos","Transporte","Vivienda","Entretenimiento","Servicios (Luz, Agua, Internet)",
                "Educacion","Salud","Ropa","Deudas","Otros"
            );
            List<String> ingresos = Arrays.asList(
                "Salario","Inversiones","Ventas","Regalos","Reembolsos","Otros"
            );

            String upsert = """
                INSERT INTO categorias (nombre, tipo, user_id)
                VALUES (?, ?, 0)
                ON DUPLICATE KEY UPDATE id = id
            """;
            try (PreparedStatement ps = cn.prepareStatement(upsert)) {
                for (String c : egresos) {
                    ps.setString(1, c);
                    ps.setString(2, "egreso");
                    ps.addBatch();
                }
                for (String c : ingresos) {
                    ps.setString(1, c);
                    ps.setString(2, "ingreso");
                    ps.addBatch();
                }
                ps.executeBatch();
                System.out.println("📌 Categorías por defecto (user_id=0) insertadas/aseguradas.");
            }

            System.out.println("🎉 Patch de categorias completado.");
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Busca el nombre de la FK de categorias.user_id que referencia a users.id, si existe. */
    private static String findUserIdForeignKey(Connection cn, String schema, String table) throws SQLException {
        String sql = """
            SELECT rc.CONSTRAINT_NAME
            FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc
            JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu
              ON rc.CONSTRAINT_SCHEMA = kcu.CONSTRAINT_SCHEMA
             AND rc.TABLE_NAME = kcu.TABLE_NAME
             AND rc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME
            WHERE rc.CONSTRAINT_SCHEMA = ?
              AND rc.TABLE_NAME = ?
              AND kcu.COLUMN_NAME = 'user_id'
              AND rc.REFERENCED_TABLE_NAME = 'users'
        """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, table);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        }
        return null;
    }

    /** Elimina un CHECK constraint si existe (MySQL 8+). */
    private static void dropCheckIfExists(Connection cn, String table, String checkName) {
        try (Statement st = cn.createStatement()) {
            st.execute("ALTER TABLE " + table + " DROP CHECK `" + checkName + "`");
            System.out.println("🧹 CHECK " + checkName + " eliminado (si existía).");
        } catch (SQLException ignore) {
            // Si no existe o versión no soporta, ignoramos.
        }
    }

    /** Verifica si existe un índice/constraint por nombre. */
    private static boolean indexExists(Connection cn, String schema, String table, String indexName) throws SQLException {
        String sql = """
            SELECT 1
              FROM INFORMATION_SCHEMA.STATISTICS
             WHERE TABLE_SCHEMA = ?
               AND TABLE_NAME   = ?
               AND INDEX_NAME   = ?
            UNION
            SELECT 1
              FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
             WHERE CONSTRAINT_SCHEMA = ?
               AND TABLE_NAME        = ?
               AND CONSTRAINT_NAME   = ?
            LIMIT 1
        """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, table);
            ps.setString(3, indexName);
            ps.setString(4, schema);
            ps.setString(5, table);
            ps.setString(6, indexName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}