package modelo.entidades;

import modelo.Arma;
import modelo.Armadura;
import modelo.habilidades.HabEspMago;

public class Mago extends Heroe {

    private int poderMagico; // Bonus al daño de habilidades mágicas
    private HabEspMago habilidadEspecial; // Habilidad exclusiva del Mago

    public Mago(String nombre, Arma arma, Armadura armadura) {
        super(nombre,
                75, 75,        // vida, vidaMax
                15, 8, 16,     // ataque, defensa, velocidad
                false,
                0, 1,          // experiencia, nivel
                60, 60,        // energia, energiaMax
                120, 120,      // mana, manaMax
                20, 180,       // probCrit, danoCrit
                arma, armadura);
            this.poderMagico = 30;
         HabEspMago habilidadEspecial = new HabEspMago();

        // ── Tabla de niveles del Mago ──────────────────────────────────
        // Formato: tablaDeNiveles.put(nivel,
        //     new StatsNivel(vidaMax, ataque, defensa, velocidad,
        //                    energiaMax, manaMax, probCrit, danoCrit))

        tablaDeNiveles.put(2, new StatsNivel( 85, 17, 10, 17,  65, 140, 22, 200));
        tablaDeNiveles.put(3, new StatsNivel( 95, 19, 12, 18,  70, 162, 24, 222));
        tablaDeNiveles.put(4, new StatsNivel(105, 21, 14, 19,  75, 186, 26, 246));
        tablaDeNiveles.put(5, new StatsNivel(115, 23, 16, 20,  80, 212, 28, 272));
    }

    public HabEspMago getHabilidadEspecial() {
		return habilidadEspecial;
	}

	public void setHabilidadEspecial(HabEspMago habilidadEspecial) {
		this.habilidadEspecial = habilidadEspecial;
	}

	@Override
    public void subirNivel() {
        super.subirNivel(); 
        this.poderMagico += 10; // stat exclusivo del Mago, se suma aparte
        System.out.println("  ¡El Mago " + getNombre() +
                           " domina nuevos conjuros! Poder mágico: " + poderMagico);
    }

    public int getPoderMagico(){ 
    	return poderMagico; 
    }
    
    public void setPoderMagico(int pm){
    	this.poderMagico = pm; 
    }
}