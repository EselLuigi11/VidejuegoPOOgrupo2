package modelo.vista;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

/**
 * VistaBatalla — ventana principal del combate.
 *
 * CAMBIOS vs versión anterior:
 *  - Carga fondo_batalla.png de forma segura (null-check).
 *  - Usa un JPanel personalizado (PanelFondo) como contentPane para que
 *    paintComponent dibuje la imagen sin romper el layout normal.
 *  - PanelEstado y PanelHistorial se mantienen opacos/transparentes según
 *    corresponde.
 *  - Expone inicializarBatalla() para que el Controlador llame a
 *    PanelEstado.inicializar() y refresque la vista al comenzar cada nivel.
 */
public class VistaBatalla extends JFrame {

    private final PanelEstado    panelEstado;
    private final PanelHistorial panelHistorial;
    private final PanelAcciones  panelAcciones;

    // ─────────────────────────────────────────────────────────────────────────

    public VistaBatalla() {
        setTitle("Videojuego POO — Batalla");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Panel con fondo pintado manualmente ───────────────────────────────
        PanelFondo panelFondo = new PanelFondo("/img/fondo_batalla.png");
        panelFondo.setLayout(new BorderLayout());
        setContentPane(panelFondo);

        // ── Sub-paneles ───────────────────────────────────────────────────────
        panelEstado    = new PanelEstado();   // ya es opaco=false internamente
        panelHistorial = new PanelHistorial();
        panelAcciones  = new PanelAcciones();

        panelHistorial.setPreferredSize(new Dimension(900, 120));
        panelHistorial.setOpaque(false);      // se ve el fondo debajo

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(panelHistorial, BorderLayout.CENTER);
        panelInferior.add(panelAcciones,  BorderLayout.SOUTH);

        panelFondo.add(panelEstado,    BorderLayout.CENTER);
        panelFondo.add(panelInferior,  BorderLayout.SOUTH);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public PanelAcciones  getPanelAcciones()  { return panelAcciones;  }
    public PanelEstado    getPanelEstado()    { return panelEstado;    }
    public PanelHistorial getPanelHistorial() { return panelHistorial; }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Agrega una línea al historial de batalla y hace scroll al final. */
    public void appendHistorial(String mensaje) {
        panelHistorial.getHistorial().append(mensaje + "\n");
        panelHistorial.getHistorial().setCaretPosition(
            panelHistorial.getHistorial().getDocument().getLength()
        );
    }

    // =========================================================================
    // Inner class: Panel con imagen de fondo
    // =========================================================================

    /**
     * JPanel que dibuja una imagen como fondo escalado al tamaño del panel.
     * Si la imagen no existe, pinta un degradado oscuro como fallback.
     */
    private static class PanelFondo extends JPanel {

        private Image imagenFondo;

        public PanelFondo(String rutaRecurso) {
            try {
                URL url = getClass().getResource(rutaRecurso);
                if (url != null) {
                    imagenFondo = new ImageIcon(url).getImage();
                } else {
                    System.out.println("[VistaBatalla] Advertencia: no se encontró "
                        + rutaRecurso + ". Se usará fondo degradado.");
                }
            } catch (Exception e) {
                System.out.println("[VistaBatalla] Error al cargar fondo: " + e.getMessage());
            }
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenFondo != null) {
                // Escala la imagen al tamaño actual del panel
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback: degradado oscuro para que el juego no quede en blanco
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(
                    0, 0, new Color(20, 10, 40),
                    0, getHeight(), new Color(5, 5, 15)
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}