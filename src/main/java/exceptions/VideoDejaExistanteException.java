package exceptions;

public class VideoDejaExistanteException extends RuntimeException {
    public VideoDejaExistanteException(String message) {
        super("\u001B[31m" + message + "\u001B[0m");
    }
}
