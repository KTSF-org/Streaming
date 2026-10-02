package modele;

import exceptions.ConversionImpossibleException;
import exceptions.LectureImpossibleException;
import exceptions.SaisieInvalideException;
import exceptions.VideoIntrouvableException;
import outils.Ffmpeg;
import video.ConvertisseurVideo;
import video.LecteurVideo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class FichierVideo extends Video implements Convertible {

    private String chemin;

    public FichierVideo(String titre, String realisateur, LocalDate dateSortie, int duree) {
        super(titre, realisateur, dateSortie, duree);
        String mediaDir = "media/";
        this.chemin = mediaDir + titre + "." + this.getSupport();
    }

    // type File -> pas necessaire comme le chemin est dans les attributs (getFile)
    public File getFichier() {
        return new File(this.chemin);
    }

    //Récupère la taille du fichier en Mo
    public Double getTaille(File file) {
        // TODO
        if (!file.exists()) {
            throw new VideoIntrouvableException("\u001B[31mFichier introuvable.\u001B[0m");
        }

        double tailleMo = 0;
        try {
            long sizeInBytes = Files.size(file.toPath());
            tailleMo = (sizeInBytes / (1024.0 * 1024.0));
        } catch (IOException e) {
            System.out.println("\u001B[31mImpossible de lire la taille du fichier " + file.getName() + "\u001B[0m");
        }
        return tailleMo;
    }


    @Override
    public FichierVideo convertir(String formatCible) throws ConversionImpossibleException, IOException, InterruptedException {
        File fichierEntree = getFichier();
        File fichierSortie;
        String nomFichierEntree = getChemin();
        String nomFichierSortie = nomFichierEntree.substring(0, nomFichierEntree.length() - 3);
        FichierVideo fichierConverti;

        switch (formatCible.toLowerCase()) {
            case "mp4":
                nomFichierSortie += "mp4";
                fichierConverti = new VideoMp4(getTitre(), getRealisateur(), getDateSortie(), getDuree());
                break;
            case "avi":
                nomFichierSortie += "avi";
                fichierConverti = new VideoAvi(getTitre(), getRealisateur(), getDateSortie(), getDuree());
                break;
            default:
                throw new ConversionImpossibleException("Conversion impossible");
        }

        fichierSortie = new File(nomFichierSortie);
        System.out.println("Début de la conversion...");
        ConvertisseurVideo convertisseur = new ConvertisseurVideo(fichierEntree, fichierSortie, fichierConverti.optionEncodage());
        convertisseur.demarrer();
        return fichierConverti;
    }


    //Lance ffplay (outils) dans un thread daemon (package video -> LecteurVideo)
    @Override
    public void lire() {
        File fichierALIre = getFichier();
        String nomFichier = getChemin();

        LecteurVideo lv = new LecteurVideo(fichierALIre, nomFichier);
        lv.demarrer();
    }

    protected abstract List<String> optionEncodage();

    protected abstract List<String> optionsStreaming();

    public List<String> getOptionStreaming() {
        return optionsStreaming();
    }


    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    @Override
    public String toString() {
        return "FichierVideo{" +
                "chemin='" + chemin + '\'' +
                '}';
    }

}
