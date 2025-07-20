package arch.attanake.exception;

public class IncompatibleSchemaException extends RuntimeException {
    public IncompatibleSchemaException() {
        super("Schema is not compatible with previous version");
    }
}