package exceptions;

public final class DatabaseException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 1;

    public DatabaseException(String message) {super(message);}

}
