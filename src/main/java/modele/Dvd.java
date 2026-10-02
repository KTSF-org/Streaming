package modele;

import exceptions.LectureImpossibleException;

import java.time.LocalDate;

public class Dvd extends Video {

    private String numero;
    private int zone;

    public Dvd(String titre, String realistaeur, LocalDate dateSortie, int duree, String numero, int zone) {
        super(titre, realistaeur, dateSortie, duree);
        this.numero = numero;
        this.zone = zone;
    }


    @Override
    public String getSupport() {
        return "DVD";
    }

    @Override
    public void lire() throws LectureImpossibleException {
        // TODO
        System.out.println("Prenez le DVD " + this.numero + " \"" + super.getTitre() + "\" et insérez-le dans un lecteur zone " + this.zone + ".");
    }
}
