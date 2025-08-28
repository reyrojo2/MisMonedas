package model;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class CrearTablaUsers {

	private static final String HOST = "mismonedas.cps2o2a0qthk.us-east-2.rds.amazonaws.com";
    private static final String PUERTO = "3306";
    private static final String BASE_DE_DATOS = "mismonedas";
    private static final String USUARIO = "admin";
    private static final String CONTRASENA = "LP1Grupo42025";
    
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DE_DATOS + "?useSSL=false&serverTimezone=America/Lima";

    public static void main(String[] args) {
        try (Connection cn = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            System.out.println("✅ Conectado: " + BASE_DE_DATOS);

            // 0) Asegurar categorías globales (por si acaso)
            ensureGlobalDefaults(cn);

            // === INGRESOS ===
            migrateTable(cn,
                    "ingresos",        // tabla
                    "ingreso",         // tipo en categorias
                    "fk_ingresos_categoria",
                    "idx_ingresos_categoria");

            // === EGRESOS ===
            migrateTable(cn,
                    "egresos",
                    "egreso",
                    "fk_egresos_categoria",
                    "idx_egresos_categoria");

            System.out.println("🎉 Migración completa.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------------------

    private static void migrateTable(Connection cn, String tabla, String tipo,
                                     String fkName, String idxName) throws SQLException {
        System.out.println("\n==== Migrando tabla: " + tabla + " (tipo=" + tipo + ") ====");

        // 1) Agregar columna categoria_id (si no existe)
        if (!columnExists(cn, BASE_DE_DATOS, tabla, "categoria_id")) {
            exec(cn, "ALTER TABLE " + tabla + " ADD COLUMN categoria_id INT NULL");
            System.out.println("➕ columna " + tabla + ".categoria_id añadida.");
        } else {
            System.out.println("ℹ️ columna " + tabla + ".categoria_id ya existe.");
        }

        // 2) Crear categorías que falten (por usuario y tipo) a partir de los textos existentes
        //    Tomamos los nombres distintos de la columna vieja 'categoria'
        String insertCats = """
            INSERT INTO categorias (user_id, nombre, tipo)
            SELECT t.user_id, t.categoria, ?
            FROM (SELECT DISTINCT user_id, categoria FROM %s WHERE categoria IS NOT NULL) t
            WHERE t.categoria <> ''
            ON DUPLICATE KEY UPDATE id = id
        """.formatted(tabla);
        try (PreparedStatement ps = cn.prepareStatement(insertCats)) {
            ps.setString(1, tipo);
            int n = ps.executeUpdate();
            System.out.println("🧭 categorías creadas/aseguradas desde " + tabla + ": " + n);
        }

        // 3) Backfill de categoria_id priorizando categoría del usuario; si no, global (user_id=0)
        // 3.a) Prioridad usuario
        String updUser = """
            UPDATE %s t
            JOIN categorias c
              ON c.nombre = t.categoria
             AND c.tipo = ?
             AND c.user_id = t.user_id
            SET t.categoria_id = c.id
            WHERE (t.categoria_id IS NULL OR t.categoria_id = 0)
              AND t.categoria IS NOT NULL AND t.categoria <> ''
        """.formatted(tabla);
        try (PreparedStatement ps = cn.prepareStatement(updUser)) {
            ps.setString(1, tipo);
            int n = ps.executeUpdate();
            System.out.println("🔗 backfill por usuario: " + n);
        }

        // 3.b) Si aún hay NULL, usar global (user_id=0)
        String updGlobal = """
            UPDATE %s t
            JOIN categorias c
              ON c.nombre = t.categoria
             AND c.tipo = ?
             AND c.user_id = 0
            SET t.categoria_id = c.id
            WHERE (t.categoria_id IS NULL OR t.categoria_id = 0)
              AND t.categoria IS NOT NULL AND t.categoria <> ''
        """.formatted(tabla);
        try (PreparedStatement ps = cn.prepareStatement(updGlobal)) {
            ps.setString(1, tipo);
            int n = ps.executeUpdate();
            System.out.println("🌐 backfill global: " + n);
        }

        // 3.c) Como red de seguridad: si aún queda NULL, crea categoría por usuario puntualmente
        String fillMissingLoop = """
            SELECT id, user_id, categoria
            FROM %s
            WHERE (categoria_id IS NULL OR categoria_id = 0)
              AND categoria IS NOT NULL AND categoria <> ''
            LIMIT 1000
        """.formatted(tabla);
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(fillMissingLoop)) {
            int patched = 0;
            while (rs.next()) {
                int rowId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String nombre = rs.getString("categoria");

                // Crear/asegurar categoría del usuario
                int catId = ensureCategoriaId(cn, userId, nombre, tipo);

                // Setear categoria_id
                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE " + tabla + " SET categoria_id=? WHERE id=?")) {
                    ps.setInt(1, catId);
                    ps.setInt(2, rowId);
                    patched += ps.executeUpdate();
                }
            }
            if (patched > 0) {
                System.out.println("🩹 backfill final (creando user-specific): " + patched);
            }
        }

        // 4) Asegurar que ya no queden NULL
        Integer remain = countNulls(cn, tabla, "categoria_id");
        if (remain != null && remain > 0) {
            throw new SQLException("Aún hay filas sin categoria_id en " + tabla + ": " + remain);
        }

        // 5) Hacer NOT NULL
        exec(cn, "ALTER TABLE " + tabla + " MODIFY categoria_id INT NOT NULL");
        System.out.println("✅ " + tabla + ".categoria_id -> NOT NULL.");

        // 6) Índice (si no existe)
        if (!indexExists(cn, BASE_DE_DATOS, tabla, idxName)) {
            exec(cn, "CREATE INDEX " + idxName + " ON " + tabla + " (categoria_id)");
            System.out.println("✅ índice " + idxName + " creado.");
        } else {
            System.out.println("ℹ️ índice " + idxName + " ya existe.");
        }

        // 7) FK (si no existe)
        if (!fkExists(cn, BASE_DE_DATOS, tabla, fkName)) {
            exec(cn, "ALTER TABLE " + tabla +
                    " ADD CONSTRAINT " + fkName +
                    " FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT");
            System.out.println("✅ FK " + fkName + " creada.");
        } else {
            System.out.println("ℹ️ FK " + fkName + " ya existe.");
        }

        // 8) Drop columna de texto 'categoria' (si existe)
        if (columnExists(cn, BASE_DE_DATOS, tabla, "categoria")) {
            exec(cn, "ALTER TABLE " + tabla + " DROP COLUMN categoria");
            System.out.println("🧹 columna " + tabla + ".categoria eliminada.");
        } else {
            System.out.println("ℹ️ " + tabla + ".categoria ya no existe.");
        }

        System.out.println("✔️ " + tabla + " migrada con éxito.");
    }

    // ---------- UTILIDADES DB ----------

    private static void ensureGlobalDefaults(Connection cn) throws SQLException {
        // Inserta globales (user_id=0) por si aún no están
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
            for (String c : egresos) { ps.setString(1, c); ps.setString(2, "egreso"); ps.addBatch(); }
            for (String c : ingresos){ ps.setString(1, c); ps.setString(2, "ingreso"); ps.addBatch(); }
            ps.executeBatch();
        }
    }

    private static int ensureCategoriaId(Connection cn, int userId, String nombre, String tipo) throws SQLException {
        String upsert = """
            INSERT INTO categorias (nombre, tipo, user_id)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE id = LAST_INSERT_ID(id)
        """;
        try (PreparedStatement ps = cn.prepareStatement(upsert)) {
            ps.setString(1, nombre.trim());
            ps.setString(2, tipo);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()")) {
            if (rs.next()) return rs.getInt(1);
        }
        // Si por algún motivo no devolvió, lee el id real
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT id FROM categorias WHERE user_id=? AND nombre=? AND tipo=? LIMIT 1")) {
            ps.setInt(1, userId);
            ps.setString(2, nombre.trim());
            ps.setString(3, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo resolver categoria_id para (" + userId + "," + nombre + "," + tipo + ")");
    }

    private static boolean columnExists(Connection cn, String schema, String table, String column) throws SQLException {
        String q = """
            SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
             WHERE TABLE_SCHEMA=? AND TABLE_NAME=? AND COLUMN_NAME=? LIMIT 1
        """;
        try (PreparedStatement ps = cn.prepareStatement(q)) {
            ps.setString(1, schema); ps.setString(2, table); ps.setString(3, column);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
    private static boolean indexExists(Connection cn, String schema, String table, String index) throws SQLException {
        String q = """
            SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS
             WHERE TABLE_SCHEMA=? AND TABLE_NAME=? AND INDEX_NAME=? LIMIT 1
        """;
        try (PreparedStatement ps = cn.prepareStatement(q)) {
            ps.setString(1, schema); ps.setString(2, table); ps.setString(3, index);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
    private static boolean fkExists(Connection cn, String schema, String table, String fk) throws SQLException {
        String q = """
            SELECT 1 FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
             WHERE CONSTRAINT_SCHEMA=? AND TABLE_NAME=? AND CONSTRAINT_NAME=? LIMIT 1
        """;
        try (PreparedStatement ps = cn.prepareStatement(q)) {
            ps.setString(1, schema); ps.setString(2, table); ps.setString(3, fk);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
    private static Integer countNulls(Connection cn, String table, String column) throws SQLException {
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table + " WHERE " + column + " IS NULL")) {
            return rs.next() ? rs.getInt(1) : null;
        }
    }
    private static void exec(Connection cn, String sql) throws SQLException {
        try (Statement st = cn.createStatement()) { st.execute(sql); }
    }
}
