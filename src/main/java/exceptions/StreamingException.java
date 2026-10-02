package exceptions;

public class StreamingException extends RuntimeException {
    public StreamingException(String message) {
        super("\u001B[31m" + message + "\u001B[0m");
    }
}
