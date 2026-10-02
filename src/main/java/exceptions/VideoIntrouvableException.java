package exceptions;

public class VideoIntrouvableException extends RuntimeException {
    public VideoIntrouvableException(String message) {
        super("\u001B[31m" + message + "\u001B[0m");
    }
}
