package exceptions;

public class SaisieInvalideException extends RuntimeException {
    public SaisieInvalideException(String message) {
        super("\u001B[31m" + message + "\u001B[0m");
    }
}
