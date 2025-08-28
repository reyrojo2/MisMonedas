// dao/CategoriaDAO.java
package dao;

import connection.ConexionMYSQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaDAO {

    /** Categorías globales por tipo: 'ingreso' | 'egreso' */
    public List<Map<String,Object>> listarPorTipo(String tipo) {
        String sql = "SELECT id, nombre FROM categorias WHERE tipo = ? ORDER BY nombre";
        List<Map<String,Object>> out = new ArrayList<>();
        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String,Object> m = new HashMap<>();
                    m.put("id", rs.getInt("id"));
                    m.put("nombre", rs.getString("nombre"));
                    out.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }
}
