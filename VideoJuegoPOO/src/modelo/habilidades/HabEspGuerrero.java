package modelo.habilidades;

import modelo.entidades.Enemigo;
import modelo.entidades.Guerrero;

public class HabEspGuerrero {
	private static final long serialVersionUID = 1L;
	private String nombre = "Golpe Protector"; //Golpe que da plus defensa y daño al mismo tiempo
	private int costoEnergia = 35;
	private int plusDano = 10;
	private int plusDefensa = 20;

	public String getNombre() {
		return nombre;
	}

	public int getCostoEnergia() {
		return costoEnergia;
	}

	public String ejecutar(Guerrero guerrero, Enemigo objetivo) {
		if (guerrero == null || objetivo == null) {
			return "No se puede usar " + nombre + " sin usuario u objetivo.";
		}

		if (!guerrero.estaVivo()) {
			return guerrero.getNombre() + " no puede usar " + nombre + " porque está fuera de combate.";
		}

		if (!objetivo.estaVivo()) {
			return objetivo.getNombre() + " ya fue derrotado.";
		}

		if (guerrero.getEnergia() < costoEnergia) {
			return guerrero.getNombre() + " no tiene energía suficiente para usar " + nombre + ".";
		}

		guerrero.setEnergia(guerrero.getEnergia() - costoEnergia);
		guerrero.aumentarDefensa(plusDefensa);
		guerrero.defenderse();

		int dano = guerrero.calcularDanoBase() + plusDano;
		objetivo.recibirDano(dano);

		return guerrero.getNombre() + " usa " + nombre + " contra " + objetivo.getNombre()
			+ ", causa " + dano + " de daño y queda en guardia.";
	}

}