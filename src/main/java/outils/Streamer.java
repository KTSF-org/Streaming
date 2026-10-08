package outils;

import exceptions.SaisieInvalideException;
import exceptions.StreamingException;
import modele.FichierVideo;
import video.DiffuseurStreaming;
import video.LecteurStreaming;

import java.util.ArrayList;
import java.util.List;

public class Streamer {

    private final String urlServeur;     // ex. "rtsp://192.168.1.50:8554"
    private volatile Process processus;  // ffmpeg en cours (partagé entre threads)
    private String fluxEnCours;          // nom du chemin diffusé, ex. "film"

    public Streamer(String urlServeur) {
        this.urlServeur = urlServeur;
    }

    /**
     * ffmpeg -re [-stream_loop -1] -i fichier <options du format> <sortie>
     */
    public void diffuserFichier(FichierVideo video, String nomFlux, boolean boucle)
            throws StreamingException, SaisieInvalideException {
        List<String> commande = new ArrayList<>();
        commande.add("ffmpeg");
        commande.add("-re");
        if (boucle) {
            commande.add("-stream_loop");
            commande.add("-1");
        }
        commande.add("-i");
        commande.add(video.getChemin());
        commande.addAll(video.getOptionStreaming());
        commande.add("rtsp");
        commande.add("-rtsp_transport");
        commande.add("tcp");
        commande.add(nomFlux);

        ProcessBuilder pb = new ProcessBuilder(commande);

        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);
        Process processus = pb.start();
        return processus.waitFor();

        if (nomFlux == null) {
            throw new SaisieInvalideException("Le nom du flux est vide");
        }

    }

    /**
     * ffmpeg <entrée caméra selon le système> <encodage direct> <sortie>
     */
    public void diffuserCamera(String nomFlux)
            throws StreamingException, SaisieInvalideException {
        List<String> commande = new ArrayList<>();
        commande.add("ffmpeg");
        commande.addAll(optionsCamera());
        commande.add(this.urlServeur + "/" + nomFlux);
        DiffuseurStreaming diffuseur = new DiffuseurStreaming(commande, "camera");
        diffuseur.demarrer();
    }

    /**
     * Arrête proprement ffmpeg : envoie "q" sur son entrée standard,
     * attend 5 s au maximum, sinon destroy().
     */
    public void arreter() { /* TODO */ }

    public boolean estEnCours() {
        return processus != null && processus.isAlive();
    }

    /**
     * URL à donner aux spectateurs, ex. rtsp://.../film
     */
    public String getUrlLecture() { /* TODO */
        return "";
    }

    /**
     * -f rtsp -rtsp_transport tcp rtsp://serveur:8554/nomFlux
     */
    private List<String> optionsSortie(String nomFlux) { /* TODO */
        return List.of();
    }

    /**
     * Entrée caméra : dshow, v4l2 ou avfoundation selon os.name
     */
    private List<String> optionsCamera() { /* TODO */
        List<String> option = new ArrayList<>();
        // Entrée DirectShow
        option.add("-f");
        option.add("dshow");
        option.add("-rtbufsize");
        option.add("100M");
        option.add("-video_size");
        option.add("1280x720");
        option.add("-framerate");
        option.add("30");
        option.add("-i");
        option.add("video=HP True Vision HD Camera:audio=Réseau de microphones (Technologie Intel® Smart Sound pour microphones numériques)");
        // Encodage vidéo
        option.add("-c:v");
        option.add("libx264");
        option.add("-preset");
        option.add("ultrafast");
        option.add("-tune");
        option.add("zerolatency");
        option.add("-pix_fmt");
        option.add("yuv420p");
        option.add("-g");
        option.add("30");
        option.add("-b:v");
        option.add("2000k");
        option.add("-maxrate");
        option.add("2000k");
        option.add("-bufsize");
        option.add("2000k");
        // Encodage audio
        option.add("-c:a");
        option.add("aac");
        option.add("-b:a");
        option.add("128k");
        option.add("-ar");
        option.add("44100");
        // Sortie RTSP
        option.add("-f");
        option.add("rtsp");
        option.add("-rtsp_transport");
        option.add("tcp");
        return option;
    }

    /**
     * Lance ffmpeg et lit sa sortie dans un thread daemon.
     */
    public void lancer(String nomFlux, String titre)
            throws StreamingException {
        LecteurStreaming lecteur = new LecteurStreaming(
                this.urlServeur + "/" + nomFlux, titre
        );
        this.fluxEnCours = this.urlServeur + "/" + nomFlux;
        lecteur.demarrer();
    }
}