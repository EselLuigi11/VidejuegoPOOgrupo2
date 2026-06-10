package modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import modelo.acciones.Atacar;
import modelo.acciones.Defender;
import modelo.acciones.UsarItem;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;

public class OrquestadorF {
	private Batalla batallaActual;
	private int contadorTurnos;
	private Partida partidaActual;
	private List<Entidad> ordenTurnos;
	private int indiceTurnoActual;
	private boolean resultadoProcesado;
	
	// Constructor
	public OrquestadorF(Batalla batallaActual, Partida partidaActual) {
		this.batallaActual = batallaActual;
		this.contadorTurnos = 0;
		this.partidaActual = partidaActual;
		this.ordenTurnos = new ArrayList<>();
		this.indiceTurnoActual = 0;
		this.resultadoProcesado = false;
		actualizarOrdenTurnos();
	}

	public Batalla getBatallaActual() {
		return batallaActual;
	}

	public void setBatallaActual(Batalla batallaActual) {
		this.batallaActual = batallaActual;
		this.contadorTurnos = 0;
		this.indiceTurnoActual = 0;
		this.resultadoProcesado = false;
		actualizarOrdenTurnos();
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
			return "No hay heroes vivos para iniciar la batalla.";
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
			if (heroe != null && heroe.estaVivo()) {
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
			if (enemigo != null && enemigo.estaVivo()) {
				enemigosVivos.add(enemigo);
			}
		}
		return enemigosVivos;
	}

	public Entidad getPersonajeActual() {
		actualizarOrdenTurnos();

		if (ordenTurnos.isEmpty()) {
			return null;
		}

		return ordenTurnos.get(indiceTurnoActual);
	}

	public Heroe getHeroeActual() {
		Entidad personajeActual = getPersonajeActual();

		if (personajeActual instanceof Heroe) {
			return (Heroe) personajeActual;
		}

		return null;
	}

	public List<Entidad> getOrdenTurnos() {
		actualizarOrdenTurnos();
		return new ArrayList<>(ordenTurnos);
	}

	public boolean esTurnoDeHeroe() {
		return getPersonajeActual() instanceof Heroe;
	}

	public boolean esTurnoDeEnemigo() {
		return getPersonajeActual() instanceof Enemigo;
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
			throw new IllegalArgumentException("Heroe e item son obligatorios.");
		}
		return new UsarItem(partidaActual, heroe, item);
	}
		
	// Procesa el siguiente turno respetando el orden global por velocidad.
	public String procesarTurno(Accion accion) {
		return procesarTurno(null, accion);
	}

	public String procesarTurno(Heroe heroeQueActua, Accion accion) {
		StringBuilder log = new StringBuilder();

		if (batallaActual == null) {
			return "No hay una batalla activa.";
		}

		if (batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO) {
			return resolverFinDeBatalla(log);
		}

		actualizarOrdenTurnos();
		if (ordenTurnos.isEmpty()) {
			return resolverFinDeBatalla(log);
		}

		procesarTurnosAutomaticosEnemigos(log);

		if (batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO) {
			return log.toString();
		}

		Entidad personajeActual = getPersonajeActual();
		if (!(personajeActual instanceof Heroe)) {
			return log.toString();
		}

		if (heroeQueActua != null && heroeQueActua != personajeActual) {
			return "No es el turno de " + heroeQueActua.getNombre()
					+ ". Es el turno de " + personajeActual.getNombre() + ".";
		}

		if (accion == null) {
			return "Es turno de " + personajeActual.getNombre() + ". No se eligio ninguna accion.";
		}

		procesarTurnoHeroe((Heroe) personajeActual, accion, log);
		procesarTurnosAutomaticosEnemigos(log);
		resolverFinDeBatalla(log);

		return log.toString();
	}

	private void procesarTurnoHeroe(Heroe heroe, Accion accion, StringBuilder log) {
		if (heroe == null || !heroe.estaVivo()) {
			avanzarTurnoDesde(heroe);
			return;
		}

		log.append("-- Turno ").append(contadorTurnos)
				.append(": ").append(heroe.getNombre()).append(" --\n");

		heroe.setEstaDefendiendo(false);
		accion.ejecutar();
		log.append("[Accion de ").append(heroe.getNombre()).append(" ejecutada]\n");
		contadorTurnos++;

		avanzarTurnoDesde(heroe);
		resolverFinDeBatalla(log);
	}

	// EN OrquestadorF.java
	// REEMPLAZAR procesarTurnosAutomaticosEnemigos:

	/**
	 * Procesa UN SOLO turno de enemigo (el que corresponde según ordenTurnos).
	 * Se llama recursivamente hasta que el turno actual sea de un Héroe o la batalla termine.
	 * Garantiza alternancia estricta: héroe → enemigo → héroe → ...
	 */
	private void procesarTurnosAutomaticosEnemigos(StringBuilder log) {
	    actualizarOrdenTurnos();

	    // Avanzamos de a un turno enemigo hasta topar con un héroe o fin de batalla
	    while (batallaActual.evaluarEstado() == EstadoBatalla.EN_CURSO
	            && !ordenTurnos.isEmpty()
	            && getPersonajeActual() instanceof Enemigo) {

	        Enemigo enemigo = (Enemigo) getPersonajeActual();

	        if (enemigo != null && enemigo.estaVivo()) {
	            List<Heroe> heroesVivos = getHeroesVivos();
	            if (!heroesVivos.isEmpty()) {
	                log.append("-- Turno ").append(contadorTurnos)
	                        .append(": ").append(enemigo.getNombre()).append(" --\n");

	                enemigo.EnemigoAtaca(heroesVivos);  // IA del enemigo — ataca a 1 héroe

	                log.append(enemigo.getNombre()).append(" ataca.\n");
	                contadorTurnos++;
	            }
	        }

	        avanzarTurnoDesde(enemigo);

	        // ── STOP: después de cada enemigo, si el próximo es un héroe, salimos.
	        // Esto garantiza que el jugador pueda actuar antes del siguiente enemigo.
	        if (getPersonajeActual() instanceof Heroe) {
	            break;
	        }
	    }
	}

	//Funcion importante de logica para determinar el orden de entidad. 
	private void actualizarOrdenTurnos() {
		if (batallaActual == null) {
			ordenTurnos = new ArrayList<>();
			indiceTurnoActual = 0;
			return;
		}

		Entidad personajeActual = null;
		if (!ordenTurnos.isEmpty() && indiceTurnoActual >= 0 && indiceTurnoActual < ordenTurnos.size()) {
			personajeActual = ordenTurnos.get(indiceTurnoActual);
		}

		List<Entidad> nuevoOrden = new ArrayList<>();
		for (Heroe heroe : batallaActual.getHeroes()) {
			if (heroe != null && heroe.estaVivo()) {
				nuevoOrden.add(heroe);
			}
		}
		for (Enemigo enemigo : batallaActual.getEnemigos()) {
			if (enemigo != null && enemigo.estaVivo()) {
				nuevoOrden.add(enemigo);
			}
		}
		// Ordenar por velocidad descendente (más rápido primero)
		nuevoOrden.sort(Comparator.comparingInt(Entidad::getVelocidad).reversed());
		ordenTurnos = nuevoOrden;

		if (ordenTurnos.isEmpty()) {
			indiceTurnoActual = 0;
			return;
		}

		int indicePreservado = personajeActual == null ? -1 : ordenTurnos.indexOf(personajeActual);
		if (indicePreservado >= 0) {
			indiceTurnoActual = indicePreservado;
		} else if (indiceTurnoActual >= ordenTurnos.size()) {
			indiceTurnoActual = 0;
		}
	}

	private void avanzarTurnoDesde(Entidad personajeQueActuo) {
		actualizarOrdenTurnos();

		if (ordenTurnos.isEmpty()) {
			indiceTurnoActual = 0;
			return;
		}

		int indicePersonaje = ordenTurnos.indexOf(personajeQueActuo);
		if (indicePersonaje >= 0) {
			indiceTurnoActual = (indicePersonaje + 1) % ordenTurnos.size();
		} else {
			indiceTurnoActual = indiceTurnoActual % ordenTurnos.size();
		}
	}

	private String resolverFinDeBatalla(StringBuilder log) {
		EstadoBatalla estadoFinal = batallaActual.evaluarEstado();

		if (estadoFinal == EstadoBatalla.EN_CURSO) {
			return log.toString();
		}

		if (resultadoProcesado) {
			return log.toString();
		}

		resultadoProcesado = true;

		if (estadoFinal == EstadoBatalla.VICTORIA) {
			log.append("¡Los heroes han ganado la batalla!\n");
			repartirExperiencia();
		} else if (estadoFinal == EstadoBatalla.DERROTA) {
			log.append("Game Over. Los enemigos ganaron.\n");
			if (partidaActual != null) {
				partidaActual.verificarEstadoPartida();
			}
		}

		return log.toString();
	}

	private void repartirExperiencia() {
		List<Heroe> heroesVivos = getHeroesVivos();
		if (heroesVivos.isEmpty() || batallaActual == null) {
			return;
		}

		for (Enemigo enemigo : batallaActual.getEnemigos()) {
			if (enemigo != null) {
				for (Heroe heroe : heroesVivos) {
					enemigo.otorgarExperiencia(heroe);
				}
			}
		}
	}
}