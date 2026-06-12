package modelo.pociones;

import modelo.Pocion;
import modelo.entidades.Heroe;

public class PocionVelocidad extends Pocion {
	private static final long serialVersionUID = 1L;
	private int aumentoVelocidad;
	
	public PocionVelocidad(String nombre, String descripcion, int aumentoVelocidad) {
		super(nombre, descripcion);
		this.aumentoVelocidad = aumentoVelocidad;
	}
	
	@Override
	public void usar(Heroe heroe) {
		System.out.println("Usando " + this.getNombre() + " en " + heroe.getNombre());
		heroe.aumentarVel(aumentoVelocidad);
		System.out.println("¡" + heroe.getNombre() + " ha aumentado " + this.aumentoVelocidad + " de velocidad!");
	}
}