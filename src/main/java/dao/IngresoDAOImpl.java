// dao/IngresoDAOImpl.java
package dao;

import model.Ingreso;
import java.sql.*;

public class IngresoDAOImpl extends GenericDAOImpl<Ingreso> {

    @Override protected String getTableName() { return "ingresos"; }

    @Override
    protected void setInsertParams(PreparedStatement ps, Ingreso ingreso) throws SQLException {
        // Orden DEBE coincidir con getInsertSql(): monto, categoria, fecha, descripcion, user_id
        ps.setDouble(1, ingreso.getMonto());
        ps.setString(2, ingreso.getCategoria());
        // fecha viene de la entidad, no de "start/end"
        java.util.Date f = ingreso.getFecha();
        ps.setDate(3, (f != null) ? new java.sql.Date(f.getTime()) : null);
        ps.setString(4, ingreso.getDescripcion());
        ps.setInt(5, ingreso.getUserId());
    }

    @Override
    protected Ingreso mapRow(ResultSet rs) throws SQLException {
        Ingreso i = new Ingreso();
        i.setId(rs.getInt("id"));
        i.setMonto(rs.getDouble("monto"));
        i.setCategoria(rs.getString("categoria"));
        i.setFecha(rs.getDate("fecha"));
        i.setDescripcion(rs.getString("descripcion"));
        i.setUserId(rs.getInt("user_id"));
        return i;
    }
}
