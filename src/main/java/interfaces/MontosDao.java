// interfaces/MontosDao.java
package interfaces;

import java.util.Date;
import java.util.List;

public interface MontosDao<T> {
    boolean save(T entity);
    List<T> findAll();

    // existentes / sin rango
    List<T> findByUserId(int userId);
    double  sumByUserId(int userId);

    //con rango
    List<T> findByUserIdAndDateRange(int userId, Date start, Date end);
    double  sumByUserIdAndDateRange(int userId, Date start, Date end);
}
