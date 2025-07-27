package arch.attanake.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId, int available) {
        super(String.format("Insufficient stock for product %s. Available: %d", productId, available));
    }
}
