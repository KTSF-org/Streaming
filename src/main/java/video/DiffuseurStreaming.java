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
            Ffmpeg.diffuser(commande, p -> {
                process = p;
                // Si arreter() a été appelé avant que le process existe
                if (arretDemande) {
                    arreter();
                }
            });
        } catch (StreamingException e) {
            // Après un arrêt volontaire, une erreur est normale : on l'ignore
            if (!arretDemande) {
                System.out.println(e.getMessage());
            }
        } catch (IOException | InterruptedException e) {
            if (!arretDemande) {
                throw new RuntimeException(e);
            }
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
            OutputStream os = p.getOutputStream();
            os.write('q');
            os.flush();
            if (!p.waitFor(5, TimeUnit.SECONDS)) {
                p.destroy();
                if (!p.waitFor(2, TimeUnit.SECONDS)) {
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
        return thread != null && thread.isAlive();
    }

}
