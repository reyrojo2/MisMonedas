package modelvm;

import model.Presupuesto;
import java.text.DecimalFormat;
import java.util.Locale;

public class PresupuestoVM extends Presupuesto {
    private String estado;

    // Sostiene compatibilidad con tu uso actual
    public PresupuestoVM(Presupuesto p, String estado) {
        super(p.getId(), p.getUserId(), p.getCategoria(),
              p.getMontoPresupuestado(), p.getMontoGastado(), p.getPeriodo());
        this.estado = estado;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // ---- Helpers para que la JSP quede limpia ----
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

    public String getMontoPresupuestadoFmt() {
        return MONEY.format(getMontoPresupuestado());
    }
    public String getMontoGastadoFmt() {
        return MONEY.format(getMontoGastado());
    }

    // Para inputs numéricos con punto (evita coma en locales)
    public String getMontoPresupuestadoRaw() {
        return String.format(Locale.US, "%.2f", getMontoPresupuestado());
    }
    public String getMontoGastadoRaw() {
        return String.format(Locale.US, "%.2f", getMontoGastado());
    }

    // Atajos para el select
    public boolean isMensual() { return "Mensual".equals(getPeriodo()); }
    public boolean isSemanal() { return "Semanal".equals(getPeriodo()); }
    public boolean isAnual()   { return "Anual".equals(getPeriodo());   }
    public boolean isUnico()   { return "Único".equals(getPeriodo());   }

    // (Opcional) estado calculado por negocio
    public boolean estaSobrePresupuesto() {
        return getMontoGastado() > getMontoPresupuestado();
    }
}
