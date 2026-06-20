package modelo;

import modelo.entidades.Heroe;

public abstract class Pocion extends Item {
	private static final long serialVersionUID = 1L;
	
	// Constructor
	public Pocion (String nombre, String descripcion) {
		super(nombre, descripcion);
	}
	public abstract void usar(Heroe heroe); 
}
