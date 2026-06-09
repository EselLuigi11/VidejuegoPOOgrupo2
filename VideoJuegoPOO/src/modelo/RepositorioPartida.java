package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class RepositorioPartida {

    // Path del archivo de guardado
    private static final String CARPETA =
        System.getProperty("user.home") + File.separator + "VideojuegoPOO";

    private static final String ARCHIVO_GUARDADO =
        CARPETA + File.separator + "partida_guardada.dat";

    // ── Guardar ───────────────────────────────────────────────

    public boolean guardar(Partida partida) {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_GUARDADO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(partida);
            System.out.println("Partida guardada — Batalla " + partida.getNivel()
                + " | Héroes vivos: " + partida.getGrupo().getHeroesVivos().size()
                + " | Ruta: " + ARCHIVO_GUARDADO);
            return true;

        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
            return false;
        }
    }

    // ── Cargar ────────────────────────────────────────────────

    public Partida cargar() {
        if (!existeSave()) {
            System.out.println("No hay ninguna partida guardada.");
            return null;
        }

        try (FileInputStream fis = new FileInputStream(ARCHIVO_GUARDADO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Partida partida = (Partida) ois.readObject();
            System.out.println("Partida cargada — Retomando desde batalla "
                + partida.getNivel()
                + " | Héroes vivos: " + partida.getGrupo().getHeroesVivos().size());
            return partida;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
            return null;
        }
    }

    // ── Utilidades ────────────────────────────────────────────

    public boolean existeSave() {
        return new File(ARCHIVO_GUARDADO).exists();
    }

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

    public String getRutaArchivo() {
        return ARCHIVO_GUARDADO;
    }
}