package modelo.vista;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;

/**
 * PanelEstado — contenedor dinámico de PanelPersonaje.
 *
 * CAMBIOS vs versión anterior:
 *  - Eliminado todo hardcodeo de "Guerrero", "Enemigo", etc.
 *  - inicializar(heroesVivos, enemigosVivos) construye los paneles en tiempo de ejecución.
 *  - Los héroes se posicionan en diagonal abajo-izquierda;
 *    los enemigos en columna arriba-derecha (igual que el layout original).
 *  - refreshTodos() delega en cada PanelPersonaje.refresh() → DRY.
 *  - Getters de lista para que el Controlador pueda iterar sin conocer posiciones.
 */
public class PanelEstado extends JPanel {

    // ── Paneles activos en esta batalla ──────────────────────────────────────
    private final List<PanelPersonaje> panelesHeroes   = new ArrayList<>();
    private final List<PanelPersonaje> panelesEnemigos = new ArrayList<>();

    // ── Layout ────────────────────────────────────────────────────────────────
    // Ancho del área de estado (sincronizado con VistaBatalla.setSize(900,…))
    private static final int ANCHO = 900;
    private static final int ALTO  = 420;

    // Posiciones base para cada bando (máx 5 héroes, máx 3 enemigos)
    private static final int[][] POS_HEROES = {
        {300, 300}, {230, 230}, {160, 160}, { 90,  90}, { 20,  20}
    };
    private static final int[][] POS_ENEMIGOS = {
        {700,  30}, {640, 130}, {700, 230}
    };

    private static final Dimension TAM_PANEL_HEROE   = new Dimension(160, 130);
    private static final Dimension TAM_PANEL_ENEMIGO = new Dimension(155, 110);

    // ─────────────────────────────────────────────────────────────────────────

    public PanelEstado() {
        setLayout(null);
        setOpaque(false); // el fondo lo pinta VistaBatalla
        setPreferredSize(new Dimension(ANCHO, ALTO));
    }

    // ── API principal ─────────────────────────────────────────────────────────

    /**
     * Construye (o reconstruye) todos los PanelPersonaje a partir de las
     * listas de entidades vivas. Llamar al inicio de cada batalla y al
     * transicionar de nivel.
     *
     * @param heroes   Lista de héroes que participan en la batalla.
     * @param enemigos Lista de enemigos que participan en la batalla.
     */
    public void inicializar(List<Heroe> heroes, List<Enemigo> enemigos) {
        // Limpiar paneles anteriores
        removeAll();
        panelesHeroes.clear();
        panelesEnemigos.clear();

        // ── Héroes (esquina inferior-izquierda, en diagonal) ──────────────────
        for (int i = 0; i < heroes.size() && i < POS_HEROES.length; i++) {
            PanelPersonaje panel = new PanelPersonaje(heroes.get(i));
            panel.setPreferredSize(TAM_PANEL_HEROE);
            panel.setBounds(
                POS_HEROES[i][0], POS_HEROES[i][1],
                TAM_PANEL_HEROE.width, TAM_PANEL_HEROE.height
            );
            panelesHeroes.add(panel);
            add(panel);
        }

        // ── Enemigos (esquina superior-derecha, en columna) ────────────────────
        for (int i = 0; i < enemigos.size() && i < POS_ENEMIGOS.length; i++) {
            PanelPersonaje panel = new PanelPersonaje(enemigos.get(i));
            panel.setPreferredSize(TAM_PANEL_ENEMIGO);
            panel.setBounds(
                POS_ENEMIGOS[i][0], POS_ENEMIGOS[i][1],
                TAM_PANEL_ENEMIGO.width, TAM_PANEL_ENEMIGO.height
            );
            panelesEnemigos.add(panel);
            add(panel);
        }

        revalidate();
        repaint();
    }

    /**
     * Refresca TODOS los paneles con el estado actual de sus entidades.
     * El Controlador llama a este único método desde actualizarInterfazGrafica().
     */
    public void refreshTodos() {
        for (PanelPersonaje p : panelesHeroes)   p.refresh();
        for (PanelPersonaje p : panelesEnemigos) p.refresh();
    }

    // ── Getters de lista (para selección de objetivo en el Controlador) ───────

    /** Paneles de los héroes en el orden en que fueron registrados. */
    public List<PanelPersonaje> getPanelesHeroes() {
        return panelesHeroes;
    }

    /** Paneles de los enemigos en el orden en que fueron registrados. */
    public List<PanelPersonaje> getPanelesEnemigos() {
        return panelesEnemigos;
    }

    // ── Compatibilidad con el Controlador viejo (delegación) ─────────────────
    // Estos métodos evitan romper código existente mientras se migra.

    /** @deprecated  Usa getPanelesHeroes().get(i) */
    @Deprecated
    public PanelPersonaje getPanelGuerrero() {
        return panelesHeroes.isEmpty() ? null : panelesHeroes.get(0);
    }

    /** @deprecated  Usa getPanelesEnemigos().get(0) */
    @Deprecated
    public PanelPersonaje getPanelEnemigo1() {
        return panelesEnemigos.size() > 0 ? panelesEnemigos.get(0) : null;
    }

    /** @deprecated  Usa getPanelesEnemigos().get(1) */
    @Deprecated
    public PanelPersonaje getPanelEnemigo2() {
        return panelesEnemigos.size() > 1 ? panelesEnemigos.get(1) : null;
    }

    /** @deprecated  Usa getPanelesEnemigos().get(2) */
    @Deprecated
    public PanelPersonaje getPanelEnemigo3() {
        return panelesEnemigos.size() > 2 ? panelesEnemigos.get(2) : null;
    }
}