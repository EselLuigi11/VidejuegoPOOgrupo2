package modelo.entidades;

import java.util.List;

import modelo.Arma;
import modelo.Armadura;
import modelo.Entidad;
import modelo.Inventario;
import modelo.Partida;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Heroe extends Entidad {
	
	private int experiencia;
	private int nivel;
	private int energia;
	private int energiaMax;
	private int mana;
	private int manaMax;
	private int probCrit;
	private int danoCrit;
	private Arma arma;
	private Armadura armadura;
    protected HashMap<Integer, StatsNivel> tablaDeNiveles; //key es el nivel, value es el objeto StatsNivel con las estadísticas para ese nivel
    //Ejemplo en Guerrero:
       // *   tablaDeNiveles.put(2, new StatsNivel(200, 28, 50, 8, 110, 0, 5, 150));
       // A nivel 2 del guerrero se reajustan las estadisticas a vidaMax 200, ataque 28 ... etc 

	
	
	
	public Heroe(String nombre, int vida, int vidaMax,
			int ataque, int defensa,
			int velocidad, boolean estaDefendiendo,
			int experiencia, int nivel,
			int energia, int energiaMax,
			int mana, int manaMax,
			int probCrit, int danoCrit,
			Arma arma, Armadura armadura) {
		
		super(nombre, vida, vidaMax, ataque, defensa, velocidad, estaDefendiendo);
		
        this.experiencia  = experiencia;
        this.nivel        = nivel;
        this.energia      = energia;
        this.energiaMax   = energiaMax;
        this.mana         = mana;
        this.manaMax      = manaMax;
        this.probCrit     = probCrit;
        this.danoCrit     = danoCrit;
        this.arma         = arma;
        this.armadura     = armadura;
        this.tablaDeNiveles = new HashMap<>();
	}

	public int calcularDanoBase() {
		int danoTotal = this.getAtaque();
		if (arma != null) {
			danoTotal += arma.getPlusDano();
		}
		return danoTotal;
	}
	
	public boolean verificarMana(int costoHabilidad) {
		return this.mana >= costoHabilidad;
	}
	
	public void consumirMana(int costoHabilidad) {
		if (verificarMana(costoHabilidad)) {
			this.mana -= costoHabilidad;
		}
	}
	
	public void recuperarMana(int cantidad) {
		this.mana += cantidad;
		
		if (this.mana > this.manaMax) {
			this.mana = this.manaMax;
		}
	}
	
	public void recuperarEnergia(int cantidad) {
		this.energia += cantidad;
		
		if (this.energia > this.energiaMax) {
			this.energia = this.energiaMax;
		}
	}
	
	public void ganarExperiencia(int exp) {
		this.experiencia += exp;
		if (this.experiencia >= nivel * 105) {
			subirNivel();
		}
	}
	public void subirNivel() {
		this.nivel++;
        this.experiencia = 0;
        System.out.println("¡" + this.getNombre() + " subió al nivel " + this.nivel + "!");
 
        StatsNivel statsDelNivel = tablaDeNiveles.get(this.nivel);
        if (statsDelNivel != null) {
            // ── Camino A: stats definidos manualmente en la tabla ──
            this.setVidaMax(statsDelNivel.getVidaMax());
            this.setAtaque(statsDelNivel.getAtaque());
            this.setDefensa(statsDelNivel.getDefensa());
            this.setVelocidad(statsDelNivel.getVelocidad());
            this.energiaMax = statsDelNivel.getEnergiaMax();
            this.manaMax    = statsDelNivel.getManaMax();
            this.probCrit   = statsDelNivel.getProbCrit();
            this.danoCrit   = statsDelNivel.getDanoCrit();
            System.out.println("  → Stats aplicados desde tabla: " + statsDelNivel);
        } 
        else 
        {
            // ── Camino B: incremento genérico de fallback ──
            // Útil si el jugador llega a un nivel no definido en la tabla.
            this.setVidaMax(this.getVidaMax() + 20);
            this.setAtaque(this.getAtaque() + 5);
            this.setDefensa(this.getDefensa() + 5);
            this.setManaMax(this.getManaMax() + 10);
            this.energiaMax += 10;
            System.out.println("  → Stats aplicados con incremento genérico (nivel no definido en tabla).");
        }
        // Al subir de nivel, el héroe recupera toda su vida, mana y energía.
        this.setVida(this.getVidaMax());
        this.setMana(this.manaMax);
        this.energia = this.energiaMax;
	}
	
	public void equiparArma(Arma arma, Inventario inventario) {
		// 1. Verificar que el arma esté en la mochila
		if (!inventario.contieneItem(arma)) {
			System.out.println("El arma '" + arma.getNombre() + "' no está en la mochila.");
			return;
		}
		// 2. Si ya tenía un arma equipada, la devuelve al inventario
		if (this.arma != null) {
			inventario.agregarItem(this.arma);
			System.out.println(this.getNombre() + " desequipa: " + this.arma.getNombre());
		}
		// 3. Equipar el arma nueva y sacarla de la mochila
		this.arma = arma;
		inventario.eliminarItem(arma);
		System.out.println(this.getNombre() + " equipa: " + arma.getNombre());
	}

	
	public void equiparArmadura(Armadura armadura, Inventario inventario) {
		// 1. Verificar que la armadura esté en la mochila
		if (!inventario.contieneItem(armadura)) {
			System.out.println("La armadura '" + armadura.getNombre() + "' no está en la mochila.");
			return;
		}
		// 2. Si ya tenía una armadura equipada, la devuelve al inventario
		if (this.armadura != null) {
			inventario.agregarItem(this.armadura);
			System.out.println(this.getNombre() + " desequipa: " + this.armadura.getNombre());
		}
		// 3. Equipar la armadura nueva y sacarla de la mochila
		this.armadura = armadura;
		inventario.eliminarItem(armadura);
		System.out.println(this.getNombre() + " equipa: " + armadura.getNombre());
	}
    
	
	

	public int getEnergiaMax() {
		return energiaMax;
	}

	public void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
	}

	public HashMap<Integer, StatsNivel> getTablaDeNiveles() {
		return tablaDeNiveles;
	}

	public void setTablaDeNiveles(HashMap<Integer, StatsNivel> tablaDeNiveles) {
		this.tablaDeNiveles = tablaDeNiveles;
	}

	// Getters y Setters
	public int getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(int experiencia) {
		this.experiencia = experiencia;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getEnergia() {
		return energia;
	}

	public void setEnergia(int energia) {
		this.energia = energia;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getManaMax() {
		return manaMax;
	}

	public void setManaMax(int manaMax) {
		this.manaMax = manaMax;
	}

	public int getProbCrit() {
		return probCrit;
	}

	public void setProbCrit(int probCrit) {
		this.probCrit = probCrit;
	}

	public int getDanoCrit() {
		return danoCrit;
	}

	public void setDanoCrit(int danoCrit) {
		this.danoCrit = danoCrit;
	}


	public Arma getArma() {
		return arma;
	}

	public void setArma(Arma arma) {
		this.arma = arma;
	}

	public Armadura getArmadura() {
		return armadura;
	}

	public void setArmadura(Armadura armadura) {
		this.armadura = armadura;
	}
}
