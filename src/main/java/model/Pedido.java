package model;

public class Pedido {
	private String direccion;
	private String celular;
	private String tamano;
	private String masa;
	private String tipo;
	private int cantidad;
	private String comentarios;
	
	public Pedido() {
	}
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getTamano() {
		return tamano;
	}
	public void setTamano(String tamanio) {
		this.tamano = tamanio;
	}
	public String getMasa() {
		return masa;
	}
	public void setMasa(String masa) {
		this.masa = masa;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	
	public double getTotal() {
		if(tamano.equals("Pequeña")) {
			return cantidad*10;
		}
		if(tamano.equals("Mediana")) {
			return cantidad*25;
		}
		else {
			return cantidad*40;
		}
		}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
		
	}
	

