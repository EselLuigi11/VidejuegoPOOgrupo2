package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.entidades.Heroe;

public class Inventario { 
	private static final long serialVersionUID = 1L;
	private List<Item> items;
    private int capacidad;

    public Inventario(int capacidad) {
        this.capacidad = capacidad;
        this.items = new ArrayList<>();
    }

    public void agregarItem(Item item) {
        if (items.size() < capacidad) {
            items.add(item);
        } else {
            System.out.println("Inventario lleno, no se puede agregar " + item.getNombre());
        }
    }

    public void eliminarItem(Item item) {
        if (items.contains(item)) {
            items.remove(item);
        } else {
            System.out.println("Item no encontrado en el inventario.");
        }
    }

    public Item buscarItem(String nombre) {
        for (Item item : items) {
            if (item.getNombre().equalsIgnoreCase(nombre)) {
                return item;
            }
        }
        return null;
    }
    
    
    public void usarItem(String nombre, Heroe heroe) {
    	Item item = buscarItem(nombre);
    	
    	if(item != null) {
    		item.usar(heroe);
    		
    		eliminarItem(item);
    		
    	}else {
    		System.out.println("No tienes el objeto");
    	}
    }

    
    
    
    
    // Getters por tipo, útil para mostrar solo armas, solo pociones, etc.
    public List<Arma> getArmas() {
        List<Arma> armas = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Arma) armas.add((Arma) item);
        }
        return armas;
    }

    public List<Armadura> getArmaduras() {
        List<Armadura> armaduras = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Armadura) armaduras.add((Armadura) item);
        }
        return armaduras;
    }

    public List<Pocion> getPociones() {
        List<Pocion> pociones = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Pocion) pociones.add((Pocion) item);
        }
        return pociones;
    }

    public boolean estaLleno() { 
    	return items.size() >= capacidad; 
    	
    }
    
    public boolean contieneItem(Item item) {
    	return items.contains(item); // contains es metodo q verifica si item esta en lista.
    }
    
    
    // Getters y setters
    public int getCantidadActual() { 
    	return items.size(); 
    	
    }
    public int getCapacidad() { 
    	return capacidad; 
    	
    }
    public List<Item> getItems() { 
    	return items; 
    	
    }

}
