// service/DashboardService.java
package service;

import java.util.*;
import java.util.stream.Collectors;

import interfaces.MontosDao;
import interfaces.PresupuestoDao;
import model.Egreso;
import model.Ingreso;
import model.Presupuesto;
import modelvm.DashboardViewModel;
import modelvm.PresupuestoVM;

public class DashboardService {

    private final MontosDao<Ingreso> ingresoDao;
    private final MontosDao<Egreso>  egresoDao;
    private final PresupuestoDao     presupuestoDao;

    public DashboardService(MontosDao<Ingreso> i, MontosDao<Egreso> e, PresupuestoDao p) {
        this.ingresoDao = i;
        this.egresoDao  = e;
        this.presupuestoDao = p;
    }

    // Versión con rango
    public DashboardViewModel buildForUser(int userId, Date start, Date end) throws Exception {
        DashboardViewModel vm = new DashboardViewModel();

        List<Ingreso> ingresos = ingresoDao.findByUserIdAndDateRange(userId, start, end);
        List<Egreso>  egresos  = egresoDao.findByUserIdAndDateRange(userId, start, end);

        vm.setTotalIngresos(ingresoDao.sumByUserIdAndDateRange(userId, start, end));
        vm.setTotalEgresos(egresoDao.sumByUserIdAndDateRange(userId, start, end));

        // Ingresos > labels/data
        List<String> ingLabels = ingresos.stream().map(Ingreso::getCategoria).distinct().collect(Collectors.toList());
        List<Double> ingData = ingLabels.stream().map(cat ->
                ingresos.stream().filter(i -> i.getCategoria().equals(cat))
                        .mapToDouble(Ingreso::getMonto).sum()
        ).collect(Collectors.toList());
        vm.setIngresoLabels(ingLabels);
        vm.setIngresoData(ingData);

        // Egresos > labels/data
        List<String> egrLabels = egresos.stream().map(Egreso::getCategoria).distinct().collect(Collectors.toList());
        List<Double> egrData = egrLabels.stream().map(cat ->
                egresos.stream().filter(e -> e.getCategoria().equals(cat))
                        .mapToDouble(Egreso::getMonto).sum()
        ).collect(Collectors.toList());
        vm.setEgresoLabels(egrLabels);
        vm.setEgresoData(egrData);

        // Presupuestos (igual que tu versión actual; filtra por periodo si aplica)
        List<Presupuesto> presupuestos = presupuestoDao.findByUserId(userId);
        List<PresupuestoVM> presVM = new ArrayList<>();
        for (Presupuesto p : presupuestos) {
            double mp = p.getMontoPresupuestado();
            double pg = (mp != 0) ? (p.getMontoGastado() / mp) * 100.0 : 0.0;
            String estado = (pg > 100) ? "Excedido" : (pg > 80) ? "Casi Lleno" : "Dentro del Límite";
            presVM.add(new PresupuestoVM(p, estado));
        }
        vm.setPresupuestos(presVM);

        return vm;
    }

    // (Deja también tu versión sin rango, por compatibilidad)
    public DashboardViewModel buildForUser(int userId) throws Exception {
        // … tu implementación original …
        // (o si quieres, que derive al rango del mes actual)
        return null /* ... */;
    }
}
