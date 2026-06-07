package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;
import modelo.acciones.Atacar;
import modelo.acciones.Defender;
import modelo.acciones.UsarItem;

public class OrquestadorF {
	private Batalla batallaActual;
	private int contadorTurnos;
	private int indiceHeroeActual;
	private boolean inicioRondaHeroes;
	private Partida partidaActual;
	
	// Constructor
	public OrquestadorF (Batalla batallaActual, Partida partidaActual) {
		this.batallaActual = batallaActual;
		this.contadorTurnos = 0; //VERIF
		this.indiceHeroeActual = 0;
		this.inicioRondaHeroes = true;
		this.partidaActual = partidaActual;
	}

	public Batalla getBatallaActual() {
		return batallaActual;
	}

	public void setBatallaActual(Batalla batallaActual) {
		this.batallaActual = batallaActual;
		this.contadorTurnos = 1;
		this.indiceHeroeActual = 0;
		this.inicioRondaHeroes = true;
	}

	public int getContadorTurnos() {
		return contadorTurnos;
	}

	public Partida getPartidaActual() {
		return partidaActual;
	}

	public EstadoBatalla getEstadoBatalla() {
		if (batallaActual == null) {
			return EstadoBatalla.DERROTA;
		}
		return batallaActual.evaluarEstado();
	}

	public boolean hayBatallaEnCurso() {
		return batallaActual != null && batallaActual.evaluarEstado() == EstadoBatalla.EN_CURSO;
	}

	public String iniciarBatalla(int numeroBatalla) {
		if (partidaActual == null) {
			return "No hay una partida cargada.";
		}

		List<Heroe> heroesVivos = partidaActual.getGrupo().getHeroesVivos();
		if (heroesVivos.isEmpty()) {
			partidaActual.verificarEstadoPartida();
			return "No hay héroes vivos para iniciar la batalla.";
		}

		Batalla nuevaBatalla = CatalogoBatalla.getInstance().construirBatalla(numeroBatalla, heroesVivos);
		if (nuevaBatalla == null) {
			return "No existe la batalla " + numeroBatalla + ".";
		}

		setBatallaActual(nuevaBatalla);
		return "Comienza la batalla " + numeroBatalla + ".";
	}

	public List<Heroe> getHeroesVivos() {
		List<Heroe> heroesVivos = new ArrayList<>();
		if (batallaActual == null) {
			return heroesVivos;
		}

		for (Heroe heroe : batallaActual.getHeroes()) {
			if (heroe.estaVivo()) {
				heroesVivos.add(heroe);
			}
		}
		return heroesVivos;
	}

	public List<Enemigo> getEnemigosVivos() {
		List<Enemigo> enemigosVivos = new ArrayList<>();
		if (batallaActual == null) {
			return enemigosVivos;
		}

		for (Enemigo enemigo : batallaActual.getEnemigos()) {
			if (enemigo.estaVivo()) {
				enemigosVivos.add(enemigo);
			}
		}
		return enemigosVivos;
	}

	public Heroe getHeroeActual() {
		if (batallaActual == null) {
			return null;
		}

		List<Heroe> heroes = batallaActual.getHeroes();
		for (int i = indiceHeroeActual; i < heroes.size(); i++) {
			Heroe heroe = heroes.get(i);
			if (heroe.estaVivo()) {
				indiceHeroeActual = i;
				return heroe;
			}
		}

		return null;
	}

	public Accion crearAccionAtacar(Heroe atacante, Enemigo objetivo) {
		if (atacante == null || objetivo == null) {
			throw new IllegalArgumentException("Atacante y objetivo son obligatorios.");
		}
		return new Atacar(atacante, objetivo);
	}

	public Accion crearAccionDefender(Heroe defensor) {
		if (defensor == null) {
			throw new IllegalArgumentException("El defensor es obligatorio.");
		}
		return new Defender(defensor);
	}

	public Accion crearAccionUsarItem(Heroe heroe, Item item) {
		if (heroe == null || item == null) {
			throw new IllegalArgumentException("Héroe e item son obligatorios.");
		}
		return new UsarItem(partidaActual, heroe, item);
	}
		
	//-------ACTUALIZACIÓN DEL PROCESO DE TURNO-------
	public String procesarTurno(Accion accion) {
		StringBuilder log = new StringBuilder();

		if (batallaActual == null) {
			return "No hay una batalla activa.";
		}

		if (accion == null) {
			return "No se eligió ninguna acción.";
		}
	 
	    // 1. Verificar que la batalla no haya terminado ya
		if (batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO) {
			return "La batalla ya terminó. No se pueden jugar más turnos.";
		}
	 
		Heroe heroeActual = getHeroeActual();
		if (heroeActual == null) {
			ejecutarTurnoEnemigos(log);
			return log.toString();
		}

		// 2. Ejecutar la acción del héroe actual
		log.append("── Ronda " + contadorTurnos + " ──\n");
		log.append("Turno de " + heroeActual.getNombre() + ".\n");
		if (inicioRondaHeroes) {
			limpiarDefensaHeroes();
			inicioRondaHeroes = false;
		}
		accion.ejecutar();
		log.append("[Acción de " + heroeActual.getNombre() + " ejecutada]\n");
	 
		// 3. Evaluar estado después de la acción del jugador
		EstadoBatalla estadoTrasAccion = batallaActual.evaluarEstado();
 
		if (estadoTrasAccion == EstadoBatalla.VICTORIA) {
			log.append("¡Los héroes han ganado la batalla!\n");
			repartirExperiencia();
			return log.toString();
		}

		avanzarAlSiguienteHeroe();
		Heroe siguienteHeroe = getHeroeActual();
		if (siguienteHeroe != null) {
			log.append("Siguiente héroe: " + siguienteHeroe.getNombre() + ".\n");
			return log.toString();
		}

		// 4. Si ya actuaron todos los héroes vivos, atacan los enemigos.
		ejecutarTurnoEnemigos(log);
	 
		// 5. Evaluar estado final tras ataques enemigos
		EstadoBatalla estadoFinal = batallaActual.evaluarEstado();
	 
		if (estadoFinal == EstadoBatalla.VICTORIA) {
			log.append("¡Los héroes han ganado la batalla!\n");
			repartirExperiencia();
		} else if (estadoFinal == EstadoBatalla.DERROTA) {
			log.append("Game Over. Los enemigos ganaron.\n");
			partidaActual.verificarEstadoPartida();
		}
	 
		return log.toString();
	}

	private void avanzarAlSiguienteHeroe() {
		if (batallaActual == null) {
			return;
		}

		List<Heroe> heroes = batallaActual.getHeroes();
		for (int i = indiceHeroeActual + 1; i < heroes.size(); i++) {
			if (heroes.get(i).estaVivo()) {
				indiceHeroeActual = i;
				return;
			}
		}

		indiceHeroeActual = heroes.size();
	}

	private void ejecutarTurnoEnemigos(StringBuilder log) {
		List<Heroe> heroesVivos = getHeroesVivos();

		for (Enemigo enemigo : batallaActual.getEnemigos()) {
			if (enemigo.estaVivo() && !heroesVivos.isEmpty()) {
				log.append(enemigo.getNombre() + " contraataca.\n");
				enemigo.EnemigoAtaca(heroesVivos);
				heroesVivos = getHeroesVivos();
			}
		}

		contadorTurnos++;
		indiceHeroeActual = 0;
		inicioRondaHeroes = true;
	}

	private void repartirExperiencia() {
		List<Heroe> heroesVivos = getHeroesVivos();
		if (heroesVivos.isEmpty()) {
			return;
		}

		for (Enemigo enemigo : batallaActual.getEnemigos()) {
			for (Heroe heroe : heroesVivos) {
				enemigo.otorgarExperiencia(heroe);
			}
		}
	}

	private void limpiarDefensaHeroes() {
		for (Heroe heroe : batallaActual.getHeroes()) {
			heroe.setEstaDefendiendo(false);
		}
	}

}