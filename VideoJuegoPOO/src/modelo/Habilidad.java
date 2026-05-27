public class HabilidadEsp implements Accion {
    private Entidad usuario;
    private Entidad objetivo;
    private String nombreHabilidad;

    public HabilidadEsp(Entidad usuario, Entidad objetivo, String nombreHabilidad) {
        this.usuario = usuario;
        this.objetivo = objetivo;
        this.nombreHabilidad = nombreHabilidad;
    }

    @Override
    public void ejecutar() {
        System.out.println("🔥 " + usuario.getNombre() + " usa su habilidad especial: " + nombreHabilidad + " en " + objetivo.getNombre());
        // Lógica de la habilidad especial
    }
}
