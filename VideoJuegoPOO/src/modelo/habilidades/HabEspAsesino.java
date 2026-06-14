package modelo.habilidades;

import modelo.entidades.Asesino;
import modelo.entidades.Enemigo;

public class HabEspAsesino implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String nombre = "Puñalada Certera";
	private int costoEnergia = 45;
	private double multiplicadorDano = 2.4;

	public String getNombre() {
		return nombre;
	}

	public int getCostoEnergia() {
		return costoEnergia;
	}

	public String ejecutar(Asesino asesino, Enemigo objetivo) {
		if (asesino == null || objetivo == null) {
			return "No se puede usar " + nombre + " sin usuario u objetivo.";
		}

		if (!asesino.estaVivo()) {
			return asesino.getNombre() + " no puede usar " + nombre + " porque está fuera de combate.";
		}

		if (!objetivo.estaVivo()) {
			return objetivo.getNombre() + " ya está derrotado.";
		}

		if (asesino.getEnergia() < costoEnergia) {
			return asesino.getNombre() + " no tiene energía suficiente para usar " + nombre + ".";
		}

		asesino.setEnergia(asesino.getEnergia() - costoEnergia);
		int dano = (int) Math.round(asesino.calcularDanoBase() * multiplicadorDano); //Calcula el daño base del asesino y lo multiplica por el multiplicador para obtener el daño total de la habilidad.
		objetivo.recibirDano(dano);

		return asesino.getNombre() + " usa " + nombre + " contra " + objetivo.getNombre()
			+ " y causa " + dano + " de daño crítico.";
	}

}
