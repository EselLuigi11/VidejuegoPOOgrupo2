package modelo;

public class Entidad {
	private String nombre;
	private int vida;
	private int vidaMax;
	private int ataque;
	private int defensa;
	private int velocidad;
	private boolean estaDefendiendo;
	
	
	//Constructor de entidad. 
	public Entidad(String nombre, int vida, int vidaMax, int ataque, int defensa, int velocidad, boolean estaDefendiendo) {
		this.nombre = nombre;
		this.vida = vida;
		this.vidaMax = vidaMax;
		this.ataque = ataque;
		this.defensa = defensa;
		this.velocidad = velocidad;
		this.estaDefendiendo = false;
		}
	
	//Métodos de Entidad, los recibirá tanto Héroe como Enemigo (como a su vez cada hérore y enemigo)
	public void recibirDano(int cantidadDano) {
	    final double K = 100.0; // Ajustar el valor en un futuro para balancear la reducción de dano por defensa
	    // 1. Reducción por estadística defensa (siempre aplica, porcentual)
	    double reduccionDefensa = this.defensa / (this.defensa + K);
	    double danoReducido = cantidadDano * (1.0 - reduccionDefensa);
	    // 2. Si está defendiendo, aplica reducción adicional encima
	    if (this.estaDefendiendo) {
	        danoReducido *= 0.7; // 30% menos adicional por guardia activa
	    }
	    // 3. Dano mínimo de 1 si el ataque era mayor a 0
	    int danoFinal = (int) danoReducido;
	    if (danoFinal < 1 && cantidadDano > 0) {
	        danoFinal = 1;
	    }
	    this.vida -= danoFinal;
	    if (this.vida < 0) {
	        this.vida = 0;
	    }
	}

	
	//
	public void morir() {
		System.out.println(this.nombre + " ha caído en combate.");
	}

	//Verificar si la entidad sigue viva
	public boolean estaVivo() {
		return this.vida > 0;
	}		

	
	
	public void curarse(int cantidad) {
	        this.vida += cantidad;
	        if (this.vida > this.vidaMax) {
	            this.vida = this.vidaMax;
	        }
	    }
	
	public void aumentarVel(int cantidad) {
		this.velocidad += cantidad;
		
	}
	
	public void aumentarAtaque(int cantidad) {
		this.ataque += cantidad;
	}
	
	public void aumentarDefensa(int cantidad) {
		this.defensa += cantidad;
	}
	
	public void defenderse() {
	        this.estaDefendiendo = true;
	    }
	
	
	// Getters y Setters de Entidad

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

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

	public boolean isEstaDefendiendo() {
		return estaDefendiendo;
	}

	public void setEstaDefendiendo(boolean estaDefendiendo) {
		this.estaDefendiendo = estaDefendiendo;
	}

}
