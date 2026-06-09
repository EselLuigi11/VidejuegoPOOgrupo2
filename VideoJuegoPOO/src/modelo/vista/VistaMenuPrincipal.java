package modelo.vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.GridLayout;
import java.awt.BorderLayout;

public class VistaMenuPrincipal extends JFrame {
    
    public JButton btnNuevaPartida;
    public JButton btnCargar;
    public JButton btnSalir;

    public VistaMenuPrincipal() {
        setTitle("Juego RPG - Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("/img/fondo_menu.png")));
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 10, 10));
        panelBotones.setOpaque(false);

        btnNuevaPartida = new JButton("Nueva Partida");
        btnCargar = new JButton("Cargar");
        btnSalir = new JButton("Salir");

        btnNuevaPartida.setOpaque(false);
        btnNuevaPartida.setContentAreaFilled(false);
        
        btnCargar.setOpaque(false);
        btnCargar.setContentAreaFilled(false);
        
        btnSalir.setOpaque(false);
        btnSalir.setContentAreaFilled(false);

        panelBotones.add(btnNuevaPartida);
        panelBotones.add(btnCargar);
        panelBotones.add(btnSalir);

        fondo.add(panelBotones, BorderLayout.CENTER);
    }
}