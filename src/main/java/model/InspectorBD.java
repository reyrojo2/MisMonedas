package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InspectorBD {
    private static final String HOST = "mismonedas.cps2o2a0qthk.us-east-2.rds.amazonaws.com";
    private static final String PUERTO = "3306";
    private static final String BASE_DE_DATOS = "mismonedas"; // tiene guion, por eso usamos ``
    private static final String USUARIO = "admin";
    private static final String CONTRASENA = "LP1Grupo42025";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS
            + "?useSSL=false&serverTimezone=UTC";

    public static void main(String[] args) {
        try (Connection cn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            System.out.println("✅ Conectado a RDS: " + BASE_DE_DATOS);

            // 1) Listar tablas
            List<String> tablas = listarTablas(cn);

            // 2) Mostrar estructura de cada tabla
            for (String tabla : tablas) {
                System.out.println("\n===============================================");
                System.out.println("📦 Tabla: " + tabla);
                System.out.println("===============================================");

                describeTabla(cn, tabla);
                showCreateTable(cn, tabla);
                listarClavesForaneas(cn, tabla);
                mostrarMuestras(cn, tabla, 5); // filas de ejemplo (opcional)
            }

            // 3) (Opcional) Consultar tabla users si existe
            if (tablas.contains("users")) {
                mostrarUsuarios(cn);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error de conexión/consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------------- helpers ----------------

    private static List<String> listarTablas(Connection cn) throws SQLException {
        String sqlTablas = "SHOW TABLES";
        List<String> tablas = new ArrayList<>();

        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sqlTablas)) {

            System.out.println("\n📋 Tablas en la BD:");
            while (rs.next()) {
                String tabla = rs.getString(1);
                tablas.add(tabla);
                System.out.println(" - " + tabla);
            }
        }
        return tablas;
    }

    private static void describeTabla(Connection cn, String tabla) throws SQLException {
        String sql = "DESCRIBE " + tabla;
        System.out.println("\n🔎 DESCRIBE " + tabla + ":");
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("  %-32s %-20s %-6s %-12s %-16s %-10s%n",
                    "Field", "Type", "Null", "Key", "Default", "Extra");
            while (rs.next()) {
                String field = rs.getString("Field");
                String type = rs.getString("Type");
                String isNull = rs.getString("Null");
                String key = rs.getString("Key");
                String def = rs.getString("Default");
                String extra = rs.getString("Extra");
                System.out.printf("  %-32s %-20s %-6s %-12s %-16s %-10s%n",
                        field, type, isNull, key, def, extra);
            }
        }
    }

    private static void showCreateTable(Connection cn, String tabla) throws SQLException {
        String sql = "SHOW CREATE TABLE " + tabla;
        System.out.println("\n🛠️  SHOW CREATE TABLE " + tabla + ":");
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String ddl = rs.getString(2); // columna 2 tiene el CREATE
                System.out.println(ddl);
            }
        }
    }

    private static void listarClavesForaneas(Connection cn, String tabla) throws SQLException {
        String sql = """
            SELECT 
              kcu.CONSTRAINT_NAME,
              kcu.COLUMN_NAME,
              kcu.REFERENCED_TABLE_NAME,
              kcu.REFERENCED_COLUMN_NAME
            FROM information_schema.KEY_COLUMN_USAGE kcu
            WHERE kcu.TABLE_SCHEMA = DATABASE()
              AND kcu.TABLE_NAME = ?
              AND kcu.REFERENCED_TABLE_NAME IS NOT NULL
            ORDER BY kcu.CONSTRAINT_NAME, kcu.ORDINAL_POSITION
            """;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, tabla);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hayFK = false;
                while (rs.next()) {
                    if (!hayFK) {
                        System.out.println("\n🔗 Claves Foráneas:");
                        System.out.printf("  %-28s %-22s -> %-22s (%s)%n",
                                "Constraint", "Columna", "Tabla Referida", "Columna Ref.");
                        hayFK = true;
                    }
                    String cons = rs.getString("CONSTRAINT_NAME");
                    String col  = rs.getString("COLUMN_NAME");
                    String rtab = rs.getString("REFERENCED_TABLE_NAME");
                    String rcol = rs.getString("REFERENCED_COLUMN_NAME");
                    System.out.printf("  %-28s %-22s -> %-22s (%s)%n", cons, col, rtab, rcol);
                }
                if (!hayFK) {
                    System.out.println("\n🔗 Claves Foráneas: (no definidas)");
                }
            }
        }
    }

    private static void mostrarMuestras(Connection cn, String tabla, int limit) throws SQLException {
        String sql = "SELECT * FROM " + tabla + " LIMIT " + limit;
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();

            System.out.println("\n📄 Muestras de '" + tabla + "' (hasta " + limit + " filas):");
            // encabezados
            StringBuilder header = new StringBuilder("  ");
            for (int i = 1; i <= cols; i++) {
                header.append(md.getColumnLabel(i)).append(i < cols ? " | " : "");
            }
            System.out.println(header);

            int count = 0;
            while (rs.next()) {
                StringBuilder row = new StringBuilder("  ");
                for (int i = 1; i <= cols; i++) {
                    Object val = rs.getObject(i);
                    row.append(val).append(i < cols ? " | " : "");
                }
                System.out.println(row);
                count++;
            }
            if (count == 0) {
                System.out.println("  (sin filas)");
            }
        } catch (SQLException e) {
            // Si no tienes permisos para alguna tabla, no rompas todo el flujo
            System.out.println("\n📄 Muestras de '" + tabla + "': " + e.getMessage());
        }
    }

    private static void mostrarUsuarios(Connection cn) {
        String sqlUsers = "SELECT * FROM users";
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sqlUsers)) {

            System.out.println("\n👤 Registros en tabla 'users':");
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();

            while (rs.next()) {
                StringBuilder sb = new StringBuilder("  ");
                for (int i = 1; i <= cols; i++) {
                    String col = md.getColumnLabel(i);
                    Object val = rs.getObject(i);
                    sb.append(col).append("=").append(val);
                    if (i < cols) sb.append(" | ");
                }
                System.out.println(sb);
            }
        } catch (SQLException e) {
            System.out.println("\n👤 No se pudo consultar 'users': " + e.getMessage());
        }
    }
}
