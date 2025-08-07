package model;

public class Egreso {
	private String descripcion;
	private double monto;
	private String fecha;
	private String categoria; // Nuevo atributo

	public Egreso() {
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getCategoria() { // Getter para categoria
		return categoria;
	}

	public void setCategoria(String categoria) { // Setter para categoria
		this.categoria = categoria;
	}
}

