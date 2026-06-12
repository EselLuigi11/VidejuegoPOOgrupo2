package modelo.habilidades;

import java.util.List;

import modelo.entidades.Curador;
import modelo.entidades.Heroe;

public class HabEspCurador {
	private static final long serialVersionUID = 1L;
	private String nombre = "Sanación Grupal";
	private int costoMana = 35;
	private int curacionBase = 30;
	
	public String getNombre() {
		return nombre;
	}

	public int getCostoMana() {
		return costoMana;
	}

	public String ejecutar(Curador curador, List<Heroe> aliados) {
		if (curador == null || aliados == null || aliados.isEmpty()) {
			return "No se puede usar " + nombre + " sin usuario o aliados.";
		}

		if (!curador.estaVivo()) {
			return curador.getNombre() + " no puede usar " + nombre + " porque está fuera de combate.";
		}

		if (!curador.verificarMana(costoMana)) {
			return curador.getNombre() + " no tiene maná suficiente para usar " + nombre + ".";
		}

		curador.consumirMana(costoMana);
		int curacion = curacionBase + curador.getDefensa();
		int aliadosCurados = 0;

		for (Heroe aliado : aliados) {
			if (aliado != null && aliado.estaVivo()) {
				aliado.curarse(curacion);
				aliadosCurados++;
			}
		}

		if (aliadosCurados == 0) {
			return "No hay aliados vivos para recibir " + nombre + ".";
		}

		return curador.getNombre() + " usa " + nombre + " y cura " + curacion
			+ " de vida a " + aliadosCurados + " aliado(s).";
	}
}