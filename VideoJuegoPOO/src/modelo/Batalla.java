package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.entidades.Arquero;
import modelo.entidades.Asesino;
import modelo.entidades.Curador;
import modelo.entidades.Enemigo;
import modelo.entidades.Guerrero;
import modelo.entidades.Heroe;
import modelo.entidades.Mago;
import modelo.pociones.PocionMana;
import modelo.pociones.PocionVida;

public class Batalla {
	private List<Heroe> heroes;
	private List<Enemigo> enemigos;
	private EstadoBatalla estadoActual;

	public Batalla(List<Heroe> heroes, List<Enemigo> enemigos) {
		this.heroes = heroes;
		this.enemigos = enemigos;
		estadoActual = EstadoBatalla.EN_CURSO;
	}

	public EstadoBatalla evaluarEstado() {
		boolean todosHeroesMuertos = true;
		for (Heroe h : heroes) {
			if (h != null && h.estaVivo()) {
				todosHeroesMuertos = false;
			}
		}

		boolean todosEnemigosMuertos = true;
		for (Enemigo e : enemigos) {
			if (e != null && e.estaVivo()) {
				todosEnemigosMuertos = false;
			}
		}

		if (todosHeroesMuertos) {
			estadoActual = EstadoBatalla.DERROTA;
		} else if (todosEnemigosMuertos) {
			estadoActual = EstadoBatalla.VICTORIA;
		} else {
			estadoActual = EstadoBatalla.EN_CURSO;
		}
		return estadoActual;
	}

	public boolean hayEnemigosVivos() {
		for (Enemigo e : enemigos) {
			if (e != null && e.estaVivo()) return true;
		}
		return false;
	}

	public List<Heroe> getHeroesVivos() {
		List<Heroe> vivos = new ArrayList<>();
		for (Heroe h : heroes) {
			if (h != null && h.estaVivo()) vivos.add(h);
		}
		return vivos;
	}

	public List<Enemigo> getEnemigosVivos() {
		List<Enemigo> vivos = new ArrayList<>();
		for (Enemigo e : enemigos) {
			if (e != null && e.estaVivo()) vivos.add(e);
		}
		return vivos;
	}

	/**
	 * Suma la experiencia total que otorgan TODOS los enemigos de esta batalla.
	 * Cohesión POO: el cálculo vive en el Modelo (Batalla), no en el Controlador.
	 */
	public int getExperienciaTotalOtorgada() {
		int total = 0;
		for (Enemigo e : enemigos) {
			if (e != null) total += e.getExperienciaOtorgada();
		}
		return total;
	}

	public void otorgarRecompensas(Inventario inv, int numeroBatalla) {
		if (inv == null) return;

		List<Heroe> vivos = getHeroesVivos();
		System.out.println("=== Recompensas de la batalla " + numeroBatalla + " ===");

		switch (numeroBatalla) {
			case 1:
				for (Heroe h : vivos) {
					Arma arma;
					if (h instanceof Guerrero) {
						arma = new Arma("Espada de Hierro", "Espada de un goblin derrotado.", 8);
					} else if (h instanceof Arquero) {
						arma = new Arma("Arco Corto", "Arco ágil del campo de batalla.", 7);
					} else if (h instanceof Asesino) {
						arma = new Arma("Daga Envenenada", "Daga con filo goblin.", 9);
					} else if (h instanceof Mago) {
						arma = new Arma("Bastón de Rama", "Canal arcano primitivo.", 5);
					} else if (h instanceof Curador) {
						arma = new Arma("Vara Sagrada", "Emana energía curativa.", 4);
					} else {
						arma = new Arma("Arma Simple", "Botín básico.", 6);
					}
					h.equiparArma(arma);
					System.out.println(h.getNombre() + " equipó: " + arma.getNombre());
				}
				inv.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP.", 50));
				inv.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP.", 50));
				break;

			case 2:
				inv.agregarItem(new PocionVida("Poción de Vida Mayor", "Restaura 80 HP.", 80));
				inv.agregarItem(new PocionVida("Poción de Vida Mayor", "Restaura 80 HP.", 80));
				inv.agregarItem(new PocionMana("Poción de Maná", "Restaura 40 MP.", 40));
				inv.agregarItem(new Arma("Espada Reforzada", "Hoja de ladrón veterano.", 12));
				break;

			case 3:
				inv.agregarItem(new PocionVida("Elixir de Vida", "Restaura 120 HP.", 120));
				inv.agregarItem(new PocionVida("Elixir de Vida", "Restaura 120 HP.", 120));
				inv.agregarItem(new Arma("Báculo del Gólem", "Forjado del núcleo del gólem.", 18));
				inv.agregarItem(new Armadura("Loriga de Piedra", "Alta defensa del gólem.", 20));
				break;

			default:
				inv.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP.", 50));
				break;
		}
	}

	public List<Enemigo> getEnemigos() { return enemigos; }
	public List<Heroe> getHeroes() { return heroes; }
}
