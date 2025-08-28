package modelvm;

import java.util.List;

public class DashboardViewModel {
    // Totales
    private double totalIngresos;
    private double totalEgresos;

    // Gráficos
    private List<String> ingresoLabels;
    private List<Double> ingresoData;
    private List<String> egresoLabels;
    private List<Double> egresoData;

    // Tabla
    private List<PresupuestoVM> presupuestos;

    // Getters/Setters
    public double getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(double totalIngresos) { this.totalIngresos = totalIngresos; }

    public double getTotalEgresos() { return totalEgresos; }
    public void setTotalEgresos(double totalEgresos) { this.totalEgresos = totalEgresos; }

    public List<String> getIngresoLabels() { return ingresoLabels; }
    public void setIngresoLabels(List<String> ingresoLabels) { this.ingresoLabels = ingresoLabels; }

    public List<Double> getIngresoData() { return ingresoData; }
    public void setIngresoData(List<Double> ingresoData) { this.ingresoData = ingresoData; }

    public List<String> getEgresoLabels() { return egresoLabels; }
    public void setEgresoLabels(List<String> egresoLabels) { this.egresoLabels = egresoLabels; }

    public List<Double> getEgresoData() { return egresoData; }
    public void setEgresoData(List<Double> egresoData) { this.egresoData = egresoData; }

    public List<PresupuestoVM> getPresupuestos() { return presupuestos; }
    public void setPresupuestos(List<PresupuestoVM> presupuestos) { this.presupuestos = presupuestos; }
}
