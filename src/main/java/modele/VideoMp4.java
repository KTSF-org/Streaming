package modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VideoMp4 extends FichierVideo {


    public VideoMp4(String titre, String realisateur, LocalDate dateSortie, int duree) {
        super(titre, realisateur, dateSortie, duree);
    }

    @Override
    public String getSupport() {
        return "MP4";
    }

    @Override
    protected List<String> optionEncodage() {
        // TODO
        List<String> options = new ArrayList<>();
        options.add("-c:v");
        options.add("libx264");
        options.add("-preset");
        options.add("fast");
        options.add("-crf");
        options.add("23");
        options.add("-c:a");
        options.add("aac");
        options.add("-b:a");
        options.add("160k");

        return options;
    }

    @Override
    protected List<String> optionsStreaming() {
        List<String> options = new ArrayList<>();
        options.add("-c");
        options.add("copy");
        return options;
    }

    public String toString() {
        return super.toString() + "VideoMp4{}";
    }
}
