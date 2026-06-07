package modelo.entidades;

import modelo.Arma;
import modelo.Armadura;
import modelo.habilidades.HabEspCurador;


public class Curador extends Heroe implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private HabEspCurador habilidadEspecial;

    public Curador(String nombre, Arma arma, Armadura armadura) {
        super(nombre,
                90, 90,      // vida, vidaMax
                10, 12, 14,  // ataque, defensa, velocidad
                false,       // estaDefendiendo
                0, 1,        // experiencia, nivel
                80, 80,      // energia, energiaMax
                80, 80,      // mana, manaMax
                5, 100,      // probCrit, danoCrit
                arma, armadura);
        this.habilidadEspecial = new HabEspCurador();

        // ── Tabla de niveles del Curador ───────────────────────────────
        // Formato: tablaDeNiveles.put(nivel,
        //     new StatsNivel(vidaMax, ataque, defensa, velocidad,
        //                    energiaMax, manaMax, probCrit, danoCrit))

        tablaDeNiveles.put(2, new StatsNivel(108, 12, 20, 15, 88, 100, 5, 105));
        tablaDeNiveles.put(3, new StatsNivel(126, 14, 28, 16, 96, 122, 5, 110));
        tablaDeNiveles.put(4, new StatsNivel(144, 16, 36, 17,104, 146, 6, 115));
        tablaDeNiveles.put(5, new StatsNivel(162, 18, 44, 18,112, 172, 6, 120));
    }

    @Override
    public void subirNivel() {
        super.subirNivel(); // aplica la tabla
        System.out.println("  ¡El Curador " + getNombre() + " aprende nuevas técnicas de sanación!");
    }

    public void curar() {
        // TODO: implementar curación a un Heroe objetivo
    }

	public HabEspCurador getHabilidadEspecial() {
		return habilidadEspecial;
	}

	public void setHabilidadEspecial(HabEspCurador habilidadEspecial) {
		this.habilidadEspecial = habilidadEspecial;
	}
    
}