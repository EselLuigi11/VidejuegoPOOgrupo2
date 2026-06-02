package modelo.vista;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelAcciones extends JPanel {

    private JButton btnAtacar;
    private JButton btnDefender;
    private JButton btnUsarItem;

    public PanelAcciones() {

        btnAtacar = new JButton("Atacar");
        btnDefender = new JButton("Defender");
        btnUsarItem = new JButton("Usar Ítem");

        add(btnAtacar);
        add(btnDefender);
        add(btnUsarItem);
    }

    public JButton getBtnAtacar() {
        return btnAtacar;
    }

    public JButton getBtnDefender() {
        return btnDefender;
    }

    public JButton getBtnUsarItem() {
        return btnUsarItem;
    }
}