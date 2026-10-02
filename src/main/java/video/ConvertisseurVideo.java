package video;

import exceptions.ConversionImpossibleException;
import modele.FichierVideo;
import outils.Ffmpeg;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConvertisseurVideo implements Runnable {

    File entree;
    File sortie;
    List<String> options;
    private Thread thread;

    public ConvertisseurVideo(File entree, File sortie, List<String> options) {
        this.entree = entree;
        this.sortie = sortie;
        this.options = options;
    }

    public void demarrer() {
        thread = new Thread(this);
        thread.setName("Convertisseur-" + entree.getAbsolutePath());
        thread.setDaemon(true);
        thread.start();
        System.out.println(Thread.currentThread().getName() + " - Lancement du thread de conversion.");
    }


    @Override
    public void run() {
        try {
            Ffmpeg.convertir(entree, sortie, options);
            System.out.println("Conversion terminée !");
        } catch (ConversionImpossibleException e) {
            System.out.println(e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
