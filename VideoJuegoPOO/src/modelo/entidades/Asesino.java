package modelo.entidades;

import java.util.List;

import modelo.Arma;
import modelo.Armadura;
import modelo.habilidades.HabEspAsesino;

public class Asesino extends Heroe implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private HabEspAsesino habilidadEspecial;

	public Asesino(String nombre, Arma arma, Armadura armadura) {
		super(nombre,
				80, 80,
				27, 8, 20,
				false,
				0, 1,
				90, 90,
				40, 200,
				arma, armadura);
		this.habilidadEspecial = new HabEspAsesino();

		tablaDeNiveles.put(2, new StatsNivel(93, 46, 10, 23, 105, 45, 225));
		tablaDeNiveles.put(3, new StatsNivel(106, 58, 12, 26, 120, 51, 252));
		tablaDeNiveles.put(4, new StatsNivel(119, 71, 14, 29, 135, 58, 281));
		tablaDeNiveles.put(5, new StatsNivel(132, 85, 16, 32, 150, 66, 312));
	}

	@Override
	public String usarHabilidadEspecial(Enemigo objetivo, List<Enemigo> enemigosVivos, List<Heroe> aliadosVivos) {
		return habilidadEspecial.ejecutar(this, objetivo);
	}

	@Override
	public void subirNivel() {
		super.subirNivel();
	}

	public HabEspAsesino getHabilidadEspecial() { return habilidadEspecial; }
	public void setHabilidadEspecial(HabEspAsesino h) { this.habilidadEspecial = h; }
}
