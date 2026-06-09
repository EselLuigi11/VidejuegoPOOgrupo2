package principal;

import java.util.ArrayList;
import controlador.ControladorJuego;
import modelo.Batalla;
import modelo.OrquestadorF;
import modelo.Partida;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;
import modelo.vista.VistaBatalla;
import modelo.vista.VistaInventario; // <-- AGREGADO: Importamos la vista del inventario
import modelo.vista.VistaMenuPrincipal;

public class Main {

	public static void main(String[] args) {
		
		Partida partida = new Partida(); // Creamos la partida base
		
		// El Orquestador necesita una Batalla para nacer. 
		// Como recién abrimos el juego, creamos una batalla con listas vacías por ahora.
		Batalla batallaInicial = new Batalla(new ArrayList<Heroe>(), new ArrayList<Enemigo>());
		OrquestadorF orquestador = new OrquestadorF(batallaInicial, partida);
		
		VistaMenuPrincipal menu = new VistaMenuPrincipal();
		VistaBatalla pantallaBatalla = new VistaBatalla();
		VistaInventario inventario = new VistaInventario(); // <-- AGREGADO: Instanciamos el inventario
		
		// Nos aseguramos de que las pantallas secundarias arranquen ocultas
		pantallaBatalla.setVisible(false); 
		inventario.setVisible(false); // <-- AGREGADO
	
		// Le pasamos las 5 referencias que el constructor necesita obligatoriamente
		ControladorJuego controlador = new ControladorJuego(partida, orquestador, menu, pantallaBatalla, inventario);
		
		controlador.iniciar();
		
	}
}