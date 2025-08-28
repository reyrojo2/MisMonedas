package model;

import java.sql.Date;

public class MetasAhorro {
    private int id;
    private String nombre;
    private double montoObjetivo;
    private double montoActual;
    private Date fechaInicio;
    private Date fechaFin;
    private int userId;

    
    //CONSTRUCTOR
    public MetasAhorro() {
    	
    }

    
    //GETTER Y SETTER
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getMontoObjetivo() {
		return montoObjetivo;
	}

	public void setMontoObjetivo(double montoObjetivo) {
		this.montoObjetivo = montoObjetivo;
	}

	public double getMontoActual() {
		return montoActual;
	}

	public void setMontoActual(double montoActual) {
		this.montoActual = montoActual;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}  
}
