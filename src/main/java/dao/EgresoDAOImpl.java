// dao/EgresoDaoImpl.java
package dao;

import model.Egreso;
import java.sql.*;

public class EgresoDAOImpl extends GenericDAOImpl<Egreso> {

    @Override protected String getTableName() { return "egresos"; }

    @Override
    protected void setInsertParams(PreparedStatement ps, Egreso egreso) throws SQLException {
        ps.setDouble(1, egreso.getMonto());
        ps.setString(2, egreso.getCategoria());
        java.util.Date f = egreso.getFecha();
        ps.setDate(3, (f != null) ? new java.sql.Date(f.getTime()) : null);
        ps.setString(4, egreso.getDescripcion());
        ps.setInt(5, egreso.getUserId());
    }

    @Override
    protected Egreso mapRow(ResultSet rs) throws SQLException {
        Egreso e = new Egreso();
        e.setId(rs.getInt("id"));
        e.setMonto(rs.getDouble("monto"));
        e.setCategoria(rs.getString("categoria"));
        e.setFecha(rs.getDate("fecha"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setUserId(rs.getInt("user_id"));
        return e;
    }
}
