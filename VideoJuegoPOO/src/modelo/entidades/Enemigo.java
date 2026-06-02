package modelo.entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import modelo.Entidad;

public class Enemigo extends Entidad {
	//Enum para identificar qué tipo de enemigo es y mapear su comportamiento de IA
	public enum TipoEnemigo {
        GOBLIN, LADRON, BRUJO, GOLEM, DRAGON
    }
	
	private int experienciaOtorgada;
	private int nivelEnemigo;
	private TipoEnemigo tipo;
	
	public Enemigo(String nombre, int vida, int vidaMax, int ataque, int defensa, int velocidad,
			boolean estaDefendiendo, int experienciaOtorgada, int nivelEnemigo, TipoEnemigo tipo) {
		super(nombre, vida, vidaMax, ataque, defensa, velocidad, estaDefendiendo);
		this.experienciaOtorgada = experienciaOtorgada;
		this.nivelEnemigo = nivelEnemigo;
		this.tipo = tipo ; 
		//default goblin, desp se cambia al crear el enemigo para x batalla/nivel.
		}
	// Método para que el enemigo ataque al héroe
	// Se puede dividir en 2, funcion A que devuelva el heroe al que ataca el enemigo seg tipo. 
	// Y funcion B que ejecute el ataque al heroe objetivo que devuelve la funcion A.
	public void EnemigoAtaca(List<Heroe> heroes) {
	    if (heroes == null || heroes.isEmpty()) return;
	    Heroe objetivo = null;
	    Random rand = new Random();
	    switch (this.tipo) {
	        case GOBLIN:
	        case BRUJO:
	            // Ataca a un héroe random
	            objetivo = heroes.get(rand.nextInt(heroes.size()));
	            break;
	        case LADRON:
	            // Ataca al primero de la lista (orden de aparición)
	            objetivo = heroes.get(0);
	            break;
	        case GOLEM:
	            // Ataca al héroe con más vida (el "tanque")
	            objetivo = heroes.get(0);
	            for (Heroe h : heroes) {
	                if (h.getVida() > objetivo.getVida()) {
	                    objetivo = h;
	                }
	            }
	            break;
	        case DRAGON:
	            // Busca si hay un Curador en el equipo
	            Heroe curador = null;
	            for (Heroe h : heroes) {
	                if (h instanceof Curador) {
	                    curador = h;
	                    break;
	                }
	            }
	            if (curador == null) {
	                // No hay curador, ataca random
	                objetivo = heroes.get(rand.nextInt(heroes.size()));
	            } else {
	                // Busca si hay alguien con MENOS vida que el curador
	                Heroe masDebil = curador;
	                for (Heroe h : heroes) {
	                    if (h.getVida() < masDebil.getVida()) {
	                        masDebil = h;
	                    }
	                }
	                // Si masDebil sigue siendo el curador, no había nadie con menos vida
	                objetivo = masDebil;
	            }
	            break;
	    }

	    if (objetivo != null) {
	        objetivo.recibirDano(this.getAtaque());
	    }
	}

	
	public int calcularDanoRecibeEnemigo(int cantDano) {
		//quiero que reciba el dano plano de los heroes, no quiero que se reduzca por la defensa, el enemigo va a tner mucha vida, lo que si, se puede defender por ia, asi que eso si contemplalo
		if (this.isEstaDefendiendo()) {
			 cantDano *= 0.7; // 30% menos por defensa activa
			 return (int) cantDano;
		}
		else { 
			return (int) cantDano; // Dano sin reducción x estadistica de defensa.
		}
	}
	
	public void recibirDano(int cantidadDano) {
		int danoFinal = calcularDanoRecibeEnemigo(cantidadDano);
		int vidaRestante = this.getVida() - danoFinal;
		this.setVida(vidaRestante);
		if (this.getVida() < 0) {
			this.setVida(0);
		}
	}
	
	public void otorgarExperiencia(Heroe heroe) {
	    heroe.ganarExperiencia(this.experienciaOtorgada);
	}
	
	public void seleccionarAccion(List<Heroe> heroes) {
	    // Habria que ver como decidir si ataca o se defiende, 
		// o si tiene alguna habilidad especial, pero por ahora solo va a atacar.
	}
	
	
	
	
	
	
	
	
	
	//Getters y Setters
	public int getExperienciaOtorgada() {
		return experienciaOtorgada;
	}

	public void setExperienciaOtorgada(int experienciaOtorgada) {
		this.experienciaOtorgada = experienciaOtorgada;
	}

	public int getNivelEnemigo() {
		return nivelEnemigo;
	}

	public void setNivelEnemigo(int nivelEnemigo) {
		this.nivelEnemigo = nivelEnemigo;
	}

	public TipoEnemigo getTipo() {
		return tipo;
	}

	public void setTipo(TipoEnemigo tipo) {
		this.tipo = tipo;
	}

	
	

}
