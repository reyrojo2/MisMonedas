package dao;

import interfaces.UserDao;
import model.User;
import connection.ConexionMYSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAOImpl implements UserDao {

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, full_name, email FROM users WHERE username = ?";

        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    return Optional.of(u);
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error consultando usuario por username", e);
        }
    }
    
    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users (username, password_hash, full_name, email) VALUES (?, ?, ?, ?)";

        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error registrando usuario", e);
        }
    }
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection cn = ConexionMYSQL.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando email", e);
        }
        return false;
    }
}
