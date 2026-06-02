package modelo;

import java.util.List;

import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;

import java.util.ArrayList;

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
	        if (h.estaVivo()) {
	            todosHeroesMuertos = false;
	        }
	    }

	    boolean todosEnemigosMuertos = true;
	    
	    for (Enemigo e : enemigos) {
	    	if (e.estaVivo()) {
	    		todosEnemigosMuertos = false;
	    	}
	    }

	    
	    if (todosHeroesMuertos) {
	    	return EstadoBatalla.DERROTA;
	    }
	    
	    else if (todosEnemigosMuertos) {
	    	return EstadoBatalla.VICTORIA;
	    }
	    
	    else {
	    	return EstadoBatalla.EN_CURSO;
	    }

	}
	
	public boolean hayEnemigosVivos() {
		for (Enemigo e : enemigos) {
			if (e.estaVivo()) return true;
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
