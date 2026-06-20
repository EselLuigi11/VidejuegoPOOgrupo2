package modelo;

import modelo.entidades.Heroe;

public class Armadura extends Item {
	private static final long serialVersionUID = 1L;
	private int plusDefensa;

	public Armadura(String nombre, String descripcion, int defensa) {
		super(nombre, descripcion);
		this.plusDefensa = defensa;
	}

	public void usar(Heroe heroe) {
		heroe.equiparArmadura(this);
	}

	public int getplusDefensa() {
		return plusDefensa;
	}

	public void setDefensa(int plusDefensa) {
		this.plusDefensa = plusDefensa;
	}

	public String toString() {
		return super.toString() + "| Defensa: " + plusDefensa;
	}
}
