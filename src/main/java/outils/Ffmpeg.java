package outils;

import exceptions.StreamingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class Ffmpeg {
    private Ffmpeg() {
    }

    /**
     * ffmpeg -y -i entree [options] sortie
     * Renvoie le code de retour de ffmpeg (0 = succès).
     */
    public static int convertir(File entree, File sortie, List<String> options)
            throws IOException, InterruptedException {
        List<String> commande = new ArrayList<>();
        commande.add("ffmpeg");
        commande.add("-y");
        commande.add("-loglevel");
        commande.add("error");                 // n'affiche que les erreurs
        commande.add("-i");
        commande.add(entree.getAbsolutePath());
        commande.addAll(options);
        commande.add(sortie.getAbsolutePath());

        //System.out.println(commande);

        ProcessBuilder pb = new ProcessBuilder(commande);
        pb.redirectErrorStream(true);          // stderr fusionné dans stdout
        Process processus = pb.start();
        // TODO : lire la sortie du processus ligne par ligne
        //        (BufferedReader) et l'afficher
        try (BufferedReader sortieProcessus = new BufferedReader(new InputStreamReader(processus.getInputStream()))) {
            String line;
            while ((line = sortieProcessus.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier.");
        }
        return processus.waitFor();            // attend la fin de ffmpeg
    }

    public static int lire(File fichier, String titreFenetre) throws IOException, InterruptedException {
        //la valeur par défaut du titre de la fenêtre est le nom du fichier d'entrée
        //TODO execute ffplay -autoexit -window_title titre fichier.
        List<String> commande = new ArrayList<>();
        commande.add("ffplay");
        commande.add("-autoexit");
        commande.add("-window_title");
        commande.add(titreFenetre);
        commande.add(fichier.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(commande);

        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);
        Process processus = pb.start();
        return processus.waitFor();
    }

    public static int lire(String url, String titreFenetre) throws IOException, InterruptedException, StreamingException {
        List<String> commande = new ArrayList<>();
        commande.add("ffplay");
        commande.add("-autoexit");
        commande.add("-window_title");
        commande.add(titreFenetre);
        commande.add("-rtsp_transport");
        commande.add("tcp");
        commande.add(url);

        System.out.println(commande);

        ProcessBuilder pb = new ProcessBuilder(commande);

        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);
        Process processus = pb.start();
        return processus.waitFor();
    }

}
