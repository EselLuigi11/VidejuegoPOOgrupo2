package modelo.pociones;

import modelo.Pocion;
import modelo.entidades.Heroe;

public class PocionMana extends Pocion {
	private static final long serialVersionUID = 1L;
	private int manaRecuperado;
	
	public PocionMana(String nombre, String descripcion, int manaRecuperado) {
		super(nombre, descripcion);
		this.manaRecuperado = manaRecuperado;
	}
	
	@Override
	public void usar(Heroe heroe) {
		System.out.println("Usando " + this.getNombre() + " en " + heroe.getNombre());
		heroe.recuperarMana(manaRecuperado);
		System.out.println("¡" + heroe.getNombre() + " ha recuperado " + this.manaRecuperado + " puntos de mana!");
	}
}