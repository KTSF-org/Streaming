package exceptions;

public class VideothequeVideException extends RuntimeException {
    public VideothequeVideException(String message) {
        super("\u001B[31m" + message + "\u001B[0m");
    }
}
