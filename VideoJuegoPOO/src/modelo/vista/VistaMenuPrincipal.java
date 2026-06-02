package modelo.vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class VistaMenuPrincipal extends JFrame {
    
    public JButton btnNuevaPartida;
    public JButton btnCargar;
    public JButton btnSalir;

    public VistaMenuPrincipal() {
        setTitle("Juego RPG - Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 10, 10));

        btnNuevaPartida = new JButton("Nueva Partida");
        btnCargar = new JButton("Cargar");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnNuevaPartida);
        panelBotones.add(btnCargar);
        panelBotones.add(btnSalir);

        add(panelBotones);
    }
}