package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/*
 * RepositorioPartida se encarga de GUARDAR y CARGAR la partida en el disco rígido.
 * 
 * ¿Cómo funciona el guardado en Java?
 * Java tiene un mecanismo llamado "serialización": convierte un objeto (como Partida)
 * en una secuencia de bytes y la escribe en un archivo .dat en la computadora.
 * Cuando querés cargar, hace el proceso inverso: lee los bytes del archivo
 * y reconstruye el objeto Partida tal cual estaba cuando se guardó.
 * 
 * Para que un objeto sea serializable, su clase tiene que implementar
 * la interfaz Serializable (que ya hace Partida).
 */
public class RepositorioPartida {

    // ── Rutas del archivo ─────────────────────────────────────────────────────

    // Carpeta donde se va a guardar el archivo de la partida.
    // System.getProperty("user.home") devuelve la carpeta del usuario del sistema,
    // por ejemplo: C:\Users\Luis en Windows, o /home/luis en Linux.
    // File.separator es la barra correcta según el sistema operativo ("\" o "/").
    // Resultado final ejemplo: C:\Users\Luis\VideojuegoPOO
    private static final String CARPETA =
        System.getProperty("user.home") + File.separator + "VideojuegoPOO";

    // Ruta completa del archivo donde se guarda la partida.
    // Resultado final ejemplo: C:\Users\Luis\VideojuegoPOO\partida_guardada.dat
    private static final String ARCHIVO_GUARDADO =
        CARPETA + File.separator + "partida_guardada.dat";


    // ── GUARDAR ───────────────────────────────────────────────────────────────

    /*
     * Serializa el objeto Partida y lo escribe en el archivo .dat.
     * Devuelve true si salió bien, false si hubo algún error.
     */
    public boolean guardar(Partida partida) {

        // Antes de escribir, nos aseguramos de que la carpeta exista.
        // Si el usuario nunca guardó antes, la carpeta VideojuegoPOO no existe todavía.
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs(); // Crea la carpeta (y las carpetas padre si hiciera falta)
        }

        // try-with-resources: abre los "streams" de escritura y los cierra automáticamente
        // al terminar el bloque, aunque haya un error. Evita dejar archivos abiertos.
        //
        // FileOutputStream: abre (o crea) el archivo físico para escribir bytes.
        // ObjectOutputStream: se apoya en FileOutputStream y sabe cómo convertir
        //                     un objeto Java en bytes (serialización).
        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_GUARDADO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            // Actualizamos la fecha de guardado dentro del objeto Partida
            // ANTES de serializarlo, para que quede registrada en el archivo.
            partida.actualizarFechaGuardado();

            // writeObject convierte el objeto Partida completo en bytes y los escribe.
            // Esto incluye todos sus campos: héroes, inventario, nivel, fecha, etc.
            oos.writeObject(partida);

            // Log en consola para confirmar que todo salió bien
            System.out.println("Partida guardada — Batalla " + partida.getNivel()
                + " | Héroes vivos: " + partida.getGrupo().getHeroesVivos().size()
                + " | Guardado: " + partida.getFechaGuardadoFormateada()
                + " | Ruta: " + ARCHIVO_GUARDADO);
            return true;

        } catch (IOException e) {
            // IOException ocurre si hay un problema de disco: sin permisos,
            // disco lleno, ruta inválida, etc.
            System.out.println("Error al guardar la partida: " + e.getMessage());
            return false;
        }
    }


    // ── CARGAR ────────────────────────────────────────────────────────────────

    /*
     * Lee el archivo .dat y reconstruye el objeto Partida (deserialización).
     * Devuelve el objeto Partida si salió bien, o null si no hay save o hubo error.
     */
    public Partida cargar() {

        // Si no existe el archivo, no hay nada que cargar
        if (!existeSave()) {
            System.out.println("No hay ninguna partida guardada.");
            return null;
        }

        // FileInputStream: abre el archivo físico para leer bytes.
        // ObjectInputStream: se apoya en FileInputStream y sabe cómo reconstruir
        //                    un objeto Java a partir de los bytes (deserialización).
        try (FileInputStream fis = new FileInputStream(ARCHIVO_GUARDADO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            // readObject lee los bytes del archivo y reconstruye el objeto.
            // El cast (Partida) le dice a Java que lo trate como un objeto Partida.
            Partida partida = (Partida) ois.readObject();

            System.out.println("Partida cargada — Retomando desde batalla "
                + partida.getNivel()
                + " | Héroes vivos: " + partida.getGrupo().getHeroesVivos().size());
            return partida;

        } catch (IOException e) {
            // IOException: problema leyendo el archivo (corrompido, sin permisos, etc.)
            System.out.println("Error al cargar la partida: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            // ClassNotFoundException: ocurre si el archivo .dat fue guardado con una
            // versión vieja del código y ahora la clase Partida cambió demasiado.
            // En ese caso el archivo ya no es compatible.
            System.out.println("Error al cargar la partida: " + e.getMessage());
            return null;
        }
    }


    // ── UTILIDADES ────────────────────────────────────────────────────────────

    /*
     * Verifica si ya existe un archivo de partida guardada.
     * Se usa antes de cargar para no intentar leer un archivo que no existe.
     */
    public boolean existeSave() {
        return new File(ARCHIVO_GUARDADO).exists();
    }

    /*
     * Borra el archivo de guardado del disco.
     * Útil para un botón "Nueva partida" que descarta el progreso anterior.
     */
    public boolean borrar() {
        File archivo = new File(ARCHIVO_GUARDADO);
        if (archivo.exists()) {
            archivo.delete();
            System.out.println("Partida guardada borrada.");
            return true;
        }
        System.out.println("No había ninguna partida guardada para borrar.");
        return false;
    }

    /*
     * Devuelve la ruta completa del archivo de guardado.
     * Puede usarse para mostrarla en la interfaz si se quiere.
     */
    public String getRutaArchivo() {
        return ARCHIVO_GUARDADO;
    }
}