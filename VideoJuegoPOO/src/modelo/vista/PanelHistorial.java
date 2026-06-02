package modelo.vista;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PanelHistorial extends JPanel {

    private JTextArea historial;

    public PanelHistorial() {

        setLayout(new BorderLayout());

        setBorder(
            BorderFactory.createTitledBorder("Historial")
        );

        historial = new JTextArea();

        historial.setEditable(false);

        historial.append("Comienza la batalla...\n");

        JScrollPane scroll =
                new JScrollPane(historial);

        add(scroll, BorderLayout.CENTER);
    }

    public JTextArea getHistorial() {
        return historial;
    }
}