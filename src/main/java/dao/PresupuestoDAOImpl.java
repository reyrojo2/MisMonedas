package dao;

import connection.ConexionMYSQL;
import interfaces.PresupuestoDao;
import model.Presupuesto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PresupuestoDAOImpl implements PresupuestoDao {

    // Solo calcula periodo_inicio según el período seleccionado.
    private Date computePeriodoInicioSQL(String periodo) {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate inicio;
        switch (String.valueOf(periodo)) {
            case "Mensual" -> inicio = today.with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());
            case "Semanal" -> inicio = today.minusDays(today.getDayOfWeek().getValue() - 1);
            case "Anual"   -> inicio = today.with(java.time.temporal.TemporalAdjusters.firstDayOfYear());
            default        -> inicio = today; // Único
        }
        return java.sql.Date.valueOf(inicio);
    }

    // Parsea y valida que la "categoría" traída del form sea un ID numérico.
    // (El form debe enviar el ID en el value del <option>).
    private int parseCategoriaId(String categoriaParam) throws SQLException {
        if (categoriaParam == null || categoriaParam.trim().isEmpty()) {
            throw new SQLException("Categoría requerida (se esperaba ID numérico).");
        }
        try {
            return Integer.parseInt(categoriaParam.trim());
        } catch (NumberFormatException nfe) {
            throw new SQLException("Categoría inválida: se esperaba ID numérico, recibido: " + categoriaParam);
        }
    }

    // =====================================================================

    @Override
    public boolean save(Presupuesto p) {
        String sql = """
            INSERT INTO presupuestos (user_id, categoria_id, monto_presupuestado, periodo, periodo_inicio)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            int categoriaId   = parseCategoriaId(p.getCategoria()); // <-- sin resolver por nombre
            Date periodoInicio = computePeriodoInicioSQL(p.getPeriodo());

            ps.setInt(1, p.getUserId());
            ps.setInt(2, categoriaId);
            ps.setDouble(3, p.getMontoPresupuestado());
            ps.setString(4, p.getPeriodo());
            ps.setDate(5, periodoInicio); // calculado aquí

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Presupuesto p) {
        String sql = """
            UPDATE presupuestos
               SET categoria_id = ?, monto_presupuestado = ?, periodo = ?, periodo_inicio = ?
             WHERE id = ? AND user_id = ?
        """;
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            int categoriaId   = parseCategoriaId(p.getCategoria()); // <-- sin resolver por nombre
            Date periodoInicio = computePeriodoInicioSQL(p.getPeriodo());

            ps.setInt(1, categoriaId);
            ps.setDouble(2, p.getMontoPresupuestado());
            ps.setString(3, p.getPeriodo());
            ps.setDate(4, periodoInicio);
            ps.setInt(5, p.getId());
            ps.setInt(6, p.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM presupuestos WHERE id = ?";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===================== Lecturas (reporte) =====================

    @Override
    public Optional<Presupuesto> findById(int id) {
        // Lee desde la vista para traer el nombre de la categoría y el gasto calculado
        String sql = """
            SELECT id, user_id, categoria, monto_presupuestado,
                   periodo, /*periodo_inicio, periodo_fin,*/
                   monto_gastado_calc
            FROM v_presupuestos_reporte
            WHERE id = ?
        """;
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Presupuesto p = new Presupuesto();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setCategoria(rs.getString("categoria")); // nombre desde la vista
                    p.setMontoPresupuestado(rs.getDouble("monto_presupuestado"));
                    p.setMontoGastado(rs.getDouble("monto_gastado_calc")); // calculado
                    p.setPeriodo(rs.getString("periodo"));
                    return Optional.of(p);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    @Override
    public boolean updateById(Presupuesto p) {
        return update(p);
    }
    
    @Override
    public List<Presupuesto> findByUserId(int userId) {
        List<Presupuesto> list = new ArrayList<>();
        String sql = """
            SELECT id, user_id, categoria, /*categoria_id,*/
                   monto_presupuestado, periodo,
                   /*periodo_inicio, periodo_fin,*/
                   monto_gastado_calc
            FROM v_presupuestos_reporte
            WHERE user_id = ?
            ORDER BY id DESC
        """;
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Presupuesto p = new Presupuesto();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setCategoria(rs.getString("categoria")); // nombre desde la vista
                    p.setMontoPresupuestado(rs.getDouble("monto_presupuestado"));
                    p.setMontoGastado(rs.getDouble("monto_gastado_calc")); // calculado
                    p.setPeriodo(rs.getString("periodo"));
                    list.add(p);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
