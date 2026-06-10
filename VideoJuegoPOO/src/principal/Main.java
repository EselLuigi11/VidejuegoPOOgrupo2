package principal;

import java.util.ArrayList;
import controlador.ControladorJuego;
import modelo.Batalla;
import modelo.OrquestadorF;
import modelo.Partida;
import modelo.CatalogoBatalla;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;
import modelo.entidades.Guerrero;
import modelo.entidades.Mago;
import modelo.vista.VistaBatalla;
import modelo.vista.VistaInventario; 
import modelo.vista.VistaMenuPrincipal;

public class Main {

	public static void main(String[] args) {
		
		Partida partida = new Partida(); 
		
		// Agregamos dos héroes reales para dar variedad al equipo de la Party
		Guerrero guerrero = new Guerrero("Guerrero", null, null);
		Mago mago = new Mago("Mago", null, null);
		partida.getGrupo().agregarHeroe(guerrero);
		partida.getGrupo().agregarHeroe(mago);
		
		// Cargamos el listado inicial de combatientes vivos
		ArrayList<Heroe> heroesEnCombate = new ArrayList<>(partida.getGrupo().getHeroesVivos());
		ArrayList<Enemigo> enemigosEnCombate = new ArrayList<>();
		
		// Instanciamos el primer contrincante del nivel 1
		Enemigo rivalBase = new Enemigo("Goblin", 100, 100, 14, 8, 4, false, 40, 1, Enemigo.TipoEnemigo.GOBLIN);
		enemigosEnCombate.add(rivalBase);
		
		// Construimos el encuentro inicial y acoplamos el Orquestador de Luis
		Batalla batallaInicial = new Batalla(heroesEnCombate, enemigosEnCombate);
		OrquestadorF orquestador = new OrquestadorF(batallaInicial, partida);
		
		// Inicializamos los componentes de las ventanas
		VistaMenuPrincipal menu = new VistaMenuPrincipal();
		VistaBatalla pantallaBatalla = new VistaBatalla();
		VistaInventario inventario = new VistaInventario(); 
		
		pantallaBatalla.setVisible(false); 
		inventario.setVisible(false); 
	
		// Enlazamos las dependencias en el constructor del controlador
		ControladorJuego controlador = new ControladorJuego(partida, orquestador, menu, pantallaBatalla, inventario);
		
		controlador.iniciar();
	}
}