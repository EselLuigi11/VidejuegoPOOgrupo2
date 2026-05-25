package modelo;

public class Partida {
	private int nivel;
	private boolean estado;
	private Party grupo;
	
	public Partida() {
		this.nivel = 1;
		this.estado = true;
		this.grupo = new Party("Los vengadores");
	}
	
	public void pasarSiguienteNivel() {
		this.nivel++;
		System.out.println("Avanzaste al nivel: " + this.nivel + "!");
	}
	
	public void verificarEstadoPartida() {
		if (this.grupo.estaVivo() == false) {
			this.estado = false;
			System.out.println("Game Over");
		}
	}
}
