
import application.Controller;
import application.Videotheque;
import exceptions.*;
import outils.Streamer;
import video.LecteurStreaming;

import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {

        //Streamer strm = new Streamer("rtsp://172.16.120.28:8554");
        //strm.diffuserCamera("camera");

        Controller c = new Controller();
        c.peuplerVideotheque("mp4");
        int choix = -1;

        while (true) {
            try {
                c.afficherMenu();
                System.out.print("Choix :");
                choix = Controller.scan.nextInt();
                Controller.scan.nextLine();
                switch (choix) {
                    case 1:
                        c.ajouterVideo();
                        break;
                    case 2:
                        c.listerVideos();
                        break;
                    case 3:
                        c.rechercherVideo();
                        break;
                    case 4:
                        c.supprimerVideo();
                        break;
                    case 5:
                        c.lectureVideo();
                        break;
                    case 6:
                        c.convertirVideo();
                        break;
                    case 7:
                        c.diffuserFichier();
                        break;
                    case 8:
                        c.diffuserCamera();
                        break;
                    case 9:
                        c.lectureStreaming();
                        break;
                    case 10:
                        c.arreterDiffusion();
                        break;
                    case 0:
                        c.arreterDiffusion();
                        System.out.println("Au revoir !");
                        Controller.scan.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("\u001B[31mChoix invalide, veuillez réessayer.\u001B[0m");
                }
            } catch (ConversionImpossibleException | LectureImpossibleException | VideoDejaExistanteException |
                     SaisieInvalideException | VideoIntrouvableException | VideothequeVideException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                Controller.scan.nextLine();
                System.out.println("\u001B[31mSaisie non valide\u001B[0m");
            }
        }
    }


}
