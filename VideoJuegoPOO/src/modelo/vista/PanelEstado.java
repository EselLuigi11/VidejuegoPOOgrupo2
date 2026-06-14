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
    // EN: VideoJuegoPOO/src/modelo/vista/PanelEstado.java
    // REEMPLAZAR las constantes de posición y tamaño:

    private static final int ANCHO = 900;
    private static final int ALTO = 500;

    // Héroes: columna izquierda, de arriba a abajo, con margen suficiente.
    // Cada panel mide 160×100 → gap vertical de 10px entre cada uno.
    // 5 héroes × 110 = 550 > ALTO, así que usamos 2 columnas side-by-side.
    private static final int[][] POS_HEROES = {
    		{ 10,  10 },   // Héroe 1
    	    { 10, 175 },   // Héroe 2  (155 + 20 de margen)
    	    {190,  10 },   // Héroe 3
    	    {190, 175 },   // Héroe 4
    	    { 10, 340 }    // Héroe 5  (solo si hay 5 héroes; cabe en ALTO=420 ajustado)
    	};

    	private static final int[][] POS_ENEMIGOS = {
    	    {660,  10  },
    	    {660,  150 },
    	    {660,  290 }
    	};

    private static final Dimension TAM_PANEL_HEROE   = new Dimension(170, 155);
    private static final Dimension TAM_PANEL_ENEMIGO = new Dimension(170, 130);

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
    
    public PanelPersonaje buscarPanelHeroe(Heroe heroe) { /*animaciones de heroe*/

        for (PanelPersonaje panel : panelesHeroes) {

            if (panel.getEntidad() == heroe) {
                return panel;
            }
        }

        return null;
    }
    
    public PanelPersonaje buscarPanelEnemigo(Enemigo enemigo) { /*animaciones de enemigo*/
        for (PanelPersonaje p : panelesEnemigos) {
            if (p.getEntidad() == enemigo) {
                return p;
            }
        }
        return null;
    }

    /**
     * Refresca TODOS los paneles con el estado actual de sus entidades.
     * El Controlador llama a este único método desde actualizarInterfazGrafica().
     */
    public void refreshTodos() {
        for (PanelPersonaje p : panelesHeroes)   p.refresh();
        for (PanelPersonaje p : panelesEnemigos) p.refresh();
    }

    /**
     * Resalta el panel correspondiente a la entidad que tiene el turno actual.
     * @param entidadActiva La entidad cuyo turno está en curso (puede ser null).
     */
    public void refrescarActivo(modelo.Entidad entidadActiva) {
        for (PanelPersonaje p : panelesHeroes) {
            p.setActivo(p.getEntidad() == entidadActiva);
        }
        for (PanelPersonaje p : panelesEnemigos) {
            p.setActivo(p.getEntidad() == entidadActiva);
        }
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