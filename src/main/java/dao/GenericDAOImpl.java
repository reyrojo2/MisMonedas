package dao;

import connection.ConexionMYSQL;
import interfaces.MontosDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class GenericDAOImpl<T> implements MontosDao<T> {

    protected abstract String getTableName();
    protected abstract void setInsertParams(PreparedStatement ps, T entity) throws SQLException;
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    /** INSERT final usando categoria_id (NOTA: posición 2 = categoria_id) */
    protected String getInsertSql() {
    	  return "INSERT INTO " + getTableName()
    	       + " (monto, categoria_id, fecha, descripcion, user_id) VALUES (?, ?, ?, ?, ?)";
    	}

    /** SELECT con JOIN a categorias para exponer 'categoria' (nombre) */
    protected String getSelectAllSql() {
    	  return "SELECT t.*, c.nombre AS categoria "
    	       + "FROM " + getTableName() + " t "
    	       + "JOIN categorias c ON c.id = t.categoria_id "
    	       + "ORDER BY t.fecha DESC";
    	}
    
    @Override
    public boolean save(T entity) {
        String sql = getInsertSql();
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            setInsertParams(ps, entity); // Debe setear: 1) monto, 2) categoria_id, 3) fecha, 4) descripcion, 5) user_id
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(getSelectAllSql())) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===== SIN RANGO =====
    @Override
    public List<T> findByUserId(int userId) {
        List<T> list = new ArrayList<>();
        String sql = "SELECT t.*, c.nombre AS categoria "
                + "FROM " + getTableName() + " t "
                + "JOIN categorias c ON c.id = t.categoria_id "
                + "WHERE t.user_id = ? "
                + "ORDER BY t.fecha DESC";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double sumByUserId(int userId) {
        String sql = "SELECT SUM(t.monto) AS total "
                   + "FROM " + getTableName() + " t "
                   + "WHERE t.user_id = ?";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ===== CON RANGO =====
    @Override
    public List<T> findByUserIdAndDateRange(int userId, Date start, Date end) {
        List<T> list = new ArrayList<>();
        String sql = "SELECT t.*, c.nombre AS categoria "
                + "FROM " + getTableName() + " t "
                + "JOIN categorias c ON c.id = t.categoria_id "
                + "WHERE t.user_id = ? AND t.fecha >= ? AND t.fecha <= ? "
                + "ORDER BY t.fecha DESC";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, new java.sql.Date(start.getTime()));
            ps.setDate(3, new java.sql.Date(end.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double sumByUserIdAndDateRange(int userId, Date start, Date end) {
        String sql = "SELECT SUM(t.monto) AS total "
                   + "FROM " + getTableName() + " t "
                   + "WHERE t.user_id = ? AND t.fecha >= ? AND t.fecha <= ?";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, new java.sql.Date(start.getTime()));
            ps.setDate(3, new java.sql.Date(end.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
