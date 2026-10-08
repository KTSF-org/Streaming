package video;

import exceptions.StreamingException;
import outils.Ffmpeg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiffuseurStreaming implements Runnable {
    private final List<String> commande;
    private final String titre;
    private volatile boolean arretDemande;
    private Thread thread;

    public DiffuseurStreaming(List<String> commande, String titre) {
        this.commande = commande;
        this.titre = titre;
    }

    /**
     * Crée le thread (daemon) et le démarre.
     * Ne fait rien si une lecture est déjà en cours.
     * 1 seule lecture à la fois
     */
    public void demarrer() {
        if (thread != null && thread.isAlive()) {
            return;
        }
        arretDemande = false;
        thread = new Thread(this);
        thread.setName("Diffuseur-" + titre);
        thread.setDaemon(true);
        thread.start();
        System.out.println(Thread.currentThread().getName() + " lancement du thread");
    }

    /**
     * Exécuté DANS le thread : ouvre le flux, crée le Player, appelle play(). *
     */
    @Override
    public void run() {
        try {
            Ffmpeg.diffuser(commande, titre);
        } catch (StreamingException e) {
            System.out.println(e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Arrête la lecture depuis un autre thread : arrêt de la video.
     */
    public void arreter() {

    }

    public boolean estEnCours() {
        //System.out.println("En cours --> " + Thread.currentThread().getName());
        return thread != null && thread.isAlive();
    }

}
