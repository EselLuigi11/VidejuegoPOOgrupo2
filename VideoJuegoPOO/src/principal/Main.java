package principal;

import java.util.ArrayList;
import controlador.ControladorJuego;
import modelo.Batalla;
import modelo.CatalogoBatalla;
import modelo.Orquestador;
import modelo.Partida;
import modelo.entidades.*;
import modelo.vista.*;

public class Main {

    public static void main(String[] args) {

        Partida partida = new Partida(); // ya agrega 3 pociones de vida en su constructor

        // ── 5 héroes
        partida.getGrupo().agregarHeroe(new Guerrero("Guerrero", null, null));
        partida.getGrupo().agregarHeroe(new Mago    ("Mago",     null, null));
        partida.getGrupo().agregarHeroe(new Arquero ("Arquero",  null, null));
        partida.getGrupo().agregarHeroe(new Asesino ("Asesino",  null, null));
        partida.getGrupo().agregarHeroe(new Curador ("Curador",  null, null));

        // ── Batalla inicial desde catálogo (nivel 1)
        Batalla batallaInicial = CatalogoBatalla.getInstance()
                .construirBatalla(1, new ArrayList<>(partida.getGrupo().getHeroesVivos()));
        Orquestador orquestador = new Orquestador(batallaInicial, partida);

        // ── Vistas
        VistaMenuPrincipal menu       = new VistaMenuPrincipal();
        VistaBatalla       batalla    = new VistaBatalla();
        VistaInventario    inventario = new VistaInventario();

        batalla.setVisible(false);
        inventario.setVisible(false);

        new ControladorJuego(partida, orquestador, menu, batalla, inventario).iniciar();
    }
}