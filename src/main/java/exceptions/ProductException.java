package exceptions;

public final class ProductException extends RuntimeException {
    @java.io.Serial
    private static final long serialVersionUID = 1;

    public ProductException(String message) {
        super(message);
    }
}
