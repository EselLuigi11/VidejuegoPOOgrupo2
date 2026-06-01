package modelo;

import modelo.entidades.Heroe;

public abstract class Item {
	private String nombre;
	private String descripcion;

	public Item(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	
	public abstract void usar(Heroe heroe);

		    // Getters y Setters
	public String getNombre() { 
		return nombre; 
	
	}
	public void setNombre(String nombre) { 
		this.nombre = nombre; 
	
	}
	public String getDescripcion() { 
		return descripcion; 
		
	}
	public void setDescripcion(String descripcion) { 
		this.descripcion = descripcion; 
		
	}
}
