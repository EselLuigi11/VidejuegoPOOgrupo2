package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.entidades.Enemigo;
import modelo.entidades.Enemigo.TipoEnemigo;
import modelo.entidades.Heroe;

/**
 Catálogo de batallas — patrón Singleton.

Tiene un HashMap<Integer, List<Enemigo>> donde:
 - KEY   = número de batalla (1, 2, 3...)
 - VALUE = lista de Enemigos que componen esa batalla
 
Uso desde Partida u Orquestador:
 	Batalla b = CatalogoBatalla.getInstance().construirBatalla(1, heroesVivos);
    orquestador.setBatallaActual(b);
 */
public class CatalogoBatalla {

    // ── Singleton 
    private static CatalogoBatalla instancia;

    // ── Datos ─────────────────────────────────────────────────
    private Map<Integer, List<Enemigo>> batallas;
    //Se compone por la key, nro de batalla, y el value, la lista de enemigos que componen ese nro de batalla.

    // ── Constructor privado ───────────────────────────────────
    private CatalogoBatalla() {
        batallas = new HashMap<>();
        cargarBatallas();
    }

    public static CatalogoBatalla getInstance() { //Obtiene la instancia del Catalogo Batalla, en caso de no existir, lo crea (La primera vez q corra)
        if (instancia == null) {
            instancia = new CatalogoBatalla();
        }
        return instancia;
    }

    // ── Definición de batallas ────────────────────────────────
    // Para agregar una batalla nueva, crear una lista, agregar los Enemigos
    // con sus stats y meterla en el HashMap con el número de batalla.
    //
    // Constructor Enemigo:
    // new Enemigo(nombre, vida, vidaMax, ataque, defensa, velocidad,
    //             estaDefendiendo, expOtorgada, nivelEnemigo, tipo)

 // EN: VideoJuegoPOO/src/modelo/CatalogoBatalla.java
 // REEMPLAZAR el método cargarBatallas() completo:

 private void cargarBatallas() {

     // ── Nivel 1 — 3 Goblins (introducción, sin presión real)
     List<Enemigo> batalla1 = new ArrayList<>();
     batalla1.add(new Enemigo("Goblin Explorador", 40, 40, 12, 5,  14, false, 30, 1, TipoEnemigo.GOBLIN));
     batalla1.add(new Enemigo("Goblin Escudo",     55, 55,  8, 18, 10, false, 25, 1, TipoEnemigo.GOBLIN));
     batalla1.add(new Enemigo("Goblin Salvaje",    45, 45, 15, 6,  16, false, 28, 1, TipoEnemigo.GOBLIN));
     batallas.put(1, batalla1);

     // ── Nivel 2 — 1 Goblin + 2 Ladrones (presión mixta)
     List<Enemigo> batalla2 = new ArrayList<>();
     batalla2.add(new Enemigo("Goblin Escudo",   55, 55,  8, 18, 10, false, 25, 1, TipoEnemigo.GOBLIN));
     batalla2.add(new Enemigo("Ladrón Veloz",    55, 55, 18,  8, 20, false, 40, 2, TipoEnemigo.LADRON));
     batalla2.add(new Enemigo("Ladrón Arquero",  45, 45, 22,  6, 18, false, 35, 2, TipoEnemigo.LADRON));
     batallas.put(2, batalla2);

     // ── Nivel 3 — 2 Brujos + 1 Gólem (tanque final)
     List<Enemigo> batalla3 = new ArrayList<>();
     batalla3.add(new Enemigo("Brujo Aprendiz",  60,  60, 20, 10, 14, false,  50, 2, TipoEnemigo.BRUJO));
     batalla3.add(new Enemigo("Brujo Oscuro",    70,  70, 25, 12, 12, false,  60, 2, TipoEnemigo.BRUJO));
     batalla3.add(new Enemigo("Gólem de Piedra", 180, 180, 28, 35,  6, false, 120, 3, TipoEnemigo.GOLEM));
     batallas.put(3, batalla3);
 }

    // ── Construcción de la Batalla real ──────────────────────
    //Basicamente, es una función de tipo Batalla que recibe el nro de Batalla y la lista de héroes vivos, y devuelve una nueva Batalla con esos héroes y los enemigos correspondientes a ese número de batalla.
    public Batalla construirBatalla(int numeroBatalla, List<Heroe> heroesVivos) {
        List<Enemigo> enemigos = batallas.get(numeroBatalla);

        if (enemigos == null) {//Validacion de que sea una batalla existente. 
            System.out.println("No existe la batalla número " + numeroBatalla);
            return null;
        }

        return new Batalla(heroesVivos, enemigos);
    }

    /** Cuántas batallas hay definidas. */
    public int getTotalBatallas() {
        return batallas.size();
    }
}