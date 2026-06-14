package controlador;

import javax.swing.JOptionPane;
import modelo.vista.PanelPersonaje;
import java.util.ArrayList;
import java.util.List;
import modelo.Partida;
import modelo.RepositorioPartida;
import modelo.Batalla;
import modelo.CatalogoBatalla;
import modelo.Orquestador;
import modelo.acciones.Atacar;
import modelo.acciones.Defender;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;
import modelo.vista.VistaMenuPrincipal;
import modelo.vista.VistaBatalla;
import modelo.vista.VistaInventario;

public class ControladorJuego {
	private Partida partida;
	private Orquestador orquestador;
	private VistaMenuPrincipal vistaMenu;
	private VistaBatalla vistaBatalla;
	private VistaInventario vistaInventario;
	private int nivelActual = 1;
	private RepositorioPartida repositorio;
	
	public ControladorJuego(Partida partida, Orquestador orquestador, VistaMenuPrincipal vistaMenu, VistaBatalla vistaBatalla, VistaInventario vistaInventario, RepositorioPartida repositorio) {
		this.partida = partida;
		this.orquestador = orquestador;
		this.vistaMenu = vistaMenu;
		this.vistaBatalla = vistaBatalla;
		this.vistaInventario = vistaInventario;
		this.repositorio = new RepositorioPartida();
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

		
		this.vistaMenu.btnCargar.addActionListener(e -> {
		    if (!repositorio.existeSave()) {
		        JOptionPane.showMessageDialog(vistaMenu, "No hay ninguna partida guardada.", "Sin guardado", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		    //Si hay partida guardada, la cargamos y reconstruimos el estado del juego
		    Partida cargada = repositorio.cargar();
		    //Llama a la funcion cargar() del repositorio. Que devuelve un objeto Partida reconstruido a partir del archivo .dat, o null si hubo error.
		    if (cargada != null) {
		        this.partida = cargada;
		        this.nivelActual = cargada.getNivel();
		        // Reconstruir el orquestador con la partida cargada
		        Batalla batallaRetomada = CatalogoBatalla.getInstance()
		            .construirBatalla(nivelActual, new ArrayList<>(partida.getGrupo().getHeroesVivos()));
		        this.orquestador = new Orquestador(batallaRetomada, partida);
		        
		        vistaMenu.setVisible(false);
		        vistaBatalla.setVisible(true);
		        vistaBatalla.getPanelEstado().inicializar(
		            partida.getGrupo().getHeroesVivos(),
		            orquestador.getBatallaActual().getEnemigos()
		        );
		        actualizarBarrasPantalla();
		        JOptionPane.showMessageDialog(vistaBatalla, 
		            "Partida cargada. Último guardado: " + partida.getFechaGuardadoFormateada(),
		            "Partida Cargada", JOptionPane.INFORMATION_MESSAGE);
		    }
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


				/*Hace que se activen las animaciones*/
				PanelPersonaje panelAtacante =
				        vistaBatalla.getPanelEstado()
				                    .buscarPanelHeroe(heroeActivo);

				if (panelAtacante != null) {
				    panelAtacante.mostrarAtaque();
				}
				
				
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
		        Heroe heroeActivo = orquestador.getHeroeActual();
		        if (heroeActivo == null) {
		            throw new IllegalStateException("No puedes usar objetos en el turno del enemigo.");
		        }

		        List<modelo.Item> items = partida.getInventarioPartida().getItems();

		        // Pasamos la lista real al inventario y lo abrimos
		        vistaInventario.cargarItems(items);
		        vistaInventario.setVisible(true);

		        // Registramos listeners de "Usar" para cada botón recién creado
		        List<javax.swing.JButton> botones = vistaInventario.getBotonesUsar();
		        for (int i = 0; i < botones.size(); i++) {
		            final int idx = i;
		            // Limpiamos listeners anteriores para evitar acumulación (DRY)
		            for (java.awt.event.ActionListener al : botones.get(idx).getActionListeners()) {
		                botones.get(idx).removeActionListener(al);
		            }
		            botones.get(idx).addActionListener(evt -> {
		                try {
		                    modelo.Item itemElegido = partida.getInventarioPartida().getItems().get(idx);
		                    modelo.Accion accion = orquestador.crearAccionUsarItem(heroeActivo, itemElegido);
		                    String log = orquestador.procesarTurno(accion);
		                    vistaBatalla.appendHistorial(log);

		                    vistaInventario.setVisible(false);
		                    actualizarBarrasPantalla();
		                    comprobarProgresoJuego();
		                } catch (Exception ex) {
		                    javax.swing.JOptionPane.showMessageDialog(
		                        vistaInventario, ex.getMessage(), "Error al usar ítem",
		                        javax.swing.JOptionPane.ERROR_MESSAGE);
		                }
		            });
		        }
		    } catch (Exception ex) {
		        javax.swing.JOptionPane.showMessageDialog(
		            vistaBatalla, ex.getMessage(), "Acción Inválida",
		            javax.swing.JOptionPane.WARNING_MESSAGE);
		    }
		});
	}

	// ── Métodos de Refactorización POO ────────────────────────────────────────

	private void actualizarBarrasPantalla() {
		// Principio de Delegación: El Controlador ordena refrescar, la Vista sabe cómo hacerlo internamente.
		this.vistaBatalla.getPanelEstado().refreshTodos();
		// Resaltar al personaje actual
		modelo.Entidad activo = orquestador.getPersonajeActual();
		this.vistaBatalla.getPanelEstado().refrescarActivo(activo);
	}

	private void comprobarProgresoJuego() {
		if (orquestador.getEnemigosVivos().isEmpty()) {
			nivelActual++;
			
			// Curamos a la party mediante métodos propios (Encapsulamiento)
			for (Heroe h : partida.getGrupo().getHeroesVivos()) {
				h.restaurarStatusCompleto();
			}
			
			// Solicitamos la nueva batalla al Catálogo
			String msgCarga = orquestador.iniciarBatalla(nivelActual);

			if (orquestador.getBatallaActual() != null) {
				JOptionPane.showMessageDialog(vistaBatalla, "¡Victoria! Avanzando al Nivel " + nivelActual, "Fase Completada", JOptionPane.INFORMATION_MESSAGE);
				this.vistaBatalla.appendHistorial("\n--- " + msgCarga + " ---");
				// RE-INICIALIZAMOS la vista dinámica porque los enemigos cambiaron
				this.vistaBatalla.getPanelEstado().inicializar(
					partida.getGrupo().getHeroesVivos(), 
					orquestador.getBatallaActual().getEnemigos()
				);
				actualizarBarrasPantalla();
			} else {
				JOptionPane.showMessageDialog(vistaBatalla, "¡Ganaste el juego!", "Juego Completado", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}
		
		if (orquestador.getHeroesVivos().isEmpty() || !partida.isEstado()) {
			JOptionPane.showMessageDialog(vistaBatalla, "Tu equipo ha caído en combate.", "Game Over", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}
