package modelo.vista;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelAcciones extends JPanel {

    private JButton btnAtacar;
    private JButton btnDefender;
    private JButton btnUsarItem;
    private JButton btnGuardarPartida;

    public PanelAcciones() {

        btnAtacar = new JButton("Atacar");
        btnDefender = new JButton("Defender");
        btnUsarItem = new JButton("Usar Ítem");

        add(btnAtacar);
        add(btnDefender);
        btnGuardarPartida = new JButton("Guardar Partida");

        add(btnUsarItem);
        add(btnGuardarPartida);
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

    public JButton getBtnGuardarPartida() {
        return btnGuardarPartida;
    }
}
