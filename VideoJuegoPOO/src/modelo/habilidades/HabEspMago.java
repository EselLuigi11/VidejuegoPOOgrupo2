package modelo.habilidades;

import java.util.List;

import modelo.entidades.Enemigo;
import modelo.entidades.Mago;

public class HabEspMago implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String nombre = "Lluvia Arcana";
	private int costoMana = 45;
	private int danoBase = 18;

	public String ejecutar(Mago mago, List<Enemigo> enemigos) {
		//Validaciones
		if (mago == null || enemigos == null || enemigos.isEmpty()) {// Si mago es null, o no hay enemigos no se ejecuta
			return "No se puede usar " + nombre + " sin usuario u objetivos.";
		}

		if (!mago.estaVivo()) {// si mago no esta vivo no se ejecuta. 
			return mago.getNombre() + " no puede usar " + nombre + " porque está fuera de combate.";
		}

		if (!mago.verificarMana(costoMana)) { //si mago no tiene suficiente mana, no se ejecuta. 
			return mago.getNombre() + " no tiene maná suficiente para usar " + nombre + ".";
		}

		mago.consumirMana(costoMana);// si pasa la validacion, se consume el mana y ejecuta hab. 
		int dano = danoBase + mago.getPoderMagico();
		int enemigosGolpeados = 0;

		for (Enemigo enemigo : enemigos) {//le aplica dano a cada enemigo vivo en el campo de batalla.
			if (enemigo != null && enemigo.estaVivo()) {
				enemigo.recibirDano(dano);
				enemigosGolpeados++;
			}
		}

		if (enemigosGolpeados == 0) { // Si no se golpeó a ningún enemigo, se informa que no hay objetivos válidos.
			return "No quedan enemigos vivos para recibir " + nombre + ".";
		}

		return mago.getNombre() + " usa " + nombre + " y causa " + dano
			+ " de daño mágico a " + enemigosGolpeados + " enemigo(s).";
	}
	
	public String getNombre() {
		return nombre;
	}

	public int getCostoMana() {
		return costoMana;
	}

}
