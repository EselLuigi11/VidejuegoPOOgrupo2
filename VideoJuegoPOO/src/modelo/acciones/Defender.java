package modelo.acciones;

import modelo.Accion;
import modelo.Entidad;

public class Defender extends Accion {
    private Entidad defensor;

    public Defender(Entidad defensor) {
        this.defensor = defensor;
    }

    @Override
    public void ejecutar() {
        if (defensor == null) {
            System.out.println("No se puede defender sin personaje.");
            return;
        }

        if (!defensor.estaVivo()) {
            System.out.println(defensor.getNombre() + " no puede defenderse porque está fuera de combate.");
            return;
        }

        defensor.defenderse();
        System.out.println(defensor.getNombre() + " adopta una postura defensiva.");
    }//hay que lograr que el personaje deje de defenderse al finalizar el turno, para eso se puede agregar un método en la clase Entidad que se llame "terminarTurno" y dentro de ese método se puede poner "this.estaDefendiendo = false;" para que al finalizar el turno el personaje deje de defenderse.
}