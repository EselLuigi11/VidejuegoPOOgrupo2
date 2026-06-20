package modelo.entidades;

import java.util.List;

import modelo.Arma;
import modelo.Armadura;
import modelo.habilidades.HabEspGuerrero;

public class Guerrero extends Heroe implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private HabEspGuerrero habilidadEspecial;

	public Guerrero(String nombre, Arma arma, Armadura armadura) {
		super(nombre,
				150, 150,
				20, 25, 8,
				false,
				0, 1,
				80, 80,
				5, 150,
				arma, armadura);
		this.habilidadEspecial = new HabEspGuerrero();

		tablaDeNiveles.put(2, new StatsNivel(200, 26, 52, 8, 90, 5, 155));
		tablaDeNiveles.put(3, new StatsNivel(255, 33, 80, 9, 100, 6, 160));
		tablaDeNiveles.put(4, new StatsNivel(315, 40, 110, 9, 110, 6, 168));
		tablaDeNiveles.put(5, new StatsNivel(380, 48, 142, 10, 120, 7, 175));
	}

	@Override
	public String usarHabilidadEspecial(Enemigo objetivo, List<Enemigo> enemigosVivos, List<Heroe> aliadosVivos) {
		return habilidadEspecial.ejecutar(this, objetivo);
	}

	@Override
	public void subirNivel() {
		super.subirNivel();
	}

	public HabEspGuerrero getHabilidadEspecial() { return habilidadEspecial; }
	public void setHabilidadEspecial(HabEspGuerrero h) { this.habilidadEspecial = h; }
}
