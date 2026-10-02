package video;

import exceptions.ConversionImpossibleException;
import exceptions.LectureImpossibleException;
import exceptions.VideoIntrouvableException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import modele.FichierVideo;
import modele.VideoAvi;
import modele.VideoMp4;
import outils.Ffmpeg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LecteurVideo implements Runnable {
    private final File video;
    private final String titre;
    private volatile boolean arretDemande;
    private Thread thread;


    public LecteurVideo(File video, String titre) {
        this.video = video;
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
        thread.setName("Lecteur-" + titre);
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
            Ffmpeg.lire(video, titre);
        } catch (LectureImpossibleException e) {
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
