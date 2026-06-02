package principal;

import java.util.ArrayList;
import controlador.ControladorJuego;
import modelo.Batalla;
import modelo.Orquestador;
import modelo.Partida;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;
import modelo.vista.VistaBatalla;
import modelo.vista.VistaMenuPrincipal;

public class Main {

	public static void main(String[] args) {
		
		Partida partida = new Partida(); // Creamos la partida base
		
		// El Orquestador necesita una Batalla para nacer. 
		// Como recién abrimos el juego, creamos una batalla con listas vacías por ahora.
		Batalla batallaInicial = new Batalla(new ArrayList<Heroe>(), new ArrayList<Enemigo>());
		Orquestador orquestador = new Orquestador(batallaInicial, partida);
		
		VistaMenuPrincipal menu = new VistaMenuPrincipal();
		VistaBatalla pantallaBatalla = new VistaBatalla();
		
		// Si no la apagamos acá, van a aparecer las dos pantallas superpuestas al mismo tiempo.
		pantallaBatalla.setVisible(false); 
	
		// Le pasamos las 4 cosas que nos pediste en el constructor que armaste antes
		ControladorJuego controlador = new ControladorJuego(partida, orquestador, menu, pantallaBatalla);
		
		controlador.iniciar();
		
	}
}