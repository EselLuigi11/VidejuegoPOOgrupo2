package modelo.vista;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class VistaInventario extends JFrame {

    private JPanel panelParty;
    private JPanel panelItems;
    private ControladorJuego controlador;

    public VistaInventario() {
        setTitle("Inventario y Equipo");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        panelParty = new JPanel();
        panelParty.setLayout(new GridLayout(0, 1));
        panelParty.add(new JLabel("--- TU PARTY ---"));

        panelItems = new JPanel();
        panelItems.setLayout(new GridLayout(0, 1));
        panelItems.add(new JLabel("--- MOCHILA ---"));

        add(panelParty, BorderLayout.WEST);
        add(panelItems, BorderLayout.CENTER);
    }

    public void setControlador(ControladorJuego c) {
        this.controlador = c;
    }

    public void actualizarInventario(List<String> nombresItems) {
        panelItems.removeAll();
        panelItems.add(new JLabel("--- MOCHILA ---"));

        for (int i = 0; i < nombresItems.size(); i++) {
            JPanel filaItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblNombre = new JLabel("- " + nombresItems.get(i));
            JButton btnUsar = new JButton("Usar");
            
            final int indice = i;
            
            btnUsar.addActionListener(e -> {
                if (controlador != null) {
                    controlador.usarPocion(indice);
                }
            });

            filaItem.add(lblNombre);
            filaItem.add(btnUsar);
            panelItems.add(filaItem);
        }
        
        panelItems.revalidate();
        panelItems.repaint();
    }
}
