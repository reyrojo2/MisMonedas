package interfaces;

import java.util.List;
import model.MetasAhorro;

public interface MetasAhorroDao {
    boolean save(MetasAhorro meta);
    List<MetasAhorro> findAll();        
    List<MetasAhorro> findByUserId(int userId);
    MetasAhorro findById(int id, int userId);
    boolean update(MetasAhorro meta, int userId);
    boolean delete(int id, int userId);
}
