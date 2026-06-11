package modelo.entidades;
 
import modelo.Arma;
import modelo.Armadura;
import modelo.habilidades.HabEspAsesino;
 
public class Asesino extends Heroe implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private HabEspAsesino habilidadEspecial;
 
    // private boolean sigilo; // activar cuando implementen la habilidad
 
    public Asesino(String nombre, Arma arma, Armadura armadura) {
        super(nombre,
                80, 80,      // vida, vidaMax
                27, 8, 20,   // ataque, defensa, velocidad
                false,       // estaDefendiendo
                0, 1,        // experiencia, nivel
                120, 120,    // energia, energiaMax
                0, 0,        // mana, manaMax (el Asesino no usa mana)
                40, 200,     // probCrit, danoCrit
                arma, armadura);
        this.habilidadEspecial = new HabEspAsesino();

 
        // ── Tabla de niveles del Asesino ───────────────────────────────
        // Formato: tablaDeNiveles.put(nivel,
        //     new StatsNivel(vidaMax, ataque, defensa, velocidad,
        //                    energiaMax, manaMax, probCrit, danoCrit))
 
        tablaDeNiveles.put(2, new StatsNivel( 93, 46, 10, 23, 132, 0, 45, 225));
        tablaDeNiveles.put(3, new StatsNivel(106, 58, 12, 26, 145, 0, 51, 252));
        tablaDeNiveles.put(4, new StatsNivel(119, 71, 14, 29, 158, 0, 58, 281));
        tablaDeNiveles.put(5, new StatsNivel(132, 85, 16, 32, 172, 0, 66, 312));
    }
 
    @Override
    public void subirNivel() {
        super.subirNivel(); // aplica la tabla
        System.out.println("  ¡El Asesino " + getNombre() + " subió de nivel!");
    }

	public HabEspAsesino getHabilidadEspecial() {
		return habilidadEspecial;
	}

	public void setHabilidadEspecial(HabEspAsesino habilidadEspecial) {
		this.habilidadEspecial = habilidadEspecial;
	}
    
}