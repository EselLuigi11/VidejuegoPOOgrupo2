package modelo;

public class Partida implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

	
	private boolean estado;
	private Party grupo;
	private Inventario inventarioPartida;
	
	public Partida() {
		this.estado = true;
		this.grupo = new Party("Luchadores de la Luz"); //Nombre del grupo, se puede cambiar por input del usuario
		this.inventarioPartida = new Inventario(20); //20 objetos por inventario, podemos mejorar el espacio en algun nivel
	}
	
	
	public void verificarEstadoPartida() {
		if (this.grupo.estaVivo() == false) {
			this.estado = false;
			System.out.println("Game Over");
		}
	}
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


