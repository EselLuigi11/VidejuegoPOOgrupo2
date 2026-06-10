package controlador;

import javax.swing.JOptionPane;
import java.util.List;
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
	private int nivelActual = 1;
	
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
		// BOTONES DEL MENÚ PRINCIPAL
		// ==========================================
		this.vistaMenu.btnNuevaPartida.addActionListener(e -> {
			this.vistaMenu.setVisible(false);
			this.vistaBatalla.setVisible(true);
			
			// POO: Inicializamos la vista dinámica inyectando las listas reales del modelo
			this.vistaBatalla.getPanelEstado().inicializar(
				partida.getGrupo().getHeroesVivos(), 
				orquestador.getBatallaActual().getEnemigos()
			);
			actualizarBarrasPantalla();
		});

		this.vistaMenu.btnSalir.addActionListener(e -> {
			System.exit(0); 
		});
		
		// ==========================================
		// 1. GESTIÓN DEL BOTÓN ATACAR
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnAtacar().addActionListener(e -> {
			try {
				Heroe heroeActivo = orquestador.getHeroeActual();
				if (heroeActivo == null) {
					throw new IllegalStateException("No es el turno de un héroe o no quedan héroes vivos.");
				}

				List<Enemigo> enemigosVivos = orquestador.getEnemigosVivos();
				if (enemigosVivos.isEmpty()) {
					throw new IllegalStateException("No hay rivales en el campo de batalla.");
				}

				// Interfaz interactiva para selección de objetivo múltiple
				String[] nombresEnemigos = enemigosVivos.stream().map(Enemigo::getNombre).toArray(String[]::new);
				String seleccion = (String) JOptionPane.showInputDialog(
						vistaBatalla,
						"Selecciona tu objetivo de ataque:",
						"Elegir Enemigo",
						JOptionPane.QUESTION_MESSAGE,
						null,
						nombresEnemigos,
						nombresEnemigos[0]
				);

				if (seleccion == null) return; 

				Enemigo enemigoObjetivo = enemigosVivos.stream()
						.filter(en -> en.getNombre().equals(seleccion))
						.findFirst()
						.orElse(enemigosVivos.get(0));

				// Ejecución del turno
				Atacar ataque = new Atacar(heroeActivo, enemigoObjetivo);
				String logBatalla = orquestador.procesarTurno(ataque);
				this.vistaBatalla.appendHistorial(logBatalla);

				actualizarBarrasPantalla();
				comprobarProgresoJuego();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Movimiento Inválido", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==========================================
		// 2. GESTIÓN DEL BOTÓN DEFENDER
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnDefender().addActionListener(e -> {
			try {
				Heroe heroeActivo = orquestador.getHeroeActual();
				if (heroeActivo == null) throw new IllegalStateException("No es turno de defenderse.");
				
				Defender defensa = new Defender(heroeActivo);
				String logBatalla = orquestador.procesarTurno(defensa);
				this.vistaBatalla.appendHistorial(logBatalla);
				
				actualizarBarrasPantalla();
				comprobarProgresoJuego();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Error al defender", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		// ==========================================
		// 3. GESTIÓN DEL INVENTARIO
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnUsarItem().addActionListener(e -> {
			try {
				// Aquí deberías evaluar si es el turno de un héroe antes de abrir
				if (orquestador.getHeroeActual() == null) {
					throw new IllegalStateException("No puedes usar objetos en el turno del enemigo.");
				}
				this.vistaInventario.setVisible(true);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Acción Inválida", JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	// ── Métodos de Refactorización POO ────────────────────────────────────────

	private void actualizarBarrasPantalla() {
		// Principio de Delegación: El Controlador ordena refrescar, la Vista sabe cómo hacerlo internamente.
		this.vistaBatalla.getPanelEstado().refreshTodos();
	}

	private void comprobarProgresoJuego() {
		if (orquestador.getEnemigosVivos().isEmpty()) {
			nivelActual++;
			JOptionPane.showMessageDialog(vistaBatalla, "¡Victoria! Avanzando al Nivel " + nivelActual, "Fase Completada", JOptionPane.INFORMATION_MESSAGE);
			
			// Curamos a la party mediante métodos propios (Encapsulamiento)
			for (Heroe h : partida.getGrupo().getHeroesVivos()) {
				h.restaurarStatusCompleto();
			}
			
			// Solicitamos la nueva batalla al Catálogo
			String msgCarga = orquestador.iniciarBatalla(nivelActual);
			this.vistaBatalla.appendHistorial("\n--- " + msgCarga + " ---");

			if (orquestador.getBatallaActual() != null) {
				// RE-INICIALIZAMOS la vista dinámica porque los enemigos cambiaron
				this.vistaBatalla.getPanelEstado().inicializar(
					partida.getGrupo().getHeroesVivos(), 
					orquestador.getBatallaActual().getEnemigos()
				);
				actualizarBarrasPantalla();
			} else {
				JOptionPane.showMessageDialog(vistaBatalla, "¡Felicitaciones! Has completado todos los niveles.", "Fin de la Aventura", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}
		
		if (orquestador.getHeroesVivos().isEmpty() || !partida.isEstado()) {
			JOptionPane.showMessageDialog(vistaBatalla, "Tu equipo ha caído en combate.", "Game Over", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}