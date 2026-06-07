package modelo.entidades;
 
import modelo.Arma;
import modelo.Armadura;
import modelo.habilidades.HabEspGuerrero;
 
public class Guerrero extends Heroe implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private HabEspGuerrero habilidadEspecial;
 
    public Guerrero(String nombre, Arma arma, Armadura armadura) {
        super(nombre,
                150, 150,   // vida, vidaMax
                20, 25, 8,  // ataque, defensa, velocidad
                false,
                0, 1,       // experiencia, nivel inicial
                100, 100,   // energia, energiaMax
                0, 0,       // mana, manaMax (el Guerrero no usa mana)
                5, 150,     // probCrit, danoCrit
                arma, armadura);
        HabEspGuerrero habilidadEspecial = new HabEspGuerrero();
 
        // ── Tabla de niveles del Guerrero ──────────────────────────
        // (vidaMax, ataque, defensa, velocidad, energiaMax, manaMax, probCrit, danoCrit)
        // El Guerrero es un tanque: crece mucho en vida y defensa.
        // Guardamos en el constructor las estadisticas. Subir de nivel la aplica. 
        tablaDeNiveles.put(2, new StatsNivel(200, 26, 52, 8,  110, 0, 5, 155));
        tablaDeNiveles.put(3, new StatsNivel(255, 33, 80, 9,  120, 0, 6, 160));
        tablaDeNiveles.put(4, new StatsNivel(315, 40, 110, 9, 130, 0, 6, 168));
        tablaDeNiveles.put(5, new StatsNivel(380, 48, 142, 10,140, 0, 7, 175));
    }
 
    @Override
    public void subirNivel() {
        super.subirNivel(); 
        System.out.println("  ¡El Guerrero " + getNombre() + " subió de nivel!");
    }

	public HabEspGuerrero getHabilidadEspecial() {
		return habilidadEspecial;
	}

	public void setHabilidadEspecial(HabEspGuerrero habilidadEspecial) {
		this.habilidadEspecial = habilidadEspecial;
	}
    
}