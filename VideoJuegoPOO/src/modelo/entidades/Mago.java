package modelo.entidades;

import java.util.List;

import modelo.Arma;
import modelo.Armadura;
import modelo.habilidades.HabEspMago;

public class Mago extends Heroe {
	private static final long serialVersionUID = 1L;

	private int poderMagico;
	private HabEspMago habilidadEspecial;

	public Mago(String nombre, Arma arma, Armadura armadura) {
		super(nombre,
				75, 75,
				15, 8, 16,
				false,
				0, 1,
				120, 120,
				20, 180,
				arma, armadura);
		this.poderMagico = 30;
		this.habilidadEspecial = new HabEspMago();

		tablaDeNiveles.put(2, new StatsNivel(85, 17, 10, 17, 140, 22, 200));
		tablaDeNiveles.put(3, new StatsNivel(95, 19, 12, 18, 162, 24, 222));
		tablaDeNiveles.put(4, new StatsNivel(105, 21, 14, 19, 186, 26, 246));
		tablaDeNiveles.put(5, new StatsNivel(115, 23, 16, 20, 212, 28, 272));
	}

	@Override
	public String usarHabilidadEspecial(Enemigo objetivo, List<Enemigo> enemigosVivos, List<Heroe> aliadosVivos) {
		return habilidadEspecial.ejecutar(this, enemigosVivos);
	}

	public HabEspMago getHabilidadEspecial() { return habilidadEspecial; }
	public void setHabilidadEspecial(HabEspMago h) { this.habilidadEspecial = h; }

	@Override
	public void subirNivel() {
		super.subirNivel();
		this.poderMagico += 10;
	}

	public int getPoderMagico() { return poderMagico; }
	public void setPoderMagico(int pm) { this.poderMagico = pm; }
}
