package modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VideoAvi extends FichierVideo {

    public VideoAvi(String titre, String realisateur, LocalDate dateSortie, int duree) {
        super(titre, realisateur, dateSortie, duree);
    }

    @Override
    public String getSupport() {
        return "AVI";
    }

    @Override
    protected List<String> optionEncodage() {
        List<String> options = new ArrayList<>();
        options.add("-c:v");
        options.add("mpeg4");
        options.add("-q:v");
        options.add("5");
        options.add("-c:a");
        options.add("libmp3lame");
        options.add("-b:a");
        options.add("192k");

        return options;
    }

    @Override
    protected List<String> optionsStreaming() {
        List<String> options = new ArrayList<>();
        options.add("-c:v");
        options.add("libx264");
        options.add("-preset");
        options.add("veryfast");
        options.add("-tune");
        options.add("zerolatency");
        options.add("-pix_fmt");
        options.add("yuv420p");
        options.add("-g");
        options.add("50");
        options.add("-b:v");
        options.add("2500k");
        options.add("-maxrate");
        options.add("2500k");
        options.add("-bufsize");
        options.add("5000k");
        options.add("-c:a");
        options.add("aac");
        options.add("-b:a");
        options.add("128k");
        options.add("-ar");
        options.add("44100");
        options.add("-f");
        return options;
    }

    public String toString() {
        return super.toString() + "AVI";
    }
}
