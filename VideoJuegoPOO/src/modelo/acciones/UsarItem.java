package modelo.acciones;

import modelo.Accion;
import modelo.Inventario;
import modelo.Item;
import modelo.Partida;
import modelo.entidades.Heroe;

public class UsarItem extends Accion {
    private Partida partida;
    private Heroe heroe; 
    private Item item;

    public UsarItem(Partida partida, Heroe heroe, Item item) {
        this.partida = partida;
        this.heroe = heroe;
        this.item = item;
    }

    @Override
    public void ejecutar() {
        if (partida == null || heroe == null || item == null) {
            System.out.println("No se puede usar el ítem porque faltan datos.");
            return;
        }

        if (!heroe.estaVivo()) {
            System.out.println(heroe.getNombre() + " no puede usar ítems porque está fuera de combate.");
            return;
        }

        Inventario inventario = partida.getInventarioPartida();
        
        if (inventario.contieneItem(item)) {
        	item.usar(heroe); 
            inventario.eliminarItem(item);
            System.out.println(heroe.getNombre() + " saca " + item.getNombre() + " de la mochila.");
            
        } else {
            System.out.println("Error: El ítem " + item.getNombre() + " no está en el inventario.");
        }
    }
}