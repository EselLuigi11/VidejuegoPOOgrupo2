package modelo.vista;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class PanelPersonaje extends JPanel {

    private JLabel lblNombre;
    private JProgressBar barraVida;
    private JLabel lblImagen;

    public PanelPersonaje(String nombre) {

        setLayout(new BorderLayout(10, 0));

        lblNombre = new JLabel(nombre);

        barraVida = new JProgressBar(0, 100);
        barraVida.setValue(100);
        barraVida.setStringPainted(true);

        add(lblNombre, BorderLayout.WEST);
        add(barraVida, BorderLayout.CENTER);
        
        // Manejamos posibles errores por si la imagen no existe
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/img/" + nombre.toLowerCase() + ".png"));
            lblImagen = new JLabel(icono);
            add(lblImagen, BorderLayout.EAST);
        } catch (Exception e) {
            System.out.println("No se encontró la imagen para: " + nombre);
        }
    }

    public JProgressBar getBarraVida() {
        return barraVida;
    }

    public void actualizarBarraVisual(int vidaActual, int vidaMax) {
        barraVida.setMaximum(vidaMax);
        barraVida.setValue(vidaActual);
        barraVida.setString(vidaActual + "/" + vidaMax);
    }
}