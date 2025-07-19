package arch.attanake.exception;

public class SchemaAlreadyExistsException extends RuntimeException {
    public SchemaAlreadyExistsException() {
        super("Schema already exists");
    }
}