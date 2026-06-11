package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.entidades.Enemigo;
import modelo.entidades.Enemigo.TipoEnemigo;
import modelo.entidades.Heroe;

public class CatalogoBatalla {

    private static CatalogoBatalla instancia;
    private Map<Integer, List<Enemigo>> batallas;

    private CatalogoBatalla() {
        batallas = new HashMap<>();
        cargarBatallas();
    }

    public static CatalogoBatalla getInstance() {
        if (instancia == null) {
            instancia = new CatalogoBatalla();
        }
        return instancia;
    }

    private void cargarBatallas() {

        // ── Nivel 1 — 3 Goblins
        List<Enemigo> batalla1 = new ArrayList<>();
        // Estadisticas: Vida, VidaMax, Ataque, Defensa, Velocidad, EstaDefendiendo, ExperienciaOtorgada, NivelEnemigo, TipoEnemigo
        batalla1.add(new Enemigo("Goblin Explorador", 50,  50, 22,  10, 14, false,  30, 1, TipoEnemigo.GOBLIN));
        batalla1.add(new Enemigo("Goblin Escudo",     70,  70,  18, 25, 10, false,  25, 1, TipoEnemigo.GOBLIN));
        batalla1.add(new Enemigo("Goblin Salvaje",    50,  50, 24,  11, 16, false,  28, 1, TipoEnemigo.GOBLIN));
        batallas.put(1, batalla1);

        // ── Nivel 2 — 1 Goblin + 2 Ladrones
        List<Enemigo> batalla2 = new ArrayList<>();
        batalla2.add(new Enemigo("Goblin Escudo",   70,  70,  18, 25, 10, false, 25, 1, TipoEnemigo.GOBLIN));
        batalla2.add(new Enemigo("Ladrón Veloz",    65,  65, 22,  9, 20, false, 40, 2, TipoEnemigo.LADRON));
        batalla2.add(new Enemigo("Ladrón Arquero",  50,  50, 27,  10, 18, false, 35, 2, TipoEnemigo.LADRON));
        batallas.put(2, batalla2);

        // ── Nivel 3 — 2 Brujos + 1 Gólem
        List<Enemigo> batalla3 = new ArrayList<>();
        batalla3.add(new Enemigo("Brujo Aprendiz",  60,  60, 30, 10, 14, false,  50, 2, TipoEnemigo.BRUJO));
        batalla3.add(new Enemigo("Brujo Oscuro",    70,  70, 25, 12, 12, false,  60, 2, TipoEnemigo.BRUJO));
        batalla3.add(new Enemigo("Gólem de Piedra", 180, 180, 28, 35,  6, false, 120, 3, TipoEnemigo.GOLEM));
        batallas.put(3, batalla3);
    }

    public Batalla construirBatalla(int numeroBatalla, List<Heroe> heroesVivos) {
        List<Enemigo> enemigos = batallas.get(numeroBatalla);
        if (enemigos == null) {
            System.out.println("No existe la batalla número " + numeroBatalla);
            return null;
        }
        return new Batalla(heroesVivos, enemigos);
    }

    public int getTotalBatallas() {
        return batallas.size();
    }
}