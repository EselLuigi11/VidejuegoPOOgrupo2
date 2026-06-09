package controlador;

import javax.swing.JOptionPane;
import modelo.Partida;
import modelo.OrquestadorF;
import modelo.acciones.Atacar;
import modelo.acciones.Defender;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;
import modelo.vista.VistaMenuPrincipal;
import modelo.vista.VistaBatalla;
import modelo.vista.VistaInventario;

public class ControladorJuego {
	private Partida partida;
	private OrquestadorF orquestador;
	private VistaMenuPrincipal vistaMenu;
	private VistaBatalla vistaBatalla;
	private VistaInventario vistaInventario;
	
	public ControladorJuego(Partida partida, OrquestadorF orquestador, VistaMenuPrincipal vistaMenu, VistaBatalla vistaBatalla, VistaInventario vistaInventario) {
		this.partida = partida;
		this.orquestador = orquestador;
		this.vistaMenu = vistaMenu;
		this.vistaBatalla = vistaBatalla;
		this.vistaInventario = vistaInventario;
	}

	public void iniciar() {
		this.vistaMenu.setVisible(true);

		// ==========================================
		// BOTONES DEL MENÚ
		// ==========================================
		this.vistaMenu.btnNuevaPartida.addActionListener(e -> {
			this.vistaMenu.setVisible(false);
			this.vistaBatalla.setVisible(true);
		});

		this.vistaMenu.btnSalir.addActionListener(e -> {
			System.exit(0); 
		});
		
		// ==========================================
		// 1. MICRÓFONO PARA "ATACAR"
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnAtacar().addActionListener(e -> {
			try {
				// Usamos los nuevos getters simplificados de Luis
				if (orquestador.getHeroesVivos().isEmpty()) throw new Exception("No quedan héroes vivos.");
				if (orquestador.getEnemigosVivos().isEmpty()) throw new Exception("No hay enemigos a los que atacar.");

				Heroe heroeActivo = orquestador.getHeroesVivos().get(0); 
				Enemigo enemigoObjetivo = orquestador.getEnemigosVivos().get(0); 

				// Creamos el ataque y dejamos que el nuevo Orquestador haga su magia
				Atacar ataque = new Atacar(heroeActivo, enemigoObjetivo);
				String logBatalla = orquestador.procesarTurno(ataque); 
				
				// Mostramos el log de la batalla en la consola (o después en tu historial visual)
				System.out.println(logBatalla);

				// Actualizamos barras visuales (asegurate de tener getVidaMax() configurado en las entidades)
				this.vistaBatalla.getPanelEstado().getPanelGuerrero().actualizarBarraVisual(heroeActivo.getVida(), heroeActivo.getVidaMax());
				this.vistaBatalla.getPanelEstado().getPanelEnemigo().actualizarBarraVisual(enemigoObjetivo.getVida(), enemigoObjetivo.getVidaMax());

				if (!enemigoObjetivo.estaVivo()) {
					JOptionPane.showMessageDialog(vistaBatalla, "¡Enemigo derrotado!", "Victoria", JOptionPane.INFORMATION_MESSAGE);
				}

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Acción Inválida", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==========================================
		// 2. MICRÓFONO PARA "DEFENDER"
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnDefender().addActionListener(e -> {
			try {
				if (orquestador.getHeroesVivos().isEmpty()) throw new Exception("No quedan héroes vivos.");
				Heroe heroeActivo = orquestador.getHeroesVivos().get(0);
				
				Defender defensa = new Defender(heroeActivo);
				String logBatalla = orquestador.procesarTurno(defensa);
				
				System.out.println(logBatalla);
				
				this.vistaBatalla.getPanelEstado().getPanelGuerrero().actualizarBarraVisual(heroeActivo.getVida(), heroeActivo.getVidaMax());

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Error al defender", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		// ==========================================
		// 3. MICRÓFONO PARA "USAR ÍTEM"
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnUsarItem().addActionListener(e -> {
			try {
				this.vistaInventario.setVisible(true);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, "No se pudo abrir el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}