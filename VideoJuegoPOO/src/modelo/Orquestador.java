package modelo;

import modelo.Arma;
import modelo.Armadura;
import modelo.pociones.PocionVida;
import modelo.pociones.PocionMana;
import modelo.entidades.Guerrero;
import modelo.entidades.Arquero;
import modelo.entidades.Asesino;
import modelo.entidades.Mago;
import modelo.entidades.Curador;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import modelo.acciones.Atacar;
import modelo.acciones.Defender;
import modelo.acciones.UsarItem;
import modelo.entidades.Enemigo;
import modelo.entidades.Heroe;

public class Orquestador {
    private Batalla batallaActual;
    private int contadorTurnos;
    private Partida partidaActual;
    private List<Entidad> ordenTurnos;
    private int indiceTurnoActual;
    private boolean resultadoProcesado;
    private int numeroBatallaActual;

    public Orquestador(Batalla batallaActual, Partida partidaActual) {
        this.batallaActual      = batallaActual;
        this.contadorTurnos     = 0;
        this.partidaActual      = partidaActual;
        this.ordenTurnos        = new ArrayList<>();
        this.indiceTurnoActual  = 0;
        this.resultadoProcesado = false;
        this.numeroBatallaActual = 0;
        actualizarOrdenTurnos();
    }

    // ── Getters básicos ───────────────────────────────────────────────────────

    public Batalla getBatallaActual()   { return batallaActual; }
    public int     getContadorTurnos()  { return contadorTurnos; }
    public Partida getPartidaActual()   { return partidaActual; }

    public EstadoBatalla getEstadoBatalla() {
        return batallaActual == null ? EstadoBatalla.DERROTA
                                     : batallaActual.evaluarEstado();
    }

    public boolean hayBatallaEnCurso() {
        return batallaActual != null
                && batallaActual.evaluarEstado() == EstadoBatalla.EN_CURSO;
    }

    // ── Gestión de batalla ────────────────────────────────────────────────────

    public void setBatallaActual(Batalla batallaActual) {
        this.batallaActual      = batallaActual;
        this.contadorTurnos     = 0;
        this.indiceTurnoActual  = 0;
        this.resultadoProcesado = false;
        actualizarOrdenTurnos();
    }

    public String iniciarBatalla(int numeroBatalla) {
        if (partidaActual == null) return "No hay una partida cargada.";

        List<Heroe> heroesVivos = partidaActual.getGrupo().getHeroesVivos();
        if (heroesVivos.isEmpty()) {
            partidaActual.verificarEstadoPartida();
            return "No hay heroes vivos para iniciar la batalla.";
        }

        Batalla nueva = CatalogoBatalla.getInstance()
                .construirBatalla(numeroBatalla, heroesVivos);
        if (nueva == null) return "No existe la batalla " + numeroBatalla + ".";

        setBatallaActual(nueva);
        this.numeroBatallaActual = numeroBatalla;
        return "Comienza la batalla " + numeroBatalla + ".";
    }

    // ── Listas de combatientes ────────────────────────────────────────────────

    public List<Heroe> getHeroesVivos() {
        List<Heroe> vivos = new ArrayList<>();
        if (batallaActual == null) return vivos;
        for (Heroe h : batallaActual.getHeroes())
            if (h != null && h.estaVivo()) vivos.add(h);
        return vivos;
    }

    public List<Enemigo> getEnemigosVivos() {
        List<Enemigo> vivos = new ArrayList<>();
        if (batallaActual == null) return vivos;
        for (Enemigo e : batallaActual.getEnemigos())
            if (e != null && e.estaVivo()) vivos.add(e);
        return vivos;
    }

    // ── Turno actual ──────────────────────────────────────────────────────────

    public Entidad getPersonajeActual() {
        actualizarOrdenTurnos();
        return ordenTurnos.isEmpty() ? null : ordenTurnos.get(indiceTurnoActual);
    }

    public Heroe getHeroeActual() {
        Entidad e = getPersonajeActual();
        return (e instanceof Heroe) ? (Heroe) e : null;
    }

    public List<Entidad> getOrdenTurnos() {
        actualizarOrdenTurnos();
        return new ArrayList<>(ordenTurnos);
    }

    public boolean esTurnoDeHeroe()   { return getPersonajeActual() instanceof Heroe; }
    public boolean esTurnoDeEnemigo() { return getPersonajeActual() instanceof Enemigo; }

    // ── Fábricas de acción ────────────────────────────────────────────────────

    public Accion crearAccionAtacar(Heroe atacante, Enemigo objetivo) {
        if (atacante == null || objetivo == null)
            throw new IllegalArgumentException("Atacante y objetivo son obligatorios.");
        return new Atacar(atacante, objetivo);
    }

    public Accion crearAccionDefender(Heroe defensor) {
        if (defensor == null)
            throw new IllegalArgumentException("El defensor es obligatorio.");
        return new Defender(defensor);
    }

    public Accion crearAccionUsarItem(Heroe heroe, Item item) {
        if (heroe == null || item == null)
            throw new IllegalArgumentException("Heroe e item son obligatorios.");
        return new UsarItem(partidaActual, heroe, item);
    }

    // ── Procesamiento de turnos ───────────────────────────────────────────────

    public String procesarTurno(Accion accion) {
        return procesarTurno(null, accion);
    }

    public String procesarTurno(Heroe heroeQueActua, Accion accion) {
        StringBuilder log = new StringBuilder();

        if (batallaActual == null)
            return "No hay una batalla activa.";

        if (batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO)
            return resolverFinDeBatalla(log);

        actualizarOrdenTurnos();
        if (ordenTurnos.isEmpty())
            return resolverFinDeBatalla(log);

        // 1. Procesar turnos de enemigos que estén antes del próximo héroe
        procesarTurnosAutomaticosEnemigos(log);

        if (batallaActual.evaluarEstado() != EstadoBatalla.EN_CURSO)
            return log.toString();

        // 2. Verificar que sea turno de héroe
        Entidad personajeActual = getPersonajeActual();
        if (!(personajeActual instanceof Heroe))
            return log.toString();

        if (heroeQueActua != null && heroeQueActua != personajeActual) {
            return "No es el turno de " + heroeQueActua.getNombre()
                    + ". Es el turno de " + personajeActual.getNombre() + ".";
        }

        if (accion == null)
            return "Es turno de " + personajeActual.getNombre()
                    + ". No se eligio ninguna accion.";

        // 3. Héroe actúa → un enemigo responde → devolver control al jugador
        procesarTurnoHeroe((Heroe) personajeActual, accion, log);
        procesarTurnosAutomaticosEnemigos(log);
        resolverFinDeBatalla(log);

        return log.toString();
    }

    // ── Internos ──────────────────────────────────────────────────────────────

    private void procesarTurnoHeroe(Heroe heroe, Accion accion, StringBuilder log) {
        if (heroe == null || !heroe.estaVivo()) {
            avanzarTurnoDesde(heroe);
            return;
        }

        log.append("-- Turno ").append(contadorTurnos)
                .append(": ").append(heroe.getNombre()).append(" --\n");

        heroe.setEstaDefendiendo(false);
        accion.ejecutar();
        log.append("[Accion de ").append(heroe.getNombre()).append(" ejecutada]\n");
        contadorTurnos++;

        avanzarTurnoDesde(heroe);
        resolverFinDeBatalla(log);
    }

    /**
     * Procesa de a UN enemigo por invocación y para en cuanto llega
     * el turno de un héroe — garantiza alternancia estricta héroe↔enemigo.
     */
    private void procesarTurnosAutomaticosEnemigos(StringBuilder log) {
        actualizarOrdenTurnos();

        while (batallaActual.evaluarEstado() == EstadoBatalla.EN_CURSO
                && !ordenTurnos.isEmpty()
                && getPersonajeActual() instanceof Enemigo) {

            Enemigo enemigo = (Enemigo) getPersonajeActual();

            if (enemigo != null && enemigo.estaVivo()) {
                List<Heroe> heroesVivos = getHeroesVivos();
                if (!heroesVivos.isEmpty()) {
                    log.append("-- Turno ").append(contadorTurnos)
                            .append(": ").append(enemigo.getNombre()).append(" --\n");
                    enemigo.EnemigoAtaca(heroesVivos);
                    log.append(enemigo.getNombre()).append(" ataca.\n");
                    contadorTurnos++;
                }
            }

            avanzarTurnoDesde(enemigo);

            // STOP: ceder control al jugador cuando toca un héroe
            if (getPersonajeActual() instanceof Heroe) break;
        }
    }

    private void actualizarOrdenTurnos() {
        if (batallaActual == null) {
            ordenTurnos = new ArrayList<>();
            indiceTurnoActual = 0;
            return;
        }

        Entidad actual = (!ordenTurnos.isEmpty()
                && indiceTurnoActual >= 0
                && indiceTurnoActual < ordenTurnos.size())
                ? ordenTurnos.get(indiceTurnoActual) : null;

        List<Entidad> nuevo = new ArrayList<>();
        for (Heroe h  : batallaActual.getHeroes())
            if (h != null && h.estaVivo()) nuevo.add(h);
        for (Enemigo e : batallaActual.getEnemigos())
            if (e != null && e.estaVivo()) nuevo.add(e);

        nuevo.sort(Comparator.comparingInt(Entidad::getVelocidad).reversed());
        ordenTurnos = nuevo;

        if (ordenTurnos.isEmpty()) { indiceTurnoActual = 0; return; }

        int idx = (actual == null) ? -1 : ordenTurnos.indexOf(actual);
        if (idx >= 0)                          indiceTurnoActual = idx;
        else if (indiceTurnoActual >= ordenTurnos.size()) indiceTurnoActual = 0;
    }

    private void avanzarTurnoDesde(Entidad quien) {
        actualizarOrdenTurnos();
        if (ordenTurnos.isEmpty()) { indiceTurnoActual = 0; return; }

        int idx = ordenTurnos.indexOf(quien);
        indiceTurnoActual = (idx >= 0)
                ? (idx + 1) % ordenTurnos.size()
                : indiceTurnoActual % ordenTurnos.size();
    }

    private String resolverFinDeBatalla(StringBuilder log) {
        EstadoBatalla estado = batallaActual.evaluarEstado();
        if (estado == EstadoBatalla.EN_CURSO) return log.toString();
        if (resultadoProcesado)               return log.toString();

        resultadoProcesado = true;

        if (estado == EstadoBatalla.VICTORIA) {
            log.append("¡Los heroes han ganado la batalla!\n");
            repartirExperiencia();
        } else if (estado == EstadoBatalla.DERROTA) {
            log.append("Game Over. Los enemigos ganaron.\n");
            if (partidaActual != null) partidaActual.verificarEstadoPartida();
        }
        return log.toString();
    }

    private void repartirExperiencia() {
        List<Heroe> vivos = getHeroesVivos();
        if (vivos.isEmpty() || batallaActual == null) return;

        for (Enemigo e : batallaActual.getEnemigos())
            if (e != null)
                for (Heroe h : vivos) e.otorgarExperiencia(h);

        otorgarRecompensas(numeroBatallaActual);
    }

    private void otorgarRecompensas(int numeroBatalla) {
        if (partidaActual == null) return;
        Inventario inv    = partidaActual.getInventarioPartida();
        List<Heroe> vivos = getHeroesVivos();

        System.out.println("=== Recompensas de la batalla " + numeroBatalla + " ===");

        switch (numeroBatalla) {
            case 1:
                for (Heroe h : vivos) {
                    Arma arma = null;
                    if      (h instanceof Guerrero) arma = new Arma("Espada de Hierro",  "Espada de un goblin derrotado.", 8);
                    else if (h instanceof Arquero)  arma = new Arma("Arco Corto",        "Arco ágil del campo de batalla.", 7);
                    else if (h instanceof Asesino)  arma = new Arma("Daga Envenenada",   "Daga con filo goblin.", 9);
                    else if (h instanceof Mago)     arma = new Arma("Bastón de Rama",    "Canal arcano primitivo.", 5);
                    else if (h instanceof Curador)  arma = new Arma("Vara Sagrada",      "Emana energía curativa.", 4);
                    else                            arma = new Arma("Arma Simple",       "Botín básico.", 6);
                    h.setArma(arma);
                    System.out.println(h.getNombre() + " equipó: " + arma.getNombre());
                }
                inv.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP.", 50));
                inv.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP.", 50));
                break;

            case 2:
                inv.agregarItem(new PocionVida("Poción de Vida Mayor", "Restaura 80 HP.", 80));
                inv.agregarItem(new PocionVida("Poción de Vida Mayor", "Restaura 80 HP.", 80));
                inv.agregarItem(new PocionMana("Poción de Maná",       "Restaura 40 MP.", 40));
                inv.agregarItem(new Arma("Espada Reforzada", "Hoja de ladrón veterano.", 12));
                break;

            case 3:
                inv.agregarItem(new PocionVida("Elixir de Vida", "Restaura 120 HP.", 120));
                inv.agregarItem(new PocionVida("Elixir de Vida", "Restaura 120 HP.", 120));
                inv.agregarItem(new Arma("Báculo del Gólem",   "Forjado del núcleo del gólem.", 18));
                inv.agregarItem(new Armadura("Loriga de Piedra", "Alta defensa del gólem.", 20));
                break;

            default:
                inv.agregarItem(new PocionVida("Poción de Vida", "Restaura 50 HP.", 50));
                break;
        }
    }
}