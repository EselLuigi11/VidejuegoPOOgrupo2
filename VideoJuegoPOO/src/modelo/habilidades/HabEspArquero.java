package modelo.habilidades;

import modelo.entidades.Arquero;
import modelo.entidades.Enemigo;

public class HabEspArquero {
	private static final long serialVersionUID = 1L;
	private String nombre = "Disparo Preciso";
	private int costoEnergia = 30;
	private double multiplicadorDano = 1.8;
	
	public String getNombre() {
		return nombre;
	}

	public int getCostoEnergia() {
		return costoEnergia;
	}

	public String ejecutar(Arquero arquero, Enemigo objetivo) {
		if (arquero == null || objetivo == null) {
			return "No se puede usar " + nombre + " sin usuario u objetivo.";
		}

		if (!arquero.estaVivo()) {
			return arquero.getNombre() + " no puede usar " + nombre + " porque está fuera de combate.";
		}

		if (!objetivo.estaVivo()) {
			return objetivo.getNombre() + " ya está derrotado.";
		}

		if (arquero.getEnergia() < costoEnergia) {
			return arquero.getNombre() + " no tiene energía suficiente para usar " + nombre + ".";
		}

		arquero.setEnergia(arquero.getEnergia() - costoEnergia);
		int dano = (int) Math.round(arquero.calcularDanoBase() * multiplicadorDano);
		objetivo.recibirDano(dano);

		return arquero.getNombre() + " usa " + nombre + " contra " + objetivo.getNombre()
			+ " y causa " + dano + " de daño.";
	}
}