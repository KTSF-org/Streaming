package modele;

import exceptions.*;

import java.io.IOException;

public interface GestionVideotheque {
    void ajouterVideo(Video v)
            throws VideoDejaExistanteException, SaisieInvalideException;
    void listerVideos() throws VideothequeVideException;
    Video rechercherVideo(String titre)
            throws VideoIntrouvableException, VideothequeVideException;
    void supprimerVideo(String titre)
            throws VideoIntrouvableException, VideothequeVideException;
    void lireVideo(String titre)
            throws VideoIntrouvableException, VideothequeVideException,
            LectureImpossibleException;
    Video convertirVideo(String titre, String formatCible)
            throws VideoIntrouvableException, VideothequeVideException,
            ConversionImpossibleException, SaisieInvalideException, IOException, InterruptedException;
}
