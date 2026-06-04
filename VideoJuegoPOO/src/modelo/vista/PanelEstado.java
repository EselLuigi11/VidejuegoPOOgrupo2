package modelo.vista;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PanelEstado extends JPanel {

	private PanelPersonaje panelGuerrero;
	private PanelPersonaje panelMago;
	private PanelPersonaje panelArquero;
	private PanelPersonaje panelAsesino;
	private PanelPersonaje panelCurador;
	private PanelPersonaje panelEnemigo;

	// CONSTRUCTOR
	public PanelEstado() {
		setLayout(new GridLayout(7, 1, 5, 5));
		setBorder(BorderFactory.createTitledBorder("Estado de los Personajes"));

		this.panelGuerrero = new PanelPersonaje("Guerrero");
		this.panelMago = new PanelPersonaje("Mago");
		this.panelArquero = new PanelPersonaje("Arquero");
		this.panelAsesino = new PanelPersonaje("Asesino");
		this.panelCurador = new PanelPersonaje("Curador");
		this.panelEnemigo = new PanelPersonaje("Enemigo");

		add(this.panelGuerrero);
		add(this.panelMago);
		add(this.panelArquero);
		add(this.panelAsesino);
		add(this.panelCurador);
		add(this.panelEnemigo);
	}
	
	public PanelPersonaje getPanelGuerrero() {
		return panelGuerrero;
	}

	public PanelPersonaje getPanelMago() {
		return panelMago;
	}

	public PanelPersonaje getPanelArquero() {
		return panelArquero;
	}

	public PanelPersonaje getPanelAsesino() {
		return panelAsesino;
	}

	public PanelPersonaje getPanelCurador() {
		return panelCurador;
	}

		

	public PanelPersonaje getPanelEnemigo() {
		return panelEnemigo;
	}
}
