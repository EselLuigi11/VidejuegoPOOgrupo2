package modelo.vista;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;

public class PanelEstado extends JPanel {

    private final List<PanelPersonaje> panelesHeroes   = new ArrayList<>();
    private final List<PanelPersonaje> panelesEnemigos = new ArrayList<>();

    private final JPanel franjaTurnos;
    private final JLabel lblOrdenTurnos;

    private static final int ANCHO       = 900;
    private static final int ALTO        = 480;
    private static final int FRANJA_Y    = 440;   // siempre debajo de todos los paneles
    private static final int FRANJA_ALTO = 36;

    // 2 columnas × 2 filas → 4 héroes visibles sin solaparse, el 5to en col 3
    private static final int[][] POS_HEROES = {
        {  10,  10 },  // Héroe 1
        {  10, 210 },  // Héroe 2
        { 190,  10 },  // Héroe 3
        { 190, 210 },  // Héroe 4
        { 370,  10 }   // Héroe 5
    };

    private static final int[][] POS_ENEMIGOS = {
        { 680,  10  },
        { 680, 155  },
        { 680, 300  }
    };

    private static final Dimension TAM_PANEL_HEROE   = new Dimension(170, 190);
    private static final Dimension TAM_PANEL_ENEMIGO = new Dimension(170, 130);

    public PanelEstado() {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(ANCHO, ALTO));

        franjaTurnos = new JPanel(new BorderLayout());
        franjaTurnos.setOpaque(true);
        franjaTurnos.setBackground(new Color(10, 10, 20));
        franjaTurnos.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 60), 1));
        franjaTurnos.setBounds(0, FRANJA_Y, ANCHO, FRANJA_ALTO);

        lblOrdenTurnos = new JLabel("Turnos: —", SwingConstants.CENTER);
        lblOrdenTurnos.setFont(new Font("Monospaced", Font.BOLD, 13));
        lblOrdenTurnos.setForeground(Color.YELLOW);
        lblOrdenTurnos.setOpaque(false);

        franjaTurnos.add(lblOrdenTurnos, BorderLayout.CENTER);
        add(franjaTurnos);
    }

    public void inicializar(List<Heroe> heroes, List<Enemigo> enemigos) {
        removeAll();
        panelesHeroes.clear();
        panelesEnemigos.clear();
        add(franjaTurnos);

        for (int i = 0; i < heroes.size() && i < POS_HEROES.length; i++) {
            PanelPersonaje panel = new PanelPersonaje(heroes.get(i));
            panel.setBounds(POS_HEROES[i][0], POS_HEROES[i][1],
                            TAM_PANEL_HEROE.width, TAM_PANEL_HEROE.height);
            panelesHeroes.add(panel);
            add(panel);
        }

        for (int i = 0; i < enemigos.size() && i < POS_ENEMIGOS.length; i++) {
            PanelPersonaje panel = new PanelPersonaje(enemigos.get(i));
            panel.setBounds(POS_ENEMIGOS[i][0], POS_ENEMIGOS[i][1],
                            TAM_PANEL_ENEMIGO.width, TAM_PANEL_ENEMIGO.height);
            panelesEnemigos.add(panel);
            add(panel);
        }

        setComponentZOrder(franjaTurnos, getComponentCount() - 1);
        revalidate();
        repaint();
    }

    public PanelPersonaje buscarPanelHeroe(Heroe heroe) {
        for (PanelPersonaje p : panelesHeroes) if (p.getEntidad() == heroe) return p;
        return null;
    }

    public PanelPersonaje buscarPanelEnemigo(Enemigo enemigo) {
        for (PanelPersonaje p : panelesEnemigos) if (p.getEntidad() == enemigo) return p;
        return null;
    }

    public void refreshTodos() {
        for (PanelPersonaje p : panelesHeroes)   p.refresh();
        for (PanelPersonaje p : panelesEnemigos) p.refresh();
    }

    public void refrescarActivo(modelo.Entidad entidadActiva) {
        for (PanelPersonaje p : panelesHeroes)   p.setActivo(p.getEntidad() == entidadActiva);
        for (PanelPersonaje p : panelesEnemigos) p.setActivo(p.getEntidad() == entidadActiva);
    }

    public void actualizarOrdenTurnos(List<modelo.Entidad> ordenTurnos) {
        if (ordenTurnos == null || ordenTurnos.isEmpty()) {
            lblOrdenTurnos.setText("Turnos: —");
        } else {
            StringBuilder sb = new StringBuilder("Turnos:  ");
            for (int i = 0; i < ordenTurnos.size(); i++) {
                sb.append(ordenTurnos.get(i).getNombre());
                if (i < ordenTurnos.size() - 1) sb.append("  →  ");
            }
            lblOrdenTurnos.setText(sb.toString());
        }
        franjaTurnos.revalidate();
        franjaTurnos.repaint();
    }

    public List<PanelPersonaje> getPanelesHeroes()   { return panelesHeroes; }
    public List<PanelPersonaje> getPanelesEnemigos() { return panelesEnemigos; }
}