package exceptions;

public final class ClientException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 1;

    public ClientException(String message) {
        super(message);
    }
}
