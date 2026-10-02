package exceptions;

public class LectureImpossibleException extends RuntimeException {
    public LectureImpossibleException(String message) {
        super("\u001B[31m" + message + "\u001B[0m");
    }
}

