package arch.attanake.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Product with ID '" + id + "' not found");
    }
}
