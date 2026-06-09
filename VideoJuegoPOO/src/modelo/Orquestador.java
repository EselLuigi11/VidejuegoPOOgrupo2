package modelo;

import java.util.List;

import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;

public class Orquestador {
	private Batalla batallaActual;
	private int contadorTurnos;
	private Partida partidaActual;
	
	// Constructor
	public Orquestador(Batalla batallaActual, Partida partidaActual) {
		this.batallaActual = batallaActual;
		this.contadorTurnos = 0; 
		this.partidaActual = partidaActual;
	}
	
	// AGREGADO: throws Exception para que tu controlador pueda atajar los errores
	public String procesarTurno(Accion accion) throws Exception {
		StringBuilder log = new StringBuilder();
 
		// 1. Verificar que la batalla no haya terminado ya
		if (batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO) {
			return "La batalla ya terminó. No se pueden jugar más turnos.";
		}
 
		// 2. Ejecutar la acción del jugador (Si falla, lanza Exception al Controlador)
		log.append("── Turno ").append(contadorTurnos).append(" ──\n");
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
					log.append(enemigo.getNombre()).append(" contraataca.\n");
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
}