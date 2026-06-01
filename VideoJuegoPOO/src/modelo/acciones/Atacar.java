package modelo.acciones;

public class Atacar implements Accion {
    private Entidad atacante;
    private Entidad objetivo;
    private int dano;

    public Atacar(Entidad atacante, Entidad objetivo, int dano) {
        this.atacante = atacante;
        this.objetivo = objetivo;
        this.dano = dano;
    }

    @Override
    public void ejecutar() {
        System.out.println(atacante.getNombre() + " ataca a " + objetivo.getNombre() + " por " + dano + " de daño.");
        objetivo.recibirDano(dano); 
    }
}
