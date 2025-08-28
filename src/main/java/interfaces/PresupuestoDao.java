package interfaces;

import model.Presupuesto;
import java.util.List;
import java.util.Optional;

public interface PresupuestoDao {
    boolean save(Presupuesto p);
    Optional<Presupuesto> findById(int id);
    List<Presupuesto> findByUserId(int userId);
    boolean update(Presupuesto p);
    boolean delete(int id);
    boolean updateById(Presupuesto p); 
}
