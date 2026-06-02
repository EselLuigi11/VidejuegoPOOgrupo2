package modelo;

public class Orquestador {
	private Batalla batallaActual;
	private int contadorTurnos;
	private Partida partidaActual;
	
	// Constructor
	public Orquestador (Batalla batallaActual, Partida partidaActual) {
		this.batallaActual = batallaActual;
		this.contadorTurnos = 1;
		this.partidaActual = partidaActual;
	}
	
	//-------ACTUALIZACIÓN DEL PROCESO DE TURNO-------
	/*
	public String procesarTurno(Accion accion) {
		StringBuilder log = new StringBuilder();
 
		// 1. Verificar que la batalla no haya terminado ya
		if (batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO) {
			return "La batalla ya terminó. No se pueden jugar más turnos.";
		}
 
		// 2. Ejecutar la acción del jugador
		log.append("── Turno " + contadorTurnos + " ──\n");
		accion.ejecutar();
		log.append("[Acción del héroe ejecutada]\n");
		contadorTurnos++;
 
		// 3. Evaluar estado después de la acción del jugador
		EstadoBatalla estadoTrasAccion = batallaActual.evaluarEstado();
 
		if (estadoTrasAccion == EstadoBatalla.VICTORIA) {
			log.append("¡Los héroes han ganado la batalla!\n");
			partidaActual.pasarSiguienteNivel();
			return log.toString();
		}
 
		// 4. Si la batalla sigue, cada enemigo VIVO contraataca
		if (estadoTrasAccion == EstadoBatalla.EN_CURSO) {
			List<Heroe> heroesVivos = batallaActual.getHeroes()
				.stream()
				.filter(h -> h.estaVivo())
				.collect(java.util.stream.Collectors.toList());
 
			for (Enemigo enemigo : batallaActual.getEnemigos()) {
				if (enemigo.estaVivo() && !heroesVivos.isEmpty()) {
					log.append(enemigo.getNombre() + " contraataca.\n");
					enemigo.EnemigoAtaca(heroesVivos); // usa la lógica de IA existente
				}
			}
		}
 
		// 5. Evaluar estado final tras contraataques
		EstadoBatalla estadoFinal = batallaActual.evaluarEstado();
 
		if (estadoFinal == EstadoBatalla.VICTORIA) {
			log.append("¡Los héroes han ganado la batalla!\n");
			partidaActual.pasarSiguienteNivel();
		} else if (estadoFinal == EstadoBatalla.DERROTA) {
			log.append("Game Over. Los enemigos ganaron.\n");
			partidaActual.verificarEstadoPartida();
		}
 
		return log.toString();
	}
*/
	//----------ESTO ES LO QUE HIZO ALGUIEN, NECESITO SABER SI CAMBIO LO DE ARRIBA POR ESTO O NO///
	public void procesarSiguienteTurno (Entidad personaje, Accion accionElegida) {
		if (this.batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO) {
			System.out.println("La batalla ya terminó. No se pueden jugar más turnos");
			return;
		}
	    
		Turno nuevoTurno = new Turno(this.contadorTurnos, personaje);
	    nuevoTurno.procesarAccion(accionElegida);
	    this.contadorTurnos++;

	    // --- EL FINAL ---
	    EstadoBatalla estadoFinal = this.batallaActual.evaluarEstado();

	    if (estadoFinal == EstadoBatalla.VICTORIA) {
	        System.out.println("¡Los héroes han ganado la batalla!");
	        this.partidaActual.pasarSiguienteNivel();
	        
	    } else if (estadoFinal == EstadoBatalla.DERROTA) {
	        System.out.println("Game Over. Los enemigos ganaron.");
	        this.partidaActual.verificarEstadoPartida();
	    }
	}
}
