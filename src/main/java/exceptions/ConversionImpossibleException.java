package exceptions;

public class ConversionImpossibleException extends RuntimeException {
    public ConversionImpossibleException(String message) {
        super("\u001B[31m" + message + "\u001B[0m");
    }
}
