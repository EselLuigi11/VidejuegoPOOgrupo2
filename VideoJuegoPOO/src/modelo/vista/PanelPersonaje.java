package modelo.vista;

import java.awt.*;
import javax.swing.*;
import modelo.Entidad;
import modelo.entidades.Heroe;

/**
 * Panel que representa visualmente a UNA entidad (Héroe o Enemigo) en combate.
 *
 * CAMBIOS vs versión anterior:
 *  - Ya NO recibe un String hardcodeado. Recibe una Entidad real del modelo.
 *  - Expone refresh() para que el Controlador sincronice la UI con el modelo
 *    en un único punto (actualizarInterfazGrafica).
 *  - Barra de maná visible solo para Héroes.
 *  - Color de alerta cuando HP < 30%.
 */
public class PanelPersonaje extends JPanel {

    // ── Referencia al modelo ──────────────────────────────────────────────────
    private final Entidad entidad;

    // ── Componentes de vista ──────────────────────────────────────────────────
    private final JLabel        lblNombre;
    private final JProgressBar  barraVida;
    private final JProgressBar  barraMana;   // null para Enemigos
    private final JLabel        lblVidaTexto;
    private final JLabel        lblManaTexto; // null para Enemigos
    private final JLabel        lblImagen;
    private ImageIcon spriteIdle; /*ambas agregadas con animaciones*/
    private ImageIcon spriteAtaque;

    // ── Paleta ────────────────────────────────────────────────────────────────
    private static final Color COLOR_VIDA_OK    = new Color(50, 200, 70);
    private static final Color COLOR_VIDA_BAJA  = new Color(220, 50, 50);
    private static final Color COLOR_MANA       = new Color(60, 120, 220);
    private static final Color COLOR_BG         = new Color(15, 15, 30, 210);
    private static final Color COLOR_BORDE      = new Color(90, 90, 150);
    private static final Font  FUENTE_NOMBRE    = new Font("Serif",      Font.BOLD,  13);
    private static final Font  FUENTE_STATS     = new Font("Monospaced", Font.PLAIN, 10);

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * @param entidad  La entidad del modelo que este panel representa.
     *                 No puede ser null.
     */
    public PanelPersonaje(Entidad entidad) {
        if (entidad == null) throw new IllegalArgumentException("Entidad no puede ser nula.");
        this.entidad = entidad;

        setLayout(new GridBagLayout());
        setOpaque(true);
        setBackground(COLOR_BG);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        setPreferredSize(new Dimension(170, esHeroe() ? 155 : 130));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets    = new Insets(2, 2, 2, 2);
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.weightx   = 1.0;

        // ── Fila 0: sprite ────────────────────────────────────────────────────
        lblImagen = cargarSprite(entidad.getNombre());
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblImagen, gbc);

        // ── Fila 1: nombre ────────────────────────────────────────────────────
        lblNombre = new JLabel(entidad.getNombre(), SwingConstants.CENTER);
        lblNombre.setFont(FUENTE_NOMBRE);
        lblNombre.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(lblNombre, gbc);

        // ── Fila 2: barra de vida ─────────────────────────────────────────────
        barraVida = crearBarra(entidad.getVidaMax(), COLOR_VIDA_OK);
        gbc.gridy = 2;
        add(barraVida, gbc);

        lblVidaTexto = crearLabelStat();
        gbc.gridy = 3;
        add(lblVidaTexto, gbc);

        // ── Filas 4-5: maná (solo Héroes) ─────────────────────────────────────
        if (esHeroe()) {
            Heroe h = (Heroe) entidad;
            barraMana    = crearBarra(Math.max(h.getManaMax(), 1), COLOR_MANA);
            lblManaTexto = crearLabelStat();
            gbc.gridy = 4; add(barraMana,    gbc);
            gbc.gridy = 5; add(lblManaTexto, gbc);
        } else {
            barraMana    = null;
            lblManaTexto = null;
        }

        // Primera sincronización
        refresh();
    }

    // ── API pública ───────────────────────────────────────────────────────────

    /**
     * Sincroniza TODOS los componentes visuales con el estado actual de la Entidad.
     * El Controlador llama a este método desde actualizarInterfazGrafica().
     */
    public void refresh() {
        int vida    = entidad.getVida();
        int vidaMax = entidad.getVidaMax();

        barraVida.setMaximum(Math.max(vidaMax, 1));
        barraVida.setValue(Math.max(0, vida));
        barraVida.setForeground(
            (double) vida / vidaMax < 0.30 ? COLOR_VIDA_BAJA : COLOR_VIDA_OK
        );
        lblVidaTexto.setText("HP " + vida + "/" + vidaMax);

        if (esHeroe()) {
            Heroe h   = (Heroe) entidad;
            int mana  = h.getMana();
            int manaM = Math.max(h.getManaMax(), 1);
            barraMana.setMaximum(manaM);
            barraMana.setValue(Math.max(0, mana));
            lblManaTexto.setText("MP " + mana + "/" + manaM);
        }

        // Tachar el nombre si cayó en combate
        boolean muerto = vida <= 0;
        lblNombre.setText(muerto
            ? "<html><s>" + entidad.getNombre() + "</s></html>"
            : entidad.getNombre());
    }

    /** Devuelve la entidad del modelo asociada a este panel. */
    public Entidad getEntidad() { return entidad; }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private boolean esHeroe() { return entidad instanceof Heroe; }

    private JProgressBar crearBarra(int maximo, Color color) {
        JProgressBar b = new JProgressBar(0, maximo);
        b.setForeground(color);
        b.setBackground(new Color(35, 35, 55));
        b.setBorderPainted(false);
        b.setStringPainted(false);
        b.setPreferredSize(new Dimension(0, 10));
        return b;
    }

    private JLabel crearLabelStat() {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setFont(FUENTE_STATS);
        lbl.setForeground(Color.LIGHT_GRAY);
        return lbl;
    }

    /**
     * Intenta cargar /img/<nombre_en_minúsculas>.png.
     * Si no existe, muestra un placeholder de texto para no romper la UI.
     */
    private JLabel cargarSprite(String nombre) { /*cambiado todo para agregar animaciones*/
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        
        System.out.println("CARPETA IMG = "
                + getClass().getResource("/img/"));

        try {
            String base = nombre.toLowerCase().replace(" ", "_");

            java.net.URL idleURL =
                    getClass().getResource("/img/" + base + "_idle.png");

            java.net.URL ataqueURL =
                    getClass().getResource("/img/" + base + "_ataque.png");
            
            System.out.println("Entidad: " + nombre);
            System.out.println("Buscando idle: /img/" + base + "_idle.png");
            System.out.println("Buscando ataque: /img/" + base + "_ataque.png");
            System.out.println("idleURL = " + idleURL);
            System.out.println("ataqueURL = " + ataqueURL);

            if (idleURL != null) {

                Image idleImg = new ImageIcon(idleURL)
                        .getImage()
                        .getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                spriteIdle = new ImageIcon(idleImg);

                lbl.setIcon(spriteIdle);

                if (ataqueURL != null) {
                    Image atkImg = new ImageIcon(ataqueURL)
                            .getImage()
                            .getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                    spriteAtaque = new ImageIcon(atkImg);
                }
            } else {
                lbl.setText("[" + nombre.charAt(0) + "]");
            }

        } catch (Exception e) {
            lbl.setText("[?]");
        }

        return lbl;
    }
    
    public void mostrarAtaque() { /*agregado con las animaciones*/

        if (spriteAtaque == null)
            return;

        lblImagen.setIcon(spriteAtaque);

        javax.swing.Timer timer =
                new javax.swing.Timer(400, e -> {
                    lblImagen.setIcon(spriteIdle);
                });

        timer.setRepeats(false);
        timer.start();
    }
}