package modelo;

public class Turno {
	// 1. Atributos
	private int numeroDeTurno;
	private Entidad personajeActual;
	
	// 2. Constructor
	public Turno (int numeroDeTurno, Entidad personajeActual) {
		this.numeroDeTurno = numeroDeTurno;
		this.personajeActual = personajeActual;
	}
	
	public void procesarAccion (Accion accionElegida) {
		accionElegida.ejecutar();
	}
}
