public class Curar implements Accion {
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
        System.out.println(curador.getNombre() + " cura a " + objetivo.getNombre() + " por " + cantidad + " puntos.");
        objetivo.curarse(cantidad);
    }
}
