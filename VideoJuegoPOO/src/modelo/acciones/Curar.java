package modelo.acciones;

import modelo.Accion;
import modelo.Entidad;

public class Curar extends Accion {
    private Entidad curador;
    private Entidad objetivo;
    private int cantidad;

    public Curar(Entidad curador, Entidad objetivo, int cantidad) {
        this.curador = curador;
        this.objetivo = objetivo;
        this.cantidad = cantidad;
    }

    @Override
    public void ejecutar() {
        if (curador == null || objetivo == null) {
            System.out.println("No se puede curar sin curador u objetivo.");
            return;
        }

        if (!curador.estaVivo()) {
            System.out.println(curador.getNombre() + " no puede curar porque está fuera de combate.");
            return;
        }

        if (!objetivo.estaVivo()) {
            System.out.println(objetivo.getNombre() + " está fuera de combate y no puede ser curado.");
            return;
        }

        System.out.println(curador.getNombre() + " cura a " + objetivo.getNombre() + " por " + cantidad + " puntos.");
        objetivo.curarse(cantidad);
    }
}