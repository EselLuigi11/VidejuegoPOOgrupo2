package vista;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class VistaBatalla extends JFrame {
	
    private PanelEstado panelEstado;
    private PanelHistorial panelHistorial;
    private PanelAcciones panelAcciones;

    public VistaBatalla() {

    	setTitle("Videojuego POO - Batalla");

        setSize(900, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        panelEstado = new PanelEstado();
        panelHistorial = new PanelHistorial();
        panelAcciones = new PanelAcciones();

        add(panelEstado, BorderLayout.NORTH);
        add(panelHistorial, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);

        setVisible(true);
    }
}
