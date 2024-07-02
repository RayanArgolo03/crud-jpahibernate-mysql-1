package exceptions;

public final class OrderException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 1;

    public OrderException(String message) {
        super(message);
    }
}
