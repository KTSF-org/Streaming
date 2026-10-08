package video;

import exceptions.StreamingException;
import outils.Ffmpeg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiffuseurStreaming implements Runnable {
    private final List<String> commande;
    private final String titre;
    private volatile boolean arretDemande;
    private volatile Process process;
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
        arretDemande = true;
        Process p = process;
        if (p == null || !p.isAlive()) {
            return;
        }

        try {
            // 1. Arrêt propre : ffmpeg quitte quand il reçoit 'q'
            OutputStream os = p.getOutputStream();
            os.write('q');
            os.flush();

            // 2. On laisse 3 s pour se terminer
            if (!p.waitFor(3, TimeUnit.SECONDS)) {
                // 3. Arrêt "poli" du processus
                p.destroy();
                if (!p.waitFor(2, TimeUnit.SECONDS)) {
                    // 4. Dernier recours
                    p.destroyForcibly();
                }
            }
        } catch (IOException e) {
            p.destroyForcibly();
        } catch (InterruptedException e) {
            p.destroyForcibly();
            Thread.currentThread().interrupt();
        }
    }

    public boolean estEnCours() {
        //System.out.println("En cours --> " + Thread.currentThread().getName());
        return thread != null && thread.isAlive();
    }

}
