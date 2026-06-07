package modelo.acciones;

import modelo.Accion;
import modelo.Entidad;
import modelo.entidades.Heroe;

public class Atacar extends Accion {
    private Entidad atacante;
    private Entidad objetivo;

    public Atacar(Entidad atacante, Entidad objetivo) {
        this.atacante = atacante;
        this.objetivo = objetivo;
    }

    @Override
    public void ejecutar() {
        if (atacante == null || objetivo == null) {
            System.out.println("No se puede atacar sin atacante u objetivo.");
            return;
        }

        if (!atacante.estaVivo()) {
            System.out.println(atacante.getNombre() + " no puede atacar porque está fuera de combate.");
            return;
        }

        if (!objetivo.estaVivo()) {
            System.out.println(objetivo.getNombre() + " ya está derrotado.");
            return;
        }

        int danoTotal = calcularDanoTotal();
        System.out.println(atacante.getNombre() + " ataca a " + objetivo.getNombre() + " por " + danoTotal + " de daño.");
        objetivo.recibirDano(danoTotal); 
    }

    private int calcularDanoTotal() {
        if (atacante instanceof Heroe) { //Tenemos una funcion de calcularDanoBase en clase heroe, si es heroe el que ataca, hace esto.
            Heroe heroe = (Heroe) atacante;
            return heroe.calcularDanoBase();
        }
        //En caso de ser un ataque enemigo, se realiza este.
        return atacante.getAtaque();
    }
}