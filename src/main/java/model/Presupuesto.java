package model;

public class Presupuesto {
    private int id;
    private int userId;
    private String categoria;
    private double montoPresupuestado;
    private double montoGastado;
    private String periodo;

    public Presupuesto() {
    }

    public Presupuesto(int id, int userId, String categoria, double montoPresupuestado, double montoGastado, String periodo) {
        this.id = id;
        this.userId = userId;
        this.categoria = categoria;
        this.montoPresupuestado = montoPresupuestado;
        this.montoGastado = montoGastado;
        this.periodo = periodo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() { // Getter para userId
        return userId;
    }

    public void setUserId(int userId) { // Setter para userId
        this.userId = userId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getMontoPresupuestado() {
        return montoPresupuestado;
    }

    public void setMontoPresupuestado(double montoPresupuestado) {
        this.montoPresupuestado = montoPresupuestado;
    }

    public double getMontoGastado() {
        return montoGastado;
    }

    public void setMontoGastado(double montoGastado) {
        this.montoGastado = montoGastado;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
