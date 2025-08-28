package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import interfaces.MetasAhorroDao;
import connection.ConexionMYSQL;
import model.MetasAhorro;

public class MetasAhorroDAOImpl implements MetasAhorroDao {

    @Override
    public boolean save(MetasAhorro meta) {
        String sql = "INSERT INTO metas_ahorro (nombre, monto_objetivo, monto_actual, fecha_inicio, fecha_fin, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, meta.getNombre());
            ps.setDouble(2, meta.getMontoObjetivo());
            ps.setDouble(3, meta.getMontoActual());
            ps.setDate(4, new java.sql.Date(meta.getFechaInicio().getTime()));
            ps.setDate(5, new java.sql.Date(meta.getFechaFin().getTime()));
            ps.setInt(6, meta.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MetasAhorro> findAll() {  
        List<MetasAhorro> metas = new ArrayList<>();
        String sql = "SELECT * FROM metas_ahorro ORDER BY fecha_inicio DESC";  
        
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             Statement st = cn.createStatement();  
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                MetasAhorro m = new MetasAhorro();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                m.setMontoObjetivo(rs.getDouble("monto_objetivo"));
                m.setMontoActual(rs.getDouble("monto_actual"));
                m.setFechaInicio(rs.getDate("fecha_inicio"));
                m.setFechaFin(rs.getDate("fecha_fin"));
                m.setUserId(rs.getInt("user_id"));
                metas.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return metas;
    }

    @Override
    public List<MetasAhorro> findByUserId(int userId) {
        List<MetasAhorro> metas = new ArrayList<>();
        String sql = "SELECT * FROM metas_ahorro WHERE user_id = ? ORDER BY fecha_inicio DESC";
        
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                MetasAhorro m = new MetasAhorro();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                m.setMontoObjetivo(rs.getDouble("monto_objetivo"));
                m.setMontoActual(rs.getDouble("monto_actual"));
                m.setFechaInicio(rs.getDate("fecha_inicio"));
                m.setFechaFin(rs.getDate("fecha_fin"));
                m.setUserId(rs.getInt("user_id"));
                metas.add(m);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return metas;
    }

    @Override
    public MetasAhorro findById(int id, int userId) {
        String sql = "SELECT * FROM metas_ahorro WHERE id = ? AND user_id = ?";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MetasAhorro m = new MetasAhorro();
                    m.setId(rs.getInt("id"));
                    m.setNombre(rs.getString("nombre"));
                    m.setMontoObjetivo(rs.getDouble("monto_objetivo"));
                    m.setMontoActual(rs.getDouble("monto_actual"));
                    m.setFechaInicio(rs.getDate("fecha_inicio"));
                    m.setFechaFin(rs.getDate("fecha_fin"));
                    m.setUserId(rs.getInt("user_id"));
                    return m;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(MetasAhorro meta, int userId) {
        String sql = "UPDATE metas_ahorro " +
                     "SET nombre = ?, monto_objetivo = ?, monto_actual = ?, fecha_inicio = ?, fecha_fin = ? " +
                     "WHERE id = ? AND user_id = ?";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, meta.getNombre());
            ps.setDouble(2, meta.getMontoObjetivo());
            ps.setDouble(3, meta.getMontoActual());
            ps.setDate(4, new java.sql.Date(meta.getFechaInicio().getTime()));
            ps.setDate(5, new java.sql.Date(meta.getFechaFin().getTime()));
            ps.setInt(6, meta.getId());
            ps.setInt(7, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        String sql = "DELETE FROM metas_ahorro WHERE id = ? AND user_id = ?";
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
