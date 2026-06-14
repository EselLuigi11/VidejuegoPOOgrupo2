package modelo.entidades;

public class StatsNivel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
    private int vidaMax;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int energiaMax;
    private int manaMax;
    private int probCrit;
    private int danoCrit;
    
    public StatsNivel(int vidaMax, int ataque, int defensa, int velocidad, int energiaMax, int manaMax, int probCrit, int danoCrit) {
		this.vidaMax = vidaMax;
		this.ataque = ataque;
		this.defensa = defensa;
		this.velocidad = velocidad;
		this.energiaMax = energiaMax;
		this.manaMax = manaMax;
		this.probCrit = probCrit;
		this.danoCrit = danoCrit;
	}
    
    
    
    
    //Getters y Setters

	public int getVidaMax() {
		return vidaMax;
	}

	public void setVidaMax(int vidaMax) {
		this.vidaMax = vidaMax;
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getDefensa() {
		return defensa;
	}

	public void setDefensa(int defensa) {
		this.defensa = defensa;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public int getEnergiaMax() {
		return energiaMax;
	}

	public void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
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
    
    
}
