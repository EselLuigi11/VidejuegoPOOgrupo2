package modelo;

import java.util.List;
import java.util.ArrayList;

import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;


public class Batalla {
	private List<Heroe> heroes;
	private List<Enemigo> enemigos;
	private EstadoBatalla estadoActual;
	
	// Constructor
	public Batalla (List<Heroe> heroes, List<Enemigo> enemigos) {
		this.heroes = heroes;
		this.enemigos = enemigos;
		estadoActual = EstadoBatalla.EN_CURSO;
	}
	
	public EstadoBatalla evaluarEstado() {
	    boolean todosHeroesMuertos = true; 
	    
	    for (Heroe h : heroes) {
	        if (h.estaVivo() && h!= null) {
	            todosHeroesMuertos = false;
	        }
	    }

	    boolean todosEnemigosMuertos = true;
	    for (Enemigo e : enemigos) {
	    	if (e.estaVivo() && e!= null) {
	    		todosEnemigosMuertos = false;
	    	}
	    }

	    if (todosHeroesMuertos) {
	    	estadoActual = EstadoBatalla.DERROTA;
	    }
	    
	    else if (todosEnemigosMuertos) {
	    	estadoActual = EstadoBatalla.VICTORIA;
	    }
	    
	    else {
	    	estadoActual = EstadoBatalla.EN_CURSO;
	    }
	    return estadoActual;

	}
	
	public boolean hayEnemigosVivos() {
		for (Enemigo e : enemigos) {
			if (e != null && e.estaVivo()) return true;
		}
		return false;
	}
 
	// Getters
	public List<Enemigo> getEnemigos() {
		return enemigos;
	}
 
	public List<Heroe> getHeroes() {
		return heroes;
	}
	
}
