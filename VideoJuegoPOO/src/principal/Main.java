// REEMPLAZAR Main.java completo:

package principal;

import java.util.ArrayList;
import controlador.ControladorJuego;
import modelo.Batalla;
import modelo.CatalogoBatalla;
import modelo.OrquestadorF;
import modelo.Partida;
import modelo.pociones.PocionVida;
import modelo.pociones.PocionMana;
import modelo.entidades.*;
import modelo.vista.*;

public class Main {

    public static void main(String[] args) {

        Partida partida = new Partida();

        // ── 5 héroes (sin arma/armadura por ahora, null es válido)
        partida.getGrupo().agregarHeroe(new Guerrero("Guerrero", null, null));
        partida.getGrupo().agregarHeroe(new Mago("Mago",         null, null));
        partida.getGrupo().agregarHeroe(new Arquero("Arquero",   null, null));
        partida.getGrupo().agregarHeroe(new Asesino("Asesino",   null, null));
        partida.getGrupo().agregarHeroe(new Curador("Curador",   null, null));

        // ── Inventario inicial con algunas pociones
        partida.getInventarioPartida().agregarItem(new PocionVida("Poción de Vida",   "Restaura 50 HP",  50));
        partida.getInventarioPartida().agregarItem(new PocionVida("Poción de Vida",   "Restaura 50 HP",  50));
        partida.getInventarioPartida().agregarItem(new PocionMana("Poción de Maná",   "Restaura 40 MP",  40));
        partida.getInventarioPartida().agregarItem(new PocionMana("Poción de Maná",   "Restaura 40 MP",  40));

        // ── Batalla inicial desde catálogo (nivel 1)
        Batalla batallaInicial = CatalogoBatalla.getInstance()
                .construirBatalla(1, new ArrayList<>(partida.getGrupo().getHeroesVivos()));
        OrquestadorF orquestador = new OrquestadorF(batallaInicial, partida);

        // ── Vistas
        VistaMenuPrincipal menu        = new VistaMenuPrincipal();
        VistaBatalla       batalla     = new VistaBatalla();
        VistaInventario    inventario  = new VistaInventario();

        batalla.setVisible(false);
        inventario.setVisible(false);

        new ControladorJuego(partida, orquestador, menu, batalla, inventario).iniciar();
    }
}