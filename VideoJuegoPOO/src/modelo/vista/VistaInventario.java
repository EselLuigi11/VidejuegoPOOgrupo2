// REEMPLAZAR VistaInventario.java completo:

package modelo.vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import modelo.Item;

public class VistaInventario extends JFrame {

    private JPanel      panelItems;
    private List<JButton> botonesUsar;

    public VistaInventario() {
        setTitle("Inventario y Equipo");
        setSize(480, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        botonesUsar = new ArrayList<>();

        // Cabecera
        JLabel titulo = new JLabel("  MOCHILA", SwingConstants.LEFT);
        titulo.setFont(new Font("Serif", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        add(titulo, BorderLayout.NORTH);

        // Panel de ítems con scroll
        panelItems = new JPanel();
        panelItems.setLayout(new BoxLayout(panelItems, BoxLayout.Y_AXIS));
        panelItems.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JScrollPane scroll = new JScrollPane(panelItems);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> setVisible(false));
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sur.add(btnCerrar);
        add(sur, BorderLayout.SOUTH);
    }

    /**
     * Carga y renderiza los ítems reales del inventario.
     * Llamar ANTES de setVisible(true) desde el Controlador.
     */
    public void cargarItems(List<Item> items) {
        panelItems.removeAll();
        botonesUsar.clear();

        if (items.isEmpty()) {
            panelItems.add(new JLabel("  (La mochila está vacía)"));
        }

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            JPanel fila = new JPanel(new BorderLayout(6, 0));
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            fila.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));

            JLabel lbl = new JLabel((i + 1) + ". " + item.getNombre()
                    + "  —  " + item.getDescripcion());
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));

            JButton btn = new JButton("Usar");
            btn.setPreferredSize(new Dimension(70, 28));
            botonesUsar.add(btn);

            fila.add(lbl,  BorderLayout.CENTER);
            fila.add(btn,  BorderLayout.EAST);
            panelItems.add(fila);
            panelItems.add(Box.createVerticalStrut(2)); // separador visual
        }

        panelItems.revalidate();
        panelItems.repaint();
    }

    public List<JButton> getBotonesUsar() { return botonesUsar; }
}