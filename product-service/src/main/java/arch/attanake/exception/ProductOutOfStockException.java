package arch.attanake.exception;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(String productId, int requested, int available) {
        super(String.format(
                "Product '%s' has insufficient stock. Requested: %d, Available: %d",
                productId, requested, available
        ));
    }
}
