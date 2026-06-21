package modelo.entidades;

import java.util.HashMap;
import java.util.List;

import modelo.Arma;
import modelo.Armadura;
import modelo.Entidad;
import modelo.Inventario;

public abstract class Heroe extends Entidad {
	private static final long serialVersionUID = 1L;

	private int experiencia;
	private int nivel;
	private int mana;
	private int manaMax;
	private int probCrit;
	private int danoCrit;
	private Arma arma;
	private Armadura armadura;
	protected HashMap<Integer, StatsNivel> tablaDeNiveles;

	public Heroe(String nombre, int vida, int vidaMax,
			int ataque, int defensa,
			int velocidad, boolean estaDefendiendo,
			int experiencia, int nivel,
			int mana, int manaMax,
			int probCrit, int danoCrit,
			Arma arma, Armadura armadura) {

		super(nombre, vida, vidaMax, ataque, defensa, velocidad, estaDefendiendo);

		this.experiencia = experiencia;
		this.nivel = nivel;
		this.mana = mana;
		this.manaMax = manaMax;
		this.probCrit = probCrit;
		this.danoCrit = danoCrit;
		this.tablaDeNiveles = new HashMap<>();

		if (arma != null) equiparArma(arma);
		if (armadura != null) equiparArmadura(armadura);
	}

	// MODIFICADO: método contractual polimórfico. Cada subclase concreta
	// (Guerrero, Mago, Arquero...) implementa su propia habilidad delegando
	// a su clase HabEsp* correspondiente. Reemplaza el bloque if/instanceof
	// que existía en Orquestador.ejecutarHabilidadStr().
	public abstract String usarHabilidadEspecial(Enemigo objetivo, List<Enemigo> enemigosVivos, List<Heroe> aliadosVivos);

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

	public void ganarExperiencia(int exp) {
		this.experiencia += exp;
		while (this.experiencia >= nivel * 105) {
			this.experiencia -= nivel * 105;
			subirNivel();
		}
	}

	public void subirNivel() {
		this.nivel++;
		StatsNivel statsDelNivel = tablaDeNiveles.get(this.nivel);
		if (statsDelNivel != null) {
			this.setVidaMax(statsDelNivel.getVidaMax());
			this.setAtaque(statsDelNivel.getAtaque());
			this.setDefensa(statsDelNivel.getDefensa());
			this.setVelocidad(statsDelNivel.getVelocidad());
			this.manaMax = statsDelNivel.getManaMax();
			this.probCrit = statsDelNivel.getProbCrit();
			this.danoCrit = statsDelNivel.getDanoCrit();
		} else {
			this.setVidaMax(this.getVidaMax() + 20);
			this.setAtaque(this.getAtaque() + 5);
			this.setDefensa(this.getDefensa() + 5);
			this.setManaMax(this.getManaMax() + 10);
		}
		this.setVida(this.getVidaMax());
		this.setMana(this.manaMax);
	}

	// MODIFICADO: equipar arma deja de ser acumulativo. Si ya había una
	// equipada, primero se resta su bonus de ataque antes de sumar el de
	// la nueva. El stat de ataque siempre refleja el equipo actual, nunca
	// un acumulado histórico de todo lo que se usó alguna vez.
	public void equiparArma(Arma nuevaArma) {
		this.arma = nuevaArma;
	}

	// MODIFICADO: misma lógica que equiparArma(), aplicada a defensa.
	public void equiparArmadura(Armadura nuevaArmadura) {
		this.armadura= nuevaArmadura;
		
	}

	// MODIFICADO: ahora delega en equiparArma(Arma) en vez de asignar el
	// campo directamente, así el bonus de ataque se recalcula siempre
	// pasando por el mismo punto único de la lógica de equipamiento.
	public String equiparArma(Arma arma, Inventario inventario) {
		if (!inventario.contieneItem(arma)) {
			return "El arma '" + arma.getNombre() + "' no está en la mochila.";
		}
		String msj = (this.arma != null)
				? getNombre() + " desequipa " + this.arma.getNombre() + " y "
				: getNombre() + " ";
		if (this.arma != null) inventario.agregarItem(this.arma);
		equiparArma(arma);
		inventario.eliminarItem(arma);
		return msj + "equipa " + arma.getNombre() + ".";
	}

	public String equiparArmadura(Armadura armadura, Inventario inventario) {
		if (!inventario.contieneItem(armadura)) {
			return "La armadura '" + armadura.getNombre() + "' no está en la mochila.";
		}
		String msj = (this.armadura != null)
				? getNombre() + " desequipa " + this.armadura.getNombre() + " y "
				: getNombre() + " ";
		if (this.armadura != null) inventario.agregarItem(this.armadura);
		equiparArmadura(armadura);
		inventario.eliminarItem(armadura);
		return msj + "equipa " + armadura.getNombre() + ".";
	}
	
	
	public int getDefensaTotal() {
	    return getDefensa() +
	           (armadura != null ? armadura.getplusDefensa() : 0);
	}
	
	public int getAtaqueTotal() {
	    return getAtaque() +
	           (arma != null ? arma.getPlusDano() : 0);
	}

	public void restaurarStatusCompleto() {
		this.setVida(this.getVidaMax());
		if (this.getManaMax() > 0) {
			this.setMana(this.getManaMax());
		}
		this.setEstaDefendiendo(false);
	}

	public String getResumenEstadisticas() {
		int bonusAtaque = (arma != null) ? arma.getPlusDano() : 0;
		int bonusDefensa = (armadura != null) ? armadura.getplusDefensa() : 0;

		StringBuilder sb = new StringBuilder();
		sb.append(getNombre()).append("  (Nivel ").append(nivel).append(")\n");
		sb.append("──────────────────────────\n");
		sb.append("Vida:      ").append(getVida()).append(" / ").append(getVidaMax()).append("\n");
		sb.append("Mana:      ").append(mana).append(" / ").append(manaMax).append("\n");
		sb.append("Ataque:    ").append(getAtaque());
		if (bonusAtaque > 0)
		    sb.append(" (+").append(bonusAtaque).append(" arma)");
		sb.append(" = ").append(getAtaque() + bonusAtaque).append("\n");;
		sb.append("Defensa:   ").append(getDefensa());
		if (bonusDefensa > 0)
		    sb.append(" (+").append(bonusDefensa).append(" armadura)");
		sb.append(" = ").append(getDefensa() + bonusDefensa).append("\n");;
		sb.append("Velocidad: ").append(getVelocidad()).append("\n");
		sb.append("Crítico:   ").append(probCrit).append("% (x").append(danoCrit / 100.0).append(")\n");
		sb.append("Arma:      ").append(arma != null ? arma.getNombre() : "Ninguna").append("\n");
		sb.append("Armadura:  ").append(armadura != null ? armadura.getNombre() : "Ninguna");
		return sb.toString();
	}

	// NUEVO: texto de la pantalla de equipamiento pedida en la corrección
	// "Interfaz #3". Cohesión POO: el Heroe describe su propio equipo,
	// el Controlador solo lo muestra en un JOptionPane.
	public String getResumenEquipamiento() {
		StringBuilder sb = new StringBuilder();
		sb.append("Equipamiento de ").append(getNombre()).append("\n");
		sb.append("──────────────────────────\n");

		sb.append("Arma:      ");
		if (arma != null) {
			sb.append(arma.getNombre())
			  .append("  (+").append(arma.getPlusDano()).append(" ataque)\n");
			sb.append("           ").append(arma.getDescripcion()).append("\n");
		} else {
			sb.append("Ninguna\n");
		}

		sb.append("Armadura:  ");
		if (armadura != null) {
			sb.append(armadura.getNombre())
			  .append("  (+").append(armadura.getplusDefensa()).append(" defensa)\n");
			sb.append("           ").append(armadura.getDescripcion()).append("\n");
		} else {
			sb.append("Ninguna\n");
		}

		sb.append("\nAtaque total:  ").append(getAtaque());
		sb.append("\nDefensa total: ").append(getDefensa());
		return sb.toString();
	}

	public HashMap<Integer, StatsNivel> getTablaDeNiveles() { return tablaDeNiveles; }
	public void setTablaDeNiveles(HashMap<Integer, StatsNivel> t) { this.tablaDeNiveles = t; }

	public int getExperiencia() { return experiencia; }
	public void setExperiencia(int experiencia) { this.experiencia = experiencia; }

	public int getNivel() { return nivel; }
	public void setNivel(int nivel) { this.nivel = nivel; }

	public int getMana() { return mana; }
	public void setMana(int mana) { this.mana = mana; }

	public int getManaMax() { return manaMax; }
	public void setManaMax(int manaMax) { this.manaMax = manaMax; }

	public int getProbCrit() { return probCrit; }
	public void setProbCrit(int probCrit) { this.probCrit = probCrit; }

	public int getDanoCrit() { return danoCrit; }
	public void setDanoCrit(int danoCrit) { this.danoCrit = danoCrit; }

	public Arma getArma() { return arma; }
	public Armadura getArmadura() { return armadura; }
}
