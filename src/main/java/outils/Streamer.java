package outils;

import exceptions.SaisieInvalideException;
import exceptions.StreamingException;
import modele.FichierVideo;
import video.LecteurStreaming;

import java.util.List;

public class Streamer {

    private final String urlServeur;     // ex. "rtsp://192.168.1.50:8554"
    private volatile Process processus;  // ffmpeg en cours (partagé entre threads)
    private String fluxEnCours;          // nom du chemin diffusé, ex. "film"

    public Streamer(String urlServeur) { this.urlServeur = urlServeur; }

    /** ffmpeg -re [-stream_loop -1] -i fichier <options du format> <sortie> */
    public void diffuserFichier(FichierVideo video, String nomFlux, boolean boucle)
            throws StreamingException, SaisieInvalideException { /* TODO */ }

    /** ffmpeg <entrée caméra selon le système> <encodage direct> <sortie> */
    public void diffuserCamera(String nomFlux)
            throws StreamingException, SaisieInvalideException { /* TODO */ }

    /** Arrête proprement ffmpeg : envoie "q" sur son entrée standard,
     attend 5 s au maximum, sinon destroy(). */
    public void arreter() { /* TODO */ }

    public boolean estEnCours() { return processus != null && processus.isAlive(); }

    /** URL à donner aux spectateurs, ex. rtsp://.../film */
    public String getUrlLecture() { /* TODO */
        return "";
    }

    /** -f rtsp -rtsp_transport tcp rtsp://serveur:8554/nomFlux */
    private List<String> optionsSortie(String nomFlux) { /* TODO */
        return List.of();
    }

    /** Entrée caméra : dshow, v4l2 ou avfoundation selon os.name */
    private List<String> optionsCamera() { /* TODO */
        return List.of();
    }

    /** Lance ffmpeg et lit sa sortie dans un thread daemon. */
    public void lancer(String nomFlux, String titre)
            throws StreamingException {
        LecteurStreaming lecteur = new LecteurStreaming(
                this.urlServeur + "/" + nomFlux, titre
        );
        this.fluxEnCours = nomFlux;
        lecteur.demarrer();
    }
}