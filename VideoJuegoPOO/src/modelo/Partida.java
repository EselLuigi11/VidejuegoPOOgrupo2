package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import modelo.pociones.PocionVida;

public class Partida implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

	private boolean estado;
	private Party grupo;
	private Inventario inventarioPartida;
	private int nivel; // AGREGADO: Para solucionar el error del Repositorio
	private LocalDateTime fechaGuardado;

	
	public Partida() {
		this.estado = true;
		this.nivel = 1; // El juego arranca en el nivel 1
		this.grupo = new Party("Luchadores de la Luz"); // Nombre del grupo
		this.inventarioPartida = new Inventario(20); // 20 objetos por inventario
	
		this.inventarioPartida.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP a un héroe.", 50));
		this.inventarioPartida.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP a un héroe.", 50));
		this.inventarioPartida.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP a un héroe.", 50));
	}

	public void verificarEstadoPartida() {
		if (this.grupo.estaVivo() == false) {
			this.estado = false;
			System.out.println("Game Over");
		}
	}
	
	public void actualizarFechaGuardado() {
		this.fechaGuardado = LocalDateTime.now();
	}
	public String getFechaGuardadoFormateada() {
		if (fechaGuardado == null) 
			return "Sin guardar";
		return fechaGuardado.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		// Formato: día/mes/año hora:minuto
	}

	
	// MÉTODOS AGREGADOS PARA ARREGLAR LOS ERRORES
	public int getNivel() {
		return nivel;
	}
	
	public void pasarSiguienteNivel() {
		this.nivel++;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	// GETTERS Y SETTERS ORIGINALES
	public Inventario getInventarioPartida() { 
		return inventarioPartida; 
	}
	
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Party getGrupo() {
		return grupo;
	}

	public void setGrupo(Party grupo) {
		this.grupo = grupo;
	}

	public void setInventarioPartida(Inventario inventarioPartida) {
		this.inventarioPartida = inventarioPartida;
	}
}
