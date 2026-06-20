package controlador;

import javax.swing.JOptionPane;
import modelo.vista.PanelPersonaje;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import modelo.Partida;
import modelo.RepositorioPartida;
import modelo.Batalla;
import modelo.CatalogoBatalla;
import modelo.Orquestador;
import modelo.acciones.Atacar;
import modelo.acciones.Defender;
import modelo.entidades.Arquero;
import modelo.entidades.Asesino;
import modelo.entidades.Enemigo;
import modelo.entidades.Guerrero;
import modelo.entidades.Heroe;
import modelo.pociones.PocionVida;
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
		this.repositorio = repositorio;
	}

	public void iniciar() {
		this.vistaMenu.setVisible(true);

		// ==========================================
		// BOTONES DEL MENÚ PRINCIPAL
		// ==========================================
		this.vistaMenu.btnNuevaPartida.addActionListener(e -> {
			this.vistaMenu.setVisible(false);
			this.vistaBatalla.setVisible(true);

			// MODIFICADO: PanelEstado ya no se arma con paneles hardcodeados
			// ("Guerrero" / "Enemigo" fijos). Ahora se inicializa dinámicamente
			// iterando las listas reales de héroes y enemigos del modelo.
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
		    Partida cargada = repositorio.cargar();
		    if (cargada != null) {
		        this.partida = cargada;
		        this.nivelActual = cargada.getNivel();
		        // MODIFICADO: CatalogoBatalla.construirBatalla() ahora devuelve
		        // enemigos CLONADOS (ver Enemigo.java y CatalogoBatalla.java),
		        // así una batalla retomada nunca arrastra enemigos muertos/dañados
		        // de una ejecución anterior.
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

		this.vistaBatalla.getPanelAcciones().getBtnGuardarPartida().addActionListener(e -> {
			partida.setNivel(nivelActual);
			boolean guardada = repositorio.guardar(partida);
			if (guardada) {
				JOptionPane.showMessageDialog(
					vistaBatalla,
					"Partida guardada. Podés retomarla desde Cargar Partida.",
					"Partida Guardada",
					JOptionPane.INFORMATION_MESSAGE
				);
			} else {
				JOptionPane.showMessageDialog(
					vistaBatalla,
					"No se pudo guardar la partida.",
					"Error al guardar",
					JOptionPane.ERROR_MESSAGE
				);
			}
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

				List<Enemigo> enemigosVivos = orquestador.getBatallaActual().getEnemigosVivos();
				if (enemigosVivos.isEmpty()) {
					throw new IllegalStateException("No hay rivales en el campo de batalla.");
				}

				// MODIFICADO: selección real de objetivo vía JOptionPane en
				// vez de tomar siempre enemigosVivos.get(0) a ciegas.
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


				Map<Heroe, Integer> nivelesAntes = obtenerNivelesActuales();
				Atacar ataque = new Atacar(heroeActivo, enemigoObjetivo);
				String logBatalla = orquestador.procesarTurno(ataque);
				vistaBatalla.appendHistorial(logBatalla);

				actualizarBarrasPantalla();
				comprobarSubidaDeNivel(nivelesAntes);
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

				Map<Heroe, Integer> nivelesAntes = obtenerNivelesActuales();
				Defender defensa = new Defender(heroeActivo);
				String logBatalla = orquestador.procesarTurno(defensa);
				vistaBatalla.appendHistorial(logBatalla);

				actualizarBarrasPantalla();
				comprobarSubidaDeNivel(nivelesAntes);
				comprobarProgresoJuego();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Error al defender", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==========================================
		// 3. GESTIÓN DEL BOTÓN HABILIDAD
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnHabilidad().addActionListener(e -> {
			try {
				Heroe heroeActivo = orquestador.getHeroeActual();
				if (heroeActivo == null) {
					throw new IllegalStateException("No es el turno de un héroe.");
				}

				Enemigo objetivo = null;

				// Guerrero, Arquero y Asesino necesitan seleccionar un objetivo enemigo
				if (heroeActivo instanceof Guerrero
						|| heroeActivo instanceof Arquero
						|| heroeActivo instanceof Asesino) {

					List<Enemigo> enemigosVivos = orquestador.getBatallaActual().getEnemigosVivos();
					if (enemigosVivos.isEmpty()) {
						throw new IllegalStateException("No hay enemigos en el campo de batalla.");
					}

					String[] nombres = enemigosVivos.stream()
							.map(Enemigo::getNombre).toArray(String[]::new);
					String seleccion = (String) JOptionPane.showInputDialog(
							vistaBatalla,
							"Selecciona el objetivo de tu habilidad:",
							"Usar Habilidad",
							JOptionPane.QUESTION_MESSAGE,
							null,
							nombres,
							nombres[0]
					);

					if (seleccion == null) return;

					objetivo = enemigosVivos.stream()
							.filter(en -> en.getNombre().equals(seleccion))
							.findFirst()
							.orElse(enemigosVivos.get(0));
				}

				Map<Heroe, Integer> nivelesAntes = obtenerNivelesActuales();
				// NOTA: la firma de procesarHabilidad() en el Orquestador no
				// cambió desde afuera; internamente ahora delega en
				// heroe.usarHabilidadEspecial(...) en vez de un if/instanceof.
				String logBatalla = orquestador.procesarHabilidad(heroeActivo, objetivo);
				registrarAccionEnHistorial("", logBatalla);
				actualizarBarrasPantalla();
				comprobarSubidaDeNivel(nivelesAntes);
				comprobarProgresoJuego();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Habilidad Inválida", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==========================================
		// 4. GESTIÓN DEL INVENTARIO
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnUsarItem().addActionListener(e -> {
		    try {
		        Heroe heroeActivo = orquestador.getHeroeActual();
		        if (heroeActivo == null) {
		            throw new IllegalStateException("No puedes usar objetos en el turno del enemigo.");
		        }

		        List<modelo.Item> items = partida.getInventarioPartida().getItems();

		        vistaInventario.cargarItems(items);
		        vistaInventario.setVisible(true);

		        List<javax.swing.JButton> botones = vistaInventario.getBotonesUsar();
		        for (int i = 0; i < botones.size(); i++) {
		            final int idx = i;
		            for (java.awt.event.ActionListener al : botones.get(idx).getActionListeners()) {
		                botones.get(idx).removeActionListener(al);
		            }
		            botones.get(idx).addActionListener(evt -> {
		                try {
		                    modelo.Item itemElegido = partida.getInventarioPartida().getItems().get(idx);
		                    Map<Heroe, Integer> nivelesAntes = obtenerNivelesActuales();
		                    // NOTA: si el ítem es Arma o Armadura, item.usar(heroe)
		                    // ahora delega en heroe.equiparArma()/equiparArmadura()
		                    // (ver Arma.java / Armadura.java), no muta stats directo.
		                    modelo.Accion accion = orquestador.crearAccionUsarItem(heroeActivo, itemElegido);
		                    String log = orquestador.procesarTurno(accion);
		                    String msjAccion = heroeActivo.getNombre() + " usa el ítem " + itemElegido.getNombre() + ".";
		                    registrarAccionEnHistorial(msjAccion, log);

		                    vistaInventario.setVisible(false);
		                    actualizarBarrasPantalla();
		                    comprobarSubidaDeNivel(nivelesAntes);
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

		// ==========================================
		// 5. GESTIÓN DEL BOTÓN "VER STATS"
		// NUEVO: corrección "Interfaz #2" del PDF — pantalla de estadísticas
		// con desglose base + bonus de equipamiento.
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnVerStats().addActionListener(e -> {
			try {
				List<Heroe> heroesVivos = partida.getGrupo().getHeroesVivos();
				if (heroesVivos.isEmpty()) {
					throw new IllegalStateException("No hay héroes vivos para mostrar.");
				}

				String[] nombres = heroesVivos.stream().map(Heroe::getNombre).toArray(String[]::new);
				String seleccion = (String) JOptionPane.showInputDialog(
					vistaBatalla,
					"Selecciona un héroe para ver sus estadísticas:",
					"Ver Estadísticas",
					JOptionPane.QUESTION_MESSAGE,
					null,
					nombres,
					nombres[0]
				);

				if (seleccion == null) return;

				Heroe heroeElegido = heroesVivos.stream()
					.filter(h -> h.getNombre().equals(seleccion))
					.findFirst()
					.orElse(heroesVivos.get(0));

				// Cohesión POO: el texto lo arma el modelo (Heroe), el Controlador solo lo muestra.
				JOptionPane.showMessageDialog(
					vistaBatalla,
					heroeElegido.getResumenEstadisticas(),
					"Estadísticas — " + heroeElegido.getNombre(),
					JOptionPane.INFORMATION_MESSAGE
				);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// ==========================================
		// 6. GESTIÓN DEL BOTÓN "VER EQUIPO"
		// NUEVO: corrección "Interfaz #3" del PDF — pantalla para ver el
		// equipamiento (arma/armadura) de cada héroe. Mismo patrón que
		// "Ver Stats": JOptionPane + texto armado por el modelo (Heroe).
		// ==========================================
		this.vistaBatalla.getPanelAcciones().getBtnVerEquipo().addActionListener(e -> {
			try {
				List<Heroe> heroesVivos = partida.getGrupo().getHeroesVivos();
				if (heroesVivos.isEmpty()) {
					throw new IllegalStateException("No hay héroes vivos para mostrar.");
				}

				String[] nombres = heroesVivos.stream().map(Heroe::getNombre).toArray(String[]::new);
				String seleccion = (String) JOptionPane.showInputDialog(
					vistaBatalla,
					"Selecciona un héroe para ver su equipamiento:",
					"Ver Equipamiento",
					JOptionPane.QUESTION_MESSAGE,
					null,
					nombres,
					nombres[0]
				);

				if (seleccion == null) return;

				Heroe heroeElegido = heroesVivos.stream()
					.filter(h -> h.getNombre().equals(seleccion))
					.findFirst()
					.orElse(heroesVivos.get(0));

				// Cohesión POO: Heroe.getResumenEquipamiento() arma su propio
				// texto (arma, armadura, bonus); el Controlador solo lo muestra.
				JOptionPane.showMessageDialog(
					vistaBatalla,
					heroeElegido.getResumenEquipamiento(),
					"Equipamiento — " + heroeElegido.getNombre(),
					JOptionPane.INFORMATION_MESSAGE
				);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(vistaBatalla, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	// ── Métodos de Refactorización POO ────────────────────────────────────────

	private void actualizarBarrasPantalla() {
		// Principio DRY: único punto que sincroniza toda la interfaz tras cada turno.
		this.vistaBatalla.getPanelEstado().refreshTodos();
		modelo.Entidad activo = orquestador.getPersonajeActual();
		this.vistaBatalla.getPanelEstado().refrescarActivo(activo);

		// NUEVO: corrección "Interfaz #1" del PDF — sincroniza el indicador
		// visual de orden de turnos en cada actualización.
		this.vistaBatalla.getPanelEstado().actualizarOrdenTurnos(orquestador.getOrdenTurnos());

		// MODIFICADO: fix de artefactos visuales — limpia restos de
		// JOptionPane que quedaban pegados sobre los paneles de personajes.
		this.vistaBatalla.repintarCompleto();
	}

	private Map<Heroe, Integer> obtenerNivelesActuales() {
		Map<Heroe, Integer> niveles = new HashMap<>();
		for (Heroe h : partida.getGrupo().getHeroesVivos()) {
			niveles.put(h, h.getNivel());
		}
		return niveles;
	}

	private void comprobarSubidaDeNivel(Map<Heroe, Integer> nivelesAnteriores) {
		for (Heroe h : partida.getGrupo().getHeroesVivos()) {
			Integer nivelAnt = nivelesAnteriores.get(h);
			if (nivelAnt != null && h.getNivel() > nivelAnt) {
				JOptionPane.showMessageDialog(vistaBatalla,
					"¡" + h.getNombre() + " ha subido al Nivel " + h.getNivel() + "!\nSus estadísticas base han mejorado.",
					"¡Subida de Nivel!",
					JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void comprobarProgresoJuego() {
		if (orquestador.getBatallaActual() == null || orquestador.getBatallaActual().getEnemigosVivos().isEmpty()) {

			// NUEVO: resumen de recompensas (XP total + loot simulado) pedido
			// en la corrección de transición de nivel.
			String resumenRecompensas = generarResumenRecompensas();

			nivelActual++;
			partida.setNivel(nivelActual);

			// Encapsulamiento: Heroe.restaurarStatusCompleto() cura vida/maná
			// post-batalla, en vez de tocar los campos desde el Controlador.
			for (Heroe h : partida.getGrupo().getHeroesVivos()) {
				h.restaurarStatusCompleto();
			}

			// MODIFICADO: CatalogoBatalla (Singleton) ahora devuelve enemigos
			// clonados frescos para el siguiente nivel.
			String msgCarga = orquestador.iniciarBatalla(nivelActual);

			if (orquestador.getBatallaActual() != null) {
				JOptionPane.showMessageDialog(
					vistaBatalla,
					"¡VICTORIA!\n\n" + resumenRecompensas + "\n\nAvanzando al Nivel " + nivelActual,
					"Fase Completada",
					JOptionPane.INFORMATION_MESSAGE
				);
				this.vistaBatalla.appendHistorial("\n--- " + msgCarga + " ---");
				// MODIFICADO: re-inicializa la vista dinámica porque los
				// enemigos del nuevo nivel son instancias distintas.
				this.vistaBatalla.getPanelEstado().inicializar(
					partida.getGrupo().getHeroesVivos(),
					orquestador.getBatallaActual().getEnemigos()
				);
				actualizarBarrasPantalla();
			} else {
				JOptionPane.showMessageDialog(
					vistaBatalla,
					"¡VICTORIA FINAL!\n\n" + resumenRecompensas + "\n\n¡Ganaste el juego!",
					"Juego Completado",
					JOptionPane.INFORMATION_MESSAGE
				);
				System.exit(0);
			}
		}

		if (orquestador.getBatallaActual() == null || orquestador.getBatallaActual().getHeroesVivos().isEmpty() || !partida.isEstado()) {
			JOptionPane.showMessageDialog(vistaBatalla, "Tu equipo ha caído en combate.", "Game Over", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	// NUEVO: cálculo de recompensas de victoria (XP total + loot simulado).
	private String generarResumenRecompensas() {
		Batalla batallaGanada = orquestador.getBatallaActual();
		int expTotal = (batallaGanada != null) ? batallaGanada.getExperienciaTotalOtorgada() : 0;

		Random rand = new Random();
		int cantidadDrops = 1 + rand.nextInt(2);
		StringBuilder loot = new StringBuilder();

		for (int i = 0; i < cantidadDrops; i++) {
			int curacion = 30 + rand.nextInt(50);
			PocionVida pocion = new PocionVida(
				"Poción de Vida", "Restaura " + curacion + " HP.", curacion
			);
			partida.getInventarioPartida().agregarItem(pocion);
			loot.append("  • ").append(pocion.getNombre())
				.append(" (+").append(curacion).append(" HP)\n");
		}

		return "Experiencia obtenida: +" + expTotal + " XP\n"
			+ "Botín recibido:\n" + loot.toString().stripTrailing();
	}

	private void registrarAccionEnHistorial(String mensajeAccion, String logOrquestador) {
		if (logOrquestador == null) logOrquestador = "";

		String logFinal = logOrquestador;

		if (mensajeAccion != null && !mensajeAccion.isEmpty()) {
			if (logFinal.matches("(?s).*acci[oó]n ejecutada.*")) {
				logFinal = logFinal.replaceAll("(?i)acci[oó]n ejecutada\\.?", mensajeAccion);
			} else {
				logFinal = logFinal.replaceFirst("(--- Turno \\d+ ---)", "$1\n" + mensajeAccion);
				if (logFinal.equals(logOrquestador)) {
					logFinal = mensajeAccion + "\n" + logFinal;
				}
			}
		} else {
			logFinal = logFinal.replaceAll("(?i)acci[oó]n ejecutada\\.?\n?", "");
		}

		if (!logFinal.trim().isEmpty()) {
			this.vistaBatalla.appendHistorial(logFinal.trim());
		}
	}
}
